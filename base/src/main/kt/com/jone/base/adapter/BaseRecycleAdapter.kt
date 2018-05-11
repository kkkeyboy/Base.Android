package com.jone.base.adapter;

import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jone.base.cache.image.ImageHelper


/**
 * Created by Jone.Luo on 2017/4/21.
 */

abstract class BaseRecycleAdapter<T>(protected val data: MutableList<T>) : RecyclerView.Adapter<BaseRecycleAdapter.BaseRecycleAdapterHolder>()
{

     var onItemClickListener: OnItemClickListener? = null
    protected var isCustomHandleClick: Boolean = false

    @LayoutRes protected abstract fun layoutResId(viewType: Int): Int

    /**
     * 绑定View
     */
    protected abstract fun onBindView(holder: BaseRecycleAdapterHolder, position: Int, itemData: T)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecycleAdapterHolder
    {
        return BaseRecycleAdapterHolder(parent, layoutResId(viewType))
    }

   final override fun onBindViewHolder(holder: BaseRecycleAdapterHolder, position: Int)
    {
        onBindView(holder, position, data[position]);
        if (!(isCustomHandleClick || onItemClickListener == null))
        {
            holder.setOnClickListener(onItemClickListener!!)
        }
    }

    final override fun getItemCount() = data.size

    fun refreshData(data: MutableList<T>)
    {
        if (!this.data.isEmpty())
        {
            this.data.clear()
        }
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<T>)
    {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    //region Holder
    class BaseRecycleAdapterHolder(parent: ViewGroup, @LayoutRes resId: Int) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(resId, null))
    {
        private val mViews: SparseArray<View>

        init
        {
            this.mViews = SparseArray()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : View> getView(@IdRes viewId: Int): T
        {
            var view: View? = mViews.get(viewId)
            if (view == null)
            {
                view = itemView.findViewById(viewId)
                mViews.put(viewId, view)
            }
            return view as T
        }

        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return
         */
        fun setText(@IdRes viewId: Int, text: CharSequence): BaseRecycleAdapterHolder
        {
            getView<TextView>(viewId).setText(text)
            return this;
        }


        /**
         * 设置图片
         *
         * @param viewId
         * @param imgUrl
         * @return
         */
        fun setImage(@IdRes viewId: Int, imgUrl: String, @DrawableRes loading: Int? = null, @DrawableRes loadError: Int? = null): BaseRecycleAdapterHolder
        {
            if (TextUtils.isEmpty(imgUrl))
            {
                return this;
            }
            val imageView = getView<ImageView>(viewId);
            ImageHelper.loadImage(imgUrl,imageView,placeholder =loading,placeholderError =loadError)
            return this;
        }

        /**
         * 设置Visibility
         *
         * @param viewId
         * @param visibility
         * @return
         */
        fun setVisibility(@IdRes viewId: Int, visibility: Int): BaseRecycleAdapterHolder
        {
            getView<View>(viewId).visibility = visibility
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @return
         */
        fun setOnClickListener(@IdRes viewId: Int, onItemClickListener: OnItemClickListener): BaseRecycleAdapterHolder
        {
            getView<View>(viewId).setOnClickListener { view ->
                onItemClickListener.onViewClick(view, layoutPosition)
            };
            return this;
        }

        fun setOnClickListener(onItemClickListener: OnItemClickListener): BaseRecycleAdapterHolder
        {
            itemView.setOnClickListener { view ->
                onItemClickListener.onViewClick(view, layoutPosition)
            };
            return this;
        }
    }
    //endregion
}
