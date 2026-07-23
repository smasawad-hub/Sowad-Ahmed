package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.ui.viewmodels.NavigationTab

@Composable
fun SphereGradientBrush(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(SphereBlue, SpherePurple, SpherePink)
    )
}

@Composable
fun SphereHeaderBar(
    title: String,
    onSearchClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    unreadNotificationsCount: Int = 2
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SphereGradientBrush()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Sphere Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onSearchClick != null) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.testTag("header_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (onNotificationClick != null) {
                    Box {
                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier.testTag("header_notifications_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (unreadNotificationsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 8.dp, end = 8.dp)
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(SpherePink)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier, size: Int = 16) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(VerifiedBlue),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified User",
            tint = Color.White,
            modifier = Modifier.size((size * 0.7).dp)
        )
    }
}

@Composable
fun SphereAvatar(
    url: String,
    modifier: Modifier = Modifier,
    size: Int = 40,
    hasGradientRing: Boolean = false
) {
    val ringModifier = if (hasGradientRing) {
        Modifier
            .size((size + 4).dp)
            .border(2.dp, SphereGradientBrush(), CircleShape)
            .padding(2.dp)
    } else {
        Modifier.size(size.dp)
    }

    Box(
        modifier = modifier.then(ringModifier),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant)
        )
    }
}

@Composable
fun SphereBottomNavigation(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = modifier.navigationBarsPadding()
    ) {
        val navItems = listOf(
            NavItem(NavigationTab.HOME_FEED, "Feed", Icons.Filled.Home, Icons.Outlined.Home),
            NavItem(NavigationTab.REELS, "Reels", Icons.Filled.SlowMotionVideo, Icons.Outlined.SlowMotionVideo),
            NavItem(NavigationTab.AI_ASSISTANT, "Sphere AI", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
            NavItem(NavigationTab.EXPLORE, "Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
            NavItem(NavigationTab.MESSAGES, "Messages", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
            NavItem(NavigationTab.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
        )

        navItems.forEach { item ->
            val isSelected = selectedTab == item.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) SpherePurple else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = SpherePurple.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("nav_${item.label.lowercase()}")
            )
        }
    }
}

private data class NavItem(
    val tab: NavigationTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
