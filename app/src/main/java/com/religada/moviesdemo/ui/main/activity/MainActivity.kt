package com.religada.moviesdemo.ui.main.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.religada.moviesdemo.R
import com.religada.moviesdemo.databinding.ActivityMainBinding
import com.religada.moviesdemo.navigator.Screens
import com.religada.moviesdemo.ui.base.BaseActivity
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import com.religada.moviesdemo.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun setUpOnCreate(view: View, bundle: Bundle?) {
        binding = ActivityMainBinding.bind(view)

        initializeViews()
    }

    override fun setUpActivity(): Int {
        return R.layout.activity_main
    }

    private fun initializeViews() {
        navigator.navigateTo(Screens.MAIN, null)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount - 1 == 0) {
            showDialogConfirmation() {
                finish();
                exitProcess(0);
            }
        }
    }

    private fun showDialogConfirmation(onResponse: () -> Unit) {
        val dialog = Dialog(baseContext)
        with(dialog) {
            requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setContentView(R.layout.dialog_yes_no)
        }
        // Texts
        dialog.findViewById<TextView>(R.id.tvTitle).setText(R.string.exit_app)
        dialog.findViewById<TextView>(R.id.tv_info).setText(R.string.are_you_sure_exit)
        // Buttons
        dialog.findViewById<Button>(R.id.bt_yes).setOnClickListener {
            dialog.dismiss()
            onResponse()
        }
        dialog.findViewById<Button>(R.id.bt_no).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}