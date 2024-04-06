package com.example.to_do_list.android.controller

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.to_do_list.android.view.AjouterTacheDialog
import com.example.to_do_list.android.view.Avatar
import com.example.to_do_list.android.view.ListeTaches
import com.example.to_do_list.android.view.ListeTachesEnRetard
import com.example.to_do_list.android.view.ListeTachesFinies
import com.example.to_do_list.android.view.AjoutTache

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun creerController (applicationContext : Context, innerPadding : PaddingValues, navController : NavHostController): NavHostController {

    val fenetreActuelle by remember {
        mutableStateOf("listeTaches")
    }
    var afficherDialog by remember { mutableStateOf(false) }
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
            afficherDialog=true
            AjouterTacheDialog(
                dialogVisible = afficherDialog,
                onDismissRequest = { afficherDialog = false }
            ) {
                AjoutTache(
                    applicationContext = applicationContext,
                    fenetreActuelle = fenetreActuelle,
                    navController = navController
                )
            }

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
    return navController
}