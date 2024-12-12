package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.ViewModel.PartnerViewModel
import com.praktikum.honeypot.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun PartnerScreen(
    navController: NavController, // Add NavController here
    onNavigateToAddPartner: () -> Unit,
    onNavigateToEditPartner: (Partner) -> Unit,
) {
    val context = LocalContext.current
    val partnerViewModel: PartnerViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )
    val partners by partnerViewModel.partners.collectAsState()

    // State for search bar
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddPartner()
                },
                containerColor = Color.White,
                contentColor = Color.White
            ) {
                Image(
                    painter = painterResource(R.drawable.add_circle),
                    contentDescription = "Add Partner"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize() // Ensure the box takes up the full available space
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = -90.dp) // Keeps the logo offset from top-left
            ) {
                // Logo positioned top-left with offset
                Image(
                    painter = painterResource(id = R.drawable.honeypot_logo),
                    contentDescription = "Honeypot Logo",
                    modifier = Modifier
                        .size(125.dp)
                        .padding(start = 16.dp, top = 16.dp) // Offsetting logo from top-left corner
                )

                // Add search bar just below the logo
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // LazyColumn to display the partner cards and fill remaining space
            val filteredPartners = partners.filter { partner ->
                partner.name.contains(searchText, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 150.dp) // Adjust to give space for logo and search bar
                    .fillMaxSize() // Ensure this takes all the remaining space
            ) {
                items(filteredPartners) { partner ->
                    PartnerCard(
                        partner = partner,
                        onClick = {
                            // Navigate to PartnerDetail screen
                            navController.navigate("partnerDetail/${partner.partner_id}")
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    androidx.compose.material3.TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text(text = "Search partner...") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun PartnerCard(partner: Partner, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick, // Use the passed in onClick lambda
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Check if the partner has a valid image URL, otherwise use the placeholder image
            val imagePainter = if (partner.imageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.placeholder_image) // Use the placeholder image
            } else {
                rememberImagePainter(partner.imageUrl) // Use the image URL from the partner data
            }

            Image(
                painter = imagePainter,
                contentDescription = "Partner Image",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = partner.name, color = Color.Black)
                Text(text = partner.address, color = Color.Gray)
            }
        }
    }
}
