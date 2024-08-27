package com.amity.socialcloud.uikit.community.home.fragments

import androidx.databinding.ObservableBoolean
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityCommunityHomeViewModel : AmityBaseViewModel() {
    var isSearchMode = ObservableBoolean(false)
    val emptySearchString = ObservableBoolean(true)
    val textChangeSubject: PublishSubject<String> = PublishSubject.create()
}
