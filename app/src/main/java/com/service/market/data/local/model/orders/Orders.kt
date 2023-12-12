package com.service.market.data.local.model.orders

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orders")
data class Orders(
    val title: String,
    val comment: String,
    val cost: Double,
    val date_time: String,
    val service_id: Int,
    val sub_service_id: Int,
    val user_id: Int,
    val phone_number: String,
    val service_title: String,
    val avatar_image: String
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}