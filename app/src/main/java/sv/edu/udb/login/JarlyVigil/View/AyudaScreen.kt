package sv.edu.udb.login.JarlyVigil.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Necesitas ayuda?",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "• Para crear un evento, utiliza el botón 'Crear Evento' en el panel principal.\n\n" +
                        "• Puedes ver y gestionar tus eventos desde la sección 'Ver Eventos'.\n\n" +
                        "• Si tienes problemas para iniciar sesión, asegúrate de que tus credenciales sean correctas.\n\n" +
                        "• Para soporte adicional, contacta a: soporte@tuevento.com",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}