
package com.example.formulariocontacto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF3949AB)

class ConfirmacionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nombre = intent.getStringExtra("nombre").orEmpty()
        val fecha = intent.getStringExtra("fecha").orEmpty()
        val telefono = intent.getStringExtra("telefono").orEmpty()
        val email = intent.getStringExtra("email").orEmpty()
        val descripcion = intent.getStringExtra("descripcion").orEmpty()

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Azul,
                    secondary = Azul
                )
            ) {
                PantallaConfirmacion(
                    nombre = nombre,
                    fecha = fecha,
                    telefono = telefono,
                    email = email,
                    descripcion = descripcion,
                    onEditar = { finish() }
                )
            }
        }
    }
}

@Composable
fun PantallaConfirmacion(
    nombre: String,
    fecha: String,
    telefono: String,
    email: String,
    descripcion: String,
    onEditar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Confirmación de datos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Text(
            text = "Revisa la información registrada",
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        DatoConfirmacion("Nombre completo", nombre)
        DatoConfirmacion("Fecha de nacimiento", fecha)
        DatoConfirmacion("Teléfono", telefono)
        DatoConfirmacion("Correo electrónico", email)
        DatoConfirmacion("Descripción del contacto", descripcion)

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onEditar,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "EDITAR DATOS",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DatoConfirmacion(
    titulo: String,
    valor: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F3FA)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 13.sp,
                color = Azul,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = valor,
                fontSize = 17.sp,
                color = Color(0xFF263238)
            )
        }
    }
}
