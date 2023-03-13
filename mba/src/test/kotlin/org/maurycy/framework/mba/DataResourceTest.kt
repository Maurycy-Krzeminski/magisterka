package org.maurycy.framework.mba

import com.github.javafaker.Faker
import io.quarkus.test.TestTransaction
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
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
    var faker = Faker()
    private val map1 = mapOf(Pair("1", "a"), Pair("2", "b"), Pair("3", "c"))
    private val map1String = "\"data\":{\"1\":\"a\",\"2\":\"b\",\"3\":\"c\"}"
    private val map2 = mapOf(Pair("4", "d"), Pair("5", "e"), Pair("6", "f"))
    private val map2String = "\"data\":{\"4\":\"d\",\"5\":\"e\",\"6\":\"f\"}"

    @Test
    @TestTransaction
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun getAllTest() {
        RestAssured.given()
            .`when`().get()
            .then()
            .statusCode(200)
            .body(CoreMatchers.`is`("[]"))
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun getByIdFailedToBuildObjectIdTest() {
        RestAssured.given()
            .`when`()
            .get("/aaa")
            .then()
            .statusCode(204)
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun getByIdFailedToFindObjectIdTest() {
        val id = faker.animal().name()
        RestAssured.given()
            .`when`()
            .get("/$id")
            .then()
            .statusCode(204)
            .body(CoreMatchers.`is`(""))
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun addDataTest() {
        val id = faker.animal().name()
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map1))
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
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun deleteDataTest() {
        val id = faker.animal().name()
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map1))
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
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun putDataTest() {
        val id = faker.animal().name()
        val body = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map1))
            .`when`().post()
            .then()
            .statusCode(201)
            .body(
                CoreMatchers.containsString("id"),
                CoreMatchers.containsString(map1String)
            ).extract().body().`as`(DataDto::class.java)

        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map2))
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
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun putDataFailedToBuildObjectIdTest() {
        val id = faker.animal().name()
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map2))
            .`when`()
            .put("/aaa")
            .then()
            .statusCode(204)
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "testUser", roles = ["admin","user"])
    fun putDataFailedToFindObjectIdTest() {
        val id = faker.animal().name()
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(DataInput(id, map2))
            .`when`()
            .put("/$id")
            .then()
            .statusCode(204)
            .body(CoreMatchers.`is`(""))
    }
}