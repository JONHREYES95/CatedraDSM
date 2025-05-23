package sv.edu.udb.login.NelsonLuna.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import androidx.compose.ui.window.Dialog
import sv.edu.udb.login.NelsonLuna.Model.Evento
import sv.edu.udb.login.NelsonLuna.Model.EventoRepository

@Composable
fun VerEventosScreen(
    userViewModel: UserViewModel,
    paddingValues: PaddingValues
) {
    var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    // Variables para el diálogo de edición y eliminación
    var eventoAEditar by remember { mutableStateOf<Evento?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var eventoAEliminar by remember { mutableStateOf<Evento?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                cargando = true
                eventos = EventoRepository.obtenerEventos()
                cargando = false
            } catch (e: Exception) {
                error = "Error al cargar eventos: ${e.message}" // Mostrar el mensaje real
                cargando = false
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text("Lista de Eventos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        if (cargando) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        } else if (eventos.isEmpty()) {
            Text("No hay eventos registrados.")
        } else {
            LazyColumn {
                items(eventos) { evento ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Título: ${evento.titulo}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Descripción: ${evento.descripcion}")
                            Text("Fecha: ${evento.fecha}  Hora: ${evento.hora}")
                            Text("Ubicación: ${evento.ubicacion}")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { eventoAEditar = evento },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Editar")
                                }
                                Button(
                                    onClick = {
                                        eventoAEliminar = evento
                                        mostrarDialogoEliminar = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para editar evento (formulario similar a CrearEventoScreen)
    eventoAEditar?.let { evento ->
        Dialog(onDismissRequest = { eventoAEditar = null }) {
            EditarEventoForm(
                evento = evento,
                onEventoActualizado = {
                    scope.launch {
                        cargando = true
                        eventos = EventoRepository.obtenerEventos()
                        cargando = false
                        eventoAEditar = null
                    }
                },
                onCancelar = { eventoAEditar = null }
            )
        }
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar && eventoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar evento") },
            text = { Text("¿Seguro que quieres eliminar este evento?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            cargando = true
                            EventoRepository.eliminarEvento(eventoAEliminar!!.id)
                            eventos = EventoRepository.obtenerEventos()
                            cargando = false
                            mostrarDialogoEliminar = false
                            eventoAEliminar = null
                        }
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                Button(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Formulario para editar evento (similar a CrearEventoScreen)
@Composable
fun EditarEventoForm(
    evento: Evento,
    onEventoActualizado: () -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf(evento.titulo) }
    var descripcion by remember { mutableStateOf(evento.descripcion) }
    var fecha by remember { mutableStateOf(evento.fecha) }
    var hora by remember { mutableStateOf(evento.hora) }
    var ubicacion by remember { mutableStateOf(evento.ubicacion) }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Editar Evento", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (ej: 2024-06-01)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (ej: 18:00)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        scope.launch {
                            val eventoActualizado = evento.copy(
                                titulo = titulo,
                                descripcion = descripcion,
                                fecha = fecha,
                                hora = hora,
                                ubicacion = ubicacion
                            )
                            val exito = EventoRepository.actualizarEvento(eventoActualizado)
                            if (exito) {
                                mensaje = "Evento actualizado correctamente"
                                onEventoActualizado()
                            } else {
                                mensaje = "Error al actualizar el evento"
                            }
                        }
                    },
                    enabled = titulo.isNotBlank() && descripcion.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && ubicacion.isNotBlank()
                ) {
                    Text("Guardar Cambios")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onCancelar) {
                    Text("Cancelar")
                }
            }
            mensaje?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    it,
                    color = if (it.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
