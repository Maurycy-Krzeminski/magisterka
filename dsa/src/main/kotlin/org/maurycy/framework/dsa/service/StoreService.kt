package org.maurycy.framework.dsa.service


import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.GetObjectResponse
import io.minio.ListObjectsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.quarkus.logging.Log
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.enterprise.context.ApplicationScoped
import org.apache.http.util.EntityUtils
import org.elasticsearch.client.Request
import org.elasticsearch.client.Response
import org.elasticsearch.client.RestClient
import org.jboss.resteasy.reactive.multipart.FileUpload
import org.maurycy.framework.dsa.model.StoredContent
import org.maurycy.framework.dsa.model.FormData


@ApplicationScoped
class StoreService(
    private val minio: MinioClient,
    private val restClient: RestClient
) {
    private val bucketName = "test"

    fun storeFiles(aFormData: FormData): String {
        createBucket()
        aFormData.files?.forEach {
            storeFile(it)
        }
        return "test"
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


    private fun storeFile(aFileUpload: FileUpload) {
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
        storedContent.id = id
        storedContent.content = streamToString(cloneForIndexing)
        storedContent.name = response.`object`()
        storedContent.bucket = response.bucket()
        storedContent.etag = response.etag()
        request.setJsonEntity(JsonObject.mapFrom(storedContent).toString())
        restClient.performRequest(request)
        cloneForMinio.close()
        cloneForIndexing.close()

    }

    private val listOfIndex = listOf("content", "bucket", "etag", "name")
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
        val response: Response = restClient.performRequest(request)
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

    private fun streamToString(aInputStream: InputStream): String {
        val textBuilder = StringBuilder()
        BufferedReader(InputStreamReader(aInputStream)).use { reader ->
            var c: Int
            while (reader.read().also { c = it } != -1) {
                textBuilder.append(c.toChar())
            }
        }
        return textBuilder.toString()
    }

}