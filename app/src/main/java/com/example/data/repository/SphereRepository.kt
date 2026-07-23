package com.example.data.repository

import com.example.data.local.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SphereRepository(private val db: SphereDatabase) {

    val allPosts: Flow<List<PostEntity>> = db.postDao().getAllPosts()
    val allCommunities: Flow<List<CommunityEntity>> = db.communityDao().getAllCommunities()
    val allNotifications: Flow<List<NotificationEntity>> = db.notificationDao().getNotifications()

    fun getChatMessages(user1: String, user2: String): Flow<List<MessageEntity>> {
        return db.messageDao().getMessagesBetween(user1, user2)
    }

    suspend fun clearAllPosts() {
        db.postDao().clearAll()
    }

    suspend fun seedInitialDataIfEmpty() {
        // Clear all existing posts to ensure no fake/mock posts exist
        db.postDao().clearAll()

        val existingCommunities = db.communityDao().getAllCommunities().first()
        if (existingCommunities.isEmpty()) {
            val initialCommunities = listOf(
                CommunityEntity(
                    id = "comm_1",
                    name = "AI Innovators & Creators",
                    category = "Artificial Intelligence",
                    description = "A global space for prompt engineers, generative artists, and AI builders.",
                    coverUrl = "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?auto=format&fit=crop&w=800&q=80",
                    membersCount = 48200,
                    isPrivate = false,
                    isJoined = true
                ),
                CommunityEntity(
                    id = "comm_2",
                    name = "Jetpack Compose Masters",
                    category = "Android Development",
                    description = "Sharing UI snippets, custom layout architectures, and performance tweaks.",
                    coverUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=800&q=80",
                    membersCount = 31500,
                    isPrivate = false,
                    isJoined = true
                ),
                CommunityEntity(
                    id = "comm_3",
                    name = "Digital Nomads & Tech Founders",
                    category = "Business & Lifestyle",
                    description = "Connecting startup founders and remote builders across 80+ countries.",
                    coverUrl = "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?auto=format&fit=crop&w=800&q=80",
                    membersCount = 19400,
                    isPrivate = true,
                    isJoined = false
                )
            )
            db.communityDao().insertCommunities(initialCommunities)
        }
    }

    suspend fun createPost(post: PostEntity) {
        db.postDao().insertPost(post)
    }

    suspend fun toggleLike(postId: String, currentStatus: Boolean, currentCount: Int) {
        val newStatus = !currentStatus
        val newCount = if (newStatus) currentCount + 1 else (currentCount - 1).coerceAtLeast(0)
        db.postDao().updateLikeStatus(postId, newStatus, newCount)
    }

    suspend fun toggleSave(postId: String, currentStatus: Boolean) {
        db.postDao().updateSaveStatus(postId, !currentStatus)
    }

    suspend fun sendMessage(msg: MessageEntity) {
        db.messageDao().insertMessage(msg)
    }

    suspend fun toggleJoinCommunity(communityId: String, isJoined: Boolean, membersCount: Int) {
        val newStatus = !isJoined
        val newCount = if (newStatus) membersCount + 1 else (membersCount - 1).coerceAtLeast(0)
        db.communityDao().updateJoinStatus(communityId, newStatus, newCount)
    }
}
