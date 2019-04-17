package com.sduduzog.slimlauncher.ui

import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.utils.Publisher
import com.sduduzog.slimlauncher.utils.Subscriber

abstract class BaseFragment : Fragment(), Subscriber {

    abstract fun getFragmentView(): View


    override fun onResume() {
        super.onResume()
        val settings = context!!.getSharedPreferences(getString(R.string.prefs_settings), AppCompatActivity.MODE_PRIVATE)
        val active = settings.getInt(getString(R.string.prefs_settings_key_theme), 0)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (active) {
                0, 3, 5 -> {
                    val flags = activity!!.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    getFragmentView().systemUiVisibility = flags
                }

            }
            val value = TypedValue()
            context!!.theme.resolveAttribute(R.attr.colorPrimary, value, true)
            activity!!.window.statusBarColor = value.data
        }
    }

    override fun onStart() {
        super.onStart()
        with(activity as Publisher){
            this.attatchSubscriber(this@BaseFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        with(activity as Publisher) {
            this.detachSubscriber(this@BaseFragment)
        }
    }

    protected fun launchActivity(view: View, intent: Intent) {
        val left = 0
        val top = 0
        val width = view.measuredWidth
        val height = view.measuredHeight
        val opts = ActivityOptionsCompat.makeClipRevealAnimation(view, left, top, width, height)
        startActivity(intent, opts.toBundle())
    }

    override fun onBack(): Boolean = false
}