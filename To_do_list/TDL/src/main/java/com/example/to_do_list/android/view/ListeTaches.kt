package com.example.to_do_list.android.view

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.to_do_list.android.afficherDonnees
import com.example.to_do_list.android.prendreDonneesDuFichier
import com.example.to_do_list.android.supprimerDonneesDuFichier
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListeTaches (navController : NavController, applicationContexte : Context){
    Box {
        Column {
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
                }}
            Column (modifier = Modifier
                .background(Color.LightGray)
                .size(400.dp)
                .verticalScroll(rememberScrollState())){
                Text("Taches")
                val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                println("donnees" + donnees)
                afficherDonnees(tableau = donnees)
            }

            Row {
                Button(
                onClick = {
                    navController.navigate("ajouterTaches")
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Gray),
                modifier = Modifier.size(width = 50.dp, height = 50.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 25.sp,
                    modifier = Modifier.size(width = 50.dp, height = 50.dp)
                )}

                Button(
                    onClick = {
                        supprimerDonneesDuFichier("myfile", applicationContexte)
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Gray),
                    modifier = Modifier.size(width = 50.dp, height = 50.dp)
                ) {
                    Text(
                        text = "-",
                        fontSize = 25.sp,
                        modifier = Modifier.size(width = 50.dp, height = 50.dp)
                    )
            }}


        }
    }

}
