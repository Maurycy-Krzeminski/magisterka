package org.maurycy.framework.mba.entities

import java.io.Serializable

class DataInput : Serializable {
    constructor(data: Map<String, String>?) {
        this.data = data
    }

    constructor()

    var data: Map<String, String>? = null
}
