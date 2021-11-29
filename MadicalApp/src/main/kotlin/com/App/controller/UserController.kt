package com.App.controller

import com.App.DTO.UserRegistrationDto
import com.App.DTO.UserloginDto
import com.App.Massage.Message
import com.App.model.User
import com.App.services.UserServices
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping
class UserController {
    @Autowired
    private lateinit var userServices: UserServices //call repository hea

    //................................................Register With DTO................................................
    @PostMapping("/register")
    fun Adduser(@RequestBody DTO: UserRegistrationDto): ResponseEntity<User> {
        val user = User()
        user.name = DTO.name
        user.email = DTO.email
        user.designation = DTO.designation
        user.password = DTO.password
        user.id = DTO.id
        return ResponseEntity.ok(userServices.addUser(user))
    }

    // .................................................LOGIN ...........................................................
    @PostMapping("/login")
    fun login(@RequestBody loginDto: UserloginDto, response: HttpServletResponse): ResponseEntity<Any> {
        // USER AND PASSWORD Authencation
        val user = userServices.findbyEmail(loginDto.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found!"))

        if (!user.comparePassword(loginDto.password)) {
            return ResponseEntity.badRequest().body(Message("Incorrect Password!"))
        }
        // return ResponseEntity.ok(user) // Return User
        //_____________________________________________________________________________________________________________

        // JWT TOKEN GENERATION.............................................................
        val issuer = user.id.toString()
        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        //return ResponseEntity.ok(jwt) // Return jwt token
        //_____________________________________________________________________________________________________________

        //Cookie code commented START // its give Cookie.....................................
        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return ResponseEntity.ok(Message("Login Success!"))
        //_____________________________________________________________________________________________________________


    }

    //.............................. GET USER BY TOKEN..................................................
    @GetMapping("/user")
    fun user(@RequestHeader("Authorization") jwt: String?): ResponseEntity<Any> {

        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
            //return ResponseEntity.ok(Message("Authenticated url"))
            //return ResponseEntity.ok(userServices.getAllUsers())
            return ResponseEntity.ok(userServices.getUserbyID(body.issuer.toInt().toLong()))
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }
    }

    // ........................................... LOGOUT..................................................
    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Logout successfully !"))
    }

}

//......................................DOING WITH SERVICE CLASS....................................................
//    //(1) POST ONE USER DATA
//    @PostMapping("/addUser")
//    fun AddUser(@RequestBody user: User): ResponseEntity<User> {
//        return ResponseEntity.ok(userServices.addUser(user))
//    }
//
//    //(2) POST ALL USER DATA (LIST OF USERS)
//    @PostMapping("/addUsers")
//    fun AddAllUsers(@RequestBody users: List<User>): ResponseEntity<List<User>> {
//        return ResponseEntity.ok(userServices.addUsers(users))
//    }
//
//    //(3) GET ALL USERS DATA
//    @GetMapping("/getUsers")
//    fun getAllUsers(): ResponseEntity<List<User?>> {
//        return ResponseEntity.ok(userServices.getAllUsers())
//    }
//
//    //(4) GET USER DARA BY ID
//    @GetMapping("/getUser/{id}")
//    fun getUserbyID(@PathVariable id: Long): ResponseEntity<Optional<User>> {
//        return ResponseEntity.ok(userServices.getUserbyID(id))
//    }
//
//    //(5) Delet BY ID
//    @DeleteMapping("/Delet/{id}")
//    fun deletbyID(@PathVariable id: Long): String {
//        userServices.deletbyID(id)
//        return "User Deleted with ID :$id"
//    }
//
//    //(6) Delet All
//    @DeleteMapping("/DeletAll")
//    fun deletAll(): String {
//        this.userServices.deletAll()
//        return "All DATA DELETED"
//    }
//
//    //(7) Updat By ID
//    @PutMapping("/updat/{id}")
//    fun upadebyID(@PathVariable id: Long,@RequestBody user: User): ResponseEntity<User>
//    {
//        return ResponseEntity.ok(userServices.updaebyID(id,user))
//    }
//
//    //(8) Find By Email
//    @GetMapping("/getbyEmail/{email}")
//    fun findByEmail(@PathVariable email : String): ResponseEntity<User>
//    {
//        return ResponseEntity.ok(userServices.findbyEmail(email))
//    }

////////////                       DOING IN CONTROLLER //////////////////////////////////////////////////////
//    //(1) GET ALL USERS DATA
//    @GetMapping("/getUsers")
//    fun getUsers(): ResponseEntity<List<User>> {
//        return ResponseEntity.ok(this.userRepository.findAll())
//    }
//
//    //(2) GET USER DARA BY ID
//    @GetMapping("/getUser/{id}")
//    fun getUsers(@PathVariable id: Long): ResponseEntity<User>
//    {
//        return ResponseEntity.ok(this.userRepository.findById(id).orElse(null))
//    }
//
//    //(3) POST ONE USER DATA
//    @PostMapping("/addUser")
//    fun AddUser(@RequestBody user: User): ResponseEntity<User>
//    {
//        return ResponseEntity.ok(this.userRepository.save(user))
//    }
//
//    //(4) POST ALL USER DATA (LIST OF USERS)
//    @PostMapping("/addUsers")
//    fun AddAllUsers(@RequestBody users : List<User>): ResponseEntity<List<User>>
//    {
//        return ResponseEntity.ok(this.userRepository.saveAll(users))
//    }
//
//    //(5) UPDATE USER DATA BY ID
//    @PutMapping("/updat/{id}")
//    fun updatUserbyID(@PathVariable id :Long, @RequestBody user: User):ResponseEntity<User>
//    {
//        var oldUserData =this.userRepository.findById(id).orElse(null)
//        oldUserData.id = user.id
//        oldUserData.name =user.name
//        oldUserData.designation=user.designation
//        oldUserData.email=user.email
//        oldUserData.password=user.password
//        return ResponseEntity.ok(this.userRepository.save(user))
//    }
//
//    //(6) DELET USER BY ID
//    @DeleteMapping("/Delet/{id}")
//    fun deletByID(@PathVariable id: Long): String
//    {
//        this.userRepository.deleteById(id)
//        return "Deleted User with ID  : $id"
//    }
//
//    //(7) DELET ALL
//    @DeleteMapping("/DeletAll")
//    fun deletAll(): String
//    {
//        this.userRepository.deleteAll()
//        return "All DATA DELETED"
//    }
