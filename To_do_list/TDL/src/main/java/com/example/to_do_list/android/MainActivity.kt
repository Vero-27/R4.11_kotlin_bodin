package com.example.to_do_list.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_do_list.android.view.AjouterTaches
import com.example.to_do_list.android.view.ListeTaches

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
    else {
        //json = json.a("Home", {})
    }

    return json
}



/*@Composable
fun CreerBouton (tasks: Tableau){
    //var text by remember { mutableStateOf("") }
    Column {
        //Text(text)
        Button(
            onClick = {
                /*val string1 = "1"
                val string2 = "2"
                val string3 = "3"
                tasks.ajouterElemATableau(string1, string2, string3)

                text = string1 + ":" + string3*/
                val navigate = Intent (tMainActivity, MainActivity2::class.java)
                startActivity(navigate)
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Gray),
            modifier = Modifier.size(width = 50.dp, height = 50.dp)
        ) {
            Text(
                text = "+",
                fontSize = 25.sp,
                modifier = Modifier.size(width = 50.dp, height = 50.dp)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TopContent() {

    LazyColumn {
    stickyHeader {
        Surface(Modifier.fillParentMaxWidth()) {
            Text(
                text = "To do list",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 8.dp
                    )
                    .fillMaxWidth()
                    .background(Color.Magenta)
            )
        }
}}}

@Composable
fun CreateTask (){
    Text (
        text = "Tache"
    )
}

@Composable
fun AffichageGlobal (tasks : Tableau){
    Box {
        Column {
            TopContent();
            Column (modifier = Modifier
                .background(Color.LightGray)
                .size(400.dp)
                .verticalScroll(rememberScrollState())){
                tasks.afficher()
            }

            CreerBouton(tasks);

        }
    }
}*/

@Composable
fun afficherDonnees (tableau: JSONArray){
    for (i in 0 until tableau.length()){
        val task = tableau [i]
        val taskString = task.toString()
        val temp = JSONObject(taskString)
        val tache = temp.getString("Task")
        val date = temp.getString("Date")

        //val final = res.getString("Task")
        Row {
            Text (tache + " : " + date)

        }
    }
}