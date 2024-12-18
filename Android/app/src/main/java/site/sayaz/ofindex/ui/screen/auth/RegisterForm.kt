package site.sayaz.ofindex.ui.screen.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.R

@Composable
fun RegisterForm(
    username: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    textEnabled: MutableState<Boolean> = mutableStateOf(true)
) {
    val isVisible = remember { mutableStateOf(false) }
    TextField(
        value = username.value,
        onValueChange = { username.value = it },
        label = { Text("Username") },
        readOnly = !textEnabled.value
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Box {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            readOnly = !textEnabled.value,
            visualTransformation = if (isVisible.value) {
                // 显示密码
                VisualTransformation.None
            } else {
                // 隐藏密码
                PasswordVisualTransformation()
            },
        )
        IconButton(modifier = Modifier.align(Alignment.CenterEnd),
            onClick = {
                isVisible.value = !isVisible.value
            }) {
            Icon(
                painter = if (isVisible.value) {
                    (painterResource(R.drawable.baseline_visibility_off_24))
                } else {
                    painterResource(id = R.drawable.baseline_visibility_24)
                },
                contentDescription = "Toggle password visibility",
                tint = Color.Gray
            )
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
    TextField(
        value = confirmPassword.value,
        onValueChange = { confirmPassword.value = it },
        label = { Text("Confirm Password") },
        readOnly = !textEnabled.value,
        visualTransformation = if (isVisible.value) {
            // 显示密码
            VisualTransformation.None
        } else {
            // 隐藏密码
            PasswordVisualTransformation()
        },
    )

}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun RegisterFormPreview() {
    Column {
        RegisterForm(
            mutableStateOf(""),
            mutableStateOf(""),
            mutableStateOf(""),
            mutableStateOf(true)
        )
    }


}