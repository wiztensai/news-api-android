package com.wiz.alfacart.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wiz.alfacart.R
import com.wiz.alfacart.databinding.FMainBinding
import com.wiz.alfacart.domain.CategoryModel
import com.wiz.alfacart.ui.ext.navAddTo
import com.wiz.alfacart.ui.news_source.F_NewsSource
import wazma.punjabi.base.BaseFragment

class F_Main:BaseFragment() {
    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FMainBinding
    lateinit var epoxyController: EC_Main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FMainBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_main, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        epoxyController = EC_Main(
            context!!,
            null, object : EC_Main.MCallback {
                override fun onLayoutClick(position: Int, model: CategoryModel, holder: EM_Main.Holder) {
                    navAddTo(F_NewsSource(model.title))
                }
            })

        epoxyController.isDebugLoggingEnabled = true
        bind.recyclerView.setController(epoxyController)

        val categories = mutableListOf<CategoryModel>()
        categories.add(CategoryModel("general", R.drawable.bg_general))
        categories.add(CategoryModel("health", R.drawable.bg_health))
        categories.add(CategoryModel("science", R.drawable.bg_science))
        categories.add(CategoryModel("business", R.drawable.bg_bussiness))
        categories.add(CategoryModel("entertainment", R.drawable.bg_entertainment))
        categories.add(CategoryModel("sports", R.drawable.bg_sports))
        categories.add(CategoryModel("technology", R.drawable.bg_tech))
        epoxyController.setData(categories)
    }
}