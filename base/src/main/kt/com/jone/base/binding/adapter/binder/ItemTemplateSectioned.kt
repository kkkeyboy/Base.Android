package com.jone.base.binding.adapter.binder

/**
 * Created by Jone.L on 2017/3/12.
 * 带头尾的分片的item template
 */

abstract class ItemTemplateSectioned<TPNode, TCNode>(override val itemTemplateSectionHeader: IItemTemplate<TPNode>, override val  itemTemplateSectionFooter: IItemTemplate<TPNode>? = null, override val  itemTemplate: IItemTemplate<TCNode>) : ISectionedItemTemplate<TPNode, TCNode> {

    override fun getItemCountForSection(pSection: TPNode): Int {
       return getItemSourceForSection(pSection)?.size?:0
    }
}