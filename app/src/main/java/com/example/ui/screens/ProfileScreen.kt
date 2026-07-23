package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.SphereAvatar
import com.example.ui.components.SphereGradientBrush
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.SphereBlue
import com.example.ui.theme.SpherePurple
import com.example.ui.viewmodels.SphereViewModel

@Composable
fun ProfileScreen(viewModel: SphereViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Cover Image & Avatar Overlay
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = user.coverUrl,
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(DarkSurfaceVariant)
                )

                SphereAvatar(
                    url = user.avatarUrl,
                    size = 80,
                    hasGradientRing = true,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp)
                )

                // Theme Toggle
                IconButton(
                    onClick = { viewModel.toggleDarkTheme() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(top = 12.dp, end = 16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .testTag("theme_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Profile Details Header
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.displayName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            if (user.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                VerifiedBadge(size = 18)
                            }
                        }
                        Text(
                            text = "@${user.username} • UID: 849201",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { viewModel.showCreatorDashboard.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SpherePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("creator_dashboard_btn")
                    ) {
                        Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Creator Hub", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Link, contentDescription = "Website", tint = SphereBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.website,
                        style = MaterialTheme.typography.labelMedium,
                        color = SphereBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatItem(count = "${user.followersCount}", label = "Followers")
                    ProfileStatItem(count = "${user.followingCount}", label = "Following")
                    ProfileStatItem(count = "${user.postsCount}", label = "Posts")
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(16.dp))

                // Settings & Tools List
                Text(
                    text = "Account & Platform Features",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2FA Security Switch
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = "2FA", tint = SpherePurple)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Two-Factor Authentication (2FA)", fontWeight = FontWeight.Bold)
                                Text("TOTP Security code required on login", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Switch(
                            checked = user.is2FAEnabled,
                            onCheckedChange = { viewModel.toggle2FA() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Admin Panel Launcher
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_panel_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = SphereBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Admin Panel & Moderation", fontWeight = FontWeight.Bold)
                                Text("Manage users, report handling, system stats", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Button(
                            onClick = { viewModel.showAdminPanel.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SphereBlue)
                        ) {
                            Text("Open")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Call Launcher
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Videocam, contentDescription = "HD Call", tint = SpherePurple)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("HD Video / Voice Calls", fontWeight = FontWeight.Bold)
                                Text("Screen sharing & group call test studio", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Button(
                            onClick = { viewModel.startCall("VIDEO", "Maya Lin") },
                            colors = ButtonDefaults.buttonColors(containerColor = SpherePurple)
                        ) {
                            Text("Test Call")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = { viewModel.logout() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("logout_btn")
                ) {
                    Icon(Icons.Default.Logout, contentDescription = "Sign Out")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out of Sphere", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
