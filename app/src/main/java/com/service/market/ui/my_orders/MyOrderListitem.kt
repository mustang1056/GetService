package com.service.market.ui.my_orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.auto.ingram.ui.navigation.BottomNavItem

import com.google.accompanist.coil.rememberCoilPainter
import com.service.market.R
import com.service.market.data.local.model.orders.MyOrders


@Composable
fun ListMyOrderViewItem(
    navController: NavController,
    remontItem: MyOrders,
    onItemClicked: (item: MyOrders) -> Unit
) {
    ListViewMyOrderItem(remontItem = remontItem, modifier = Modifier
        .clickable() {
            onItemClicked(remontItem)
            navController.navigate(BottomNavItem.MyOrdersDetails.screen_route)
        })
}


@Composable
fun ListViewMyOrderItem(
    remontItem: MyOrders, modifier: Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth(),
        elevation = 2.dp) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            //RemontImageBanner(imagePath = remontItem.image)
            MyOrderMetadataItem(movieItem = remontItem)
        }
    }
}

@Composable
fun MyOrderImageBanner(imagePath: String) {

    Image(
        painter = rememberCoilPainter(
            request = imagePath),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,            // crop the image
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyOrderMetadataItem(movieItem: MyOrders) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        ListItem(
            text = { Text(movieItem.service_title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            secondaryText = { Text(movieItem.comment, fontSize = 18.sp) },
            trailing = {Text(""+movieItem.cost+"",
                color = Color.Red, fontSize = 20.sp)}
        )
    }
}