package com.example.client.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.client.uicomponents.HeaderSection
import com.example.client.uicomponents.InputFieldsSection
import com.example.client.uicomponents.MasterInfoCard
import com.example.client.uicomponents.PhotoUploadSection
import com.example.client.uicomponents.PortfolioSection
import com.example.client.data.ProductType
import com.example.client.uicomponents.ProductTypeGrid
import com.example.client.uicomponents.SubmitButton


@Composable
@Preview(showBackground = true)
fun NewOrderScreen() {

    var selectedType by remember { mutableStateOf("Стул / Кресло") }

    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("50 000") }

    // Список данных (теперь у каждого есть уникальный ID)
    val productTypes = remember {
        listOf(
            ProductType(1, "Стул / Кресло", Icons.Default.Home),
            ProductType(2, "Стол", Icons.Default.Build),
            ProductType(3, "Кровать", Icons.Default.Star),
            ProductType(4, "Шкаф", Icons.Default.Info),
            ProductType(5, "Кухня", Icons.Default.ShoppingCart),
            ProductType(6, "Другое", Icons.Default.MoreVert)
        )
    }
    var selectedTypeId by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            //Нижний бар
        },
        containerColor = Color(0xFFF5F7FA) // Светло-серый фон за пределами основной карточки
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF6366F1)) // Синяя рамка (как на скриншоте)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { HeaderSection() }
                item {
                    ProductTypeGrid(
                        items = productTypes,
                        selectedId = selectedTypeId,
                        onTypeSelected = { id -> selectedTypeId = id }
                    )
                }
                item { InputFieldsSection(
                    price = budget,
                    description = description,
                    onValueChanePrice = { newPrice ->
                        budget = newPrice
                    },
                    onVAlueChangeDescription = { newDescription ->
                        description = newDescription
                    }
                ) }
                item { PhotoUploadSection() }

                item { SubmitButton(
                    onClick = {
                        //Создание самого заказа
                    }
                ) }
                item { MasterInfoCard() }
                item { PortfolioSection() }
            }
        }
    }
}