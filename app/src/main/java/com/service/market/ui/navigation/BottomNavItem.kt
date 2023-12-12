package com.auto.ingram.ui.navigation

import com.service.market.R


sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Order : BottomNavItem("Заказы", R.drawable.icons8_order_24,"order")
    object MyOrders: BottomNavItem("Мои заказы",R.drawable.icons8_order_completed_24,"my_orders")
    object Profile: BottomNavItem("Профиль",R.drawable.icons8_male_user_24,"profile")

    object Create : BottomNavItem("Создать", R.drawable.icons8_order_24,"create")


    object RemontDetail : BottomNavItem("remontDetail", R.drawable.ic_launcher_background, "remontDetail")

    object RemontList : BottomNavItem("remontList", R.drawable.ic_launcher_background, "RemontList")

    object RemontAddForm : BottomNavItem("remontForm", R.drawable.ic_launcher_background, "RemontForm")

    object MyOrdersDetails : BottomNavItem("myOrderDetails", R.drawable.ic_launcher_background, "MyOrderDetails")


    object CategoryFilter : BottomNavItem("categoryFilter", R.drawable.ic_launcher_background, "CategoryFilter")

    object RegistrForm : BottomNavItem("registrForm", R.drawable.ic_launcher_background, "RegistrForm")

    object AuthForm : BottomNavItem("authForm", R.drawable.ic_launcher_background, "AuthForm")

}