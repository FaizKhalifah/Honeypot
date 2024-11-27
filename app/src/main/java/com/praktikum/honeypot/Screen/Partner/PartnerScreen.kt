package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Screen.Product.ProductCard
import com.praktikum.honeypot.Screen.Product.ProductDetail

@Composable
fun PartnerScreen(
    onNavigateToAddPartner: () -> Unit,
    onNavigateToEditPartner: (Partner) -> Unit,
) {
    val context = LocalContext.current
    val partnerViewModel: PartnerViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )
    val partners by partnerViewModel.partners.collectAsState()
    val selectedPartner by partnerViewModel.selectedPartner.collectAsState()

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
        if (selectedPartner != null) {
            // Tampilkan halaman detail produk
            PartnerDetail(
                partner = selectedPartner!!,
                onDismiss = { partnerViewModel.clearSelectedPartner() },
                onEdit = { partner -> onNavigateToEditPartner(partner) },
                onDelete = { partner ->
                    partnerViewModel.deleteProduct(partner.partner_id)
                    partnerViewModel.clearSelectedPartner()
                }
            )
        } else {
            // Tampilkan daftar partner
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(partners) { partner ->
                    PartnerCard(
                        partner = partner,
                        onClick = { partnerViewModel.selectPartner(partner) }
                    )
                }
            }
        }
    }
}

@Composable
fun PartnerCard(partner: Partner, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick, // Fungsi klik
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.store),
                contentDescription = null,
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
