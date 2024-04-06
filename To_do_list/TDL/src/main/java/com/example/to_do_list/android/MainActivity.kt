package com.example.to_do_list.android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.to_do_list.android.view.CreerScaffold
import com.example.to_do_list.android.controller.mettreDonneesDansFichier
import com.example.to_do_list.android.controller.supprimerDonneesDuFichier

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {

                    /*mettreDonneesDansFichier("myfile", "", applicationContext)
                    supprimerDonneesDuFichier("myfile", applicationContext)*/


                    CreerScaffold(applicationContext = applicationContext)

                }
            }
        }


    }
}


