package com.jone.base.adapter;

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jone.base.cache.image.ImageHelper


/**
 * Created by Jone.Luo on 2017/4/6.
 * 需要BaseAdapter适配器的列表控件可使用
 */

abstract class BaseCommonAdapter<T>(protected val data: MutableList<T>) : BaseAdapter() {
    var onItemClickListener: OnItemClickListener? = null

    protected var isCustomHandleClick: Boolean = false

    /**
     * 从继承者获取资源id
     */
    @LayoutRes protected abstract fun layoutResId(): Int

    /**
     * 绑定View
     */
    protected abstract fun onBindView(holder: ViewHolder, position: Int, itemData: T)

    final override fun getCount() = data.size

    final override fun getItem(position: Int): T = data[position]

    override fun getItemId(position: Int) = position.toLong()

    final override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = ViewHolder.getViewHolder(layoutResId(), position, convertView, parent);
        onBindView(holder, position, data[position]);
        if (!(isCustomHandleClick || onItemClickListener == null)) {
            holder.setOnClickListener(onItemClickListener!!)
        }
        return holder.convertView;
    }


    fun refreshData(data: List<T>) {
        if (!this.data.isEmpty()) {
            this.data.clear();

        }

        this.data.addAll(data);
        notifyDataSetChanged();
    }

    fun addData(data: List<T>) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    fun addData(data: T) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    class ViewHolder(val parent: ViewGroup, val layoutId: Int, private var mPosition: Int) {

        private val mViews: SparseArray<View>
        val convertView: View
        /**获取当前position*/
        val position = mPosition

        init {
            convertView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            convertView.tag = ViewHolder@ this

            mViews = SparseArray<View>()
        }


        internal companion object {
            fun getViewHolder(layoutId: Int, position: Int, convertViewTmp: View?, parent: ViewGroup): ViewHolder {
                if (convertViewTmp == null) {
                    return ViewHolder(parent, layoutId, position)
                }
                val holder = convertViewTmp.tag as ViewHolder
                holder.mPosition = position
                return holder
            }
        }


        /**
         * 获取View
         *
         * @param viewId
         * @return view
         */
        @SuppressWarnings("unchecked")
        @Suppress("UNCHECKED_CAST")
        fun <T : View> getView(@IdRes viewId: Int): T {
            var view = mViews.get(viewId);
            if (view == null) {
                view = convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (view as T?)!!
        }

        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return
         */
        fun setText(@IdRes viewId: Int, text: CharSequence): ViewHolder {
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
        fun setImage(@IdRes viewId: Int, imgUrl: String, @DrawableRes loading: Int? = null, @DrawableRes loadError: Int? = null): ViewHolder {
            if (TextUtils.isEmpty(imgUrl)) {
                return this;
            }
            val imageView = getView<ImageView>(viewId);

            ImageHelper.loadImage(imgUrl, imageView, placeholder = loading, placeholderError = loadError)
            return this;
        }

        /**
         * 设置Visibility
         *
         * @param viewId
         * @param visibility
         * @return
         */
        fun setVisibility(@IdRes viewId: Int, visibility: Int): ViewHolder {
            getView<View>(viewId).visibility = visibility
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @return
         */
        fun setOnClickListener(@IdRes viewId: Int, onItemClickListener: OnItemClickListener): ViewHolder {
            getView<View>(viewId).setOnClickListener { view ->
                onItemClickListener.onViewClick(view, position)
            };
            return this;
        }

        fun setOnClickListener(onItemClickListener: OnItemClickListener): ViewHolder {
            convertView.setOnClickListener { view ->
                onItemClickListener.onViewClick(view, position)
            };
            return this;
        }

    }

}
