package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.SphereBlue
import com.example.ui.theme.SpherePurple

@Composable
fun AdminPanelScreen(onDismiss: () -> Unit) {
    var reportedItems by remember {
        mutableStateOf(
            listOf(
                ReportedItem("rep_1", "User @spammer_bot", "Suspected automated spam comments on feed"),
                ReportedItem("rep_2", "Post #8210", "Flagged by AI Moderation for copyright image match")
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                        Text(
                            text = "Sphere Admin & Safety Panel",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // System Health Banner
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SphereBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Dns, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Sphere Network Status: Healthy 🟢", fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("E2EE Cluster: Active • Latency: 22ms • Uptime: 99.98%", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.9f))
                                }
                            }
                        }
                    }

                    // Moderation Queue
                    item {
                        Text(
                            text = "Content Moderation Queue (${reportedItems.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(reportedItems.size) { index ->
                        val item = reportedItems[index]
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.target, fontWeight = FontWeight.Bold)
                                    Text("AI Toxicity Score: 0.88", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(item.reason, style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    OutlinedButton(
                                        onClick = { reportedItems = reportedItems.filter { it.id != item.id } },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Dismiss")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { reportedItems = reportedItems.filter { it.id != item.id } },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Ban / Remove")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class ReportedItem(val id: String, val target: String, val reason: String)
