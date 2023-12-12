package com.service.market.data.local.model.auth

data class Users (
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone_number: String
)