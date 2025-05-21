package sv.edu.udb.login.JonathanReyes.Model

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rol: String = "usuario"
)