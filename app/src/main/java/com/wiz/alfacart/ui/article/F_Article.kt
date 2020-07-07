package com.wiz.alfacart.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiz.alfacart.R
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.databinding.FArticleBinding
import com.wiz.alfacart.dialog.DF_Info
import com.wiz.alfacart.domain.ArticleModel
import com.wiz.alfacart.ui.article_search.F_ArticleSearch
import com.wiz.alfacart.ui.ext.navAddTo
import com.wiz.alfacart.ui.ext.popBackStack
import com.wiz.alfacart.ui.ext.setFabScrollListener
import com.wiz.alfacart.ui.news_detail.F_News_Detail
import com.wiz.alfacart.util.EndlessRecyclerViewUtil
import com.wiz.alfacart.viewmodel.VM_Article
import kotlinx.coroutines.launch
import wazma.punjabi.base.BaseFragment

class F_Article(val sourceId:String, val category:String) : BaseFragment() {
    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FArticleBinding
    lateinit var epoxyController: EC_Article
    lateinit var vm: VM_Article
    lateinit var scrollListener : EndlessRecyclerViewUtil

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FArticleBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_article, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this, VM_Article.VMF_Article(context!!)).get(VM_Article::class.java)

        epoxyController = EC_Article(
            context!!, null, object : EC_Article.MCallback {
                override fun onLayoutClick(position: Int, model: ArticleModel, holder: EM_Article.Holder) {
                    navAddTo(F_News_Detail(model))
                }
            })

        epoxyController.isDebugLoggingEnabled = true
        bind.recyclerView.setController(epoxyController)
        vm.getArticle(sourceId)

        bind.tvCategory.setText("${category.capitalize()} Category")

        onClick()
        onListener()
        onObserve()
    }

    private fun onObserve() {
        vm.dataArticle.observe(this, Observer {
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
                            vm.getArticle(sourceId)
                        }
                    }
                }).show(childFragmentManager, null)
            }
        })
    }

    private fun onListener() {
        scrollListener = object : EndlessRecyclerViewUtil(bind.recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                vm.getArticle(sourceId, page)
            }
        }
        bind.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun onClick() {
        bind.btnSearch.setOnClickListener {
            navAddTo(F_ArticleSearch(sourceId, category))
        }

        bind.btnBack.setOnClickListener {
            popBackStack()
        }

        bind.fab.setOnClickListener {
            bind.recyclerView.smoothScrollToPosition(0)
        }
    }
}