package com.pyschool.app.ui.common

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.databinding.ItemNavTabBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen

/**
 * Bottom Navigation แบบ custom (Home / Visual / Quiz / History)
 * ใช้ซ้ำได้ทุกหน้าด้วย <com.pyschool.app.ui.common.BottomNavView/> + setActive(...)
 * การกดจะส่งต่อให้ [AppNavigator.openTab] เสมอ
 */
class BottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private data class Tab(val screen: Screen, @DrawableRes val icon: Int, @StringRes val label: Int)

    private val tabs = listOf(
        Tab(Screen.HOME, R.drawable.ic_home, R.string.nav_home),
        Tab(Screen.VISUAL, R.drawable.ic_play, R.string.nav_visual),
        Tab(Screen.QUIZ, R.drawable.ic_quiz, R.string.nav_quiz),
        Tab(Screen.HISTORY, R.drawable.ic_chart, R.string.nav_history),
    )
    private val bindings = LinkedHashMap<Screen, ItemNavTabBinding>()

    init {
        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.bg_bottom_nav)
        val inflater = LayoutInflater.from(context)
        tabs.forEach { tab ->
            val item = ItemNavTabBinding.inflate(inflater, this, false)
            item.icon.setImageResource(tab.icon)
            item.label.setText(tab.label)
            item.root.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            item.root.setOnClickListener { (context as? Activity)?.let { AppNavigator.openTab(it, tab.screen) } }
            addView(item.root)
            bindings[tab.screen] = item
        }
        applyNavigationBarPadding()
    }

    fun setActive(active: Screen) {
        val activeColor = ContextCompat.getColor(context, R.color.purple_deep)
        val idleColor = ContextCompat.getColor(context, R.color.mid_muted)
        bindings.forEach { (screen, b) ->
            val selected = screen == active
            val color = if (selected) activeColor else idleColor
            b.icon.setBackgroundResource(if (selected) R.drawable.bg_nav_active else 0)
            b.icon.setColorFilter(color)
            b.label.setTextColor(color)
        }
    }
}
