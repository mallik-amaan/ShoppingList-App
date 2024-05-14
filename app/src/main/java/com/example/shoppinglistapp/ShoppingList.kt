package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun ShoppingListScreen() {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var sItems by remember {
        mutableStateOf(listOf<ShoppingList>())
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Button(
            onClick = { showDialog = true },
        ) {
            Text("Add Item")
        }

    LazyColumn {
        items(sItems) {
            item->
            if(item.isEditing)
            {
                ShoppingItemEdit(item = item) {
                    editedName,editedQuantity->
                    sItems=sItems.map { it.copy(isEditing = false)}
                    val newitem=sItems.find{it.id==item.id}
                    newitem?.let {
                        it.name=editedName
                        it.quantity=editedQuantity
                    }

                }
            }
            else
            ShoppingListItem(item = item, onEditClick = {
                                                        sItems=sItems.map{it.copy(isEditing = it.id==item.id)}
            }, onDeleteClick = {
                sItems=sItems-item
            })
        }
    } }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, confirmButton = { },
            title = { Text("Add New Items") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName, onValueChange = {
                            itemName = it
                        },
                        label = {
                            Text("Item Name")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                    OutlinedTextField(label = {
                        Text("Quantity")
                    }, value = itemQuantity, onValueChange = {
                        itemQuantity = it
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .background(color = Color.Green), onClick = {
                            if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                                val newItem = ShoppingList(
                                    id = sItems.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt()
                                )
                                sItems += newItem
                                showDialog = false
                                itemName = ""
                                itemQuantity = ""
                            }
                        }) {
                        Text("Add Item", style = TextStyle(color = Color.White))
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(item: ShoppingList, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, Color(0xFF00BCD4))
            )
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = item.quantity.toString(), modifier = Modifier.padding(8.dp))
        Button(onClick = onEditClick, modifier = Modifier.padding(8.dp)) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
        }
        Button(onClick = onDeleteClick, modifier = Modifier
            .padding(8.dp)
            .background(color = Color.Transparent)) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)

        }
    }
}


@Composable
fun ShoppingItemEdit(
    item:ShoppingList,
    onEditComplete:(String,Int)->Unit
)
{
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.White),
    ){
        Column {
            BasicTextField(value = editedName, onValueChange = {editedName=it},singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
            BasicTextField(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp), singleLine = true, value = editedQuantity, onValueChange = {editedQuantity=it})
        }
    Button(onClick = {
                     isEditing=false
        onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
    }, modifier = Modifier.padding(8.dp))
        {
            Icon(imageVector = Icons.Rounded.Done, contentDescription = null)
        }
    }

}