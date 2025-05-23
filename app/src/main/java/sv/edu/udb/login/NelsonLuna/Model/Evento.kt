package sv.edu.udb.login.NelsonLuna.Model

data class Evento(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val ubicacion: String = "",
    val organizadorId: String = "",
    val asistentes: Map<String, Boolean> = emptyMap()
)