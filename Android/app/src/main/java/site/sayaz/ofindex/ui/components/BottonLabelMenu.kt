package site.sayaz.ofindex.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import site.sayaz.ofindex.R

@Composable
fun ButtonLabelMenu(){
    val expanded = remember { mutableStateOf(false) }
    Box{
        ButtonWithLabel(
            iconResId = R.drawable.baseline_favorite_border_24,
            label = "add to shelf",
            onClick = {
                expanded.value = true
            }
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { /* Handle edit! */ },
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { /* Handle settings! */ },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = { /* Handle send feedback! */ },
            )
        }
    }

}

@Preview
@Composable
fun ButtonLabelMenuPreview(){
    ButtonLabelMenu()
}