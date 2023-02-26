package org.maurycy.framework.mba.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepositoryBase
import javax.enterprise.context.ApplicationScoped
import org.maurycy.framework.mba.model.DataDto

@ApplicationScoped
class DataRepository : ReactivePanacheMongoRepositoryBase<DataDto, String>