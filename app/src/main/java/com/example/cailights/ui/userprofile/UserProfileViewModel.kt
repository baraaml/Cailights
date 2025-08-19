package com.example.cailights.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.cailights.data.model.User
import com.example.cailights.ui.history.HighlightItem

data class UserProfileState(
    val user: User? = null,
    val highlights: List<HighlightItem> = emptyList(),
    val pinnedHighlight: HighlightItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class UserProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                // TODO: Replace with actual repository call
                val user = getMockUser(userId)
                val highlights = getMockUserHighlights(userId)
                val pinnedHighlight = highlights.firstOrNull()

                _state.update {
                    it.copy(
                        user = user,
                        highlights = highlights,
                        pinnedHighlight = pinnedHighlight,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun toggleFollow() {
        _state.update { currentState ->
            currentState.user?.let { user ->
                val updatedUser = user.copy(
                    isFollowing = !user.isFollowing,
                    followersCount = if (user.isFollowing) {
                        user.followersCount - 1
                    } else {
                        user.followersCount + 1
                    }
                )
                currentState.copy(user = updatedUser)
            } ?: currentState
        }
    }

    // Mock data - replace with repository calls
    private fun getMockUser(userId: String): User {
        return when (userId) {
            "user_sarah" -> User(
                id = "user_sarah",
                username = "sarah_dev",
                displayName = "Sarah Johnson",
                bio = "Senior Android Developer at TechCorp • Kotlin enthusiast • Building better mobile experiences",
                followersCount = 247,
                followingCount = 89,
                highlightsCount = 34,
                isFollowing = false,
                isVerified = true
            )
            "user_alex" -> User(
                id = "user_alex",
                username = "alex_codes",
                displayName = "Alex Chen",
                bio = "Full-stack developer • Open source contributor • Coffee addict ☕",
                followersCount = 156,
                followingCount = 203,
                highlightsCount = 28,
                isFollowing = true
            )
            "user_priya" -> User(
                id = "user_priya",
                username = "priya_designs",
                displayName = "Priya Patel",
                bio = "UX/UI Designer • Creating delightful user experiences • Design systems advocate",
                followersCount = 389,
                followingCount = 145,
                highlightsCount = 42,
                isFollowing = false
            )
            // Handle highlight user IDs
            "user_101" -> User(
                id = "user_101",
                username = "gnanaraja",
                displayName = "Gnanraj",
                bio = "Software Engineer • Learning and growing every day",
                followersCount = 95,
                followingCount = 134,
                highlightsCount = 12,
                isFollowing = false
            )
            "user_102" -> User(
                id = "user_102",
                username = "amanda",
                displayName = "Amanda Rodriguez",
                bio = "Tech Writer • Android Developer • Jetpack Compose enthusiast",
                followersCount = 312,
                followingCount = 178,
                highlightsCount = 47,
                isFollowing = false
            )
            "user_103" -> User(
                id = "user_103",
                username = "alex_dev",
                displayName = "Alex Thompson",
                bio = "System Architect • Distributed systems expert • Microservices advocate",
                followersCount = 445,
                followingCount = 267,
                highlightsCount = 63,
                isFollowing = false
            )
            "user_104" -> User(
                id = "user_104",
                username = "sarah_codes",
                displayName = "Sarah Miller",
                bio = "Team Lead • Scrum Master • Empowering developers to grow",
                followersCount = 289,
                followingCount = 156,
                highlightsCount = 38,
                isFollowing = false
            )
            "user_105" -> User(
                id = "user_105",
                username = "dev_mike",
                displayName = "Mike Johnson",
                bio = "React Native Developer • Performance optimization specialist",
                followersCount = 178,
                followingCount = 223,
                highlightsCount = 29,
                isFollowing = false
            )
            "user_106" -> User(
                id = "user_106",
                username = "bug_hunter",
                displayName = "Lisa Chen",
                bio = "Senior Developer • Bug detective • Production hero 🦸‍♀️",
                followersCount = 356,
                followingCount = 189,
                highlightsCount = 52,
                isFollowing = false
            )
            "user_107" -> User(
                id = "user_107",
                username = "open_source_fan",
                displayName = "David Kim",
                bio = "Open Source Contributor • UI Library maintainer • Community builder",
                followersCount = 523,
                followingCount = 412,
                highlightsCount = 78,
                isFollowing = false
            )
            "user_108" -> User(
                id = "user_108",
                username = "kotlin_lover",
                displayName = "Emma Wilson",
                bio = "Kotlin Developer • Coroutines expert • Workshop instructor",
                followersCount = 267,
                followingCount = 145,
                highlightsCount = 34,
                isFollowing = false
            )
            else -> User(
                id = userId,
                username = "unknown_user",
                displayName = "Unknown User",
                bio = "This user's profile is not available",
                followersCount = 0,
                followingCount = 0,
                highlightsCount = 0
            )
        }
    }

    private fun getMockUserHighlights(userId: String): List<HighlightItem> {
        val now = System.currentTimeMillis()
        val hourInMillis = 60 * 60 * 1000L
        val dayInMillis = 24 * hourInMillis
        
        return when (userId) {
            "user_sarah" -> listOf(
                HighlightItem(
                    id = 201,
                    date = "15 Nov",
                    title = "Launched new feature with 99.9% crash-free rate",
                    fullContent = "Successfully shipped the new notification system with extensive testing and monitoring. Achieved 99.9% crash-free rate in the first week.",
                    tag = "achievement",
                    username = "sarah_dev",
                    userId = "user_sarah"
                ),
                HighlightItem(
                    id = 202,
                    date = "12 Nov",
                    title = "Mentored junior developer through first major project",
                    fullContent = "Guided a junior team member through their first major feature implementation. They successfully delivered on time and learned valuable debugging skills.",
                    tag = "mentoring",
                    username = "sarah_dev",
                    userId = "user_sarah"
                ),
                HighlightItem(
                    id = 203,
                    date = "8 Nov",
                    title = "Gave tech talk on Jetpack Compose best practices",
                    fullContent = "Presented to 50+ developers about state management and performance optimization in Compose. Received great feedback and follow-up questions.",
                    tag = "speaking",
                    username = "sarah_dev",
                    userId = "user_sarah"
                )
            )
            "user_alex" -> listOf(
                HighlightItem(
                    id = 301,
                    date = "14 Nov",
                    title = "Open source contribution merged into popular library",
                    fullContent = "My PR to add dark mode support was merged into a library with 10k+ stars. Excited to see it help the community!",
                    tag = "open-source",
                    username = "alex_codes",
                    userId = "user_alex"
                ),
                HighlightItem(
                    id = 302,
                    date = "10 Nov",
                    title = "Reduced API response time by 40%",
                    fullContent = "Optimized database queries and added caching layer. Response times went from 200ms to 120ms average.",
                    tag = "performance",
                    username = "alex_codes",
                    userId = "user_alex"
                )
            )
            "user_priya" -> listOf(
                HighlightItem(
                    id = 401,
                    date = "16 Nov",
                    title = "Design system adoption reached 80% across teams",
                    fullContent = "The design system I created is now used by 8 out of 10 product teams. This has reduced design inconsistencies by 60%.",
                    tag = "design-systems",
                    username = "priya_designs",
                    userId = "user_priya"
                ),
                HighlightItem(
                    id = 402,
                    date = "13 Nov",
                    title = "User research led to 25% increase in conversions",
                    fullContent = "Conducted user interviews and usability testing. The insights led to design changes that improved conversion rates significantly.",
                    tag = "user-research",
                    username = "priya_designs",
                    userId = "user_priya"
                )
            )
            // Highlights for feed users
            "user_101" -> listOf(
                HighlightItem(
                    id = 101,
                    date = "Today",
                    title = "figured out how it's hard to keep wasting time in doing non important things.",
                    fullContent = "I realized that I've been spending too much time on activities that don't contribute to my goals. Need to be more intentional with my time.",
                    tag = "bootcamp",
                    createdAt = now - 5 * 60 * 1000L,
                    username = "gnanaraja",
                    userId = "user_101"
                ),
                HighlightItem(
                    id = 501,
                    date = "Yesterday",
                    title = "Completed JavaScript fundamentals course",
                    fullContent = "Finally finished the JS course and built my first interactive web application. The concepts are starting to click!",
                    tag = "learning",
                    createdAt = now - dayInMillis,
                    username = "gnanaraja",
                    userId = "user_101"
                )
            )
            "user_102" -> listOf(
                HighlightItem(
                    id = 102,
                    date = "Today",
                    title = "Made another career achievement",
                    fullContent = "Published technical blog post about Jetpack Compose, specially about state management which was featured in a popular newsletter.",
                    tag = "newAchievement",
                    createdAt = now - 61 * 60 * 1000L,
                    username = "amanda",
                    userId = "user_102"
                ),
                HighlightItem(
                    id = 502,
                    date = "2 days ago",
                    title = "Spoke at local Android meetup",
                    fullContent = "Gave a presentation about modern Android development practices. Great discussion with fellow developers afterward.",
                    tag = "speaking",
                    createdAt = now - 2 * dayInMillis,
                    username = "amanda",
                    userId = "user_102"
                )
            )
            "user_103" -> listOf(
                HighlightItem(
                    id = 103,
                    date = "Yesterday",
                    title = "Completed advanced system design course",
                    fullContent = "Finished the distributed systems course and immediately applied the concepts to optimize our microservices architecture.",
                    tag = "lesson-learned",
                    createdAt = now - 4 * hourInMillis,
                    username = "alex_dev",
                    userId = "user_103"
                ),
                HighlightItem(
                    id = 503,
                    date = "3 days ago",
                    title = "Architected new microservice for user authentication",
                    fullContent = "Designed and implemented a scalable authentication service that reduced login times by 50% and improved security.",
                    tag = "architecture",
                    createdAt = now - 3 * dayInMillis,
                    username = "alex_dev",
                    userId = "user_103"
                )
            )
            "user_104" -> listOf(
                HighlightItem(
                    id = 104,
                    date = "Yesterday",
                    title = "Led my first team standup meeting",
                    fullContent = "Successfully facilitated the daily standup and helped the team identify blockers early. Felt confident in my leadership skills.",
                    tag = "newAchievement",
                    createdAt = now - 18 * hourInMillis,
                    username = "sarah_codes",
                    userId = "user_104"
                ),
                HighlightItem(
                    id = 504,
                    date = "1 week ago",
                    title = "Implemented team retrospective process",
                    fullContent = "Introduced bi-weekly retrospectives that improved team velocity by 30% and increased satisfaction scores.",
                    tag = "leadership",
                    createdAt = now - 7 * dayInMillis,
                    username = "sarah_codes",
                    userId = "user_104"
                )
            )
            "user_105" -> listOf(
                HighlightItem(
                    id = 105,
                    date = "2 days ago",
                    title = "Learned about React Native performance optimization",
                    fullContent = "Deep dive into React Native performance bottlenecks and how to use Flipper for debugging. Applied these techniques to reduce app startup time by 40%.",
                    tag = "bootcamp",
                    createdAt = now - 2 * dayInMillis,
                    username = "dev_mike",
                    userId = "user_105"
                ),
                HighlightItem(
                    id = 505,
                    date = "5 days ago",
                    title = "Optimized app bundle size by 35%",
                    fullContent = "Used bundle analyzer tools and code splitting to significantly reduce app size, improving download times for users.",
                    tag = "performance",
                    createdAt = now - 5 * dayInMillis,
                    username = "dev_mike",
                    userId = "user_105"
                )
            )
            "user_106" -> listOf(
                HighlightItem(
                    id = 106,
                    date = "3 days ago",
                    title = "Fixed critical production bug under pressure",
                    fullContent = "Identified and resolved a memory leak that was causing crashes for 15% of users. The fix went out in a hotfix release within 2 hours.",
                    tag = "lesson-learned",
                    createdAt = now - 3 * dayInMillis,
                    username = "bug_hunter",
                    userId = "user_106"
                ),
                HighlightItem(
                    id = 506,
                    date = "1 week ago",
                    title = "Set up comprehensive monitoring and alerting",
                    fullContent = "Implemented monitoring dashboards and automated alerts that help us catch issues before they affect users.",
                    tag = "devops",
                    createdAt = now - 7 * dayInMillis,
                    username = "bug_hunter",
                    userId = "user_106"
                )
            )
            "user_107" -> listOf(
                HighlightItem(
                    id = 107,
                    date = "4 days ago",
                    title = "Started contributing to open source project",
                    fullContent = "Made my first contribution to a popular UI library. The maintainers were very welcoming and provided great feedback on my PR.",
                    tag = "newAchievement",
                    createdAt = now - 4 * dayInMillis,
                    username = "open_source_fan",
                    userId = "user_107"
                ),
                HighlightItem(
                    id = 507,
                    date = "1 week ago",
                    title = "Became maintainer of UI component library",
                    fullContent = "After months of contributions, I was invited to become a core maintainer of a library used by 10k+ developers.",
                    tag = "open-source",
                    createdAt = now - 7 * dayInMillis,
                    username = "open_source_fan",
                    userId = "user_107"
                )
            )
            "user_108" -> listOf(
                HighlightItem(
                    id = 108,
                    date = "1 week ago",
                    title = "Attended advanced Kotlin workshop",
                    fullContent = "Learned about Kotlin coroutines flow operators and how to handle complex async operations. Already planning to refactor our current implementation.",
                    tag = "bootcamp",
                    createdAt = now - 7 * dayInMillis,
                    username = "kotlin_lover",
                    userId = "user_108"
                ),
                HighlightItem(
                    id = 508,
                    date = "2 weeks ago",
                    title = "Built custom Kotlin DSL for configuration",
                    fullContent = "Created a type-safe configuration DSL that makes our app setup more readable and prevents runtime errors.",
                    tag = "kotlin",
                    createdAt = now - 14 * dayInMillis,
                    username = "kotlin_lover",
                    userId = "user_108"
                )
            )
            else -> emptyList()
        }
    }
}
