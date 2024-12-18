package site.sayaz.ofindex.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import site.sayaz.ofindex.ui.components.Loading
import site.sayaz.ofindex.ui.theme.Typography
import site.sayaz.ofindex.viewmodel.AuthViewModel
import site.sayaz.ofindex.viewmodel.RegisterState


@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateLogin: () -> Unit
) {
    val context = LocalContext.current
    val registerState by authViewModel.registerState.collectAsState()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val textEnabled = remember { mutableStateOf(true) }
    val showUseridDialog = remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                showUseridDialog.value = true
            }

            is RegisterState.Error -> {
                Toast.makeText(
                    context,
                    (registerState as RegisterState.Error).error,
                    Toast.LENGTH_SHORT
                ).show()
                println((registerState as RegisterState.Error).error)
                textEnabled.value = true
            }

            is RegisterState.Loading -> {
                textEnabled.value = false
            }

            is RegisterState.Idle -> {
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
                text = "Register",
                style = Typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(16.dp))
            RegisterForm(username, password, confirmPassword, textEnabled)
            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    authViewModel.register(username.value, password.value)
                }
            ) {
                when (registerState) {
                    is RegisterState.Loading -> Loading()
                    else -> Text("register")
                }
            }
            TextButton(
                onClick = onNavigateLogin
            ) {
                Text("already have an account? Login")
            }


        }
        //on reg success
        if (showUseridDialog.value) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            text = "Remember your userid:",
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            (registerState as RegisterState.Success).message,
                            fontWeight = FontWeight.Bold,
                        )
                        TextButton(onClick = onNavigateLogin) {
                            Text("Login")
                        }
                    }

                }
            }
        }
    }


}

