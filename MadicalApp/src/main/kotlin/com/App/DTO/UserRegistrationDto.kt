package com.App.DTO

class UserRegistrationDto {

    var name: String? = null
    var designation: String? = null
    var email: String? = null
    var password: String? = null
    var id: Long? =null

    constructor() {}
    constructor(name: String?, designation: String?, email: String?, password: String?, id: Long?) {
        this.name = name
        this.designation = designation
        this.email = email
        this.password = password
        this.id = id
    }

}


