
package com.example.formulariocontacto

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val Azul = Color(0xFF3949AB)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Azul,
                    secondary = Azul
                )
            ) {
                FormularioContacto { nombre, fecha, telefono, email, descripcion ->

                    val intent = Intent(
                        this,
                        ConfirmacionActivity::class.java
                    ).apply {
                        putExtra("nombre", nombre)
                        putExtra("fecha", fecha)
                        putExtra("telefono", telefono)
                        putExtra("email", email)
                        putExtra("descripcion", descripcion)
                    }

                    startActivity(intent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContacto(
    onSiguiente: (String, String, String, String, String) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }

    var mostrarCalendario by remember { mutableStateOf(false) }
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }

    val camposCompletos = nombre.isNotBlank() &&
            fecha.isNotBlank() &&
            telefono.isNotBlank() &&
            email.isNotBlank() &&
            descripcion.isNotBlank()

    val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val telefonoValido = telefono.all { it.isDigit() } &&
            telefono.length in 7..15

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Mi Contacto",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Text(
            text = "Ingresa tus datos personales",
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            isError = mostrarErrores && nombre.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Box {
            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de nacimiento") },
                trailingIcon = { Text("📅") },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { mostrarCalendario = true }
            )
        }

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevo ->
                telefono = nuevo.filter { it.isDigit() }.take(15)
            },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true,
            isError = mostrarErrores && !telefonoValido,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            isError = mostrarErrores && !emailValido,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción del contacto") },
            minLines = 4,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth()
        )

        if (mostrarErrores &&
            (!camposCompletos || !emailValido || !telefonoValido)
        ) {
            Text(
                text = "Completa todos los campos. " +
                        "Verifica el correo y el teléfono (7 a 15 dígitos).",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                mostrarErrores = true

                if (camposCompletos && emailValido && telefonoValido) {
                    onSiguiente(
                        nombre.trim(),
                        fecha,
                        telefono,
                        email.trim(),
                        descripcion.trim()
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SIGUIENTE", fontSize = 16.sp)
        }
    }

    if (mostrarCalendario) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formato = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            )
                            formato.timeZone = TimeZone.getTimeZone("UTC")
                            fecha = formato.format(Date(millis))
                        }
                        mostrarCalendario = false
                    }
                ) {
                    Text("ACEPTAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarCalendario = false }
                ) {
                    Text("CANCELAR")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
