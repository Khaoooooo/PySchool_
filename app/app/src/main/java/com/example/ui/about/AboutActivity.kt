package com.pyschool.app.ui.about

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.databinding.ActivityAboutBinding
import com.pyschool.app.databinding.ItemMemberCardBinding
import com.pyschool.app.databinding.ItemTechChipBinding
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.applyNavigationBarPadding
import com.pyschool.app.ui.common.applyStatusBarPadding

/**
 * หน้า About: ผู้จัดทำ, เทคโนโลยีที่ใช้ และเครดิต/แหล่งอ้างอิง
 */
class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    private data class Member(
        val number: String,
        @param:StringRes val name: Int,
        @param:StringRes val studentId: Int,
        @param:StringRes val role: Int,
        @param:DrawableRes val imageResId: Int? = null,
    )

    private val team = listOf(
        Member(
            "01",
            R.string.member_01_name,
            R.string.member_01_id,
            R.string.member_01_role,
            R.drawable.img_6706021611325,
        ),
        Member(
            "02",
            R.string.member_02_name,
            R.string.member_02_id,
            R.string.member_02_role,
            R.drawable.img_6706021611287,
        ),
        Member(
            "03",
            R.string.member_03_name,
            R.string.member_03_id,
            R.string.member_03_role,
            R.drawable.img_6706021611091,
        ),
    )

    private val technologies = listOf("Kotlin", "XML", "Android Studio", "SQLite", "Canvas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.content.applyNavigationBarPadding()
        binding.header.headerTitle.setText(R.string.about_title)
        binding.header.backButton.setOnClickListener { finish() }

        buildTeam()
        buildTechChips()
    }

    private fun buildTeam() {
        val gap = (5.5f * resources.displayMetrics.density).toInt()
        team.forEachIndexed { index, member ->
            val card = ItemMemberCardBinding.inflate(layoutInflater, binding.teamGrid, false)
            if (member.imageResId != null) {
                card.avatarImage.setImageResource(member.imageResId)
                card.avatarImage.visibility = View.VISIBLE
                card.avatarText.visibility = View.GONE
            } else {
                card.avatarImage.visibility = View.GONE
                card.avatarText.text = member.number
                card.avatarText.visibility = View.VISIBLE
            }
            card.memberName.setText(member.name)
            card.memberId.setText(member.studentId)
            card.memberRole.setText(member.role)
            val color = if (index % 2 == 0) R.color.purple else R.color.yellow
            card.avatar.setCardBackgroundColor(ContextCompat.getColor(this, color))
            card.root.layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
            ).apply {
                width = 0
                setMargins(gap, gap, gap, gap)
            }
            binding.teamGrid.addView(card.root)
        }
    }

    private fun buildTechChips() {
        technologies.forEachIndexed { index, name ->
            val chip = ItemTechChipBinding.inflate(layoutInflater, binding.techGroup, false).root
            chip.text = name
            val dark = (index % 2 == 0)
            chip.setBackgroundResource(if (dark) R.drawable.bg_chip_ink else R.drawable.bg_chip_purple)
            chip.setTextColor(ContextCompat.getColor(this, if (dark) R.color.white else R.color.ink))
            binding.techGroup.addView(chip)
        }
    }
}
