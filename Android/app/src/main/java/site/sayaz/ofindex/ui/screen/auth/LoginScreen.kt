package site.sayaz.ofindex.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.ui.components.Loading
import site.sayaz.ofindex.ui.theme.Typography
import site.sayaz.ofindex.viewmodel.AuthViewModel
import site.sayaz.ofindex.viewmodel.LoginState
import site.sayaz.ofindex.viewmodel.RegisterState

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateRegister: () -> Unit,
    onNavigateMain: () -> Unit
) {
    val context = LocalContext.current
    val loginState by authViewModel.loginState.collectAsState()
    val userid = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val textEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(loginState) {
        authViewModel.resetLoginState()
        when (loginState) {
            is LoginState.Success -> {
                Toast.makeText(
                    context,
                    (loginState as LoginState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateMain()
            }
            is LoginState.Error -> {
                Toast.makeText(
                    context,
                    (loginState as LoginState.Error).error,
                    Toast.LENGTH_SHORT
                ).show()
                println((loginState as LoginState.Error).error)
                textEnabled.value = true
            }

            is LoginState.Loading -> {
                textEnabled.value = false
            }

            is LoginState.Idle -> {
                textEnabled.value = true
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OFIndex",
                style = Typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Login",
                style = Typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(16.dp))
            LoginForm(userid, password, textEnabled)
            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if(
                        userid.value.isEmpty() ||
                        password.value.isEmpty()
                    ){
                        Toast.makeText(
                            context,
                            "Please fill in all fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    authViewModel.login(userid.value.toInt(), password.value)
                }
            ) {
                when (loginState) {
                    is LoginState.Loading -> Loading()
                    else -> Text("login")
                }
            }
            TextButton(
                onClick = onNavigateRegister
            ) {
                Text("don't have an account? Register")
            }
        }
    }


}

