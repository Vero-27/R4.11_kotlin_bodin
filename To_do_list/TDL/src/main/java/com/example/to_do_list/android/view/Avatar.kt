package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.to_do_list.android.controller.prendreDonneesDuFichier
import org.json.JSONObject

@Composable
fun Avatar (applicationContext : Context, innerPadding : PaddingValues){
    Box {
        Column {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val donnees = prendreDonneesDuFichier("myfile", applicationContext)
                val temporaire = JSONObject(donnees[0].toString())
                val nombre = temporaire.getString("Nombre")

                LazyVerticalGrid(columns = GridCells.Adaptive(65.dp), content = {
                    items (100){  item ->

                        if (item < nombre.toInt()){
                        AsyncImage(
                            model = "https://avatar.iran.liara.run/public/" + (item+1).toString(),
                            contentDescription = "Translated description of what the image contains"
                        )}
                        else {
                            Canvas(
                                modifier = Modifier.size(65.dp),
                                onDraw = {
                                    drawCircle(Color(0xFFCABEC5))
                                }
                            )
                        }

                    }
                })
            }
        }
    }
}