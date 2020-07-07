package com.wiz.alfacart.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.domain.ArticleListModel
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.domain.NewsSourceListModel
import com.wiz.alfacart.domain.NewsSourceModel
import com.wiz.alfacart.repository.R_NewsSource
import com.wiz.alfacart.util.InternetConnectionUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

data class DataNewsSource(var newsSourceModel: NewsSourceListModel, var networkState: NetworkState)

class VM_NewsSource(private val context: Context) : ViewModel() {

    private var repo: R_NewsSource

    var dataSourceData = MutableLiveData<DataNewsSource>()

    init {
        repo = R_NewsSource(context)
    }

    fun getNewsSource(category: String) {
        if (!InternetConnectionUtil.isConnected(context)) {
            dataSourceData.value = DataNewsSource(NewsSourceListModel(), NetworkState.error(CONST.NO_INTERNET_CONN))
            return
        }

        viewModelScope.launch {
            try {
                dataSourceData.value = DataNewsSource(NewsSourceListModel(), NetworkState.LOADING)
                val res = repo.getNewsSource(category)
                dataSourceData.value = DataNewsSource(res, NetworkState.LOADED)
            } catch (t: Throwable) {
                Timber.e(t, "ERROR")
                dataSourceData.value = DataNewsSource(NewsSourceListModel(), NetworkState.error(t.message))
            }
        }

    }

    fun searchNewsSource(keyword: String): DataNewsSource {
        if (!InternetConnectionUtil.isConnected(context)) {
            return DataNewsSource(NewsSourceListModel(), NetworkState.error(CONST.NO_INTERNET_CONN))
        }

        val filter = dataSourceData.value?.newsSourceModel?.sources?.filter {
            it.name.toLowerCase(Locale.getDefault()).contains(keyword.toLowerCase(Locale.getDefault()))
        }?.toMutableList()

        if (filter == null) {
            return DataNewsSource(NewsSourceListModel(), NetworkState.FAILED)
        } else if (filter.isEmpty()) {
            return DataNewsSource(NewsSourceListModel(), NetworkState.FAILED)
        } else {
            val list = NewsSourceListModel()
            list.sources = filter
            return DataNewsSource(list, NetworkState.LOADED)
        }
    }

    class VMF_NewsSource(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = VM_NewsSource(context) as T
    }
}