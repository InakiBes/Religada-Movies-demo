package com.religada.moviesdemo.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Interfaces that defines an app navigator.
 */
interface AppNavigator {
    // Navigate to a given screen.
    fun navigateTo(screen: Screens, bundle: Bundle?)
    // Set current fragment
    fun setCurrentFragment(fragment: Fragment)
}