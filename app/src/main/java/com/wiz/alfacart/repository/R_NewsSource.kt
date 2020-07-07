package com.wiz.alfacart.repository

import android.content.Context
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.domain.ArticleListModel
import com.wiz.alfacart.domain.ArticleModel
import com.wiz.alfacart.domain.NewsSourceListModel
import com.wiz.alfacart.network.NewsApiService
import com.wiz.alfacart.util.ApiUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class R_NewsSource(private var context: Context) {

    suspend fun getNewsSource(category:String):NewsSourceListModel {
        val apiService = ApiUtil.retrofit.create(NewsApiService::class.java)
        return apiService.getNewsSource(category, CONST.API_KEY)
    }
}