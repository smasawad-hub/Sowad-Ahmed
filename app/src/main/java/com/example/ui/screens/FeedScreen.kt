package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodels.SphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(viewModel: SphereViewModel) {
    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val activeFilter by viewModel.feedFilter.collectAsState()
    val activeStoryViewer by viewModel.activeStoryViewer.collectAsState()
    val showCreatePostSheet by viewModel.showCreatePostSheet.collectAsState()
    val newPostText by viewModel.newPostText.collectAsState()
    val isGeneratingCaption by viewModel.isGeneratingCaption.collectAsState()
    val selectedTopic by viewModel.selectedPostTopic.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SphereHeaderBar(
                title = "Sphere Feed",
                onSearchClick = { viewModel.onTabSelected(com.example.ui.viewmodels.NavigationTab.EXPLORE) },
                onNotificationClick = { }
            )

            // Feed Filter Tabs Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("For You", "Following", "Trending").forEach { filter ->
                    val isSelected = activeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.feedFilter.value = filter },
                        label = {
                            Text(
                                text = if (filter == "For You") "✨ $filter" else filter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SpherePurple.copy(alpha = 0.2f),
                            selectedLabelColor = SpherePurple
                        )
                    )
                }
            }

            // Main Feed List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Story Tray Section
                item {
                    StoryTray(
                        stories = stories,
                        onStoryClick = { viewModel.activeStoryViewer.value = it },
                        onAddStoryClick = { viewModel.showCreatePostSheet.value = true }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Feed Posts
                if (posts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "No Posts",
                                    tint = SpherePurple.copy(alpha = 0.6f),
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No Posts Yet",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "The feed is clear. Tap + to share your first post!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(posts, key = { it.id }) { post ->
                        PostCard(
                            post = post,
                            onLikeClick = { viewModel.toggleLike(post.id, post.isLiked, post.likesCount) },
                            onSaveClick = { viewModel.toggleSave(post.id, post.isSaved) },
                            onTranslateClick = { viewModel.translatePost(post) },
                            onCommentClick = { },
                            onShareClick = { }
                        )
                    }
                }
            }
        }

        // Post Creator Floating Action Button (FAB)
        FloatingActionButton(
            onClick = { viewModel.showCreatePostSheet.value = true },
            containerColor = SpherePurple,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .testTag("create_post_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create New Post",
                modifier = Modifier.size(28.dp)
            )
        }

        // Story Viewer Modal
        activeStoryViewer?.let { story ->
            StoryViewerDialog(
                story = story,
                onDismiss = { viewModel.activeStoryViewer.value = null }
            )
        }

        // Create Post Modal Sheet
        if (showCreatePostSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.showCreatePostSheet.value = false },
                containerColor = MaterialTheme.colorScheme.surface,
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Create New Post",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { viewModel.showCreatePostSheet.value = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPostText,
                        onValueChange = { viewModel.newPostText.value = it },
                        placeholder = { Text("What's happening in your sphere?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("new_post_text_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // AI Caption Helper Button
                    Button(
                        onClick = { viewModel.generateAiCaptionForPost() },
                        colors = ButtonDefaults.buttonColors(containerColor = SphereBlue.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isGeneratingCaption) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = SphereBlue,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gemini AI Writing Caption...", color = SphereBlue)
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Caption",
                                tint = SphereBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Caption Generator", color = SphereBlue, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { viewModel.submitNewPost() },
                            colors = ButtonDefaults.buttonColors(containerColor = SpherePurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("submit_post_btn")
                        ) {
                            Text("Publish Post", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
