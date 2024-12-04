package com.amity.socialcloud.uikit.community.compose.socialhome.components

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmitySocialHomeTopNavigationComponentBehavior {

    class Context(val componentContext: android.content.Context)

    open fun goToCreateCommunityPage(
        context: Context,
    ) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context.componentContext,
            mode = AmityCommunitySetupPageMode.Create
        )
        context.componentContext.startActivity(intent)
    }

    open fun goToUserProfilePage(
        context: android.content.Context,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = AmityCoreClient.getUserId()
        )
        context.startActivity(intent)
    }
}
