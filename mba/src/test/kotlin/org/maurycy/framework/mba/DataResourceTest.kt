package org.maurycy.framework.mba

import io.quarkus.logging.Log
import io.quarkus.test.TestTransaction
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.restassured.RestAssured
import java.util.UUID
import javax.ws.rs.core.MediaType
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order

@QuarkusTest
@TestHTTPEndpoint(DataResource::class)
class DataResourceTest {
    private val hexString = "63e79d0ae5b643052ff92664"


    @Test
    @TestTransaction
    fun getAllTest() {
        Log.info(1)
        RestAssured.given()
            .`when`().get()
            .then()
            .statusCode(200)
            .body(CoreMatchers.`is`("[]"))
    }

    @Test
    @TestTransaction
    fun getByIdFailedToBuildObjectIdTest() {
        Log.info(2)
        RestAssured.given()
            .`when`()
            .get("/aaa")
            .then()
            .statusCode(400)
            .body(CoreMatchers.`is`("{\"error\":\"Failed to build object id exception\"}"))
    }

    @Test
    @TestTransaction
    fun getByIdFailedToFindObjectIdTest() {
        RestAssured.given()
            .`when`()
            .get("/$hexString")
            .then()
            .statusCode(204)
            .body(CoreMatchers.`is`(""))
    }

    @Test
    @TestTransaction
    fun addDataTest() {
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(mapOf(Pair("1", "a"), Pair("2", "b"), Pair("3", "c"))))
            .`when`().post()
            .then()
            .statusCode(201)
            .body(
                CoreMatchers.containsString("id"),
                CoreMatchers.containsString("\"data\":{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\"}")
            ).extract().body().`as`(DataDto::class.java)

        RestAssured.given()
            .`when`()
            .get("/${body.id}")
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("\"id\":\"${body.id}\""),
                CoreMatchers.containsString("\"data\":{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\"}")
            )
    }

    @Test
    @TestTransaction
    fun deleteDataTest() {
        Log.info(5)
        TODO()
    }

    @Test
    @TestTransaction
    fun putDataTest() {
        Log.info(6)
        TODO()
    }
}