package com.example.to_do_list.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter


class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                val navController = rememberNavController ()

                NavHost (navController=navController, startDestination = "listeTaches"){
                    composable (
                        route = "listeTaches"
                    ){
                        ListeTaches(navController, applicationContext)
                    }
                    composable(
                        route = "ajouterTaches"
                    ){
                        AjouterTaches(navController, applicationContext)
                    }
                }}
            }
                }
            }


}

fun mettreDonneesDansFichier (string : String, fileName : String, context : Context){
    val outputStream: FileOutputStream

    try {
        outputStream = context.openFileOutput(fileName, ComponentActivity.MODE_PRIVATE)
        outputStream.write(string.toByteArray())
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun prendreDonneesDuFichier (fileName: String, context : Context): JSONArray {
    val inputStream : FileInputStream
    inputStream =  context.openFileInput(fileName)

    val inputAsString = inputStream.readBytes().toString(Charsets.UTF_8)
    var json = JSONArray()
    if (inputAsString.isNotEmpty()){
        json = JSONArray(inputAsString)
    }

    return json
}

fun supprimerDonneesDuFichier(fileName: String, context : Context){
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
        "[\n" +

                "  ]\n"

    mettreDonneesDansFichier(string, fileName, context)
}

fun supprimerUneDonneeDuFichier (fileName: String, context: Context, index : Int){
    val donneesActuelles = prendreDonneesDuFichier(fileName, context)
    for (i in 0 until donneesActuelles.length()){
        val task = donneesActuelles[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        val taskIndex = temp.getString("Index")
        if (taskIndex==index.toString()){
            donneesActuelles.remove(i)
        }
    }
    mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", context)
}

@Composable
fun afficherDonnees (tableau: JSONArray, context : Context){

    val listeDeTaches = remember {
        mutableStateListOf(listOf("",1))
    }
    listeDeTaches.removeAll(listeDeTaches)
    for (i in 0 until tableau.length()) {
        val task = tableau[i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        val tache = temp.getString("Task")
        val date = temp.getString("Time")
        listeDeTaches.add(listOf(tache + " : " + date, i))
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
                item = tache,
                onDelete = {
                    listeDeTaches.removeAt(tache[1] as Int)
                    supprimerUneDonneeDuFichier("myfile", context, tache[1] as Int)

                }
            ) { tache->
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
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
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
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}