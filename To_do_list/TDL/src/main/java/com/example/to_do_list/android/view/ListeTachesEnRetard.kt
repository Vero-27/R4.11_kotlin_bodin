package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.to_do_list.android.AfficherDonnees
import com.example.to_do_list.android.PrendreDonneesDuFichier

@Composable
fun ListeTachesEnRetard(applicationContexte: Context, innerPadding: PaddingValues) {
    Box {
        Column {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val donnees = PrendreDonneesDuFichier("myfile", applicationContexte)
                println ("en retard" + donnees)
                AfficherDonnees(tableau = donnees, applicationContexte, "En retard", "listeTachesEnRetard")
            }
        }
    }
}