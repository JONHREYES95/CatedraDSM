package sv.edu.udb.login.gui.Panel.HomeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.BarChart

// Define una data class para representar cada función del panel
data class AdminFunction(val title: String, val icon: ImageVector, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    organizadorId: String,
    userRole: String
) {
    val scrollState = rememberScrollState()

    // Define las funciones del panel de administración
    val adminFunctions = if (userRole == "admin") {listOf(
        AdminFunction("Gestionar Usuarios", Icons.Filled.Group, {
            navController.navigate("admin_usuarios")
        }),
        AdminFunction("Crear Evento", Icons.Filled.AddCircle, {
            navController.navigate("crear_evento")
        }),
        AdminFunction("Ver Eventos", Icons.Filled.CalendarMonth, {
            navController.navigate("ver_eventos")
        }),
        AdminFunction("RSVP Evento", Icons.Filled.HowToReg, {
            navController.navigate("rsvp_evento")
        }),
        AdminFunction("Historial Eventos", Icons.Filled.History, {
            navController.navigate("historial_eventos")
        }),
        AdminFunction("Estadísticas", Icons.Filled.BarChart, {
            navController.navigate("estadisticas")
        }),
        // Puedes agregar más funciones aquí
    )}else {
        listOf(
            AdminFunction("RSVP Evento", Icons.Filled.HowToReg, {
                navController.navigate("rsvp_evento")
            }),
            AdminFunction("Historial Eventos", Icons.Filled.History, {
                navController.navigate("historial_eventos")
            }),
            AdminFunction("Estadísticas", Icons.Filled.BarChart, {
                navController.navigate("estadisticas")
            }),
        )
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Divide las funciones en filas de dos
        adminFunctions.chunked(2).forEach { rowFunctions ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                rowFunctions.forEach { function ->
                    AdminFunctionItem(function = function)
                }
                // Si la fila tiene solo un elemento, agrega un Spacer para mantener la alineación
                if (rowFunctions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFunctionItem(function: AdminFunction) {
    Card(
        onClick = function.onClick,
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = function.icon,
                contentDescription = function.title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = function.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}