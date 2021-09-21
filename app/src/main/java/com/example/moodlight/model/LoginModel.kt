package com.example.moodlight.model

class LoginModel {
    public lateinit var email : String
    public lateinit var password : String
    public lateinit var accessToken : String

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}