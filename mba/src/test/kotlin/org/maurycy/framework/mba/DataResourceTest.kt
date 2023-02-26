package org.maurycy.framework.mba

import io.quarkus.test.TestTransaction
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import javax.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.maurycy.framework.mba.model.DataDto
import org.maurycy.framework.mba.model.DataInput
import org.maurycy.framework.mba.resource.DataResource

@QuarkusTest
@TestHTTPEndpoint(DataResource::class)
class DataResourceTest {
    private val hexString = "63e79d0ae5b643052ff92664"
    private val map1 = mapOf(Pair("1", "a"), Pair("2", "b"), Pair("3", "c"))
    private val map1String = "\"data\":{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\"}"
    private val map2 = mapOf(Pair("4", "d"), Pair("5", "e"), Pair("6", "f"))
    private val map2String = "\"data\":{\"4\":\"d\",\"5\":\"e\",\"6\":\"f\"}"
    private val failedToBuildObjectId = "{\"error\":\"Failed to build object id exception\"}"

    @Test
    @TestTransaction
    fun getAllTest() {
        RestAssured.given()
            .`when`().get()
            .then()
            .statusCode(200)
            .body(CoreMatchers.`is`("[]"))
    }

    @Test
    @TestTransaction
    fun getByIdFailedToBuildObjectIdTest() {
        RestAssured.given()
            .`when`()
            .get("/aaa")
            .then()
            .statusCode(400)
            .body(CoreMatchers.`is`(failedToBuildObjectId))
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
            .body(DataInput(hexString, map1))
            .`when`().post()
            .then()
            .statusCode(201)
            .body(
                CoreMatchers.containsString("id"),
                CoreMatchers.containsString(map1String)
            ).extract().body().`as`(DataDto::class.java)

        RestAssured.given()
            .`when`()
            .get("/${body.id}")
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("\"id\":\"${body.id}\""),
                CoreMatchers.containsString(map1String)
            )
    }

    @Test
    @TestTransaction
    fun deleteDataTest() {
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(hexString, map1))
            .`when`().post()
            .then()
            .statusCode(201)
            .body(
                CoreMatchers.containsString("id"),
                CoreMatchers.containsString(map1String)
            ).extract().body().`as`(DataDto::class.java)

        for (i in 1..3) {
            RestAssured.given()
                .`when`()
                .delete("/${body.id}")
                .then()
                .statusCode(204)
                .body(CoreMatchers.`is`(""))
        }
    }

    @Test
    @TestTransaction
    fun putDataTest() {
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(hexString, map1))
            .`when`().post()
            .then()
            .statusCode(201)
            .body(
                CoreMatchers.containsString("id"),
                CoreMatchers.containsString(map1String)
            ).extract().body().`as`(DataDto::class.java)

        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(hexString, map2))
            .`when`()
            .put("/${body.id}")
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("\"id\":\"${body.id}\""),
                CoreMatchers.containsString(map2String)
            )
    }

    @Test
    @TestTransaction
    fun putDataFailedToBuildObjectIdTest() {
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(hexString, map2))
            .`when`()
            .put("/aaa")
            .then()
            .statusCode(400)
            .body(CoreMatchers.`is`(failedToBuildObjectId))
    }

    @Test
    @TestTransaction
    fun putDataFailedToFindObjectIdTest() {
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(hexString, map2))
            .`when`()
            .put("/$hexString")
            .then()
            .statusCode(204)
            .body(CoreMatchers.`is`(""))
    }
}