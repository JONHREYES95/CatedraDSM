package sv.edu.udb.login.NelsonLuna.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sv.edu.udb.login.NelsonLuna.Model.Evento
import sv.edu.udb.login.NelsonLuna.Model.EventoRepository

class EventosViewModel : ViewModel() {
    var eventos by mutableStateOf<List<Evento>>(emptyList())
        private set

    init {
        cargarEventos()
    }

    fun cargarEventos() {
        viewModelScope.launch {
            eventos = EventoRepository.obtenerEventos()
        }
    }

    fun guardarRSVP(eventoId: String, userName: String, estado: String?) {
        viewModelScope.launch {
            val evento = EventoRepository.obtenerEventoPorId(eventoId)
            if (evento != null) {
                val asistentes = evento.asistentes.toMutableMap()
                when (estado) {
                    "asistire" -> asistentes[userName] = true
                    "no_asistire" -> asistentes[userName] = false
                    "tal_vez", null -> asistentes.remove(userName)
                }
                val eventoActualizado = evento.copy(asistentes = asistentes)
                EventoRepository.actualizarEvento(eventoActualizado)
                cargarEventos()
            }
        }
    }
}