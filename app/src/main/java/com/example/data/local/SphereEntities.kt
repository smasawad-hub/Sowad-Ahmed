package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorUid: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val isVerified: Boolean,
    val timestamp: String,
    val contentText: String,
    val mediaUrl: String?,
    val mediaType: String,
    val likesCount: Int,
    val commentsCount: Int,
    val repostsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isReposted: Boolean,
    val topicTag: String,
    val aiTranslatedText: String?
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderUid: String,
    val senderName: String,
    val recipientUid: String,
    val text: String,
    val mediaUrl: String?,
    val isVoice: Boolean,
    val voiceDuration: Int,
    val timestamp: String,
    val readReceipt: String,
    val isE2EE: Boolean
)

@Entity(tableName = "communities")
data class CommunityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val coverUrl: String,
    val membersCount: Int,
    val isPrivate: Boolean,
    val isJoined: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String,
    val actorName: String,
    val actorAvatar: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean
)
