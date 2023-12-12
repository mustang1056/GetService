package com.service.market.data.local.model.orders

import com.google.gson.annotations.SerializedName

data class OrderList(
    @SerializedName("page")
    val pageNumber: Int = 0,
    val content: List<Orders>,
    val last: Boolean
)

/*
data class PageData (
    val pageNumber : Int,
    val pageSize: Int,
    val sort: List<SortData>
    )


data class SortData(
    val empty: Boolean,
    val unsorted: Boolean,
    val sorted: Boolean,

)*/