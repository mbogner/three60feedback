package dev.mbo.t60f.domain.user.converter

import dev.mbo.t60f.domain.user.Role
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class RoleListConverter : AttributeConverter<List<Role>, String> {

    override fun convertToDatabaseColumn(list: List<Role>?): String? {
        if (list.isNullOrEmpty()) return null
        return list.joinToString(",") { it.name }
    }

    override fun convertToEntityAttribute(str: String?): List<Role> {
        if (str.isNullOrEmpty()) return emptyList()
        return str.split(',').map { Role.valueOf(it.uppercase()) }.toList()
    }
}