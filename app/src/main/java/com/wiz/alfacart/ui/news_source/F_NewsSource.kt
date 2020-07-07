package com.wiz.alfacart.ui.news_source

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.view.doOnAttach
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wiz.alfacart.R
import com.wiz.alfacart.databinding.DfInfoBinding
import com.wiz.alfacart.databinding.FNewsSourceBinding
import com.wiz.alfacart.dialog.DF_Info
import com.wiz.alfacart.domain.NewsSourceModel
import com.wiz.alfacart.ui.article.F_Article
import com.wiz.alfacart.ui.ext.navAddTo
import com.wiz.alfacart.ui.ext.popBackStack
import com.wiz.alfacart.ui.ext.setFabScrollListener
import com.wiz.alfacart.util.KeyboardUtil
import com.wiz.alfacart.viewmodel.VM_NewsSource
import kotlinx.coroutines.launch
import wazma.punjabi.base.BaseFragment

class F_NewsSource(val category:String) : BaseFragment() {
    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FNewsSourceBinding
    lateinit var epoxyController: EC_NewsSource
    lateinit var vm: VM_NewsSource

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FNewsSourceBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_news_source, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this, VM_NewsSource.VMF_NewsSource(context!!)).get(VM_NewsSource::class.java)

        epoxyController = EC_NewsSource(
            context!!,
            null,object : EC_NewsSource.MCallback {
                override fun onLayoutClick(position: Int, model: NewsSourceModel, holder: EM_NewsSource.Holder) {
                    navAddTo(F_Article(model.id, category))
                }
            })

        epoxyController.isDebugLoggingEnabled = true
        bind.recyclerView.setController(epoxyController)

        bind.tvCategory.setText("${category.capitalize()} Category")

        vm.getNewsSource(category)

        onClick()
        onListener()
        onObserve()
    }

    private fun onObserve() {
        vm.dataSourceData.observe(this, Observer {
            epoxyController.networkState = it.networkState
            epoxyController.setData(it.newsSourceModel.sources)

            if (it.newsSourceModel.sources.isNotEmpty()) {
                bind.tvCategory.setText("${category.capitalize()} Category (${it.newsSourceModel.sources.size} result)")
            }

            setFabScrollListener(bind.recyclerView, bind.fab)

            it.networkState.msg?.let {
                DF_Info(it, object : DF_Info.MCallback {
                    override fun onRetry() {
                        vm.getNewsSource(category)
                    }
                }).show(childFragmentManager, null)
            }
        })
    }

    private fun onClick() {
        bind.btnClearText.setOnClickListener {
            bind.etSearch.setText("")
            bind.tvInfo.isVisible = false
        }

        bind.etSearch.setOnClickListener {
            KeyboardUtil.showKeyboard(it.context)
        }

        bind.etSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.getAction() === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    KeyboardUtil.hideKeyboard(v)
                    return true
                }
                return false
            }
        })

        bind.fab.setOnClickListener {
            bind.recyclerView.smoothScrollToPosition(0)
        }

        bind.btnBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun onListener() {
        RxTextView.textChanges(bind.etSearch)
            .map({ t: CharSequence ->
                return@map t.toString()
            })
            .subscribe {
                val res = vm.searchNewsSource(it)
                epoxyController.networkState = res.networkState

                coroutineScope.launch {
                    if (it.length >= 1) {
                        if (res.newsSourceModel.sources.isEmpty()) {
                            bind.tvInfo.visibility = View.VISIBLE
                            bind.recyclerView.visibility = View.INVISIBLE
                        } else {
                            bind.tvInfo.visibility = View.GONE
                            bind.btnClearText.visibility = View.VISIBLE
                            bind.recyclerView.visibility = View.VISIBLE

                            epoxyController.setData(res.newsSourceModel.sources)
                        }
                    } else {
                        // reset
                        bind.tvInfo.visibility = View.GONE
                        bind.btnClearText.visibility = View.INVISIBLE
                        bind.recyclerView.visibility = View.VISIBLE

                        vm.dataSourceData.value?.let {
                            epoxyController.setData(it.newsSourceModel.sources)
                        }
                    }
                }
            }.autoDispose()
    }

}