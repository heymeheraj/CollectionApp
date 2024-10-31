package info.netork.collectionapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import info.netork.collectionapp.data.CollectionItem

@Composable
fun InputScreen(
    onAddItem: (CollectionItem) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var voucherCode by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Collection") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = currentDate,
                onValueChange = { },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = voucherCode,
                onValueChange = { voucherCode = it },
                label = { Text("Voucher Code") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && voucherCode.isNotEmpty() && amount.isNotEmpty()) {
                        val newAmount = amount.toDoubleOrNull() ?: 0.0
                        val newItem = CollectionItem(
                            sn = "PENDING", // This will be set in the parent component
                            name = name,
                            date = currentDate,
                            voucherCode = voucherCode,
                            amount = newAmount
                        )
                        onAddItem(newItem)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add Collection", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel", fontSize = 16.sp)
            }
        }
    }
}