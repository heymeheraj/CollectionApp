package info.netork.collectionapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import info.netork.collectionapp.ui.CollectionViewModel

@Composable
fun MainScreen(
    onAddClick: () -> Unit,
    viewModel: CollectionViewModel = viewModel()
) {
    val collectionList by viewModel.allItems.collectAsState(initial = emptyList())
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collection App") },
                actions = {
                    IconButton(onClick = { viewModel.backupDatabase() }) {
                        Icon(Icons.Default.Save, contentDescription = "Backup")
                    }
                    IconButton(onClick = { viewModel.restoreDatabase() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restore")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            val totalCollection = collectionList.sumOf { it.amount }
            Text(
                text = "Total Collection: ${"%.2f".format(totalCollection)} Riyal",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(collectionList.filter {
                    it.name.contains(searchText, ignoreCase = true) ||
                            it.acquisitionDate.contains(searchText, ignoreCase = true) ||
                            it.category.contains(searchText, ignoreCase = true)
                }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("ID: ${item.id}")
                            Text("Name: ${item.name}")
                            Text("Date: ${item.acquisitionDate}")
                            Text("Category: ${item.category}")
                            Text("Description: ${item.description}")
                            // Assuming amount is part of the description or a separate field
                            // You might need to adjust this based on your actual data structure
                            Text("Amount: ${"%.2f".format(item.amount)} Riyal")
                        }
                    }
                }
            }
        }
    }
}