package com.religada.moviesdemo.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.repository.MovieRepositoryLocal
import com.religada.moviesdemo.ui.main.fragment.MainFragment
import com.religada.moviesdemo.utils.PrefManager
import com.religada.moviesdemo.utils.log
import javax.inject.Inject

/**
 * Navigator implementation.
 */
class AppNavigatorImpl @Inject constructor(
    private val activity: FragmentActivity,
    private val prefManager: PrefManager,
    private val respositoryLocal: MovieRepositoryLocal
) : AppNavigator {

    private var currentFragment: Fragment? = null

    init {
        if (currentFragment == null)
            currentFragment = MainFragment(this, respositoryLocal)
    }

    override fun setCurrentFragment(fragment: Fragment) {
        currentFragment = fragment
    }

    override fun navigateTo(screen: Screens, bundle: Bundle?) {
        val destinationFragment = getFragment(screen)
        val currentAnimation = getAnimation(currentFragment!!)
        val destinationAnimation = getAnimation(destinationFragment)

        // Manage bundle
        bundle?.let {
            destinationFragment.arguments = it
        }

        // Manage animations
        val animations: List<Int> = if (destinationAnimation > 99000) {
            listOf(
                R.anim.fade_in, R.anim.nothing,
                R.anim.fade_out, R.anim.nothing
            )
        } else if (destinationAnimation > 10000) {
            listOf(
                R.anim.bottom_up, R.anim.nothing,
                R.anim.bottom_down, R.anim.nothing
            )
        } else {
            if (currentAnimation <= destinationAnimation) {
                listOf(
                    R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right
                )
            } else {
                listOf(
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                    R.anim.enter_right_to_left, R.anim.exit_right_to_left
                )
            }
        }

        // Make transaction
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                animations[0],
                animations[1],
                animations[2],
                animations[3]
            )
            .replace(R.id.main_fragment, destinationFragment)
            .addToBackStack(destinationFragment::class.java.canonicalName)
            .commit()

        currentFragment = destinationFragment
    }

    private fun getAnimation(fragment: Fragment): Int {
        fragment.let {
            return when {
                //Login
                it::class.java.canonicalName.equals(MainFragment::class.java.canonicalName) -> 1000

                else -> 0
            }
        }
    }

    private fun getFragment(screen: Screens): Fragment {
        return when (screen) {
            // Main
            Screens.MAIN -> MainFragment(this, respositoryLocal)

            else -> MainFragment(this, respositoryLocal).also {
                log(
                    "ERROR: $screen is not implemented in navigator"
                )
            }
        }
    }
}