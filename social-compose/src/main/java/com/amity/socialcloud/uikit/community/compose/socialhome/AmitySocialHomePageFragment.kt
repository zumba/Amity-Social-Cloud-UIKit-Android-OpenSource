package com.amity.socialcloud.uikit.community.compose.socialhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class AmitySocialHomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AmitySocialHomePage()
            }
        }
    }

    companion object {
        fun newInstance() = AmitySocialHomePageFragment()
    }
}
