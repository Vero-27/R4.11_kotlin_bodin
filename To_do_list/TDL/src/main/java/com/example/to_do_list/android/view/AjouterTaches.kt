package com.example.to_do_list.android.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.navigation.NavController
import com.example.to_do_list.android.MettreDonneesDansFichier
import com.example.to_do_list.android.PrendreDonneesDuFichier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.math.absoluteValue


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AjouterTaches(navController: NavController, applicationContexte: Context, innerPadding: PaddingValues) {
    val myCalendar = Calendar.getInstance();
    Box {
        Column (
            modifier = Modifier
                .padding(innerPadding)

        ){
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
            var textFieldDate by remember { mutableStateOf(TextFieldValue("")) }
            val DATE_MASK = "##/##/####"
            val DATE_LENGTH = 8
            TextField(
                value = textFieldDate,
                onValueChange = {
                    if (it.text.length <= DATE_LENGTH) {
                        textFieldDate = it
                    }

                },
                visualTransformation = MaskVisualTransformation(DATE_MASK),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )

            val openDialog = remember { mutableStateOf(false)}

            Button(
                onClick = {
                    var date : LocalDate? = null
                    if (textFieldDate.text != "") {
                        val dateRenseignee = textFieldDate.text
                        if (dateRenseignee.length == 8) {
                            val jourRenseigne = dateRenseignee[0] + dateRenseignee[1].toString()
                            val moisRenseigne = dateRenseignee[2] + dateRenseignee[3].toString()
                            val anneeRenseignee =
                                dateRenseignee[4] + dateRenseignee[5].toString() + dateRenseignee[6].toString() + dateRenseignee[7].toString()
                            if (verifierValiditeDate(jourRenseigne.toInt(),moisRenseigne.toInt())){
                                date = LocalDate.of(
                                    anneeRenseignee.toInt(),
                                    moisRenseigne.toInt(),
                                    jourRenseigne.toInt()
                                )
                            }
                            else {
                                openDialog.value = true
                                return@Button
                            }
                        }
                        else {
                            openDialog.value = true
                            return@Button

                        }
                    }
                    else {
                        date = null
                    }

                    val heure = LocalDateTime.now()

                    val status = if (date !=null && date.isBefore(LocalDate.now())) {
                        "En retard"
                    } else {
                        "En cours"
                    }

                    val donnees = PrendreDonneesDuFichier("myfile", applicationContexte)
                    val new = JSONObject()
                    new.put("Task", textFieldName.text)
                    new.put("Description", textFieldDescription.text)
                    new.put("Date", date)
                    new.put("Status", status)
                    new.put(
                        "Index",
                        donnees.length() + heure.dayOfMonth + heure.monthValue + heure.year + heure.dayOfYear + heure.hour + heure.minute + heure.second + heure.dayOfYear + heure.dayOfMonth
                    )
                    donnees.put(new)
                    MettreDonneesDansFichier(donnees.toString(), "myfile", applicationContexte)
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

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Erreur de date")
                    },
                    text = {
                        Text("La date que vous avez renseignée n'est pas valide.")
                    },
                    confirmButton = {
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Ok")
                        }
                    }
                )
            }

        }
    }

}

fun verifierValiditeDate (jour : Int, mois : Int): Boolean {
    if (jour < 1 || jour > 31){
        return false
    }
    if (mois < 1 || mois > 12){
        return false
    }

    if (mois == 2 && jour > 29){
        return false
    }
    if (mois == 4 || mois == 6 || mois == 9 || mois == 11){
        if (jour > 30){
            return false
        }
    }
    return true
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