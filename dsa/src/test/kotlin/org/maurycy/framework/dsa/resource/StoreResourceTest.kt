package org.maurycy.framework.dsa.resource

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import java.io.File
import javax.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@QuarkusTest
@TestHTTPEndpoint(StoreResource::class)
class StoreResourceTest {
    private val fileName = "test.txt"

    @Test
    fun uploadFileFailedWhenEmpty() {
        RestAssured.given()
            .`when`().post()
            .then()
            .statusCode(415)
            .body(CoreMatchers.`is`(""))
    }

    @Test
    fun uploadFile() {
        val file = File(
            javaClass.classLoader.getResource(fileName)?.file ?: fail("File $fileName not found in resource directory")
        )
        RestAssured.given()
            .multiPart("files", file)
            .accept(MediaType.APPLICATION_JSON)
            .`when`().post()
            .then()
            .statusCode(201)
            .header("Location", "http://localhost:8081/store/$fileName")
            .body(CoreMatchers.`is`(""))
    }

    @Test
    fun downloadFile() {
        val file = File(
            javaClass.classLoader.getResource(fileName)?.file ?: fail("File $fileName not found in resource directory")
        )
        RestAssured.given()
            .multiPart("files", file)
            .accept(MediaType.APPLICATION_JSON)
            .`when`().post()


        RestAssured.given()
            .accept("*/*")
            .get(fileName)
            .then()
            .statusCode(200)
            .body(CoreMatchers.`is`(""))
            .header("Content-Disposition", "attachment;filename=$fileName")

    }

    @Test
    fun getAll() {
        RestAssured.given()
            .`when`().get()
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("["),
                CoreMatchers.containsString("]")
            )
    }

    @Test
    fun searchForFile() {
    }
}