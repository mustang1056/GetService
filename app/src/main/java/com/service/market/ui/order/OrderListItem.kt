@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.getdriver.ui.order

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.auto.ingram.ui.navigation.BottomNavItem

import com.google.accompanist.coil.rememberCoilPainter
import com.service.market.R
import com.service.market.data.local.model.orders.Orders
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ListViewItem(
    navController: NavController,
    orderItem: Orders,
    onItemClicked: (item: Orders) -> Unit
) {
    ListViewItem(orderItem = orderItem, modifier = Modifier
        .clickable() {
            onItemClicked(orderItem)
            navController.navigate(BottomNavItem.RemontDetail.screen_route)
        })
}


@Composable
fun ListViewItem(
    orderItem: Orders, modifier: Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth(),
        elevation = 1.dp) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            //RemontImageBanner(imagePath = remontItem.image)
            RemontMetadataItem(orderItem = orderItem)
        }
    }




}

@Composable
fun RemontImageBanner(imagePath: String) {

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

fun Date.getTimeAgo(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val currentCalendar = Calendar.getInstance()

    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)

    return if (year < currentYear ) {
        val interval = currentYear - year
        if (interval == 1) "$interval год назад" else "$interval лет назад"
    } else if (month < currentMonth) {
        val interval = currentMonth - month
        if (interval == 1) "$interval мес. назад" else "$interval мес. назад"
    } else  if (day < currentDay) {
        val interval = currentDay - day
        if (interval == 1) "$interval час назад" else "$interval дн. назад"
    } else if (hour < currentHour) {
        val interval = currentHour - hour
        if (interval == 1) "$interval час назад" else "$interval час назад"
    } else if (minute < currentMinute) {
        val interval = currentMinute - minute
        if (interval == 1) "$interval мин. назад" else "$interval мин. назад"
    } else {
        "недавно"
    }
}

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemontMetadataItem(orderItem: Orders) {

    var image_path by remember {
        mutableStateOf("https://www.freeiconspng.com/img/3991")
    }
    val context = LocalContext.current.applicationContext

    val sharedPrefUrls =  context.getSharedPreferences("urls", Context.MODE_PRIVATE)

    val image_url =  sharedPrefUrls.getString("image_path","")

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        ListItem(
            text = { Text(orderItem.service_title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            secondaryText = { Text(orderItem.title, fontSize = 18.sp, maxLines = 3) },
            icon = {
                AsyncImage(
                    model = image_url+orderItem.avatar_image,
                    placeholder = painterResource(id = R.drawable.photo_icon),
                    error = painterResource(id = R.drawable.photo_icon),
                    contentDescription = "The delasign logo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                   },
            trailing = {
                val date = orderItem.date_time

                val dateFormat_yyyyMMddHHmmss = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH
                )
                val datetime = dateFormat_yyyyMMddHHmmss.parse(date)


                Column {
                    Text(
                        "" + orderItem.cost + "",
                        color = Color.Red, fontSize = 20.sp
                    )
                    Text(
                        datetime.getTimeAgo(),
                        color = Color.Gray, fontSize = 10.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        )
    }
}