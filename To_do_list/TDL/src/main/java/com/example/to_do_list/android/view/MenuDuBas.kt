package com.example.to_do_list.android.view

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun CreerMenuDuBas (navController : NavController){

    var fenetreSelectionnee by remember { mutableStateOf("listeTaches") }

    NavigationBar(
        modifier = Modifier
            .background(Color.Transparent)
    ) {
        NavigationBarItem(
            colors = (NavigationBarItemDefaults
                .colors(
                    selectedIconColor = Color.Black
                )),
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
            colors = (NavigationBarItemDefaults
                .colors(
                    selectedIconColor = Color.Black
                )),
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
            colors = (NavigationBarItemDefaults
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
            label = { Text("Finies") })
        NavigationBarItem(
            colors = (NavigationBarItemDefaults
                .colors(
                    selectedIconColor = Color.Black
                )),
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
            label = { Text("Personnages") })


    }
}