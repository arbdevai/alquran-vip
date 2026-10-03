package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QoriSelectionSheet(
    selectedQoriKey: String,
    onSelectQori: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.screenPadding, vertical = 8.dp)
        ) {
            Text(
                text = "Pilih Qari Audio",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(Reciters.names.entries.toList()) { (key, name) ->
                    val isSelected = key == selectedQoriKey
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectQori(key)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isSelected) AmberAccent else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Kode Audio: $key",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Terpilih",
                                    tint = AmberAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
