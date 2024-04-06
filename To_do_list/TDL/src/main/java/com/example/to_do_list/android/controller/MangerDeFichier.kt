package com.example.to_do_list.android.controller

import android.content.Context
import androidx.activity.ComponentActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.time.LocalDate

fun mettreDonneesDansFichier(contenu: String, fichierJson: String, applicationContext: Context) {
    val outputStream: FileOutputStream

    try {
        outputStream = applicationContext.openFileOutput(fichierJson, ComponentActivity.MODE_PRIVATE)
        outputStream.write(contenu.toByteArray())
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun prendreDonneesDuFichier(fichierJson: String, applicationContext: Context): JSONArray {
    val inputStream: FileInputStream = applicationContext.openFileInput(fichierJson)

    val inputStreamString = inputStream.readBytes().toString(Charsets.UTF_8)
    var jsonArray = JSONArray()
    if (inputStreamString.isNotEmpty()) {
        jsonArray = JSONArray(inputStreamString)
    }

    return jsonArray
}

fun supprimerDonneesDuFichier(fichierJson: String, applicationContext: Context) {
    val outputStream: FileOutputStream

    try {
        outputStream = applicationContext.openFileOutput(fichierJson, ComponentActivity.MODE_PRIVATE)
        val printWiter = PrintWriter(fichierJson)
        printWiter.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val contenu =
        "[ {\"Nombre\" : 0 } ]"

    mettreDonneesDansFichier(contenu, fichierJson, applicationContext)
}

fun supprimerUneDonneeDuFichier(fichierJson: String, applicationContext: Context, index: Int) {
    val donneesActuelles = prendreDonneesDuFichier(fichierJson, applicationContext)
    if (donneesActuelles.length() == 1) {
        supprimerDonneesDuFichier(fichierJson, applicationContext)
    } else {
        for (i in 1 until donneesActuelles.length()) {
            val task = donneesActuelles[i]
            val taskString = task.toString()
            val temp = JSONObject(taskString)
            val taskIndex = temp.getString("Index")
            if (taskIndex.toInt() == index) {
                donneesActuelles.remove(i)
                mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", applicationContext)
                break
            }
        }
    }
}

fun verifierDateTache(date: String): Boolean {
    val jourRenseigne = date[8] + date[9].toString()
    val moisRenseigne = date[5] + date[6].toString()
    val anneeRenseignee = date[0] + date[1].toString() + date[2].toString() + date[3].toString()
    var dateRenseignee =
        LocalDate.of(anneeRenseignee.toInt(), moisRenseigne.toInt(), jourRenseigne.toInt())
    val dateActuelle = LocalDate.now()
    return dateRenseignee.isBefore(dateActuelle)
}

fun changerStatusTache(fichierJson: String, applicationContext: Context, status: String, index: Int) {
    var donneesActuelles = prendreDonneesDuFichier(fichierJson, applicationContext)
    for (i in 1 until donneesActuelles.length()) {
        val tache = donneesActuelles[i]
        val tacheString = tache.toString()
        val temporaire = JSONObject(tacheString)
        val tacheIndex = temporaire.getString("Index")
        if (tacheIndex == index.toString()) {
            val tacheMiseAJour = JSONObject()
            val nomTache = temporaire.getString("Task")
            val nomDescription = temporaire.getString("Description")
            val imageTache: String? = if (temporaire.isNull("Image")) {
                null
            } else {
                temporaire.getString("Image")
            }
            val date: String? = if (temporaire.isNull("Date")) {
                null
            } else {
                temporaire.getString("Date")
            }
            tacheMiseAJour.put("Task", nomTache)
            tacheMiseAJour.put("Description", nomDescription)
            tacheMiseAJour.put("Image", imageTache)
            tacheMiseAJour.put("Date", date)
            tacheMiseAJour.put("Status", status)
            tacheMiseAJour.put("Index", index)

            donneesActuelles.put(i, tacheMiseAJour)
            break
        }
    }
    mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", applicationContext)
}

fun verifierStatusTache(date: String, index: Int, status: String, applicationContext: Context) {
    if (verifierDateTache(date) && status != "En retard") {
        changerStatusTache("myfile", applicationContext, "En retard", index)
    }
}

fun ajouterPersonnage(fichierJson: String, applicationContext: Context) {
    var donneesActuelles = prendreDonneesDuFichier(fichierJson, applicationContext)
    val tache = donneesActuelles[0]
    val tacheString = tache.toString()
    val temporaire = JSONObject(tacheString)
    val nombre = temporaire.getString("Nombre")
    val jsonObject = JSONObject()
    jsonObject.put("Nombre", nombre.toInt() + 1)
    donneesActuelles.put(0, jsonObject)
    mettreDonneesDansFichier(donneesActuelles.toString(), "myfile", applicationContext)
}