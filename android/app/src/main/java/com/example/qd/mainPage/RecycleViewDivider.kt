package com.example.qd.mainPage

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.support.v4.content.ContextCompat
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View


class RecycleViewDivider
/**
 * 默认分割线：高度为2px，颜色为灰色
 *
 * @param context
 * @param orientation 列表方向
 */
    (
    context: Context, private val mOrientation: Int//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
) : RecyclerView.ItemDecoration() {

    private lateinit var mPaint: Paint
    private var mDivider: Drawable? = null
    private var mDividerHeight = 2//分割线高度，默认为1px

    init {
        if (mOrientation != LinearLayoutManager.VERTICAL && mOrientation != LinearLayoutManager.HORIZONTAL) {
            throw IllegalArgumentException("请输入正确的参数！")
        }
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.setStyle(Paint.Style.FILL)
        val a = context.obtainStyledAttributes(ATTRS)//使用TypeArray加载该系统资源
        mDivider = a.getDrawable(0)
        a.recycle()//缓存
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context, orientation: Int, drawableId: Int) : this(context, orientation) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider!!.intrinsicHeight

    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(
        context,
        orientation
    ) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.setColor(dividerColor)
        mPaint.setStyle(Paint.Style.FILL)
    }


    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //设置偏移的高度是mDivider.getIntrinsicHeight，该高度正是分割线的高度
        outRect.set(0, 0, 0, mDividerHeight)
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    //绘制横向 item 分割线
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft//获取分割线的左边距，即RecyclerView的padding值
        val right = parent.measuredWidth - parent.paddingRight//分割线右边距
        val childSize = parent.childCount
        //遍历所有item view，为它们的下方绘制分割线
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

    //绘制纵向 item 分割线
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)//使用系统自带的listDivider
    }
}