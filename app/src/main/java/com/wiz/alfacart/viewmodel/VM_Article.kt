package com.wiz.alfacart.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.domain.ArticleListModel
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.repository.R_Article
import com.wiz.alfacart.util.InternetConnectionUtil
import kotlinx.coroutines.launch
import timber.log.Timber

data class DataArticle(var articleListModel : ArticleListModel, var networkState: NetworkState)

class VM_Article(private val context: Context): ViewModel() {

    private var repo:R_Article

    var dataArticle = MutableLiveData<DataArticle>()

    init {
        repo = R_Article(context)
    }

    fun getArticle(sourceId:String, page:Int = 1) {
        if (!InternetConnectionUtil.isConnected(context)) {
            dataArticle.value = DataArticle(ArticleListModel(), NetworkState.error(CONST.NO_INTERNET_CONN))
            return
        }

        viewModelScope.launch {
            // get latest value livedata
            val temp = dataArticle.value?.articleListModel?:let {
                ArticleListModel()
            }

            // trigger view loading di recyclerview
            dataArticle.value = DataArticle(temp, NetworkState.LOADING)

            try {
                val res = repo.getArticle(sourceId, page)

                // res data manipulation and keep others res value
                val resResult = res.articles

                // reset res result for latest value livedata
                res.articles = temp.articles

                // append res result to current value livedata
                res.articles.addAll(resResult)

                dataArticle.value = DataArticle(res, NetworkState.LOADED)
            } catch (t: Throwable) {
                Timber.e(t, "ERROR")
                dataArticle.value = DataArticle(temp, NetworkState.error(t.message))
            }

        }
    }

    class VMF_Article(private val context: Context): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = VM_Article(context) as T
    }
}