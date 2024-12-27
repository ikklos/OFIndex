package site.sayaz.ofindex.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.viewmodel.AvatarStatus
import site.sayaz.ofindex.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val user = profileViewModel.user.collectAsState()
    val avatarStatus = profileViewModel.avatarStatus.collectAsState()

    var username by remember { mutableStateOf(TextFieldValue(user.value.userName)) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }
    var showUserNameDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    profileViewModel.changeAvatar(it)
                }
            })

    val onChangeAvatar: () -> Unit = {
        launcher.launch("image/*")
    }

    val onChangeUserName: (String) -> Unit = {
        profileViewModel.changeUserName(it)
    }
    val onChangePassword: (String) -> Unit = {
        profileViewModel.changePassword(it)
    }

    LaunchedEffect(Unit) {
        profileViewModel.getUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 显示头像
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.value.avatar,
                    placeholder = painterResource(id = R.drawable.outline_account_circle_24_opa50),
                    error = painterResource(id = R.drawable.outline_account_circle_24_opa50)
                ),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = {
                    onChangeAvatar()
                }, modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(painterResource(R.drawable.baseline_edit_24), contentDescription = "Edit")
            }
            if (avatarStatus.value is AvatarStatus.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(200.dp))
            }
            if (avatarStatus.value is AvatarStatus.Error) {
                Toast.makeText(context, (avatarStatus.value as AvatarStatus.Error).message, Toast.LENGTH_SHORT).show()
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // 显示用户ID
        Text(text = "User ID: ${user.value.userId}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // 显示用户名
        Text(text = "User Name: ${user.value.userName}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // 更改头像按钮
        Button(
            onClick = {
                onChangeAvatar()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Avatar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 更改用户名按钮
        Button(
            onClick = {
                showUserNameDialog = true
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change User Name")
        }

        Spacer(modifier = Modifier.height(8.dp))
        // 更改密码按钮
        Button(
            onClick = {
                showPasswordDialog = true
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }

        Spacer(modifier = Modifier.height(8.dp))

    }


    if (showUserNameDialog) {
        AlertDialog(onDismissRequest = { showUserNameDialog = false },
            title = { Text("更改用户名") },
            text = {
                TextField(value = username, onValueChange = { newValue ->
                    username = newValue
                }, label = { Text("请输入新用户名") }, singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (username.text.isNotBlank()) {
                        onChangeUserName(username.text)
                        showUserNameDialog = false
                    }
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUserNameDialog = false }) {
                    Text("取消")
                }
            })
    }
    if (showPasswordDialog) {
        AlertDialog(onDismissRequest = { showPasswordDialog = false },
            title = { Text("更改密码") },
            text = {
                Column {
                    TextField(value = password,
                        onValueChange = { newValue ->
                            password = newValue
                        },
                        label = { Text("请输入新密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(value = confirmPassword,
                        onValueChange = { newValue ->
                            confirmPassword = newValue
                        },
                        label = { Text("请再次输入新密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (password.text != confirmPassword.text) {
                        Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if (username.text.isNotBlank()) {
                        onChangePassword(password.text)
                        showPasswordDialog = false
                    }
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("取消")
                }
            })
    }
}
