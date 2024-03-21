package com.example.to_do_list.android.view

import android.content.Context
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_do_list.android.mettreDonneesDansFichier
import com.example.to_do_list.android.prendreDonneesDuFichier
import org.json.JSONObject

@Composable
fun AjouterTaches (navController : NavController, applicationContexte : Context){
    Button(
        onClick = {
            val string1 = "1"
            val string2 = "2"
            val string3 = "3"

            val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
            val new = JSONObject()
            new.put("Task", string1)
            new.put("Time", string2)
            new.put("Date", string3)
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