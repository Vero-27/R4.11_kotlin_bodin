package com.example.to_do_list.android.view

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import com.example.to_do_list.android.mettreDonneesDansFichier
import com.example.to_do_list.android.prendreDonneesDuFichier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.absoluteValue


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AjouterTaches (navController : NavController, applicationContexte : Context){
    val myCalendar = Calendar.getInstance () ;
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
            var textFieldDate by remember { mutableStateOf(TextFieldValue(""))  }
             val DATE_MASK = "##/##/####"
             val DATE_LENGTH = 8
            TextField(
                value = textFieldDate,
                onValueChange = {
                    if(it.text.length<=DATE_LENGTH){
                    textFieldDate = it
                    }

                },
                visualTransformation = MaskVisualTransformation(DATE_MASK),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )


            Button(
                onClick = {

                                val dateRenseignee = textFieldDate.text
                                val jourRenseigne = dateRenseignee[0] + dateRenseignee[1].toString()
                                val moisRenseigne = dateRenseignee[2] + dateRenseignee[3].toString()
                                val anneeRenseignee = dateRenseignee[4] + dateRenseignee[5].toString() + dateRenseignee[6].toString() + dateRenseignee[7].toString()
                                var date = LocalDate.of(anneeRenseignee.toInt(), moisRenseigne.toInt(), jourRenseigne.toInt())
                                val simpleDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val heure = LocalDateTime.now()

                    val status = if (date.isBefore(LocalDate.now())){
                        "En retard"
                    } else {
                        "En cours"
                    }

                    val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                                val new = JSONObject()
                                new.put("Task", textFieldName.text)
                                new.put("Description", textFieldDescription.text)
                                new.put("Date", date)
                                new.put("Status", status)
                                new.put("Index", donnees.length() + date.dayOfMonth + date.monthValue + date.year + date.dayOfYear + heure.hour + heure.minute + heure.second + heure.dayOfYear + heure.dayOfMonth)
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


class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator(): OffsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}
