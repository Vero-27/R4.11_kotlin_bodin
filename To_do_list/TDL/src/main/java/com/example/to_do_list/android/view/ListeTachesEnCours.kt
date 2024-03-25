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
import com.example.to_do_list.android.VerifierStatusTache
import org.json.JSONObject

@Composable
fun ListeTaches(applicationContexte: Context, innerPadding: PaddingValues) {
    Box {
        Column {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val donnees = PrendreDonneesDuFichier("myfile", applicationContexte)
                for (i in 0 until donnees.length()) {
                    val task = donnees[i]
                    val taskString = task.toString()
                    val temp = JSONObject(taskString)
                    var date = temp.isNull("Date")
                    if (!date) {
                        println("here")
                        val date2 = temp.getString("Date")
                        val index = temp.getString("Index")
                        val status = temp.getString("Status")
                        VerifierStatusTache(date2, index.toInt(), status, applicationContexte)
                    }
                }
                AfficherDonnees(tableau = donnees, applicationContexte, "En cours")
            }

        }
    }

}
