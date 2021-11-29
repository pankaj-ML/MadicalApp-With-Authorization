package com.App.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Document("USER")
class User {



    @Id
    var id: Long? =null

    var name: String? =null

    var designation: String? =null

    @Indexed(unique=true)
    var email: String? =null

    var password: String? =null
        @JsonIgnore
        get() = field
        set(value) {
             val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }
    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password,this.password)
    }

    //var roles: Collection<Role>? = null

    constructor(name: String?, designation: String?, email: String?, password: String?) {
        this.name = name
        this.designation = designation
        this.email = email
        this.password = password
            //this.roles = roles
    }

    constructor()
}