package org.maurycy.framework.dsa.service


import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.GetObjectResponse
import io.minio.ListObjectsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.quarkus.logging.Log
import io.quarkus.tika.TikaContent
import io.quarkus.tika.TikaMetadata
import io.quarkus.tika.TikaParser
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import org.apache.http.util.EntityUtils
import org.elasticsearch.client.Request
import org.elasticsearch.client.Response
import org.elasticsearch.client.RestClient
import org.jboss.resteasy.reactive.multipart.FileUpload
import org.maurycy.framework.dsa.exception.NoFileSentException
import org.maurycy.framework.dsa.exception.TooManyFilesSentException
import org.maurycy.framework.dsa.model.FormData
import org.maurycy.framework.dsa.model.StoredContent


@ApplicationScoped
class StoreService(
    private val minio: MinioClient,
    private val elasticSearchClient: RestClient,
    private val tikaParser: TikaParser
) {
    private val bucketName = "test"

    fun storeFiles(aFormData: FormData): String {
        createBucket()
        val files = aFormData.files
        if (files == null) {
            throw NoFileSentException()
        }
        if (files.size > 1) {
            throw TooManyFilesSentException()
        }
        return storeFile(files[0])
    }

    fun findFile(aFileName: String): GetObjectResponse {
        return minio.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(aFileName)
                .build()
        )
    }

    private fun createBucket() {
        if (minio.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            return
        }
        minio.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
    }


    private fun storeFile(aFileUpload: FileUpload): String {
        val inputStream = FileInputStream(aFileUpload.filePath().toString())
        val baos = ByteArrayOutputStream()
        inputStream.transferTo(baos)
        inputStream.close()
        val cloneForMinio: InputStream = ByteArrayInputStream(baos.toByteArray())
        val cloneForIndexing: InputStream = ByteArrayInputStream(baos.toByteArray())

        val response = minio.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(cloneForMinio, aFileUpload.size(), -1)
                .contentType(aFileUpload.contentType())
                .`object`(aFileUpload.fileName())
                .build()
        )
        val id = response.bucket() + "-" + response.`object`()
        val request = Request(
            "PUT",
            "/minio/_doc/$id"
        )
        val storedContent = StoredContent()
        val parsed = parseInputStream(cloneForIndexing)
        storedContent.id = id
        storedContent.content = parsed.text
        storedContent.metaData = metaDataParse(parsed.metadata)
        storedContent.name = response.`object`()
        storedContent.bucket = response.bucket()
        storedContent.etag = response.etag()
        request.setJsonEntity(JsonObject.mapFrom(storedContent).toString())
        elasticSearchClient.performRequest(request)
        cloneForMinio.close()
        cloneForIndexing.close()
        return aFileUpload.fileName()
    }

    private fun metaDataParse(tikaMetadata: TikaMetadata): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        tikaMetadata.names.forEach {
            val res = tikaMetadata.getValues(it)
            map[it] = res
            Log.info("$it :$res")
        }
        return map
    }

    private val listOfIndex = listOf("content", "bucket", "etag", "name", "metaData")
    fun searchFull(aInput: String): List<StoredContent> {
        val set = mutableSetOf<StoredContent>()
        set.addAll(search(listOfIndex, aInput))
        Log.info("set size is: ${set.size}")

        return set.toList()
    }

    @Throws(IOException::class)
    private fun search(aTerms: List<String>, aMatch: String): List<StoredContent> {
        val request = Request(
            "GET",
            "/minio/_search"
        )
        val terms = JsonArray()
        aTerms.forEach {
            terms.add(it)
        }

        /**
         * Query based on:  <a href="URL#https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html">link</a>
         **/
        val queryString = JsonObject().put("fields", terms)
        queryString.put("query", "*$aMatch*")
        val matchJson = JsonObject().put("query_string", queryString)
        val queryJson = JsonObject().put("query", matchJson)
        Log.info("query json: ${queryJson.encodePrettily()}")
        request.setJsonEntity(queryJson.encode())
        val response: Response = elasticSearchClient.performRequest(request)
        val responseBody: String = EntityUtils.toString(response.entity)
        val json = JsonObject(responseBody)
        val hits: JsonArray = json.getJsonObject("hits").getJsonArray("hits")
        val results: MutableList<StoredContent> = ArrayList(hits.size())
        for (i in 0 until hits.size()) {
            val hit: JsonObject = hits.getJsonObject(i)
            val storedContent: StoredContent = hit.getJsonObject("_source").mapTo(StoredContent::class.java)
            results.add(storedContent)
        }
        return results
    }

    fun listObjects(): List<String> {
        createBucket()
        val list: MutableList<String> = mutableListOf()
        minio.listObjects(ListObjectsArgs.builder().bucket(bucketName).build()).forEach {
            list.add(it.get().objectName())
        }
        return list
    }

    private fun parseInputStream(aInputStream: InputStream): TikaContent {
        return tikaParser.parse(aInputStream)
    }

}