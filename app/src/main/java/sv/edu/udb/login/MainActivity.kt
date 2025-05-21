package sv.edu.udb.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import sv.edu.udb.login.JarlyVigil.View.AppScaffold
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import sv.edu.udb.login.HenryNajera.View.HomeScreen
import sv.edu.udb.login.JarlyVigil.theme.LoginTheme
import sv.edu.udb.login.JonathanReyes.ViewModel.AuthViewModel
import sv.edu.udb.login.gui.LoginScreen
import sv.edu.udb.login.HenryNajera.View.CalificarEventoScreen
import sv.edu.udb.login.HenryNajera.View.ComentariosScreen
import sv.edu.udb.login.NelsonLuna.View.CrearEventoScreen
import sv.edu.udb.login.JarlyVigil.View.EstadisticasEventoScreen
import sv.edu.udb.login.NelsonLuna.ViewModel.EventosViewModel
import sv.edu.udb.login.JarlyVigil.View.HistorialEventosScreen
import sv.edu.udb.login.JarlyVigil.View.RSVPEventoScreen
import sv.edu.udb.login.NelsonLuna.View.VerEventosScreen
import sv.edu.udb.login.JarlyVigil.View.AyudaScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import sv.edu.udb.login.JonathanReyes.View.RegistroScreen
import sv.edu.udb.login.JonathanReyes.View.AdminUsuariosScreen
import sv.edu.udb.login.JonathanReyes.View.PerfilScreen
import androidx.core.content.edit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtener la preferencia guardada
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val darkModePref = prefs.getBoolean("dark_mode", false)

        setContent {
            var isDarkMode by remember { mutableStateOf(darkModePref) }
            LoginApp(
                isDarkMode = isDarkMode,
                onToggleDarkMode = {
                    isDarkMode = !isDarkMode
                    // Guardar la preferencia cada vez que cambie
                    prefs.edit { putBoolean("dark_mode", isDarkMode) }
                }
            )
        }
    }
}

@Composable
fun LoginApp(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val eventosViewModel: EventosViewModel = viewModel()

    LoginTheme(darkTheme = isDarkMode) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    onLoginSuccess = { username ->
                        userViewModel.setUsername(username)
                        navController.navigate("home")
                    },
                    navController = navController
                )
            }
            composable("home") {
                val rol by userViewModel.rol.collectAsState()
                AppScaffold(
                    title = "Administración de Usuarios",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    showBackButton = false
                ) { paddingValues ->
                    val organizadorId = userViewModel.username.value ?: "default_id"
                    HomeScreen(
                        paddingValues = paddingValues,
                        navController = navController,
                        organizadorId = organizadorId,
                        userRole = rol
                    )
                }
            }
            composable("crear_evento") {
                AppScaffold(
                    title = "Crear Evento",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                ) { paddingValues ->
                    val organizadorId = userViewModel.username.value ?: "default_id"
                    CrearEventoScreen(
                        organizadorId = organizadorId,
                        onEventoCreado = { navController.popBackStack() },
                        userViewModel = userViewModel,
                        paddingValues = paddingValues
                    )
                }
            }
            composable("ver_eventos") {
                AppScaffold(
                    title = "Lista de Eventos",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    VerEventosScreen(
                        userViewModel = userViewModel,
                        paddingValues = paddingValues
                    )
                }
            }
            composable("rsvp_evento") {
                AppScaffold(
                    title = "RSVP Evento",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <-- Añade esta línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    RSVPEventoScreen(
                        eventos = eventosViewModel.eventos,
                        onRSVP = { eventoId, estado ->
                            eventosViewModel.guardarRSVP(eventoId, userViewModel.username.value ?: "default_id", estado)
                        },
                        userViewModel = userViewModel,
                        navController = navController,
                        paddingValues = paddingValues,
                        userName = userViewModel.username.value ?: "default_id"
                    )
                }
            }
            composable("historial_eventos") {
                AppScaffold(
                    title = "Historial de Eventos",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    HistorialEventosScreen(
                        userViewModel = userViewModel,
                        userId = userViewModel.username.value ?: "default_id",
                        paddingValues = paddingValues
                    )
                }
            }
            composable("comentarios/{eventoId}") { backStackEntry ->
                AppScaffold(
                    title = "Comentarios",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <--línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    val eventoId = backStackEntry.arguments?.getString("eventoId") ?: ""
                    ComentariosScreen(
                        eventoId = eventoId,
                        userViewModel = userViewModel,
                        onBack = { navController.popBackStack() },

                    )
                }
            }
            composable("calificar/{eventoId}") { backStackEntry ->
                AppScaffold(
                    title = "Calificar Evento",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <--línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    val eventoId = backStackEntry.arguments?.getString("eventoId") ?: ""
                    CalificarEventoScreen(
                        eventoId = eventoId,
                        userViewModel = userViewModel,
                        navController = navController,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable("estadisticas") {
                AppScaffold(
                    title = "Estadísticas de Eventos",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    EstadisticasEventoScreen(
                        eventos = eventosViewModel.eventos,
                        navController = navController,
                        paddingValues = paddingValues
                    )
                }
            }
            composable("ayuda") {
                AppScaffold(
                    title = "Ayuda",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.popBackStack() },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <--línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    AyudaScreen(onBack = { navController.popBackStack() })
                }
            }
            composable("admin_usuarios") {
                AppScaffold(
                    title = "Administrar Usuarios",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <--línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    AdminUsuariosScreen(
                        paddingValues = paddingValues
                    )
                }
            }
            composable("registro") {
                RegistroScreen(
                    authViewModel = authViewModel,
                    onRegistroExitoso = { navController.popBackStack() },
                    navController = navController,
                )
            }
            composable("perfil") {
                AppScaffold(
                    title = "Perfil",
                    userViewModel = userViewModel,
                    onHelpClick = { navController.navigate("ayuda") },
                    onToggleDarkMode = onToggleDarkMode,
                    isDarkMode = isDarkMode,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate("perfil") },
                    onLogout = {
                        userViewModel.logout()
                        authViewModel.signOut() // <--línea para cerrar sesión en Firebase y Google
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { paddingValues ->
                    PerfilScreen(
                        userViewModel = userViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
