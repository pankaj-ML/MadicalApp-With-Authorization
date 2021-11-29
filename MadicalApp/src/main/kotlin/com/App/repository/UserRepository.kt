package com.App.repository

import com.App.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User,Long>{
    @Query(value="{'email' : ?0}")
    fun findbyEmail(email: String): User?


}