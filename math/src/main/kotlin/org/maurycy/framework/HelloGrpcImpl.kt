package org.maurycy.framework

import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Uni
import org.maurycy.framework.math.HelloGrpc
import org.maurycy.framework.math.HelloReply
import org.maurycy.framework.math.HelloRequest

@GrpcService
class HelloGrpcImpl : HelloGrpc {
    override fun sayHello(request: HelloRequest?): Uni<HelloReply> {
        return Uni.createFrom()
            .item(HelloReply.newBuilder().setMessage("test").build())
    }
}