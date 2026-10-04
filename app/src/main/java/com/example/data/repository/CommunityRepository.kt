package com.example.data.repository

import com.example.data.local.RecipeAndCommunityData
import com.example.data.model.CommunityPost
import com.example.data.model.FitnessChallenge
import com.example.data.model.LeaderboardUser
import com.example.data.model.PostCategory
import com.example.data.model.PostComment
import com.example.data.model.VirtualBadge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommunityRepository {

    private val _posts = MutableStateFlow(RecipeAndCommunityData.INITIAL_COMMUNITY_POSTS)
    val posts: Flow<List<CommunityPost>> = _posts.asStateFlow()

    private val _challenge = MutableStateFlow(RecipeAndCommunityData.CURRENT_CHALLENGE)
    val challenge: Flow<FitnessChallenge> = _challenge.asStateFlow()

    private val _leaderboard = MutableStateFlow(RecipeAndCommunityData.LEADERBOARD_USERS)
    val leaderboard: Flow<List<LeaderboardUser>> = _leaderboard.asStateFlow()

    private val _badges = MutableStateFlow(RecipeAndCommunityData.VIRTUAL_BADGES)
    val badges: Flow<List<VirtualBadge>> = _badges.asStateFlow()

    fun toggleLike(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val wasLiked = post.isLikedByMe
                    val newCount = if (wasLiked) (post.likesCount - 1).coerceAtLeast(0) else post.likesCount + 1
                    post.copy(isLikedByMe = !wasLiked, likesCount = newCount)
                } else post
            }
        }
    }

    fun addCheer(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(cheersCount = post.cheersCount + 1)
                } else post
            }
        }
    }

    fun addComment(postId: String, text: String, authorName: String = "Felhasználó (Te)") {
        if (text.isBlank()) return
        val newComment = PostComment(
            id = "c_${System.currentTimeMillis()}",
            authorName = authorName,
            authorAvatarEmoji = "💪",
            text = text.trim(),
            timeAgoHu = "Épp most"
        )
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(
                        comments = post.comments + newComment,
                        commentsCount = post.commentsCount + 1
                    )
                } else post
            }
        }
    }

    fun createPost(
        title: String,
        content: String,
        category: PostCategory,
        statsTag: String = "",
        authorName: String = "Felhasználó (Te)"
    ) {
        val newPost = CommunityPost(
            id = "post_${System.currentTimeMillis()}",
            authorName = authorName,
            authorBadge = "Aktív Tag",
            authorAvatarEmoji = "💪",
            timeAgoHu = "Épp most",
            category = category,
            title = title.trim(),
            content = content.trim(),
            statsTag = statsTag,
            likesCount = 1,
            isLikedByMe = true,
            commentsCount = 0,
            cheersCount = 1,
            comments = emptyList()
        )
        _posts.update { listOf(newPost) + it }

        // Boost points on leaderboard
        _leaderboard.update { list ->
            list.map { user ->
                if (user.isCurrentUser) {
                    user.copy(weeklyPoints = user.weeklyPoints + 40, recentActivity = "Új közösségi poszt megosztva!")
                } else user
            }
        }
    }

    fun joinOrLeaveChallenge() {
        _challenge.update { it.copy(isJoined = !it.isJoined) }
    }

    fun incrementChallengeDay() {
        _challenge.update { current ->
            val nextDays = (current.completedDays + 1).coerceAtMost(current.targetDays)
            current.copy(completedDays = nextDays)
        }
        // Check if unlocked 30 day badge
        if (_challenge.value.completedDays >= _challenge.value.targetDays) {
            _badges.update { list ->
                list.map { b ->
                    if (b.id == "badge_challenge_champion") {
                        b.copy(isUnlocked = true, unlockedDateHu = "Ma", progressCurrent = 30)
                    } else b
                }
            }
        }
    }
}
