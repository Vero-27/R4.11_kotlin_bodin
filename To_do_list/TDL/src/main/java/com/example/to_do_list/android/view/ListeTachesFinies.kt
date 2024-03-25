package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.to_do_list.android.afficherDonnees
import com.example.to_do_list.android.prendreDonneesDuFichier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListeTachesFinies(navController: NavController, applicationContexte: Context, innerPadding: PaddingValues) {

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
                    .padding(innerPadding)
                    .fillMaxSize()
                //.verticalScroll(rememberScrollState())
            ) {
                //Text("Taches")
                val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                afficherDonnees(tableau = donnees, applicationContexte, "Finie")
            }
            NavigationBar {
                NavigationBarItem(
                    selected = false,
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
                    selected = true,
                    onClick = {
                        navController.navigate("listeTachesFinies")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    },
                    label = { Text("Finies") })
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