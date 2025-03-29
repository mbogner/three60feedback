package dev.mbo.t60f.global

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringSetConverter : AttributeConverter<Set<String>, String> {

    override fun convertToDatabaseColumn(obj: Set<String>?): String? {
        if (obj.isNullOrEmpty()) return null
        return obj.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): Set<String> {
        return dbData
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toSet()
            ?: emptySet()
    }

}