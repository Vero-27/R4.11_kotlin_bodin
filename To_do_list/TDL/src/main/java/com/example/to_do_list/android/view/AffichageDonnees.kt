package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.to_do_list.android.controller.ajouterPersonnage
import com.example.to_do_list.android.controller.changerStatusTache
import com.example.to_do_list.android.controller.supprimerUneDonneeDuFichier
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherDonnees(donnees: JSONArray, applicationContext: Context, contraintes: String, pageActuelle: String) {

    val listeDeTaches = remember {
        mutableStateListOf(listOf("", "", 1, null, null))
    }
    listeDeTaches.removeAll(listeDeTaches)
    for (i in 1 until donnees.length()) {
        val tache = donnees[i]
        val tacheString = tache.toString()
        val temporaire = JSONObject(tacheString)
        val tacheStatus = temporaire.getString("Status")
        if (tacheStatus == contraintes) {
            val tacheIndex = temporaire.getString("Index")
            val tacheNom = temporaire.getString("Task")
            val tacheDescription = temporaire.getString("Description")
            var tacheImage: String? = if (!temporaire.isNull("Image")) {
                temporaire.getString("Image")
            } else {
                null
            }
            val tacheDate: String? = if (!temporaire.isNull("Date")) {
                temporaire.getString("Date")
            } else {
                null
            }
            listeDeTaches.add(
                listOf(
                    tacheNom,
                    tacheDescription,
                    tacheIndex.toInt(),
                    tacheDate,
                    tacheImage

                )
            )

        }
    }
    var afficher by remember { mutableStateOf(false) }
    var afficherDialog by remember{
        mutableStateOf(false)
    }

    FelicitationDialog(
        dialogVisible = afficherDialog,
        onDismissRequest = { afficherDialog = false }
    ) {
        Felicitation(
            onDismissRequest = { afficherDialog = false },
            applicationContext = applicationContext,
            url = "https://avatar.iran.liara.run/public/" + (JSONObject(donnees[0].toString()).getString(
                "Nombre"
            ).toInt() + 1).toString()
        )
    }


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(
            items = listeDeTaches,
            key = { it }
        ) { tache ->
            var expandedState by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(
                targetValue = if (expandedState) 180f else 0f, label = ""
            )

            SwipePourEffacer(
                contraintes = contraintes,
                item = tache,
                onDelete = {
                    listeDeTaches -= tache
                    if (pageActuelle == "listeTachesFinies") {
                        supprimerUneDonneeDuFichier("myfile", applicationContext, tache[2] as Int)
                    } else {
                        changerStatusTache("myfile", applicationContext, "Finie", tache[2] as Int)
                        ajouterPersonnage("myfile", applicationContext)
                        afficher = !afficher

                        afficherDialog = true
                    }

                }

            )
            { tache ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    onClick = {
                        expandedState = !expandedState
                    },
                ) {
                    Column {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tache[0] as String,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (tache[3] != null) {

                                    Text(
                                        text = tache[3] as String,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp),
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier
                                    .rotate(rotationState),
                                onClick = {
                                    expandedState = !expandedState
                                }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                )
                            }


                        }
                        if (expandedState) {
                            Text(
                                text = tache[1] as String,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                            if (tache[4] != null) {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(250.dp),
                                    model = ImageRequest.Builder(applicationContext)
                                        .data(tache[4])
                                        .crossfade(enable = true)
                                        .build(),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

    }


}
