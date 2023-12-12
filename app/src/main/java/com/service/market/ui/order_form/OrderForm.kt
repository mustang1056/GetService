package com.example.getdriver.ui.order_form

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.auto.ingram.ui.navigation.BottomNavItem
import com.example.getdriver.viewmodels.OrdersFormViewModel
import com.service.market.data.local.model.service.Services
import com.service.market.ui.tools.AppBarDetail
import com.service.market.viewmodels.AuthViewModel
import com.service.market.viewmodels.ServicesViewModel
import kotlinx.coroutines.launch


/*
@Composable
fun MyUI() {
    val london = LatLng(51.52061810406676, -0.12635325270312533)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(london, 10f)
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
        ){


            MarkerInfoWindow(
                state = MarkerState(position = london)) { marker ->
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(35.dp, 35.dp, 35.dp, 35.dp)
                        )
                    ,
                ) {




                }

            }
        }
    }}
*/



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyUI(navController : NavController, service_id: Int, service_title: String) {


    val viewModel = hiltViewModel<OrdersFormViewModel>()
    val servicesViewModel = hiltViewModel<ServicesViewModel>()

    val services by servicesViewModel.servicesVal.collectAsState(initial = listOf())
    val context = LocalContext.current.applicationContext


    val coroutineScope = rememberCoroutineScope()

    var service_title by remember {
        mutableStateOf(service_title)
    }

    var category_id by remember {
        mutableStateOf(service_id)
    }

    var title by remember {
        mutableStateOf("")
    }

    var comment by remember {
        mutableStateOf("")
    }

    var cost by remember {
        mutableStateOf("")
    }

    var isTitleValid by remember { mutableStateOf(false) }
    var isCommentValid by remember { mutableStateOf(false) }
    var isCostValid by remember { mutableStateOf(false) }

    val sharedPreference =  context.getSharedPreferences("account_info", Context.MODE_PRIVATE)

    val image_path = sharedPreference.getString("avatar_image","")!!
    val phone_number =  sharedPreference.getString("phone", "")
    val user_id =  sharedPreference.getInt("user_id", 0)


    var textStr: String? = null

    /*
    AndroidBus.behaviorSubject.subscribe() {
        if(it is String) {
            textStr = it
            from_addr = textStr as String

            if (modalBottomSheetState.isVisible){
                coroutineScope.launch { modalBottomSheetState.hide() }
            }


        }
    }*/

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val contextForToast = LocalContext.current.applicationContext

    //focusManager.clearFocus()
    Scaffold(
        topBar = { AppBarDetail(navController = navController) }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)

                .verticalScroll(rememberScrollState())
        ) {

            // Declaring a boolean value to store
            // the expanded state of the Text Field
            var mExpanded by remember { mutableStateOf(false) }

            // Create a list of cities

            // Create a string value to store the selected city
            var mSelectedText by remember { mutableStateOf(service_title) }

            var mSelectedNumber by remember { mutableStateOf(0) }

            var mTextFieldSize by remember { mutableStateOf(Size.Zero)}


            // Up Icon when expanded and down icon when collapsed
            val icon = if (mExpanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            Column() {

                // Create an Outlined Text Field
                // with icon and not expanded
                TextField(
                    value = mSelectedText,
                    onValueChange = { mSelectedText = it },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{mExpanded = !mExpanded}
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = {Text("Услуга")},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    trailingIcon = {
                        Icon(icon,"contentDescription",
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )

                // Create a drop-down menu with list of cities,
                // when clicked, set the Text Field text as the city selected
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                ) {
                    services.forEach { label ->
                        DropdownMenuItem(onClick = {
                            mSelectedText = label.title
                            mSelectedNumber = label.id
                            mExpanded = false

                            category_id = mSelectedNumber
                        }) {
                            Text(text = label.title)
                        }
                    }
                }
            }

            TextField(
                value = title,
                onValueChange = { newText ->
                    title = newText
                },
                isError = isTitleValid,
                label = { Text(text = "Название") },
                placeholder = { Text(text = "Название товара или услуги") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {


                                }
                            }
                        }
                    }
            )

            if (isTitleValid) {
                Text(
                    text = "Введите название",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            TextField(
                value = comment,
                onValueChange = { newText ->
                    comment = newText
                },
                isError = isCommentValid,
                label = { Text(text = "Описание работ") },
                placeholder = { Text(text = "Описание работ") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {


                                }
                            }
                        }
                    }
            )

            if (isCommentValid) {
                Text(
                    text = "Введите описание",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            TextField(
                value = cost,
                label = { Text(text = "Стоймость") },
                isError = isCostValid,
                placeholder = { Text(text = "Укажите стоймость") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                onValueChange = {
                    if (it.isEmpty()) {
                        cost = it
                    } else {
                        cost = when (it.toDoubleOrNull()) {
                            null -> cost //old value
                            else -> it   //new value
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (isCostValid) {
                Text(
                    text = "Укажите стоймость",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }


            Button(
                onClick = {

                    var is_error = false

                    if (title.isEmpty()) {
                        isTitleValid = true
                        is_error = true
                    } else {
                        isTitleValid = false
                    }

                    if (comment.isEmpty()) {
                        isCommentValid = true
                        is_error = true
                    } else {
                        isCommentValid = false
                    }


                    if (cost.isEmpty()) {
                        isCostValid = true
                        is_error = true
                    } else {
                        isCostValid = false
                    }

                    if(is_error == false) {
                        coroutineScope.launch {
                            if (phone_number != null) {
                                viewModel.addOrder(
                                    title.trim(),
                                    comment.trim(),
                                    cost.trim().toDouble(),
                                    category_id,
                                    phone_number,
                                    mSelectedText,
                                    image_path,
                                    user_id
                                ).collect {

                                }
                            }

                            viewModel._loginState.collect { loginUIState ->
                                when (loginUIState) {
                                    is OrdersFormViewModel.LoginUIState.Success -> {
                                        Toast.makeText(contextForToast, "Ваш заказ отправлен", Toast.LENGTH_LONG).show()
                                        navController.navigate(BottomNavItem.RemontList.screen_route)
                                    }
                                    is OrdersFormViewModel.LoginUIState.Loading -> {

                                    }
                                    is OrdersFormViewModel.LoginUIState.Error -> {

                                    }
                                    is OrdersFormViewModel.LoginUIState.Empty -> {

                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(size = 22.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 15.dp),
            ) {
                Text(
                    text = "Заказать",
                    fontSize = 16.sp
                )

            }
        }

}




@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout() {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.Expanded },
        skipHalfExpanded = false
    )

    var isSheetFullScreen by remember { mutableStateOf(false) }
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp


    val modifier = if (isSheetFullScreen)
        Modifier
            .fillMaxSize()
    else
        Modifier.fillMaxWidth()

    BackHandler(modalSheetState.isVisible) {
        //coroutineScope.launch { modalSheetState.hide() }
    }


}



@Composable
fun FragmentContainer(
    modifier: Modifier = Modifier,
    fragmentManager: FragmentManager,
    commit: FragmentTransaction.(containerId: Int) -> Unit
) {
    val containerId by rememberSaveable {
        mutableStateOf(View.generateViewId()) }
    var initialized by rememberSaveable { mutableStateOf(false) }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FragmentContainerView(context)
                .apply { id = containerId }
        },
        update = { view ->
            //if (!initialized) {
                fragmentManager.commit { commit(view.id) }
                initialized = true
            //} else {
              //  fragmentManager.onContainerAvailable(view)
            //}
        }
    )
}

    /*
/** Access to package-private method in FragmentManager through reflection */
private fun FragmentManager.onContainerAvailable(view: FragmentContainerView) {
    val method = FragmentManager::class.java.getDeclaredMethod(
        "onContainerAvailable",
        FragmentContainerView::class.java
    )
    method.isAccessible = true
    method.invoke(this, view)
}
*/




@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    errorMsg: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
}}