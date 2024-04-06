package com.example.to_do_list.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.to_do_list.android.view.AjouterTacheDialog
import com.example.to_do_list.android.view.Avatar
import com.example.to_do_list.android.view.FelicitationDialog
import com.example.to_do_list.android.view.ListeTaches
import com.example.to_do_list.android.view.ListeTachesAFaire
import com.example.to_do_list.android.view.ListeTachesEnRetard
import com.example.to_do_list.android.view.ListeTachesFinies
import com.example.to_do_list.android.view.MaskVisualTransformation
import com.example.to_do_list.android.view.afficherDialog
import com.example.to_do_list.android.view.ajoutTache
import com.example.to_do_list.android.view.felicitation
import com.example.to_do_list.android.view.verifierValiditeDate
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.time.LocalDate
import java.time.LocalDateTime


class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme (

            ){

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {

                    /*MettreDonneesDansFichier("myfile", "", applicationContext)
                    SupprimerDonneesDuFichier("myfile", applicationContext)*/
                    val navController = rememberNavController()
                    var fenetreSelectionnee by remember { mutableStateOf("listeTaches") }
                    var showDialog by remember {
                        mutableStateOf(false)
                    }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Check it!") },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    titleContentColor = Color.Black,
                                    containerColor = Color(0xFFE7D9EC)
                                ),
                                navigationIcon = { Icons.Default.DateRange }
                            )
                        },
                        floatingActionButtonPosition = FabPosition.End,
                        floatingActionButton = {
                            FloatingActionButton(onClick = {
                                navController.navigate("ajouterTaches")

                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "fab icon"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize(),
                        content = { innerPadding ->

                            NavHost(
                                navController = navController,
                                startDestination = "listeTaches"
                            ) {
                                composable(
                                    route = "listeTaches"
                                ) {
                                    ListeTaches(
                                        applicationContext,
                                        innerPadding
                                    )
                                }
                                composable(
                                    route = "ajouterTaches"
                                ) {
                                    showDialog=true
                                    AjouterTacheDialog(
                                        dialogVisible = showDialog,
                                        onDismissRequest = { showDialog = false }
                                    ) {
                                        ajoutTache(
                                            applicationContext = applicationContext,
                                            navController = navController,
                                            fenetreSelectionnee = fenetreSelectionnee
                                        )
                                    }
                                    afficherDialog(
                                        applicationContext = applicationContext ,
                                        fenetreSelectionnee = fenetreSelectionnee,
                                        navController = navController
                                    )
                                }
                                composable(
                                    route = "listeTachesFinies"
                                ) {
                                    ListeTachesFinies(
                                        applicationContext,
                                        innerPadding
                                    )
                                }
                                composable(
                                    route = "listeTachesEnRetard"
                                ) {
                                    ListeTachesEnRetard(
                                        applicationContext,
                                        innerPadding
                                    )
                                }
                                composable(
                                    route = "avatar"
                                ) {
                                    Avatar(
                                        applicationContext,
                                        innerPadding
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            NavigationBar (
                                modifier = Modifier
                                    .background(Color.Transparent)
                            ) {
                                NavigationBarItem(
                                    colors = (androidx.compose.material3.NavigationBarItemDefaults
                                        .colors(
                                            selectedIconColor = Color.Black)),
                                    selected = fenetreSelectionnee == "listeTaches",
                                    onClick = {
                                        navController.navigate("listeTaches")
                                        fenetreSelectionnee = "listeTaches"
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("En cours") })
                                NavigationBarItem(
                                    colors = (androidx.compose.material3.NavigationBarItemDefaults
                                        .colors(
                                            selectedIconColor = Color.Black)),
                                    selected = fenetreSelectionnee == "listeTachesEnRetard",
                                    onClick = {
                                        navController.navigate("listeTachesEnRetard")
                                        fenetreSelectionnee = "listeTachesEnRetard"
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("En retard") })
                                NavigationBarItem(
                                    colors = (androidx.compose.material3.NavigationBarItemDefaults
                                        .colors(
                                            selectedIconColor = Color.Black
                                        )),
                                    selected = fenetreSelectionnee == "listeTachesFinies",
                                    onClick = {
                                        navController.navigate("listeTachesFinies")
                                        fenetreSelectionnee = "listeTachesFinies"
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("Finie") })
                                NavigationBarItem(
                                    colors = (androidx.compose.material3.NavigationBarItemDefaults
                                        .colors(
                                            selectedIconColor = Color.Black)),
                                    selected = fenetreSelectionnee == "avatar",
                                    onClick = {
                                        navController.navigate("avatar")
                                        fenetreSelectionnee = "avatar"
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("Avatar") })


                            }
                        }
                    )


                }


            }
        }
    }


}

fun MettreDonneesDansFichier(string: String, fileName: String, context: Context) {
    val outputStream: FileOutputStream

    try {
        outputStream = context.openFileOutput(fileName, ComponentActivity.MODE_PRIVATE)
        outputStream.write(string.toByteArray())
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun PrendreDonneesDuFichier(fileName: String, context: Context): JSONArray {
    val inputStream: FileInputStream
    inputStream = context.openFileInput(fileName)

    val inputAsString = inputStream.readBytes().toString(Charsets.UTF_8)
    var json = JSONArray()
    if (inputAsString.isNotEmpty()) {
        json = JSONArray(inputAsString)
    }

    return json
}

fun SupprimerDonneesDuFichier(fileName: String, context: Context) {
    val outputStream: FileOutputStream

    try {
        outputStream = context.openFileOutput(fileName, ComponentActivity.MODE_PRIVATE)
        val pw = PrintWriter(fileName)
        pw.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val string =
        "[ {\"Nombre\" : 0 } ]"

    MettreDonneesDansFichier(string, fileName, context)
}

fun SupprimerUneDonneeDuFichier(fileName: String, context: Context, index: Int) {
    val donneesActuelles = PrendreDonneesDuFichier(fileName, context)
    if (donneesActuelles.length() == 1) {
        SupprimerDonneesDuFichier(fileName, context)
    } else {
        for (i in 1 until donneesActuelles.length()) {
            val task = donneesActuelles[i]
            val taskString = task.toString()
            val temp = JSONObject(taskString)
            val taskIndex = temp.getString("Index")
            if (taskIndex.toInt() == index) {
                println("task index : " + taskIndex.toInt() + "index " + index)
                donneesActuelles.remove(i)
                MettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
                break
            }
        }
    }
}

fun VerifierDateTache(date: String): Boolean {
    val jourRenseigne = date[8] + date[9].toString()
    val moisRenseigne = date[5] + date[6].toString()
    val anneeRenseignee = date[0] + date[1].toString() + date[2].toString() + date[3].toString()
    var dateRenseignee =
        LocalDate.of(anneeRenseignee.toInt(), moisRenseigne.toInt(), jourRenseigne.toInt())
    val dateActuelle = LocalDate.now()
    return dateRenseignee.isBefore(dateActuelle)
}

fun ChangerStatusTache(fileName: String, context: Context, status: String, index: Int) {
    var donneesActuelles = PrendreDonneesDuFichier(fileName, context)
    for (i in 1 until donneesActuelles.length()) {
        val task = donneesActuelles[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        val taskIndex = temp.getString("Index")
        if (taskIndex == index.toString()) {
            val new = JSONObject()
            val task = temp.getString("Task")
            val description = temp.getString("Description")
            val image: String? = if (temp.isNull("Image")) {
                null
            } else {
                temp.getString("Image")
            }
            val date: String? = if (temp.isNull("Date")) {
                null
            } else {
                temp.getString("Date")
            }
            //SupprimerUneDonneeDuFichier(fileName, context, index)
            new.put("Task", task)
            new.put("Description", description)
            new.put("Image", image)
            new.put("Date", date)
            new.put("Status", status)
            new.put("Index", index)

            donneesActuelles.put(i, new)
            break
        }
    }
    MettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
}

fun VerifierStatusTache(date: String, index: Int, status: String, context: Context) {
    if (VerifierDateTache(date) && status != "En retard") {
        ChangerStatusTache("myfile", context, "En retard", index)
    }
}

fun ajouterAvater (fileName: String, context : Context){
    var donneesActuelles = PrendreDonneesDuFichier(fileName, context)
    val task = donneesActuelles[0]
    val taskString = task.toString()
    val temp = JSONObject(taskString)
    val nombre = temp.getString("Nombre")
    val new = JSONObject()
    new.put("Nombre", nombre.toInt()+1)
    donneesActuelles.put(0, new)
    MettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherDonnees(tableau: JSONArray, context: Context, contraintes: String, page : String) {

    val listeDeTaches = remember {
        mutableStateListOf(listOf("", "", 1, null, null))
    }
    listeDeTaches.removeAll(listeDeTaches)
    for (i in 1 until tableau.length()) {
        val task = tableau[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        var status = temp.getString("Status")
        if (status == contraintes) {
            val index = temp.getString("Index")
            val tache = temp.getString("Task")
            val description = temp.getString("Description")
            var image: String?
            image = if(!temp.isNull("Image")){
                temp.getString("Image")
            } else{
                null
            }
            var date: String?
            date = if (!temp.isNull("Date")) {
                temp.getString("Date")
            } else {
                null
            }
            listeDeTaches.add(
                listOf(
                    tache,
                    description,
                    index.toInt(),
                    date,
                    image

                )
            )

        }
    }
    var show by remember { mutableStateOf(false) }
    var showDialog by remember {
        mutableStateOf(false)
    }

    FelicitationDialog(
        dialogVisible = showDialog,
        onDismissRequest = { showDialog = false }
    ) {
        felicitation(
            onDismissRequest = { showDialog = false },
            applicationContext = context,
            url = "https://avatar.iran.liara.run/public/" + (JSONObject(tableau [0].toString()).getString("Nombre").toInt()+1).toString()
        )
    }


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(
            items = listeDeTaches,
            key = { it }
        ) { tache ->
            var expandedState by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(
                targetValue = if (expandedState) 180f else 0f, label = ""
            )

            SwipePourEffacer(
                contraintes = contraintes,
                item = tache,
                onDelete = {
                    listeDeTaches -= tache
                    if (page == "listeTachesFinies"){
                        SupprimerUneDonneeDuFichier("myfile", context, tache[2] as Int)
                    }
                    else {
                        ChangerStatusTache("myfile", context, "Finie", tache[2] as Int)
                        ajouterAvater("myfile", context)
                        show = !show

                        showDialog = true
                    }

                }

            )
            { tache ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    onClick = {
                        expandedState = !expandedState
                    },
                ) {
                    Column {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tache[0] as String,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (tache[3] != null) {

                                    Text(
                                        text = tache[3] as String,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp),
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier
                                    .rotate(rotationState),
                                onClick = {
                                    expandedState = !expandedState
                                }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                )
                            }


                        }
                        if (expandedState) {
                            Text(
                                text = tache[1] as String,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                            if (tache[4]!=null){
                            AsyncImage(
                                modifier = Modifier
                                    .size(250.dp),
                                model = ImageRequest.Builder(context)
                                    .data(tache[4])
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop)
                            }}
                        }
                    }
                }
            }

        }


    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipePourEffacer(
    contraintes: String,
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit,

    ) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToEnd) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }


    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(

            state = state,
            background = {
                BackgroundDuSwipe(swipeDismissState = state, contraintes = contraintes)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.StartToEnd)
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundDuSwipe(
    swipeDismissState: DismissState, contraintes: String
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        if (contraintes == "En cours" || contraintes == "En retard") {
            Color.Green
        } else {
            Color.Red
        }
    } else {
        Color.Transparent
    }

    val icon = if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        if (contraintes == "En cours" || contraintes == "En retard") {
            Icons.Default.Check
        } else {
            Icons.Default.Delete
        }
    } else {
        null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}

