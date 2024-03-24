package com.example.to_do_list.android

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_do_list.android.view.AjouterTaches
import com.example.to_do_list.android.view.ListeTaches
import com.example.to_do_list.android.view.ListeTachesEnRetard
import com.example.to_do_list.android.view.ListeTachesFinies
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.time.LocalDate


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "listeTaches") {
                        composable(
                            route = "listeTaches"
                        ) {
                            ListeTaches(navController, applicationContext)
                        }
                        composable(
                            route = "ajouterTaches"
                        ) {
                            AjouterTaches(navController, applicationContext)
                        }
                        composable(
                            route = "listeTachesFinies"
                        ) {
                            ListeTachesFinies(navController, applicationContext)
                        }
                        composable(
                            route = "listeTachesEnRetard"
                        ) {
                            ListeTachesEnRetard(navController, applicationContext)
                        }
                    }
                }


            }
        }
    }


}

fun mettreDonneesDansFichier(string: String, fileName: String, context: Context) {
    val outputStream: FileOutputStream

    try {
        outputStream = context.openFileOutput(fileName, ComponentActivity.MODE_PRIVATE)
        outputStream.write(string.toByteArray())
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun prendreDonneesDuFichier(fileName: String, context: Context): JSONArray {
    val inputStream: FileInputStream
    inputStream = context.openFileInput(fileName)

    val inputAsString = inputStream.readBytes().toString(Charsets.UTF_8)
    var json = JSONArray()
    if (inputAsString.isNotEmpty()) {
        json = JSONArray(inputAsString)
    }

    return json
}

fun supprimerDonneesDuFichier(fileName: String, context: Context) {
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
        "[\n" + "  ]\n"

    mettreDonneesDansFichier(string, fileName, context)
}

fun supprimerUneDonneeDuFichier(fileName: String, context: Context, index: Int) {
    val donneesActuelles = prendreDonneesDuFichier(fileName, context)
    if (donneesActuelles.length() == 1) {
        supprimerDonneesDuFichier(fileName, context)
    } else {
        for (i in 0 until donneesActuelles.length()) {
            val task = donneesActuelles[i]
            val taskString = task.toString()
            val temp = JSONObject(taskString)
            val taskIndex = temp.getString("Index")
            if (taskIndex.toInt() == index) {
                println("task index : " + taskIndex.toInt() + "index " + index)
                donneesActuelles.remove(i)
                mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
                break
            }
        }


    }

}

fun verifierDate(date: String): Boolean {
    val jourRenseigne = date[8] + date[9].toString()
    val moisRenseigne = date[5] + date[6].toString()
    val anneeRenseignee = date[0] + date[1].toString() + date[2].toString() + date[3].toString()
    var dateRenseignee =
        LocalDate.of(anneeRenseignee.toInt(), moisRenseigne.toInt(), jourRenseigne.toInt())
    val dateActuelle = LocalDate.now()
    return dateRenseignee.isBefore(dateActuelle)
}

fun changerStatusTache(fileName: String, context: Context, status: String, index: Int) {
    var donneesActuelles = prendreDonneesDuFichier(fileName, context)
    for (i in 0 until donneesActuelles.length()) {
        val task = donneesActuelles[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        val taskIndex = temp.getString("Index")
        if (taskIndex == index.toString()) {
            val new = JSONObject()
            val task = temp.getString("Task")
            val description = temp.getString("Description")
            val date : String?
            if (temp.isNull("Date")){
                date = null
            }
            else {
                date = temp.getString("Date")
            }
            supprimerUneDonneeDuFichier(fileName, context, index)
            new.put("Task", task)
            new.put("Description", description)
            new.put("Date", date)
            new.put("Status", status)
            new.put("Index", index)

            donneesActuelles = prendreDonneesDuFichier(fileName, context)
            donneesActuelles.put(new)
            break
        }
    }
    mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
}

fun verifierStatus(date: String, index: Int, status: String, context: Context) {
    if (verifierDate(date) && status != "En retard") {
        changerStatusTache("myfile", context, "En retard", index)
    }
}

@Composable
fun afficherDonnees(tableau: JSONArray, context: Context, contraintes: String) {

    val listeDeTaches = remember {
        mutableStateListOf(listOf("", 1, 1))
    }
    listeDeTaches.removeAll(listeDeTaches)
    for (i in 0 until tableau.length()) {
        val task = tableau[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        var status = temp.getString("Status")
        if (status == contraintes) {
            val index = temp.getString("Index")
            val tache = temp.getString("Task")
            val description = temp.getString("Description")
            var date : String?
            if (!temp.isNull("Date")){
                date = temp.getString("Date")
            }
            else {
                date = null
            }
            listeDeTaches.add(
                listOf(
                    tache + " - " + description + " : " + date,
                    index.toInt(),
                    i
                )
            )

        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = listeDeTaches,
            key = { it }
        ) { tache ->
            SwipeToDeleteContainer(
                applicationContext = context,
                contraintes = contraintes,
                item = tache,
                onDelete = {
                    listeDeTaches -= tache
                    if (contraintes == "En cours") {
                        //println("liste visuelle" + listeDeTaches.toList())

                        changerStatusTache("myfile", context, "Finie", tache[1] as Int)
                        println("ca marche")
                        //supprimerUneDonneeDuFichier("myfile", context, tache[1] as Int)

                    } else {
                        supprimerUneDonneeDuFichier("myfile", context, tache[1] as Int)
                    }

                }

            )
            { tache ->
                Text(
                    text = tache[0] as String,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    applicationContext: Context,
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

            /*if (toTheRight){
                Toast.makeText(applicationContext, "Tache validée", Toast.LENGTH_SHORT).show ()
            }
            else {
                Toast.makeText(applicationContext, "Tache effacée", Toast.LENGTH_SHORT).show ()
            }*/

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
                background(swipeDismissState = state, contraintes = contraintes)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.StartToEnd)
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun background(
    swipeDismissState: DismissState, contraintes: String
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        if (contraintes == "En cours") {
            Color.Green
        } else {
            Color.Red
        }
    } else {
        Color.Transparent
    }

    val icon = if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        if (contraintes == "En cours") {
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

