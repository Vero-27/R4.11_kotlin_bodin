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
import com.example.to_do_list.android.controller.verifierStatusTache
import org.json.JSONObject

@Composable
fun ListeTaches(applicationContext: Context, innerPadding: PaddingValues) {
    Box {
        Column {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val donnees = prendreDonneesDuFichier("myfile", applicationContext)
                for (i in 1 until donnees.length()) {
                    val tache = donnees[i]
                    val tacheString = tache.toString()
                    val temporaire = JSONObject(tacheString)
                    val tacheDate = temporaire.isNull("Date")
                    if (!tacheDate) {
                        val date2 = temporaire.getString("Date")
                        val index = temporaire.getString("Index")
                        val status = temporaire.getString("Status")
                        verifierStatusTache(date2, index.toInt(), status, applicationContext)
                    }
                }
                AfficherDonnees(donnees = donnees, applicationContext, "En cours", "listeTaches")
            }

        }
    }

}
