package com.service.market.ui.tools

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auto.ingram.ui.navigation.BottomNavItem
import com.service.market.R

@Composable
fun AppBarDetail(navController: NavController) {
    val context = LocalContext.current.applicationContext

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {

                val sharedPreference = context.getSharedPreferences(
                    "back_stack",
                    Context.MODE_PRIVATE
                )

                var editor = sharedPreference.edit()
                editor.putBoolean("back", true)
                editor.commit()

                navController.popBackStack()

            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = colorResource(R.color.purple_200),
        contentColor = Color.White,
        elevation = 10.dp
    )
}

@Composable
fun AppBarRegist(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = colorResource(R.color.purple_200),
        contentColor = Color.White,
        elevation = 10.dp
    )
}


@Composable
fun BasicBarDetail(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        backgroundColor = colorResource(R.color.purple_200),
        contentColor = Color.White,
        elevation = 10.dp
    )
}