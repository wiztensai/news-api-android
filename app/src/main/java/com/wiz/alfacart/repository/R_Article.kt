package com.wiz.alfacart.repository

import android.content.Context
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.domain.ArticleListModel
import com.wiz.alfacart.domain.NewsSourceListModel
import com.wiz.alfacart.network.NewsApiService
import com.wiz.alfacart.util.ApiUtil
import kotlinx.coroutines.CoroutineScope

class R_Article(private var context: Context) {

    suspend fun getArticle(sources:String, page:Int):ArticleListModel {
        val apiService = ApiUtil.retrofit.create(NewsApiService::class.java)
        return apiService.getArticle(sources, CONST.API_KEY, CONST.PAGE_SIZE, page)
    }

    suspend fun searchArticle(sources:String, keyword:String, page:Int):ArticleListModel {
        val apiService = ApiUtil.retrofit.create(NewsApiService::class.java)
        return apiService.searchArticle(sources, CONST.API_KEY, keyword, CONST.PAGE_SIZE, page)
    }
}