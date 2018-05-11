package com.jone.base.binding.adapter.binder

/**
 * Created by Jone.L on 2018/4/12.
 */
interface ISectionedItemTemplate<in TPNode, TCNode> {
    val itemTemplateSectionHeader: IItemTemplate<TPNode>

    val itemTemplateSectionFooter: IItemTemplate<TPNode>?

    val itemTemplate: IItemTemplate<TCNode>

    fun getItemSourceForSection(pSection: TPNode): List<TCNode>?
    fun getItemCountForSection(pSection: TPNode): Int
}