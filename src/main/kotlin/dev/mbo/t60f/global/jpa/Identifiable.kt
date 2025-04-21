package dev.mbo.t60f.global.jpa

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Transient
import java.io.Serializable

interface Identifiable<T : Serializable> : Serializable {

    @Transient
    @JsonIgnore
    fun getIdentifier(): T?

    fun setIdentifier(id: T?)

}