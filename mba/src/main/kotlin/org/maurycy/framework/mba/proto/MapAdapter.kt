package org.maurycy.framework.mba.proto

import org.infinispan.protostream.annotations.ProtoAdapter
import org.infinispan.protostream.annotations.ProtoFactory
import org.infinispan.protostream.annotations.ProtoField


@ProtoAdapter(HashMap::class)
class MapAdapter {
    @ProtoFactory
    fun create(entries: List<Entry>): HashMap<String, String> {
        val map = HashMap<String, String>()
        for (entry in entries) {
            map[entry.key] = entry.value
        }
        return map
    }

    @ProtoField(1)
    fun getEntries(map: HashMap<String, String>): List<Entry> {
        return map.entries.stream()
            .map { e -> Entry(e.key, e.value) }
            .toList()
    }

    class Entry @ProtoFactory constructor(@get:ProtoField(1) var key: String, @get:ProtoField(2) var value: String)
}