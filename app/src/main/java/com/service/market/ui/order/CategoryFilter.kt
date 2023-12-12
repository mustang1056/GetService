package com.service.market.ui.order


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auto.ingram.ui.navigation.BottomNavItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.service.market.R
import com.service.market.data.local.model.service.Services
import com.service.market.utils.TinyDB
import com.service.market.viewmodels.ServicesViewModel
import java.lang.reflect.Type

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiComboBox(
    labelText: String,
    options: List<Services>,
    onOptionsChosen: (List<Services>) -> Unit,
    modifier: Modifier = Modifier,
    selectedIds: List<Int> = emptyList(),
) {
    var expanded by remember { mutableStateOf(false) }
    // when no options available, I want ComboBox to be disabled
    val isEnabled by rememberUpdatedState { options.isNotEmpty() }
    var selectedOptionsList  = remember { mutableStateListOf<Int>()}

    //Initial setup of selected ids
    selectedIds.forEach{
        selectedOptionsList.add(it)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (isEnabled()) {
                expanded = !expanded
                if (!expanded) {
                    onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
                }
            }
        },
        modifier = modifier,
    ) {
        val selectedSummary = when (selectedOptionsList.size) {
            0 -> ""
            1 -> options.first { it.id == selectedOptionsList.first() }.title
            else -> "Wybrano ${selectedOptionsList.size}"
        }
        TextField(
            enabled = isEnabled(),
            readOnly = true,
            value = selectedSummary,
            onValueChange = {},
            label = { Text(text = labelText) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
            },
        ) {
            for (option in options) {

                //use derivedStateOf to evaluate if it is checked
                var checked = remember {
                    derivedStateOf{option.id in selectedOptionsList}
                }.value

                DropdownMenuItem(
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { newCheckedState ->
                                    if (newCheckedState) {
                                        selectedOptionsList.add(option.id)
                                    } else {
                                        selectedOptionsList.remove(option.id)
                                    }
                                },
                            )
                            Text(text = option.title)
                        }
                    },
                    onClick = {
                        if (!checked) {
                            selectedOptionsList.add(option.id)
                        } else {
                            selectedOptionsList.remove(option.id)
                        }
                    }
                )
            }
        }
    }
}


fun saveCategory(categoryList: ArrayList<Int>, context: Context){
    //val activity = LocalContext.current as Activity
    /*
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with (sharedPref.edit()) {
        putStringSet("Test", categoryList)
        apply()
    }*/

    val tinydb = TinyDB(context)
   // tinydb.putListInt("saveCategory", categoryList.toList())

}

fun getCategory(context: Context) : ArrayList<Int>?{
    val tinydb = TinyDB(context)
    val categoryList = tinydb.getListInt("saveCategory")

    return categoryList
}


fun saveArrayList(list: Set<Int?>?, key: String?, context: Context) {
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor: SharedPreferences.Editor = prefs.edit()
    val gson = Gson()
    val json: String = gson.toJson(list)
    editor.putString(key, json)
    editor.apply()
}

fun getArrayList(key: String?, context: Context): Set<Int> {
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val gson = Gson()
    val json: String? = prefs.getString(key, "")
    val type: Type = object : TypeToken<Set<Int>>() {}.getType()
    return gson.fromJson(json, type)
}

@Composable
fun AppBarCategory(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(BottomNavItem.RemontList.screen_route)
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = colorResource(R.color.purple_200),
        contentColor = Color.White,
        elevation = 10.dp
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CheckboxExample(viewModel: ServicesViewModel, navController: NavController) {
    val selectedOptions = remember { mutableStateOf(setOf<Int>()) }
    val context = LocalContext.current

    val test by viewModel.servicesVal.collectAsState(initial = listOf())

    try {
        var categoryList = getArrayList("saveCategory", context)

        if (categoryList != null) {
            if (categoryList.size != 0) {
                selectedOptions.value = categoryList
            }
        }

    }catch (e:Exception){

    }

    Scaffold(
        topBar = { AppBarCategory(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                        navController.navigate(BottomNavItem.RemontList.screen_route)
                }
            ) {
                Icon(Icons.Filled.Done,"OK")
            }
        }

    )
    {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
                // New vertical spacing
                verticalArrangement = Arrangement.spacedBy(1.dp),

            ) {

                items(test) { item ->

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .clickable() {
                                val currentSelected = selectedOptions.value.toMutableSet()
                                if (item != null) {
                                    currentSelected.add(item.id)

                                } else {
                                    currentSelected.remove(item?.id)
                                }

                                if (selectedOptions.value.contains(item?.id)) {
                                    currentSelected.remove(item?.id)
                                }
                                selectedOptions.value = currentSelected
                                Log.d("value", selectedOptions.value.toString())

                                val list = selectedOptions.value

                                saveArrayList(list, "saveCategory", context)


                            },
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Checkbox(
                            checked = selectedOptions.value.contains(item?.id),
                            onCheckedChange = { selected ->
                                val currentSelected = selectedOptions.value.toMutableSet()
                                if (selected) {
                                    if (item != null) {
                                        currentSelected.add(item.id)
                                    }
                                } else {
                                    currentSelected.remove(item?.id)
                                }
                                selectedOptions.value = currentSelected
                                Log.d("value", selectedOptions.value.toString())

                                val list = selectedOptions.value

                                saveArrayList(list, "saveCategory", context)

                            }
                        )
                        Spacer(Modifier.width(3.dp))

                        if (item != null) {
                            Text(item.title)
                        }
                    }
                }

            }
        /*
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            RemontDetailsBanner(movieItem = movieItem)
            RemontDetailsText(movieItem = movieItem)
        }*/

    }






}


