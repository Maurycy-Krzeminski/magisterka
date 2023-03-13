package org.maurycy.framework.math.resource

import io.quarkus.logging.Log
import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.maurycy.framework.math.model.StatisticInput
import org.maurycy.framework.math.model.StatisticOutput


@Path("/statistics")
class StatisticsResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user", "admin")
    fun getDescriptiveStatistics(x: StatisticInput): Any {
        val stats = DescriptiveStatistics()
        x.inputArray.forEach { value ->
            stats.addValue(value)
        }
        Log.info(stats)

        return StatisticOutput(
            stats
        )
    }
}