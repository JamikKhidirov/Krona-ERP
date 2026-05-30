package com.example.uikit.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uikit.data.Facade
import com.example.uikit.data.Hardware
import com.example.uikit.data.Material
import com.example.uikit.data.MaterialColor
import com.example.uikit.data.KoronaCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialCatalogSheet(
    onMaterialSelected: (Material, MaterialColor, Facade?, Hardware?) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMaterial by remember { mutableStateOf<Material?>(null) }
    var selectedColor by remember { mutableStateOf<MaterialColor?>(null) }
    var selectedFacade by remember { mutableStateOf<Facade?>(null) }
    var selectedHardware by remember { mutableStateOf<Hardware?>(null) }
    var step by remember { mutableIntStateOf(0) }

    val titles = listOf("Выбор материала", "Выбор цвета", "Выбор фасада", "Выбор фурнитуры")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = titles[step],
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (step) {
                0 -> MaterialGrid(
                    materials = KoronaCatalog.materials,
                    selectedId = selectedMaterial?.id,
                    onSelect = {
                        selectedMaterial = it
                        selectedColor = null
                        step = 1
                    }
                )
                1 -> if (selectedMaterial != null) ColorGrid(
                    colors = selectedMaterial!!.colors,
                    selectedName = selectedColor?.name,
                    onSelect = {
                        selectedColor = it
                        step = 2
                    }
                )
                2 -> FacadeGrid(
                    facades = KoronaCatalog.facades,
                    selectedId = selectedFacade?.id,
                    onSelect = {
                        selectedFacade = it
                        step = 3
                    }
                )
                3 -> HardwareGrid(
                    hardware = KoronaCatalog.hardware,
                    selectedId = selectedHardware?.id,
                    onSelect = {
                        selectedHardware = it
                        if (selectedMaterial != null && selectedColor != null) {
                            onMaterialSelected(selectedMaterial!!, selectedColor!!, selectedFacade, it)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 0) {
                    OutlinedButton(onClick = { step-- }) {
                        Text("Назад")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (step == 3) {
                    Button(
                        onClick = {
                            if (selectedMaterial != null && selectedColor != null) {
                                onMaterialSelected(selectedMaterial!!, selectedColor!!, selectedFacade, selectedHardware)
                            }
                        }
                    ) {
                        Text("Готово")
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialGrid(
    materials: List<Material>,
    selectedId: Int?,
    onSelect: (Material) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(materials) { material ->
            MaterialCard(material = material, isSelected = material.id == selectedId, onClick = { onSelect(material) })
        }
    }
}

@Composable
private fun MaterialCard(material: Material, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = material.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    text = "${material.category} · ${material.colors.size} цветов",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun ColorGrid(
    colors: List<MaterialColor>,
    selectedName: String?,
    onSelect: (MaterialColor) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(colors) { color ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(color.hexCode)))
                        .then(
                            if (color.name == selectedName) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            else Modifier
                        )
                        .clickable { onSelect(color) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = color.name,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun FacadeGrid(
    facades: List<Facade>,
    selectedId: Int?,
    onSelect: (Facade) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(facades) { facade ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(facade) }
                    .then(
                        if (facade.id == selectedId) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        else Modifier
                    ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = facade.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
                    if (facade.description.isNotBlank()) {
                        Text(
                            text = facade.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HardwareGrid(
    hardware: List<Hardware>,
    selectedId: Int?,
    onSelect: (Hardware) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(hardware) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(item) }
                    .then(
                        if (item.id == selectedId) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        else Modifier
                    ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.name, fontWeight = FontWeight.Medium)
                        Text(text = item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
