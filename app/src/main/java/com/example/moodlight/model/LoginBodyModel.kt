package com.example.moodlight.model

class LoginBodyModel {
    public lateinit var email : String
    public lateinit var password : String
    public lateinit var firebaseToken : String

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    constructor(email: String, password: String, accessToken : String) {
        this.email = email
        this.password = password
        this.firebaseToken = accessToken
    }

}