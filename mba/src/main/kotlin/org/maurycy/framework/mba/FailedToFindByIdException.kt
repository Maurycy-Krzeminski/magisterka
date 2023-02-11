package org.maurycy.framework.mba

import org.bson.types.ObjectId

class FailedToFindByIdException(id: ObjectId) : Exception("Failed to find object with id: $id")
