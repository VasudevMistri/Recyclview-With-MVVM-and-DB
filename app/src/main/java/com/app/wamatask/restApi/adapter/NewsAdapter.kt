package com.app.wamatask.restApi.adapter

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.wamatask.R
import com.app.wamatask.databinding.RowNewsListBinding
import com.app.wamatask.restApi.RestApiActivity
import com.app.wamatask.restApi.model.Article
import com.app.wamatask.utils.MyBioSpannable

class NewsAdapter(
    val context: RestApiActivity,
    var arrayList: ArrayList<Article>
) : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    var mLowestPosition = -1

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflater.inflate(R.layout.row_news_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding!!.data = arrayList[position]
        holder.mBinding!!.position = position
        holder.mBinding!!.handler = NewsAdapterHandler(context)

        if (position > mLowestPosition) {
            if (arrayList.get(position).content != null && !arrayList.get(position).content.toString()
                    .isEmpty()
            ) {
                holder.mBinding!!.tvContent.text = arrayList.get(position).content
                makeTextViewResizable(holder.mBinding!!.tvContent, 2, "..ReadMore", true)
            }
            mLowestPosition = position
        }

    }

    override fun getItemCount(): Int {
        return if (arrayList == null) 0 else arrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder internal constructor(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        var mBinding: RowNewsListBinding?

        init {
            mBinding = DataBindingUtil.bind(itemView!!)
        }
    }


    fun makeTextViewResizable(
        tv: TextView,
        maxLine: Int,
        expandText: String,
        viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    val lineEndIndex = tv.layout.getLineEnd(0)
                    val text =
                        tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                            .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    val text =
                        tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                            .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else {
                    val lineEndIndex =
                        tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    val text =
                        tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                }
            }
        })
    }


    fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MyBioSpannable(false) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "..ReadLess", false)
                    } else {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 2, "..ReadMore", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }


}
