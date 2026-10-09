package com.pyschool.app.ui.lesson

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.data.content.Block
import com.pyschool.app.data.content.Section
import com.pyschool.app.databinding.BlockChipsBinding
import com.pyschool.app.databinding.BlockCodeBinding
import com.pyschool.app.databinding.BlockMappingBinding
import com.pyschool.app.databinding.BlockParagraphBinding
import com.pyschool.app.databinding.BlockTipBinding
import com.pyschool.app.databinding.ItemSectionCardBinding
import com.pyschool.app.databinding.ItemTypeChipBinding
import com.pyschool.app.databinding.RowMappingBinding
import com.pyschool.app.ui.common.PythonHighlighter

/**
 * แปลงข้อมูลเนื้อหา (Section/Block) เป็น View
 * ทุกบทเรียนใช้ตัวนี้ร่วมกัน → หน้าตาสม่ำเสมอ และเพิ่มบทใหม่ได้โดยไม่ต้องเขียน layout ใหม่
 */
class LessonRenderer(private val context: Context) {

    private val inflater = LayoutInflater.from(context)
    private val density = context.resources.displayMetrics.density

    fun section(parent: ViewGroup, number: Int, section: Section): View {
        val card = ItemSectionCardBinding.inflate(inflater, parent, false)
        card.sectionIndex.text = "%02d".format(number)
        card.sectionTitle.text = section.title
        section.blocks.forEach { block ->
            val view = block(card.blocks, block)
            view.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            ).apply { topMargin = dp(12) }
            card.blocks.addView(view)
        }
        return card.root
    }

    private fun block(parent: ViewGroup, block: Block): View = when (block) {
        is Block.Paragraph -> BlockParagraphBinding.inflate(inflater, parent, false).also { it.root.text = block.text }.root
        is Block.Tip -> BlockTipBinding.inflate(inflater, parent, false).also { it.tipText.text = block.text }.root
        is Block.Code -> code(parent, block)
        is Block.Mapping -> mapping(parent, block)
        is Block.Chips -> chips(parent, block)
    }

    private fun code(parent: ViewGroup, block: Block.Code): View {
        val b = BlockCodeBinding.inflate(inflater, parent, false)
        b.codeText.text = PythonHighlighter.highlight(context, block.code)
        if (block.output != null) {
            b.outputCard.visibility = View.VISIBLE
            b.outputText.text = block.output
        }
        return b.root
    }

    private fun mapping(parent: ViewGroup, block: Block.Mapping): View {
        val b = BlockMappingBinding.inflate(inflater, parent, false)
        block.rows.forEach { (code, description) ->
            val row = RowMappingBinding.inflate(inflater, b.rows, false)
            row.code.text = code
            row.description.text = description
            b.rows.addView(row.root)
        }
        // แถวแรกไม่ต้องมีระยะห่างบน (ระยะห่างระหว่างบล็อกจัดการแล้ว)
        (b.rows.getChildAt(0)?.layoutParams as? LinearLayout.LayoutParams)?.topMargin = 0
        return b.root
    }

    private fun chips(parent: ViewGroup, block: Block.Chips): View {
        val b = BlockChipsBinding.inflate(inflater, parent, false)
        block.items.forEachIndexed { index, (name, value) ->
            val chip = ItemTypeChipBinding.inflate(inflater, b.grid, false)
            chip.typeName.text = name
            chip.typeValue.text = value
            // ลำดับที่ 2–3 พื้นเหลืองอ่อน (ตรงกับ nth-child ใน CSS ของโปรโตไทป์)
            val bg = if (index == 1 || index == 2) R.color.yellow_pale else R.color.purple_pale
            chip.root.setCardBackgroundColor(ContextCompat.getColor(context, bg))
            chip.root.layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
            ).apply {
                width = 0
                setMargins(dp(4), dp(4), dp(4), dp(4))
            }
            b.grid.addView(chip.root)
        }
        return b.root
    }

    private fun dp(value: Int) = (value * density).toInt()
}
