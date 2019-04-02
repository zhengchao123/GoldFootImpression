package com.gold.footimpression.bindingadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.ui.event.WorkerClickEvent

open class CommonAdapter<T>() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null
    private var mDataList: MutableList<T>? = mutableListOf()
    private var layoutId = 0 //
    private var variableId = 0//
    private var clickVariabledId = 0
    private var mOnItemClick: OnItemClick? = null

    constructor(context: Context, dataList: MutableList<T>?, layoutId: Int, variableId: Int) : this() {
        this.mContext = context
        this.mDataList!!.clear()
        this.mDataList!!.addAll(dataList!!)
        this.layoutId = layoutId
        this.variableId = variableId
    }

    constructor(context: Context, dataList: MutableList<T>?, layoutId: Int, variableId: Int, clickVariabledId: Int) : this() {
        this.mContext = context
        this.mDataList!!.clear()
        this.mDataList!!.addAll(dataList!!)
        this.layoutId = layoutId
        this.variableId = variableId
        this.clickVariabledId = clickVariabledId
    }

    fun setOnItemClick(click: OnItemClick) {
        this.mOnItemClick = click
    }

    fun update(datas: MutableList<T>) {
        mDataList!!.clear()
        mDataList!!.addAll(datas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        return CommViweHolder<T>(binding)
    }

    override fun getItemCount(): Int = mDataList!!.size

    fun getDatas(): MutableList<T>? = mDataList

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as CommViweHolder<T>
        holder.bindData(mDataList!!.get(position), position, variableId)
    }

    inner class CommViweHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TAG = this.javaClass.simpleName
        var dataBinding: ViewDataBinding? = null

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.dataBinding = binding
        }

        fun bindData(dataItem: T, position: Int, variableId: Int) {
            LogUtils.i(TAG, " bindData $position")
            this.itemView.setOnClickListener { v: View ->
                LogUtils.i(TAG, " click item ${v.id} position $position")
//                EmptyActivity.start(mContext!!)
                mOnItemClick?.onItemClick(v, position)
            }
            dataBinding!!.setVariable(variableId, dataItem)
            dataBinding!!.setVariable(clickVariabledId, object : WorkerClickEvent<T>(mContext!!, dataItem) {
                override fun onClickView(view: View?) {
                    defaultAction(false)
                    super.onClickView(view)
                    LogUtils.i(TAG, " click item ${view?.id} position $position 2222")
                    mOnItemClick?.onItemClick(view!!, position, instance!!,view.id)
                }
            })
            dataBinding!!.executePendingBindings()
        }
    }
}