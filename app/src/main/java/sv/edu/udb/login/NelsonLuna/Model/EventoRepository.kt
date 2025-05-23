package sv.edu.udb.login.NelsonLuna.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EventoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val eventosRef = db.collection("eventos")
    private val notificacionesRef = db.collection("notificaciones")

    suspend fun agregarEvento(evento: Evento): Boolean {
        return try {
            val docRef = eventosRef.document()
            val eventoConId = evento.copy(id = docRef.id)
            docRef.set(eventoConId).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun obtenerEventoPorId(eventoId: String): Evento? {
        return try {
            val doc = eventosRef.document(eventoId).get().await()
            doc.toObject(Evento::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
    suspend fun obtenerHistorialEventos(userId: String): List<Evento> {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val hoy = dateFormat.format(Date()) // Obtener la fecha actual como String
            val hoyDate = dateFormat.parse(hoy)
            val snapshot = eventosRef.get().await() // Obtener los documentos de Firestore
            snapshot.documents.mapNotNull { it.toObject(Evento::class.java)?.copy(id = it.id) }
                .filter {
                    val fechaEvento = dateFormat.parse(it.fecha)
                    fechaEvento != null && hoyDate != null && fechaEvento.before(hoyDate)
                            && it.asistentes.containsKey(userId)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun obtenerEventos(): List<Evento> {
        return try {
            val snapshot = eventosRef.get().await()
            println("Documentos obtenidos: ${snapshot.documents.size}")
            snapshot.documents.mapNotNull {
                val evento = it.toObject(Evento::class.java)?.copy(id = it.id)
                println("Evento: $evento")
                evento
            }
        } catch (e: Exception) {
            println("Error al obtener eventos: ${e.message}")
            emptyList()
        }
    }
    suspend fun guardarRSVP(eventoId: String, userId: String, estado: String?): Boolean {
        return try {
            val docRef = eventosRef.document(eventoId)
            val valor = when (estado) {
                "asistire" -> true
                "no_asistire" -> false
                "tal_vez" -> null
                else -> null
            }
            val updateMap = if (valor != null) {
                mapOf("asistentes.$userId" to valor)
            } else {
                mapOf<String, Any?>("asistentes.$userId" to null)
            }
            docRef.update(updateMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun eliminarEvento(eventoId: String, adminNombre: String = ""): Boolean {
        return try {
            eventosRef.document(eventoId).delete().await()
            // Crear notificación para todos los usuarios
            val mensaje = if (adminNombre.isNotBlank())
                "El administrador $adminNombre ha eliminado un evento."
            else
                "Un administrador ha eliminado un evento."
            notificacionesRef.add(mapOf(
                "mensaje" to mensaje,
                "fecha" to System.currentTimeMillis()
            )).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun actualizarEvento(evento: Evento, adminNombre: String = ""): Boolean {
        return try {
            eventosRef.document(evento.id).set(evento).await()
            // Crear notificación para todos los usuarios
            val mensaje = if (adminNombre.isNotBlank())
                "El administrador $adminNombre ha editado un evento."
            else
                "Un administrador ha editado un evento."
            notificacionesRef.add(mapOf(
                "mensaje" to mensaje,
                "fecha" to System.currentTimeMillis()
            )).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}