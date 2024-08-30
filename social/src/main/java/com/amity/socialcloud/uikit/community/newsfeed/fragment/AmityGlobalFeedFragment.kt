package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.databinding.AmityViewGlobalFeedEmptyBinding
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomeViewModel
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityGlobalFeedViewModel
import com.amity.socialcloud.uikit.community.ui.view.AmityCommunityCreatorActivity
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.rxjava3.core.Flowable

class AmityGlobalFeedFragment : AmityFeedFragment() {

    private val communityHomeViewModel: AmityCommunityHomeViewModel by activityViewModels()
    private var userClickListener: AmityUserClickListener? = null
    private var communityClickListener: AmityCommunityClickListener? = null
    private var postShareClickListener: AmityPostShareClickListener? = null
    private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userClickListener == null) {
            userClickListener = defaultUserClickListener(this)
        }
        if (communityClickListener == null) {
            communityClickListener = defaultCommunityClickListener(this)
        }
        if (postShareClickListener == null) {
            postShareClickListener = defaultPostShareClickListener()
        }
        getViewModel().let { viewModel ->
            viewModel.userClickListener = userClickListener
            viewModel.communityClickListener = communityClickListener
            viewModel.postShareClickListener = postShareClickListener
            viewModel.feedRefreshEvents = feedRefreshEvents
        }
    }

    override fun getViewModel(): AmityGlobalFeedViewModel {
        return ViewModelProvider(requireActivity()).get(AmityGlobalFeedViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        val binding = AmityViewGlobalFeedEmptyBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        binding.btnExplore.setOnClickListener {
            communityHomeViewModel.triggerEvent(AmityEventIdentifier.EXPLORE_COMMUNITY)
        }

        // tvCreateCommunity is hidden because we don't want to let users create communities on their own
        binding.tvFindCommunity.visibility = View.GONE
        binding.tvCreateCommunity.visibility = View.GONE

        binding.tvCreateCommunity.setOnClickListener {
            val intent = Intent(requireContext(), AmityCommunityCreatorActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        getViewModel().let { viewModel ->
            viewModel.userClickListener = null
            viewModel.communityClickListener = null
            viewModel.postShareClickListener = null
            viewModel.feedRefreshEvents = Flowable.never()
        }
    }

    class Builder internal constructor() {
        private var userClickListener: AmityUserClickListener? = null
        private var communityClickListener: AmityCommunityClickListener? = null
        private var postShareClickListener: AmityPostShareClickListener? = null
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmityGlobalFeedFragment {
            val fragment = AmityGlobalFeedFragment()
            // val viewModel = ViewModelProvider(activity).get(AmityGlobalFeedViewModel::class.java)
            fragment.userClickListener = userClickListener
                ?: defaultUserClickListener(fragment)
            fragment.communityClickListener = communityClickListener
                ?: defaultCommunityClickListener(fragment)
            fragment.postShareClickListener = postShareClickListener
                ?: defaultPostShareClickListener()
            fragment.feedRefreshEvents = feedRefreshEvents
            return fragment
        }

        fun userClickListener(userClickListener: AmityUserClickListener): Builder {
            return apply { this.userClickListener = userClickListener }
        }

        fun postShareClickListener(postShareClickListener: AmityPostShareClickListener): Builder {
            return apply { this.postShareClickListener = postShareClickListener }
        }

        fun feedRefreshEvents(feedRefreshEvents: Flowable<AmityFeedRefreshEvent>): Builder {
            return apply { this.feedRefreshEvents = feedRefreshEvents }
        }

    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }

        private fun defaultUserClickListener(fragment: Fragment) = object : AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(fragment, user)
            }
        }

        private fun defaultCommunityClickListener(fragment: Fragment) =
            object : AmityCommunityClickListener {
                override fun onClickCommunity(community: AmityCommunity) {
                    AmitySocialUISettings.globalCommunityClickListener.onClickCommunity(
                        fragment, community
                    )
                }
            }

        private fun defaultPostShareClickListener() = AmitySocialUISettings.postShareClickListener
    }

}
