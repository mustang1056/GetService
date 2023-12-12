package com.service.market.data.local.model.service

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class Services(
    val title: String
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}