package sv.edu.udb.login.gui.Panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    userViewModel: UserViewModel,
    onHelpClick: () -> Unit = {},
    onToggleDarkMode: () -> Unit = {},
    isDarkMode: Boolean = false,
    onBack: () -> Unit,
    onProfileClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    showBackButton: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    val username by userViewModel.username.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star, // Cambia por tu logo si tienes uno
                            contentDescription = "Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(28.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 15.sp), // Tamaño grande
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Botón de usuario con círculo verde de estado
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            IconButton(onClick = onProfileClick) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Usuario conectado",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            // Círculo verde de estado
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-2).dp, y = (-2).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = androidx.compose.ui.graphics.Color.Green // Color verde fijo
                                ) {
                                    Box(modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        if (isDarkMode) {
                            Icon(Icons.Filled.LightMode, contentDescription = "Modo Claro")
                        } else {
                            Icon(Icons.Filled.DarkMode, contentDescription = "Modo Oscuro")
                        }
                    },
                    label = {
                        if (isDarkMode) Text("Modo Claro") else Text("Modo Oscuro")
                    },
                    selected = false,
                    onClick = onToggleDarkMode
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = onProfileClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = "Ayuda") },
                    label = { Text("Ayuda") },
                    selected = false,
                    onClick = onHelpClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión") },
                    label = { Text("Salir") },
                    selected = false,
                    onClick = onLogout
                )
            }
        },
        content = { paddingValues ->
            content(paddingValues)
        }
    )
}

