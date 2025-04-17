package sv.edu.udb.login.gui.LoginScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: (Any?) -> Unit,
    startColor: Color = Color.Magenta,
    endColor: Color = Color.Blue
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val slideDistance = with(density) { (-100).dp.roundToPx() }
    val scrollState = rememberScrollState()
    val textColor = Color.White

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var visibleState by remember { mutableStateOf(false) }

            LaunchedEffect(key1 = true) {
                visibleState = true
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 500, easing = EaseOut)
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 600, easing = EaseOut)
                )
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", color = Color.White) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = "Correo electrónico",
                            tint = Color.White,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = textColor)
                )
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 700, easing = EaseOut)
                )
            ) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color.White) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Contraseña",
                            tint = Color.White,
                        )
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = textColor)
                )
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 800, easing = EaseOut)
                )
            ) {
                if (loginError) {
                    Text(
                        "Correo electrónico o contraseña incorrectos",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 900, easing = EaseOut)
                )
            ) {
                Button(
                    onClick = {
                        if (email == "admin@gmail.com" && password == "1234") {
                            // Navegar a la pantalla de administración pero con el nombre de usuario
                            onLoginSuccess(email)
                        } else {
                            loginError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Iniciar Sesión", color = Color.White)
                }
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 1000, easing = EaseOut)
                )
            ) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 1100, easing = EaseOut)
                )
            ) {
                TextButton(onClick = {
                    // Aquí puedes agregar la lógica para navegar a la pantalla de registro o recuperación de contraseña
                    println("¿Olvidaste tu contraseña?")
                }) {
                    Text("¿Olvidaste tu contraseña?", color = Color.White)
                }
            }
        }
    }
}