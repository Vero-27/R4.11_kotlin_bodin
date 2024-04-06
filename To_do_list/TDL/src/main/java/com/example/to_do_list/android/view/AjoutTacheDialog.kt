package com.example.to_do_list.android.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.to_do_list.android.MettreDonneesDansFichier
import com.example.to_do_list.android.PrendreDonneesDuFichier
import com.example.to_do_list.android.R
import com.example.to_do_list.android.plannifierNotification
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun AjouterTacheDialog(
    dialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    contenu: @Composable () -> Unit,
) {

    var dialogAnimeVisible by remember { mutableStateOf(false) }

    LaunchedEffect(dialogVisible) {
        if (dialogVisible) dialogAnimeVisible = true
    }

    if (dialogAnimeVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            val fenetreDialog = fenetreDialog()

            SideEffect {
                fenetreDialog.let { fenetre ->
                    fenetre?.setDimAmount(0f)
                    fenetre?.setWindowAnimations(-1)
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var animationIn by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { animationIn = true }
                AnimatedVisibility(
                    visible = animationIn && dialogVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                            .background(Color.Black.copy(alpha = .56f))
                            .fillMaxSize()
                    )
                }
                AnimatedVisibility(
                    visible = animationIn && dialogVisible,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessHigh)) + scaleIn(
                        initialScale = .8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                    exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
                ) {
                    Box(
                        Modifier
                            .pointerInput(Unit) { detectTapGestures { } }
                            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                            .width(330.dp)
                            .height(380.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(16.dp),
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF80DEEA),
                                        Color(0xFFC5E1A5),
                                        Color(0xFFBA68C8),
                                        Color(0xFFFFCC80)
                                    )
                                )
                            )
                            .background(
                                MaterialTheme.colorScheme.surface,
                            ),

                        contentAlignment = Alignment.Center
                    ) {
                        contenu()
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            dialogAnimeVisible = false
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ajoutTache(
    applicationContext: Context,
    navController : NavController,
    fenetreActuelle : String
) {
    Column(Modifier.background(MaterialTheme.colorScheme.surface)) {

        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = expandVertically(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                expandFrom = Alignment.CenterVertically,
            )
        ) {


        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Ajouter une tache",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            var textFieldName by remember { mutableStateOf(TextFieldValue("")) }
            var textErreur by remember { mutableStateOf("") }
            OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .height(60.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0xFF80DEEA),
                        unfocusedBorderColor = Color(0xFF80DEEA),
                        unfocusedLabelColor =  Color.Black,
                        unfocusedLeadingIconColor = Color(0xFF80DEEA),
                        focusedTextColor = Color(0xFF80DEEA),
                        focusedBorderColor = Color(0xFF80DEEA),
                        focusedLabelColor = Color.Black,
                        focusedLeadingIconColor = Color(0xFF80DEEA),
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null
                        )
                    },
                    value = textFieldName,
                    onValueChange = {
                        textFieldName = it
                    },
                    label = { Text(text = "Nom de la tache") },
                )

            var textFieldDescription by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .height(60.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0xFFC5E1A5),
                        unfocusedBorderColor = Color(0xFFC5E1A5),
                        unfocusedLabelColor =  Color.Black,
                        unfocusedLeadingIconColor = Color(0xFFC5E1A5),
                        focusedTextColor = Color(0xFFC5E1A5),
                        focusedBorderColor = Color(0xFFC5E1A5),
                        focusedLabelColor = Color.Black,
                        focusedLeadingIconColor =Color(0xFFC5E1A5),
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    },
                    value = textFieldDescription,
                    onValueChange = {
                        textFieldDescription = it
                    },
                    label = { Text(text = "Description de la tache") },
                )

            var textFieldDate by remember { mutableStateOf(TextFieldValue("")) }
            val DATE_MASK = "##/##/####"
            val DATE_LENGTH = 8
             OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .height(60.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0xFFBA68C8),
                        unfocusedBorderColor =Color(0xFFBA68C8),
                        unfocusedLabelColor =  Color.Black,
                        unfocusedLeadingIconColor = Color(0xFFBA68C8),
                        focusedTextColor = Color(0xFFBA68C8),
                        focusedBorderColor = Color(0xFFBA68C8),
                        focusedLabelColor = Color.Black,
                        focusedLeadingIconColor = Color(0xFFBA68C8),
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null
                        )
                    },
                    value = textFieldDate,
                    onValueChange = {
                        if (it.text.length <= DATE_LENGTH) {
                            textFieldDate = it
                        }

                    },
                    label = { Text(text = "Date limite de réalisation") },
                    visualTransformation = MaskVisualTransformation(DATE_MASK),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )


            var imageUri by remember { mutableStateOf<Uri?>(null) }
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                println("selected file URI ${it.data?.data}")
                imageUri = it.data?.data
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_OPEN_DOCUMENT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                            .apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                            }
                        launcher.launch(intent)
                    }
                    .weight(1f)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
               Image (

                   painterResource(id =R.drawable.galerie ), "")
            }

            Text(
                text = textErreur
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        val date: LocalDate?
                        if (textFieldDate.text != "") {
                            val dateRenseignee = textFieldDate.text
                            if (dateRenseignee.length == 8) {
                                val jourRenseigne =
                                    dateRenseignee[0] + dateRenseignee[1].toString()
                                val moisRenseigne =
                                    dateRenseignee[2] + dateRenseignee[3].toString()
                                val anneeRenseignee =
                                    dateRenseignee[4] + dateRenseignee[5].toString() + dateRenseignee[6].toString() + dateRenseignee[7].toString()
                                if (verifierValiditeDate(
                                        jourRenseigne.toInt(),
                                        moisRenseigne.toInt()
                                    )
                                ) {
                                    date = LocalDate.of(
                                        anneeRenseignee.toInt(),
                                        moisRenseigne.toInt(),
                                        jourRenseigne.toInt()
                                    )
                                    plannifierNotification(
                                        textFieldName.text,
                                        dateRenseignee,
                                        applicationContext
                                    )

                                } else {
                                    textErreur = "La date renseignée n'est pas valide"
                                    return@clickable
                                }
                            } else {
                                textErreur = "La date renseignée n'est pas valide"
                                return@clickable

                            }
                        } else {
                            date = null
                        }

                        val heure = LocalDateTime.now()

                        val status = if (date != null && date.isBefore(LocalDate.now())) {
                            "En retard"
                        } else {
                            "En cours"
                        }

                        val donnees = PrendreDonneesDuFichier("myfile", applicationContext)
                        val new = JSONObject()
                        new.put("Task", textFieldName.text)
                        new.put("Description", textFieldDescription.text)
                        new.put("Date", date)
                        new.put("Image", imageUri)
                        new.put("Status", status)
                        new.put(
                            "Index",
                            donnees.length() + heure.dayOfMonth + heure.monthValue + heure.year + heure.dayOfYear + heure.hour + heure.minute + heure.second + heure.dayOfYear + heure.dayOfMonth
                        )
                        donnees.put(new)
                        MettreDonneesDansFichier(
                            donnees.toString(),
                            "myfile",
                            applicationContext
                        )
                        navController.navigate(fenetreActuelle)
                    }
                    .weight(1f)
                    .padding(vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Ajouter", fontWeight = FontWeight.Bold, color = Color.Black)
            }
}
        }
    }
}

