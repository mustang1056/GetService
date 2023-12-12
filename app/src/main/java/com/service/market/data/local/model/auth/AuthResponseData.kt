package com.service.market.data.local.model.auth

data class AuthResponseData (
    val token: String,
    val user_id: Int,
    val phone_number: String,
    val avatar_image: String,
    val email: String,
    val error: Int,
    val text: String
)