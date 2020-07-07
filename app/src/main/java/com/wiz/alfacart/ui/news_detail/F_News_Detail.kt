package com.wiz.alfacart.ui.news_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.wiz.alfacart.R
import com.wiz.alfacart.databinding.FNewsDetailBinding
import com.wiz.alfacart.domain.ArticleModel
import com.wiz.alfacart.domain.NewsSourceModel
import com.wiz.alfacart.ui.article.F_Article
import com.wiz.alfacart.ui.ext.navAddTo
import com.wiz.alfacart.ui.ext.popBackStack
import com.wiz.alfacart.ui.news_source.EC_NewsSource
import com.wiz.alfacart.ui.news_source.EM_NewsSource
import com.wiz.alfacart.viewmodel.VM_NewsSource
import wazma.punjabi.base.BaseFragment

class F_News_Detail(val model: ArticleModel):BaseFragment() {

    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FNewsDetailBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FNewsDetailBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_news_detail, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // make webview background transparent
        bind.webview.setBackgroundColor(0)

        bind.webview.loadUrl(model.url)

        bind.webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                bind.loadingView.isVisible = false
            }
        })

        bind.btnBack.setOnClickListener {
            popBackStack()
        }
    }
}