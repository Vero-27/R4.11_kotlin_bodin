package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_do_list.android.afficherDonnees
import com.example.to_do_list.android.prendreDonneesDuFichier
import com.example.to_do_list.android.supprimerDonneesDuFichier
import com.example.to_do_list.android.verifierStatus
import org.json.JSONObject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListeTachesEnCours(navController: NavController, applicationContexte: Context, innerPadding: PaddingValues) {
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
                }
            }
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .size(400.dp)
                    .padding(innerPadding)
                //.verticalScroll(rememberScrollState())
            ) {
                Text("Taches")
                val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                for (i in 0 until donnees.length()) {
                    val task = donnees[i]
                    val taskString = task.toString()
                    val temp = JSONObject(taskString)
                    var date = temp.isNull("Date")
                    if (!date){
                        println("here")
                        val date2 = temp.getString("Date")
                        val index = temp.getString("Index")
                        val status = temp.getString("Status")
                        verifierStatus(date2, index.toInt(), status, applicationContexte)
                    }
                }
                afficherDonnees(tableau = donnees, applicationContexte, "En cours")
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
                    )
                }

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
                }
            }
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate("listeTaches")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    },
                    label = { Text("En cours") })
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("listeTachesFinies")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    },
                    label = { Text("Finie") })
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("listeTachesEnRetard")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    label = { Text("En retard") })


            }


        }
    }

}
