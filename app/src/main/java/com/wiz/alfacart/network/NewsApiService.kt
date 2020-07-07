package com.wiz.alfacart.network

import com.wiz.alfacart.domain.ArticleListModel
import com.wiz.alfacart.domain.NewsSourceListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("sources")
    suspend fun getNewsSource(
        @Query("category") category : String,
        @Query("apiKey") apiKey : String
    ): NewsSourceListModel

    @GET("top-headlines")
    suspend fun getArticle(
        @Query("sources") sources : String,
        @Query("apiKey") apiKey : String,
        @Query("pageSize") pageSize : Int,
        @Query("page") page : Int
    ): ArticleListModel

    @GET("top-headlines")
    suspend fun searchNewsSource(
        @Query("category") category : String,
        @Query("apiKey") apiKey : String,
        @Query("q") keyword : String,
        @Query("pageSize") pageSize : Int,
        @Query("page") page : Int
    ): ArticleListModel

    @GET("everything")
    suspend fun searchArticle(
        @Query("sources") sources : String,
        @Query("apiKey") apiKey : String,
        @Query("q") keyword : String,
        @Query("pageSize") pageSize : Int,
        @Query("page") page : Int
    ): ArticleListModel
}
