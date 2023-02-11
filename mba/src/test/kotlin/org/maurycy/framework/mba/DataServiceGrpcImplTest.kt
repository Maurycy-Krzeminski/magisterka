package org.maurycy.framework.mba

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.quarkus.grpc.GrpcClient
import io.quarkus.test.junit.QuarkusTest
import java.time.Duration
import java.util.concurrent.TimeUnit
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail


@QuarkusTest
class DataServiceGrpcImplTest{

    private var channel: ManagedChannel? = null
    private var dataService: MutinyDataServiceGrpc.MutinyDataServiceStub? = null

    @BeforeEach
    fun init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9001).usePlaintext().build()
        dataService = MutinyDataServiceGrpc.newMutinyStub(channel)
    }

    @AfterEach
    @Throws(InterruptedException::class)
    fun cleanup() {
        channel!!.shutdown()
        channel!!.awaitTermination(10, TimeUnit.SECONDS)
    }

    @Test
    fun addData() {
        val addDataRequest = AddDataRequest.newBuilder().putData("1", "a")
            .putData("2", "b")
            .putData("3", "c")
            .build()
        dataService!!.addData(addDataRequest).map {
            try {
                ObjectId(it.id)
            } catch (aE: IllegalArgumentException) {
                fail(aE)
            }
        }
    }

    @Test
    fun getDataById() {
        val data = mapOf(Pair("1", "a"), Pair("2", "b"), Pair("3", "c"))
        val addDataRequest = AddDataRequest.newBuilder().putAllData(data)

            .build()
        val created = dataService!!.addData(addDataRequest).await().atMost(Duration.ofSeconds(5))

        val getDataRequest = GetDataRequest.newBuilder()
            .setId(created.id)
            .build()
        val response = dataService!!
            .getDataById(getDataRequest).await().indefinitely()

        assert(response.id == created.id)
        assert(response.dataMap == data)
    }

    @Test
    fun getAllData() {
        val data = mapOf(Pair("1", "a"), Pair("2", "b"), Pair("3", "c"))
        val addDataRequest = AddDataRequest.newBuilder().putAllData(data).build()
        val created = dataService!!.addData(addDataRequest).await().atMost(Duration.ofSeconds(5))

        val getAllDataRequest = GetAllDataRequest.newBuilder().build()
        dataService!!.getAllData(getAllDataRequest).map {
            assert(it.id == created.id)
            assert(it.dataMap == data)
        }
    }
}