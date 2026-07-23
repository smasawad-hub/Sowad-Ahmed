package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SphereBottomNavigation
import com.example.ui.screens.*
import com.example.ui.theme.SphereTheme
import com.example.ui.viewmodels.NavigationTab
import com.example.ui.viewmodels.SphereViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SphereViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()
            val selectedTab by viewModel.currentTab.collectAsState()
            val showCreatorDashboard by viewModel.showCreatorDashboard.collectAsState()
            val showAdminPanel by viewModel.showAdminPanel.collectAsState()

            SphereTheme(darkTheme = isDarkTheme) {
                if (!isLoggedIn) {
                    AuthScreen(viewModel = viewModel)
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            SphereBottomNavigation(
                                selectedTab = selectedTab,
                                onTabSelected = { viewModel.onTabSelected(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Crossfade(targetState = selectedTab, label = "TabSwitch") { tab ->
                                when (tab) {
                                    NavigationTab.HOME_FEED -> FeedScreen(viewModel = viewModel)
                                    NavigationTab.REELS -> ReelsScreen(viewModel = viewModel)
                                    NavigationTab.AI_ASSISTANT -> AiAssistantScreen(viewModel = viewModel)
                                    NavigationTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                                    NavigationTab.MESSAGES -> MessagesScreen(viewModel = viewModel)
                                    NavigationTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                                }
                            }

                            // Overlays
                            if (showCreatorDashboard) {
                                CreatorDashboardScreen(
                                    viewModel = viewModel,
                                    onDismiss = { viewModel.showCreatorDashboard.value = false }
                                )
                            }

                            if (showAdminPanel) {
                                AdminPanelScreen(
                                    onDismiss = { viewModel.showAdminPanel.value = false }
                                )
                            }

                            CallScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
