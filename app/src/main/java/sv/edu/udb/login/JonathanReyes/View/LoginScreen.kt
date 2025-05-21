package sv.edu.udb.login.gui

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import sv.edu.udb.login.JonathanReyes.ViewModel.AuthViewModel
import sv.edu.udb.login.JonathanReyes.ViewModel.GoogleSignInButton
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit,
    navController: NavController,
    startColor: Color = Color.Black,
    endColor: Color = Color.Blue
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val slideDistance = with(density) { (-100).dp.roundToPx() }

    val scrollState = rememberScrollState()
    val textColor = Color.White

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(key1 = authState) {
        when (val state = authState) {
            is AuthViewModel.AuthState.Authenticated -> {
                val user = state.user
                val uid = user?.uid
                if (uid != null) {
                    authViewModel.obtenerRolUsuario(
                        uid,
                        onResult = { rol ->
                            userViewModel.setUsername(user.displayName ?: user.email ?: "Usuario")
                            userViewModel.setRol(rol)
                            onLoginSuccess(user.displayName ?: user.email ?: "Usuario")
                        },
                        onError = { error ->
                            // Maneja el error
                        }
                    )
                }
            }
            is AuthViewModel.AuthState.Error -> {
                println("LoginScreen: Error de autenticación - ${state.message}")
            }
            is AuthViewModel.AuthState.Loading -> {
                println("LoginScreen: Estado de carga... (${state.message ?: ""})")
            }
            AuthViewModel.AuthState.Unauthenticated -> {
                println("LoginScreen: Estado no autenticado.")
            }
        }
    }

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
            LaunchedEffect(key1 = Unit) {
                visibleState = true
            }

            // Título Animado
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

            // Campo de Email Animado
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
                    textStyle = TextStyle(color = textColor),
                    singleLine = true,
                    isError = authState is AuthViewModel.AuthState.Error
                )
            }

            // Campo de Contraseña Animado (sin Visibility/VisibilityOff)
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
                            // Usamos el mismo ícono de candado para ambos estados
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = textColor),
                    singleLine = true,
                    isError = authState is AuthViewModel.AuthState.Error
                )
            }

            // Mensaje de Error Animado
            AnimatedVisibility(
                visible = visibleState && authState is AuthViewModel.AuthState.Error,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 800, easing = EaseOut)
                )
            ) {
                val errorMessage = (authState as? AuthViewModel.AuthState.Error)?.message ?: "Error desconocido"
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botón Iniciar Sesión Animado
            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 1900, easing = EaseOut)
                )
            ) {
                Button(
                    onClick = {
                        authViewModel.signInWithEmailPassword(email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = authState !is AuthViewModel.AuthState.Loading
                ) {
                    if (authState is AuthViewModel.AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar Sesión", color = Color.White)
                    }
                }
            }

            // Espaciador Animado
            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 2000, easing = EaseOut)
                )
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón Google Sign-In Animado
            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 2500, easing = EaseOut)
                )
            ) {
                GoogleSignInButton(authViewModel = authViewModel, userViewModel = userViewModel)
            }

            // Botón Olvidaste Contraseña Animado
            AnimatedVisibility(
                visible = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { -slideDistance },
                    animationSpec = tween(durationMillis = 1200, easing = EaseOut)
                )
            ) {
                TextButton(onClick = {
                    navController.navigate("registro")
                }) {
                    Text("Aun no tienes un usuario creado, Ingresa acá", color = Color.White)
                }
            }
        }
    }
}