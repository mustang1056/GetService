package com.service.market.ui.order

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.service.market.R
import com.service.market.data.local.model.orders.Orders
import com.service.market.ui.tools.AppBarDetail


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderDetails(movieItem: Orders, navController: NavController
){
    Scaffold(
        topBar = { AppBarDetail(navController = navController) }) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            RemontDetailsBanner(movieItem = movieItem)

            RemontDetailsText(movieItem = movieItem)


        }


        /*
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            RemontDetailsBanner(movieItem = movieItem)
            RemontDetailsText(movieItem = movieItem)
        }*/

    }
}

@Composable
fun RemontDetailsBanner(movieItem: Orders){
    /*
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)) {
        Image(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),painter = rememberCoilPainter(request = movieItem.image_group) , contentDescription = "")
    }*/
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)) {

        /*
        Image(
            painter = rememberCoilPainter(request = "http://172.20.10.2:8081/images/download/"+movieItem.image_group,
                previewPlaceholder = R.drawable.ic_home),

            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,            // crop the image
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
        )
        Text(
            text = movieItem.blog_name,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold

        )*/
    }
}


@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {

    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp)) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = label,
                modifier = Modifier.height(24.dp),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color.Black

            )
        }


        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }

        Text(
            text = value,
            style = style,
            fontSize = 20.sp
        )


    }
}

@Composable
fun RemontDetailsText(movieItem: Orders){

    val uriHandler = LocalUriHandler.current

    val context = LocalContext.current.applicationContext

    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .weight(1f, false)) {

            ProfileProperty("Категория", movieItem.service_title, false)
            Divider(Modifier.padding(top = 1.dp, bottom = 1.dp),color = colorResource(R.color.gray))
            ProfileProperty("Описание", movieItem.title, false)
            Divider(Modifier.padding(top = 1.dp, bottom = 1.dp),color = colorResource(R.color.gray))
            ProfileProperty("Комментарий", movieItem.comment, false)
            Divider(Modifier.padding(top = 1.dp, bottom = 1.dp),color = colorResource(R.color.gray))
            ProfileProperty("Стоймость", movieItem.cost.toString(), false)


        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        ) {
            Button(
                onClick = {

                    //uriHandler.openUri(movieItem.link_to_group)
                    val intent = Intent(Intent.ACTION_DIAL);
                    intent.data = Uri.parse("tel:" + movieItem.phone_number)
                    context.startActivity(intent)


                },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .height(45.dp)
                    .width(170.dp),
            ) {
                Text(
                    text = "Позвонить",
                    fontSize = 16.sp
                )
            }



            Button(
                onClick = {
                    try {
                        val sendIntent = Intent("android.intent.action.MAIN")
                        sendIntent.component =
                            ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                        sendIntent.putExtra(
                            "jid",
                            PhoneNumberUtils.stripSeparators(movieItem.phone_number) + "@s.whatsapp.net"
                        ) //phone number without "+" prefix

                        context.startActivity(sendIntent)

                    } catch (e: Exception) {
                        Toast.makeText(context, "Установите WhatsApp", Toast.LENGTH_LONG).show()
                    }

                },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .height(45.dp)
                    .width(170.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF25D366))
            ) {
                Text(
                    text = "WhatsApp",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }

}