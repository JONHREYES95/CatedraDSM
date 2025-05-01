package sv.edu.udb.login.gui.Panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sv.edu.udb.login.gui.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    userViewModel: UserViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val username by userViewModel.username.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                        Text(text = username)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Profile */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Settings */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Modo Oscuro") },
                    label = { Text("Modo Oscuro") },
                    selected = false,
                    onClick = { /* TODO: Toggle Dark Mode */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = "Ayuda") },
                    label = { Text("Ayuda") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Help */ }
                )
            }
        },
        content = content
    )
}