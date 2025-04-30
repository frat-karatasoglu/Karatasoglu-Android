package com.example.myapplication.datastore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsBadgeManager {

    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge

    fun updateBadgeState(shouldShow: Boolean) {
        _showBadge.value = shouldShow
    }
}
