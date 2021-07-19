package com.example.moodlight.Hash

import java.math.BigInteger
import java.security.MessageDigest

object sha {
    @Throws(Exception::class)
    fun encryptSHA(data: String?): String {
        val sha: MessageDigest = MessageDigest.getInstance("SHA-256")
        sha.update(data!!.toByteArray())

        return BigInteger(1, sha.digest()).toString(16)
    }
}