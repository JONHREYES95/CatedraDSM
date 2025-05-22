package sv.edu.udb.login.JarlyVigil.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import sv.edu.udb.login.NelsonLuna.Model.Evento


@Composable
fun RSVPEventoScreen(
    userName: String,
    eventos: List<Evento>,
    onRSVP: (eventoId: String, estado: String?) -> Unit,
    userViewModel: UserViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 6.dp, vertical = 16.dp)
    ) {
        Text("Invitaciones a eventos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(eventos) { evento ->
                val rsvp = evento.asistentes[userName] // <-- Usar userName como clave
                val tieneRespuesta = evento.asistentes.containsKey(userName)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Box {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(evento.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Fecha: ${evento.fecha} - Hora: ${evento.hora}")
                            Text("Ubicación: ${evento.ubicacion}")
                            Text("Descripción: ${evento.descripcion}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "¿Asistirás?: ${
                                    when {
                                        rsvp == true -> "Asistiré"
                                        rsvp == false -> "No asistiré"
                                        tieneRespuesta && rsvp == null -> "Tal vez"
                                        else -> "Sin respuesta"
                                    }
                                }"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onRSVP(evento.id, "asistire") },
                                    enabled = rsvp != true,
                                    modifier = Modifier.weight(1f)
                                ) { Text("Sí", fontSize = 12.sp) }
                                Button(
                                    onClick = { onRSVP(evento.id, "no_asistire") },
                                    enabled = rsvp != false,
                                    modifier = Modifier.weight(1f)
                                ) { Text("No", fontSize = 12.sp) }
                                Button(
                                    onClick = { onRSVP(evento.id, "tal_vez") },
                                    enabled = !(tieneRespuesta && rsvp == null),
                                    modifier = Modifier.weight(1f)
                                ) { Text("Tal vez", fontSize = 12.sp) }
                                Button(
                                    onClick = { onRSVP(evento.id, null) },
                                    enabled = tieneRespuesta,
                                    modifier = Modifier.weight(1f)
                                ) { Text("Cancelar", fontSize = 9.sp) }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Calificar",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable {
                                        navController.navigate("calificar/${evento.id}")
                                    }
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Te invito al evento: ${evento.titulo} el ${evento.fecha} a las ${evento.hora} en ${evento.ubicacion}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Compartir evento"))
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir")
                        }
                    }
                }
            }
        }
    }
}
