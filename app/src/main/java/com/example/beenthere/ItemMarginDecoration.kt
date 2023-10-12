package com.example.beenthere

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemMarginDecoration(context: Context, private val marginInDp: Int) : RecyclerView.ItemDecoration() {
    private val margin: Int = 16

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = margin
    }
}