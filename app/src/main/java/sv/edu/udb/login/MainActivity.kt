package sv.edu.udb.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sv.edu.udb.login.gui.LoginScreen.LoginScreen
import sv.edu.udb.login.gui.Panel.AppScaffold
import sv.edu.udb.login.gui.UserViewModel
import sv.edu.udb.login.gui.Panel.HomeScreen.HomeScreen
import sv.edu.udb.login.ui.theme.LoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginApp()
        }
    }
}

@Composable
fun LoginApp() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    LoginTheme {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(onLoginSuccess = { username ->
                    userViewModel.setUsername(username)
                    navController.navigate("home")
                })
            }
            composable("home") {
                AppScaffold(title = "Panel de Administración", userViewModel = userViewModel) { paddingValues ->
                    HomeScreen(paddingValues = paddingValues)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginTheme {
        LoginScreen(onLoginSuccess = { _ -> }) // Preview ignora el parámetro
    }
}
