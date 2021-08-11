package com.app.mytask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.mytask.repositry.NewsRepository
import com.app.wamatask.restApi.model.NewsResponseModel

class NewsViewModel : ViewModel() {

    var mutableLiveData: MutableLiveData<NewsResponseModel>? = null
    var repository: NewsRepository? = null

    init {
        repository = NewsRepository()
    }

    fun getEmployeeData(): MutableLiveData<NewsResponseModel> {
        mutableLiveData = null
        if (mutableLiveData == null) {
            mutableLiveData = repository!!.callApi()
        }
        return mutableLiveData!!
    }
}