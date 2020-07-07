package com.wiz.alfacart.ui.article_search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wiz.alfacart.R
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.databinding.FArticleSearchBinding
import com.wiz.alfacart.dialog.DF_Info
import com.wiz.alfacart.domain.ArticleModel
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.ui.article.EM_Article
import com.wiz.alfacart.ui.ext.navAddTo
import com.wiz.alfacart.ui.ext.popBackStack
import com.wiz.alfacart.ui.ext.setFabScrollListener
import com.wiz.alfacart.ui.news_detail.F_News_Detail
import com.wiz.alfacart.util.EndlessRecyclerViewUtil
import com.wiz.alfacart.util.KeyboardUtil
import com.wiz.alfacart.viewmodel.VM_Article_Search
import kotlinx.coroutines.launch
import wazma.punjabi.base.BaseFragment
import java.util.concurrent.TimeUnit


class F_ArticleSearch(val sourceId:String, val category:String) : BaseFragment() {
    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FArticleSearchBinding
    lateinit var epoxyController: EC_ArticleSearch
    lateinit var vm: VM_Article_Search
    lateinit var scrollListener : EndlessRecyclerViewUtil
    var keyword = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FArticleSearchBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_article_search, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this, VM_Article_Search.VMF_Article_Search(context!!)).get(VM_Article_Search::class.java)

        epoxyController = EC_ArticleSearch(
            context!!, null, null, object : EC_ArticleSearch.MCallback {
                override fun onLayoutClick(position: Int, model: ArticleModel, holder: EM_Article.Holder) {
                    navAddTo(F_News_Detail(model))
                }
            })

        epoxyController.isDebugLoggingEnabled = true
        bind.recyclerView.setController(epoxyController)

        bind.tvCategory.setText("${category.capitalize()} Category")

        KeyboardUtil.showKeyboard(view.context)
        bind.etSearch.requestFocus()

        onClick()
        onObserve()
        onListener()
    }

    private fun onListener() {
        RxTextView.textChanges(bind.etSearch)
            .map({ t: CharSequence ->
                return@map t.toString()
            })
            .throttleWithTimeout(850, TimeUnit.MILLISECONDS)
            .subscribe {
                coroutineScope.launch {
                    // setiap ketik, harus direset data searchingnya
                    resetSearch()

                    if (it.length >= 2) {
                        bind.btnClearText.visibility = View.VISIBLE
                        vm.search(sourceId, it)
                        keyword = it
                        bind.recyclerView.visibility = View.VISIBLE
                    } else {
                        bind.btnClearText.visibility = View.INVISIBLE
                        bind.recyclerView.visibility = View.INVISIBLE
                        bind.fab.hide()
                    }
                }
            }.autoDispose()

        scrollListener = object : EndlessRecyclerViewUtil(bind.recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                coroutineScope.launch {
                    vm.search(sourceId, keyword, page)
                }
            }
        }
        bind.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun onObserve() {
        vm.dataArticle.observe(this, Observer {
            epoxyController.firstRequest = true

            epoxyController.networkState = it.networkState
            epoxyController.setData(it.articleListModel.articles)

            if (it.articleListModel.totalResults != 0) {
                bind.tvCategory.setText("${category.capitalize()} Category (${it.articleListModel.totalResults} result)")
            }
            setFabScrollListener(bind.recyclerView, bind.fab)

            it.networkState.msg?.let {
                DF_Info(it, object : DF_Info.MCallback {
                    override fun onRetry() {
                        coroutineScope.launch {
                            vm.search(sourceId, keyword)
                        }
                    }
                }).show(childFragmentManager, null)
            }
        })
    }

    private fun onClick() {
        bind.btnBack.setOnClickListener {
            popBackStack()
        }

        bind.btnClearText.setOnClickListener {
            bind.etSearch.setText("")
            resetSearch()
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
    }

    private fun resetSearch() {
        epoxyController.firstRequest = null
        scrollListener.resetState()
        vm.resetDataArticle()
    }
}