package sv.edu.udb.login.HenryNajera.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ComentarioRepository {
    private val db = FirebaseFirestore.getInstance()
    private val comentariosRef = db.collection("comentarios")

    suspend fun obtenerComentarios(eventoId: String): List<Comentario> {
        return try {
            val snapshot = comentariosRef.whereEqualTo("eventoId", eventoId).get().await()
            snapshot.documents.mapNotNull { it.toObject(Comentario::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun agregarComentario(eventoId: String, autor: String, texto: String) {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val comentario = Comentario(
            eventoId = eventoId,
            autor = autor,
            texto = texto,
            fecha = fecha
        )
        comentariosRef.add(comentario).await()
    }

    suspend fun eliminarComentario(comentarioId: String) {
        comentariosRef.document(comentarioId).delete().await()
    }
}