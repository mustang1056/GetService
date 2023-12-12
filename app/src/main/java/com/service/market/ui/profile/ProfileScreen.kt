package com.service.market.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.auto.ingram.ui.navigation.BottomNavItem
import com.google.accompanist.coil.rememberCoilPainter
import com.service.market.R
import com.service.market.ui.tools.AppBarDetail
import com.service.market.viewmodels.AuthViewModel
import com.service.market.viewmodels.ServicesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private var user_id = 5
private var bitmap_photo: Bitmap? = null

@Composable
fun ProfileScreen(navController: NavController){

    val authViewModel = hiltViewModel<AuthViewModel>()


    var openDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    var image_path by remember {
        mutableStateOf("https://www.freeiconspng.com/img/3991")
    }

    val sharedPreference =  context.getSharedPreferences("account_info", Context.MODE_PRIVATE)
    val sharedPrefUrls =  context.getSharedPreferences("urls", Context.MODE_PRIVATE)

    val image_url =  sharedPrefUrls.getString("image_path","")


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var retry_password by remember {
        mutableStateOf("")
    }

    var error_text by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var isEmailValid by remember { mutableStateOf(false) }

    var isPasswordValid by remember { mutableStateOf(false) }
    var isRetryPasswordValid by remember { mutableStateOf(false) }
    var isPhoneValid by remember { mutableStateOf(false) }

    var isLoaded = false

    val coroutineScopeImage = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    var enabled by remember { mutableStateOf(true)}


    phone = sharedPreference.getString("phone","")!!
    email =  sharedPreference.getString("email","")!!
    password = sharedPreference.getString("password","")!!
    retry_password = sharedPreference.getString("password","")!!
    image_path = sharedPreference.getString("avatar_image","")!!

    var avatar_image by remember {
        mutableStateOf("")
    }



    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Add a single item
                    item {
                        ListViewMenu("Галерея", Modifier.clickable(){
                            launcher.launch("image/*")
                            openDialog.value = false
                        })
                    }
                    // Add another single item
                    item {
                        ListViewMenu("Сделать фото", Modifier.clickable(){
                            launcher.launch("image/*")
                            openDialog.value = false
                        })
                    }
                }

            },
            buttons = {}
        )

    }

    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver,it)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver,it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }

        bitmap.value?.let {  btm ->
            bitmap_photo = btm
            image_path = imageUri.toString()
            avatar_image = image_path

            isLoaded = true
        }

    }

    var openAlertDialog = remember { mutableStateOf(false) }

    if(image_path.length > 0 && isLoaded == false){
        avatar_image = image_url+image_path
    }
    else{
        avatar_image = image_path
    }

    if(openAlertDialog.value) {
        AlertDialogExample(
            navController,
            context = context,
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false

            },
            dialogTitle = "Выход",
            dialogText = "Вы действительно хотите выйти?",
            icon = Icons.Default.Info
        )
    }

    Scaffold() {

        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = avatar_image,
                    placeholder = painterResource(id = R.drawable.photo_icon),
                    error = painterResource(id = R.drawable.photo_icon),
                    contentDescription = "The delasign logo",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)

                        .clickable(
                            enabled = true,
                            onClickLabel = "Clickable image",
                            onClick = {
                                openDialog.value = true
                                //launcher.launch("image/*")
                            }
                        )
                )

                TextField(
                    value = email,
                    onValueChange = { newText ->
                        email = newText.toString()
                    },
                    label = { Text(text = "E-mail") },
                    placeholder = { Text(text = "E-mail") },
                    isError = isEmailValid,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    trailingIcon = {
                        if (isEmailValid == true) {
                            Icon(Icons.Filled.Warning, "error has occurred")
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    }
                )

                if (isEmailValid) {
                    Text(
                        text = "Введите корректный e-mail",
                        color = Color.Red,
                        modifier = Modifier.padding(start = 14.dp)
                    )
                }

                TextField(
                    value = password,
                    onValueChange = { newText ->
                        password = newText.toString()
                    },
                    label = { Text(text = "Пароль") },
                    placeholder = { Text(text = "Пароль") },
                    isError = isPasswordValid,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    trailingIcon = {
                        if (isEmailValid == true) {
                            Icon(Icons.Filled.Warning, "error has occurred")
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    visualTransformation = (if (showPassword) VisualTransformation.None else PasswordVisualTransformation()) as VisualTransformation
                )

                TextField(
                    value = retry_password,
                    onValueChange = { newText ->
                        retry_password = newText.toString()
                    },
                    label = { Text(text = "Повторите пароль") },
                    placeholder = { Text(text = "Повторите пароль") },
                    isError = isRetryPasswordValid,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    trailingIcon = {
                        if (isEmailValid == true) {
                            Icon(Icons.Filled.Warning, "error has occurred")
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    visualTransformation = (if (showPassword) VisualTransformation.None else PasswordVisualTransformation()) as VisualTransformation
                )

                if (isPasswordValid) {
                    Text(
                        text = error_text,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 14.dp)
                    )
                }

                TextField(
                    value = phone,
                    onValueChange = { newText ->
                        phone = newText.toString()
                    },
                    label = { Text(text = "Телефон") },
                    placeholder = { Text(text = "Укажите телефон") },
                    isError = isPhoneValid,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        if (isEmailValid == true) {
                            Icon(Icons.Filled.Warning, "error has occurred")
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = "Lock Icon"
                        )
                    },
                )

                if (isPhoneValid) {
                    Text(
                        text = "Укажите корректный 11-значный телефон",
                        color = Color.Red,
                        modifier = Modifier.padding(start = 14.dp)
                    )
                }

                Button(
                    onClick = {
                        val time = Calendar.getInstance().time

                        val formatter = SimpleDateFormat("yyyy-MM-dd_HHmmss")
                        val current = formatter.format(time)

                        val filename = "" + user_id + "" + current + ".jpg"

                        var is_error = false

                        if (email.isEmpty()) {
                            isEmailValid = true
                            is_error = true
                        } else {
                            isEmailValid = false
                        }

                        if (password.isEmpty()) {
                            isPasswordValid = true
                            is_error = true
                            error_text = "Введите пароль"
                        } else {
                            isPasswordValid = false
                        }

                        if (retry_password.isEmpty()) {
                            isRetryPasswordValid = true
                            is_error = true
                            error_text = "Повторите пароль"
                        } else {
                            isRetryPasswordValid = false
                        }

                        if (password.length < 4) {
                            isPasswordValid = true
                            is_error = true
                            error_text = "Пароль должен быть больше 3 символов"
                        } else {
                            isPasswordValid = false
                        }

                        if (phone.isEmpty() || phone.length < 11) {
                            isPhoneValid = true
                            is_error = true
                        } else {
                            isPhoneValid = false
                        }


                        if (!password.equals(retry_password)) {
                            isRetryPasswordValid = true
                            is_error = true
                            error_text = "Пароли не совпадают"
                        } else {
                            isRetryPasswordValid = false
                        }


                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            isEmailValid = true
                        } else {
                            isEmailValid = false
                        }

                        if (is_error == false) {
                            coroutineScope.launch {
                                authViewModel.updateUser(
                                    user_id,
                                    "",
                                    email.trim(),
                                    password.trim(),
                                    phone.trim()
                                ).collect {
                                    if (bitmap_photo != null) {
                                        createFile(
                                            filename,
                                            bitmap_photo!!,
                                            authViewModel,
                                            context,
                                            coroutineScopeImage
                                        )
                                    }
                                }

                                authViewModel._loginState.collect { loginUIState ->
                                    when (loginUIState) {
                                        is AuthViewModel.LoginUIState.Success -> {

                                            var editor = sharedPreference.edit()
                                            editor.putString("email", email)
                                            editor.putString("phone", phone)
                                            editor.putString("password", password)
                                            editor.putString("avatar_image", filename)

                                            editor.commit()

                                            Toast.makeText(
                                                context,
                                                "Изменения сохранены",
                                                Toast.LENGTH_LONG
                                            ).show()

                                        }
                                        is AuthViewModel.LoginUIState.Loading -> {}
                                        is AuthViewModel.LoginUIState.Error -> {
                                            //showDialog = true

                                        }
                                        is AuthViewModel.LoginUIState.Empty -> {

                                        }

                                    }
                                }
                            }


                        }


                    },
                    shape = RoundedCornerShape(size = 22.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 15.dp)
                        .padding(30.dp, 0.dp),
                ) {
                    Text(
                        text = "Сохранить",
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
            ) {
                Text("Выйти",
                    color = Color.Red,

                    modifier = Modifier
                        .clickable(enabled = enabled) {

                            openAlertDialog.value = true

                        })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    navController: NavController,
    context: Context,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()


                    val sharedPreference =  context.getSharedPreferences("account_info", Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putBoolean("is_auth", false)
                    editor.commit()


                    navController.navigate(BottomNavItem.AuthForm.screen_route)

                }
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Нет")
            }
        }
    )
}


fun createFile(filename: String, bitmap: Bitmap, authViewModel: AuthViewModel, context: Context, coroutineScope: CoroutineScope){

    //create a file to write bitmap data
    val f = File(context.getCacheDir(), filename);
    f.createNewFile();

    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 30 /*ignored for PNG*/, bos);
    val bitmapdata = bos.toByteArray();

    //write the bytes in file
    var fos: FileOutputStream? = null;
    try {
        fos = FileOutputStream(f);
    } catch (e: FileNotFoundException) {
        e.printStackTrace();
    }
    try {
        fos?.write(bitmapdata);
        fos?.flush();
        fos?.close();

        bos.flush()
        bos.close()

    } catch (e : IOException) {
        e.printStackTrace();
    }

    val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), f)
    val body = MultipartBody.Part.createFormData("image", f.name, reqFile)


    coroutineScope.launch {
        authViewModel.addImage(body).collect{

            val sharedPreference = context.getSharedPreferences(
                "account_info",
                Context.MODE_PRIVATE
            )

            var editor = sharedPreference.edit()
            editor.putString("avatar_image", filename)
            editor.commit()

        }
    }

    /*
    val service = Retrofit.Builder()
        .baseUrl("http://172.20.10.2:8081/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = service.create(FileUploadService::class.java)

    val req: Call<ResponseBody?>? = api.postImage(body)
    if (req != null) {
        req.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                //failure message
                t.printStackTrace()
            }
        })
    }*/


}

@Composable
fun ListViewMenu(
    text: String, modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, fontSize = 20.sp)
        }
    }
}