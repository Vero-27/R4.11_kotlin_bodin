package com.example.to_do_list.android.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.to_do_list.android.controller.creerController

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreerScaffold (applicationContext : Context){
    var navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check it!") },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.Black,
                    containerColor = Color(0xFFE7D9EC)
                ),
                navigationIcon = { Icons.Default.DateRange }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("ajouterTaches")

            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "fab icon"
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        content = { innerPadding ->
            navController = creerController(applicationContext = applicationContext, innerPadding = innerPadding, navController )

        },
        bottomBar = {
            CreerMenuDuBas(navController = navController)
        })
}