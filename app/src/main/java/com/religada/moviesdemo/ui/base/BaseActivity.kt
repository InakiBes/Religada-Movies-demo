package com.religada.moviesdemo.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import com.religada.moviesdemo.databinding.ActivityBaseBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.notifications.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity: AppCompatActivity() {

    @Inject lateinit var navigator: AppNavigator
    @Inject
    lateinit var tokenManager: TokenManager

    private val bindingBase: ActivityBaseBinding by lazy {
        ActivityBaseBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewStub: ViewStub = bindingBase.navHostFragmentNavigationDrawer
        viewStub.layoutResource = setUpActivity()
        setContentView(bindingBase.root)
        setUpOnCreate(viewStub.inflate(), intent.extras)

        tokenManager.getInstaFirebase()
    }

    abstract fun setUpOnCreate(inflate: View, bundle: Bundle?)
    abstract fun setUpActivity(): Int
}