package sv.edu.udb.login.NelsonLuna.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import android.app.DatePickerDialog
import sv.edu.udb.login.NelsonLuna.Model.Evento
import sv.edu.udb.login.NelsonLuna.Model.EventoRepository
import java.util.Calendar

@Composable
fun CrearEventoScreen(
    organizadorId: String,
    onEventoCreado: () -> Unit,
    userViewModel: UserViewModel,
    paddingValues: PaddingValues
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    var errorFecha by remember { mutableStateOf<String?>(null) }
    var errorHora by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    fun validarFecha(fecha: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validarHora(hora: String): Boolean {
        return hora.matches(Regex("^([01]\\d|2[0-3]):([0-5]\\d)$"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text("Crear Evento", style = MaterialTheme.typography.headlineMedium)
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
            onValueChange = {
                fecha = it
                errorFecha = if (validarFecha(it)) null else "Formato de fecha inválido (ej: 2024-06-01)"
            },
            label = { Text("Fecha (ej: 2024-06-01)") },
            isError = errorFecha != null,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val mes = month + 1
                            val fechaSeleccionada = String.format("%04d-%02d-%02d", year, mes, dayOfMonth)
                            fecha = fechaSeleccionada
                            errorFecha = if (validarFecha(fechaSeleccionada)) null else "Formato de fecha inválido"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            readOnly = true
        )
        if (errorFecha != null) {
            Text(errorFecha!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                errorHora = if (validarHora(it)) null else "Formato de hora inválido (ej: 18:00)"
            },
            label = { Text("Hora (ej: 18:00)") },
            isError = errorHora != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (errorHora != null) {
            Text(errorHora!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    if (!validarFecha(fecha) || !validarHora(hora)) {
                        mensaje = "Corrige los errores antes de guardar."
                        return@launch
                    }
                    if (titulo.isBlank() || descripcion.isBlank() || ubicacion.isBlank()) {
                        mensaje = "Todos los campos son obligatorios."
                        return@launch
                    }
                    val evento = Evento(
                        titulo = titulo,
                        descripcion = descripcion,
                        fecha = fecha,
                        hora = hora,
                        ubicacion = ubicacion,
                        organizadorId = organizadorId
                    )
                    val exito = EventoRepository.agregarEvento(evento)
                    if (exito) {
                        mensaje = "Evento guardado correctamente"
                        onEventoCreado()
                    } else {
                        mensaje = "Error al guardar el evento"
                    }
                }
            },
            enabled = titulo.isNotBlank() && descripcion.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && ubicacion.isNotBlank()
        ) {
            Text("Guardar Evento")
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