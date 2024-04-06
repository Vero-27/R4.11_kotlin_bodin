package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.to_do_list.android.controller.prendreDonneesDuFichier

@Composable
fun ListeTachesFinies(applicationContexte: Context, innerPadding: PaddingValues) {

    Box {
        Column {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                AfficherDonnees(donnees = donnees, applicationContexte, "Finie", "listeTachesFinies")
            }
        }
    }
}


