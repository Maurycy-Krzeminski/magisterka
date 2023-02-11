package org.maurycy.framework.mba

import java.io.Serializable

class DataInput : Serializable {
    constructor(data: Map<String, String>?) {
        this.data = data
    }

    constructor()

    var data: Map<String, String>? = null
}
