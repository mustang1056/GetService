package com.service.market

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.auto.ingram.ui.navigation.MainWindows
import com.service.market.ui.theme.GetServiceTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference = applicationContext.getSharedPreferences(
            "urls",
            Context.MODE_PRIVATE
        )

        var editor = sharedPreference.edit()
        editor.putString("image_path", "http://192.168.1.38:8082/auth/upload/")
        editor.commit()

        setContent {
            GetServiceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val contextForToast = LocalContext.current.applicationContext

                    val sharedPreference = contextForToast.getSharedPreferences(
                        "account_info",
                        Context.MODE_PRIVATE
                    )
                /*
                    var editor = sharedPreference.edit()
                    editor.putString("token", "")
                    editor.putInt("user_id", 0)
                    editor.putBoolean("is_auth", false)
                    editor.commit()
                */

                    MainWindows()

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GetServiceTheme {
        Greeting("Android")
    }
}