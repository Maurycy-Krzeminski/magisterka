package org.maurycy.framework.mba

import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

@GrpcService
class DataServiceGrpcImpl(
    private val dataRepository: DataRepository
) : DataServiceIF {
    override fun addData(request: AddDataRequest): Uni<AddDataReply> {
        return dataRepository.persist(DataDto(request.dataMap))
            .onItem().transform { persistedDataDto ->
                AddDataReply.newBuilder()
                    .setId(persistedDataDto.id.toString())
                    .build()
            }
    }

    override fun getDataById(request: GetDataRequest): Uni<GetDataReply> {
        return dataRepository.findById(request.id)
            .map { dataToReturn ->
                if (dataToReturn == null) {
                    throw NullPointerException()
                }
                GetDataReply.newBuilder()
                    .setId(dataToReturn.id.toString())
                    .putAllData(dataToReturn.data)
                    .build()
            }
    }

    override fun getAllData(request: GetAllDataRequest?): Multi<GetDataReply> {
        return dataRepository.findAll().stream().map { it: DataDto ->
            GetDataReply.newBuilder()
                .setId(it.id.toString())
                .putAllData(it.data)
                .build()
        };
    }
    
}