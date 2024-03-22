package com.example.to_do_list.android.view

import android.app.AlertDialog
import android.content.Context
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
import androidx.navigation.NavController
import com.example.to_do_list.android.mettreDonneesDansFichier
import com.example.to_do_list.android.prendreDonneesDuFichier
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
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
                    /*val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateActuelle = simpleDateFormat.format(Date())

                    val jourActuel = dateActuelle[0] + dateActuelle[1].code
                    val moisActuel = dateActuelle[3] + dateActuelle [4].code
                    val anneeActuelle = dateActuelle[5] + dateActuelle[6].code + dateActuelle[7].code + dateActuelle[8].code

                    val jourRenseignee = textFieldDate.text[0] + textFieldDate.text[1].code
                    val moisRenseignee = textFieldDate.text[2] + textFieldDate.text[3].code
                    val anneeRenseignee = textFieldDate.text[4] + textFieldDate.text[5].code + textFieldDate.text[6].code + textFieldDate.text[7].code

                    if (anneeRenseignee.toInt() < anneeActuelle.toInt()){
                        val alert = AlertDialog.Builder(applicationContexte)
                        alert.setTitle("Mauvaise date")
                        alert.setMessage("La date que vous avez choisie n'est pas valide.")
                        alert.setPositiveButton("OK", null)
                        alert.show()
                    }
                    else {
                        if (moisRenseignee.toInt() < moisActuel.toInt()){
                            val alert = AlertDialog.Builder(applicationContexte)
                            alert.setTitle("Mauvaise date")
                            alert.setMessage("La date que vous avez choisie n'est pas valide.")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                        else {
                            if (jourRenseignee.toInt() < jourActuel.toInt()){
                                val alert = AlertDialog.Builder(applicationContexte)
                                alert.setTitle("Mauvaise date")
                                alert.setMessage("La date que vous avez choisie n'est pas valide.")
                                alert.setPositiveButton("OK", null)
                                alert.show()
                            }
                            else {*/
                                val donnees = prendreDonneesDuFichier("myfile", applicationContexte)
                                val new = JSONObject()
                                new.put("Task", textFieldName.text)
                                new.put("Description", textFieldDescription.text)
                                new.put("Date", textFieldDate.text)
                                new.put("Status", "En cours")
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