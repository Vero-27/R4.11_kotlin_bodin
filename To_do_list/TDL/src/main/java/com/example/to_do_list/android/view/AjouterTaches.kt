package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_do_list.android.mettreDonneesDansFichier
import com.example.to_do_list.android.prendreDonneesDuFichier
import org.json.JSONObject

@Composable
fun AjouterTaches (navController : NavController, applicationContexte : Context){
    Box {
        Column {
            var textFieldName by remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = textFieldName,
                onValueChange = {
                    textFieldName = it
                },
                label = { Text(text = "Nom de la tache") },
            )
            var textFieldDescription by remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = textFieldDescription,
                onValueChange = {
                    textFieldDescription = it
                },
                label = { Text(text = "Description de la tache") },
            )
            Button(
                onClick = {
                    val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                    val new = JSONObject()
                    new.put("Task", textFieldName.text)
                    new.put("Time", textFieldDescription.text)
                    new.put("Index", donnees.length())
                    donnees.put(new)
                    mettreDonneesDansFichier(donnees.toString(), "myfile", applicationContexte)
                    navController.navigate("listeTaches")
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Gray),
                modifier = Modifier.size(width = 50.dp, height = 50.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 25.sp,
                    modifier = Modifier.size(width = 50.dp, height = 50.dp)
                )
            }
        }
    }

}