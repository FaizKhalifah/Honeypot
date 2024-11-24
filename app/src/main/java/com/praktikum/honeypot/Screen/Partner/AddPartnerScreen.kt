package com.praktikum.honeypot.Screen.Partner

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.ViewModel.PartnerViewModel

@Composable
fun AddPartnerScreen(
    viewModel: PartnerViewModel,
    navController: NavController
) {
    var partnerid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = partnerid,
            onValueChange = { partnerid = it },
            label = { Text("Partner Id") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Partner Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isBlank() || address.isBlank() ) {
                    Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val newPartner = Partner(
                    partner_id = partnerid.toIntOrNull() ?: 0,
                    name = name,
                    address = address,
                    PartnerStocks =  emptyList()
                )
                viewModel.addPartner(newPartner,
                    onSuccess = {
                        Toast.makeText(context, "Partner added successfully!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Partner")
        }
    }

}