package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserProfile(
    val uid: String,
    val username: String,
    val displayName: String,
    val bio: String,
    val avatarUrl: String,
    val coverUrl: String,
    val website: String = "",
    val isVerified: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val isPrivate: Boolean = false,
    val isFollowing: Boolean = false,
    val email: String = "",
    val is2FAEnabled: Boolean = false,
    val isCreator: Boolean = true,
    val earnings: Double = 1240.50
)

data class PostItem(
    val id: String,
    val authorUid: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val isVerified: Boolean = false,
    val timestamp: String,
    val contentText: String,
    val mediaUrl: String? = null,
    val mediaType: String = "NONE", // NONE, IMAGE, VIDEO, POLL
    val pollOptions: List<String> = emptyList(),
    val pollVotes: List<Int> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val repostsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isReposted: Boolean = false,
    val topicTag: String = "General",
    val aiTranslatedText: String? = null,
    val isAiGenerated: Boolean = false
)

data class CommentItem(
    val id: String,
    val postId: String,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int = 0
)

data class ReelItem(
    val id: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatar: String,
    val videoUrl: String,
    val caption: String,
    val musicTitle: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val autoCaptions: String = "",
    val filterName: String = "Sphere Glow"
)

data class StoryItem(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val mediaUrl: String,
    val isVideo: Boolean = false,
    val timestamp: String = "2h ago",
    val isSeen: Boolean = false,
    val viewsCount: Int = 42
)

data class ChatMessage(
    val id: String,
    val senderUid: String,
    val senderName: String,
    val recipientUid: String,
    val text: String,
    val mediaUrl: String? = null,
    val isVoice: Boolean = false,
    val voiceDuration: Int = 0,
    val timestamp: String,
    val readReceipt: String = "READ", // SENT, DELIVERED, READ
    val isE2EE: Boolean = true
)

data class CommunityGroup(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val coverUrl: String,
    val membersCount: Int,
    val isPrivate: Boolean = false,
    val isJoined: Boolean = false,
    val channels: List<String> = listOf("Announcements", "General Chat", "Showcase", "Events")
)

data class NotificationItem(
    val id: String,
    val type: String, // LIKE, COMMENT, FOLLOW, MENTION, MESSAGE
    val actorName: String,
    val actorAvatar: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class AiChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String
)
