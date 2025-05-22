package sv.edu.udb.login.HenryNajera.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CalificacionRepository {
    private val db = FirebaseFirestore.getInstance()
    private val calificacionesRef = db.collection("calificaciones")

    suspend fun agregarCalificacion(eventoId: String, usuarioId: String, calificacion: Int, comentario: String) {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val calificacionObj = Calificacion(
            eventoId = eventoId,
            usuarioId = usuarioId,
            calificacion = calificacion,
            comentario = comentario,
            fecha = fecha
        )
        calificacionesRef.add(calificacionObj).await()
    }
    suspend fun obtenerCalificaciones(eventoId: String): List<Calificacion> {
        return try {
            val snapshot = calificacionesRef.whereEqualTo("eventoId", eventoId).get().await()
            snapshot.documents.mapNotNull { it.toObject(Calificacion::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}