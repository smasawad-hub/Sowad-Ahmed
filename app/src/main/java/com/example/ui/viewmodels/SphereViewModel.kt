package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiService
import com.example.data.local.MessageEntity
import com.example.data.local.PostEntity
import com.example.data.local.SphereDatabase
import com.example.data.models.*
import com.example.data.repository.SphereRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NavigationTab {
    HOME_FEED,
    REELS,
    AI_ASSISTANT,
    EXPLORE,
    MESSAGES,
    PROFILE
}

class SphereViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SphereDatabase.getDatabase(application)
    private val repository = SphereRepository(db)
    private val geminiService = GeminiService()

    // --- Authentication & User State ---
    val currentUser = MutableStateFlow(
        UserProfile(
            uid = "current_user",
            username = "alex_sphere",
            displayName = "Alex Vance",
            bio = "Digital architect & tech enthusiast building on @Sphere. AI + Mobile Native ✨",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1000&q=80",
            website = "https://sphere.social/alex",
            isVerified = true,
            followersCount = 0,
            followingCount = 0,
            postsCount = 0,
            email = "alex@sphere.app",
            is2FAEnabled = true,
            isCreator = true
        )
    )

    val isLoggedIn = MutableStateFlow(true)
    val isGuestMode = MutableStateFlow(false)
    val show2FADialog = MutableStateFlow(false)
    val twoFactorCodeInput = MutableStateFlow("")

    // --- Navigation & Theme ---
    val currentTab = MutableStateFlow(NavigationTab.HOME_FEED)
    val isDarkTheme = MutableStateFlow(true)

    // --- Home Feed State ---
    val posts = repository.allPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val feedFilter = MutableStateFlow("For You") // "For You", "Following", "Trending"
    val showCreatePostSheet = MutableStateFlow(false)
    val newPostText = MutableStateFlow("")
    val isGeneratingCaption = MutableStateFlow(false)
    val selectedPostTopic = MutableStateFlow("General")

    // --- Stories State ---
    val stories = MutableStateFlow<List<StoryItem>>(emptyList())
    val activeStoryViewer = MutableStateFlow<StoryItem?>(null)

    // --- Reels State ---
    val reels = MutableStateFlow<List<ReelItem>>(emptyList())
    val activeReelIndex = MutableStateFlow(0)

    // --- Direct Messaging State ---
    val chatPartners = MutableStateFlow<List<UserProfile>>(emptyList())
    val selectedChatPartner = MutableStateFlow<UserProfile?>(null)
    val chatInputText = MutableStateFlow("")

    val activeChatMessages = selectedChatPartner.flatMapLatest { partner ->
        if (partner == null) flowOf(emptyList())
        else repository.getChatMessages("current_user", partner.uid)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Communities & Search State ---
    val communities = repository.allCommunities.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val searchQuery = MutableStateFlow("")
    val searchFilter = MutableStateFlow("All") // "All", "Users", "Posts", "Hashtags", "Groups"

    // --- AI Assistant State ---
    val aiMessages = MutableStateFlow(
        listOf(
            AiChatMessage(
                id = "ai_welcome",
                isFromUser = false,
                text = "Welcome to Sphere AI Assistant! I can help you draft viral post captions, translate feed posts, moderate community content, or analyze your audience growth.",
                timestamp = "Just now"
            )
        )
    )
    val aiInputText = MutableStateFlow("")
    val isAiThinking = MutableStateFlow(false)

    // --- Notifications State ---
    val notifications = repository.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Creator & Call State ---
    val showCreatorDashboard = MutableStateFlow(false)
    val showAdminPanel = MutableStateFlow(false)

    val activeCallState = MutableStateFlow("IDLE") // "IDLE", "VOICE", "VIDEO"
    val activeCallPartnerName = MutableStateFlow("Contact")
    val activeCallPartnerAvatar = MutableStateFlow("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80")
    val isCallMuted = MutableStateFlow(false)
    val isCallCameraOn = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            repository.clearAllPosts()
            repository.seedInitialDataIfEmpty()
        }
    }

    // --- Actions ---

    fun onTabSelected(tab: NavigationTab) {
        currentTab.value = tab
    }

    fun toggleDarkTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }

    fun toggleLike(postId: String, isLiked: Boolean, likesCount: Int) {
        viewModelScope.launch {
            repository.toggleLike(postId, isLiked, likesCount)
        }
    }

    fun toggleSave(postId: String, isSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSave(postId, isSaved)
        }
    }

    fun generateAiCaptionForPost() {
        val topic = newPostText.value.ifEmpty { "tech innovation and creative social media vibes" }
        viewModelScope.launch {
            isGeneratingCaption.value = true
            val generated = geminiService.generateCaption(topic)
            newPostText.value = generated
            isGeneratingCaption.value = false
        }
    }

    fun submitNewPost() {
        val text = newPostText.value.trim()
        if (text.isEmpty()) return

        val newPost = PostEntity(
            id = "post_${System.currentTimeMillis()}",
            authorUid = currentUser.value.uid,
            authorName = currentUser.value.displayName,
            authorUsername = currentUser.value.username,
            authorAvatarUrl = currentUser.value.avatarUrl,
            isVerified = currentUser.value.isVerified,
            timestamp = "Just now",
            contentText = text,
            mediaUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
            mediaType = "IMAGE",
            likesCount = 0,
            commentsCount = 0,
            repostsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isSaved = false,
            isReposted = false,
            topicTag = selectedPostTopic.value,
            aiTranslatedText = null
        )

        viewModelScope.launch {
            repository.createPost(newPost)
            newPostText.value = ""
            showCreatePostSheet.value = false
        }
    }

    fun translatePost(post: PostEntity) {
        viewModelScope.launch {
            val translated = geminiService.translatePost(post.contentText)
            val updated = post.copy(aiTranslatedText = translated)
            repository.createPost(updated)
        }
    }

    fun sendChatMessage() {
        val text = chatInputText.value.trim()
        val partner = selectedChatPartner.value ?: return
        if (text.isEmpty()) return

        val newMsg = MessageEntity(
            id = "msg_${System.currentTimeMillis()}",
            senderUid = currentUser.value.uid,
            senderName = currentUser.value.displayName,
            recipientUid = partner.uid,
            text = text,
            mediaUrl = null,
            isVoice = false,
            voiceDuration = 0,
            timestamp = "Just now",
            readReceipt = "SENT",
            isE2EE = true
        )

        viewModelScope.launch {
            repository.sendMessage(newMsg)
            chatInputText.value = ""
        }
    }

    fun sendAiPrompt() {
        val text = aiInputText.value.trim()
        if (text.isEmpty()) return

        val userMsg = AiChatMessage(
            id = "user_${System.currentTimeMillis()}",
            isFromUser = true,
            text = text,
            timestamp = "Just now"
        )

        aiMessages.value = aiMessages.value + userMsg
        aiInputText.value = ""
        isAiThinking.value = true

        viewModelScope.launch {
            val replyText = geminiService.askAssistant(text)
            val aiMsg = AiChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                isFromUser = false,
                text = replyText,
                timestamp = "Just now"
            )
            aiMessages.value = aiMessages.value + aiMsg
            isAiThinking.value = false
        }
    }

    fun toggleCommunityJoin(community: CommunityGroup) {
        viewModelScope.launch {
            repository.toggleJoinCommunity(community.id, community.isJoined, community.membersCount)
        }
    }

    fun startCall(type: String, partnerName: String = "Maya Lin") {
        activeCallPartnerName.value = partnerName
        activeCallState.value = type
    }

    fun endCall() {
        activeCallState.value = "IDLE"
    }

    fun toggle2FA() {
        val current = currentUser.value
        currentUser.value = current.copy(is2FAEnabled = !current.is2FAEnabled)
    }

    fun performLogin(email: String, provider: String = "Email") {
        if (currentUser.value.is2FAEnabled) {
            show2FADialog.value = true
        } else {
            isLoggedIn.value = true
            isGuestMode.value = false
        }
    }

    fun verify2FA() {
        if (twoFactorCodeInput.value.length >= 4) {
            show2FADialog.value = false
            isLoggedIn.value = true
            isGuestMode.value = false
            twoFactorCodeInput.value = ""
        }
    }

    fun loginAsGuest() {
        isLoggedIn.value = true
        isGuestMode.value = true
    }

    fun logout() {
        isLoggedIn.value = false
        isGuestMode.value = false
    }
}
