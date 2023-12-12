package com.service.market.data.local.model.orders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nextKey: Int?,
    val isEndReached: Boolean
)