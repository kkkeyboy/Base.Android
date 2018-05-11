package com.jone.base.cache.image

/**
 * Created by Jone.L on 2017/12/1.
 */
class ImageHelperConfig(private val build: Builder)
{
    internal val type: Int  //图片加载类型，目前只有默认类型，以后可以扩展

    internal val placeHolder: Int //当没有成功加载的时候显示的图片

    internal var placeHolderError: Int //加载错误的图片

    internal val loadStrategy: Int  //加载策略，目前只有默认加载策略，以后可以扩展

    init
    {
        this.type = build.type
        this.loadStrategy = build.loadStrategy
        this.placeHolder = build.placeHolder
        this.placeHolderError = build.placeHolderError

        ImageHelper.customImageHelperConfig = this
    }

    class Builder
    {
        //图片默认加载类型 以后有可能有多种类型
        private val PIC_DEFAULT_TYPE = 0

        //图片默认加载策略 以后有可能有多种图片加载策略
        private val LOAD_STRATEGY_DEFAULT = 0

        internal var type: Int = PIC_DEFAULT_TYPE
        internal var loadStrategy: Int =LOAD_STRATEGY_DEFAULT

        internal var placeHolder: Int = 0
        internal var placeHolderError: Int = 0

        fun setType(type: Int): Builder
        {
            this.type = type
            return this
        }


        fun setPlaceHolder(placeHolder: Int): Builder
        {
            this.placeHolder = placeHolder
            return this
        }


        fun setStrategy(strategy: Int): Builder
        {
            this.loadStrategy = strategy
            return this
        }

        fun seImageHelperImpl(imageHelperImpl: ImageHelperProtocol): Builder
        {
            ImageHelperStrategy.seImageHelperImpl(imageHelperImpl)
            return this
        }

        fun build(): ImageHelperConfig
        {
            return ImageHelperConfig(this)
        }


    }
}