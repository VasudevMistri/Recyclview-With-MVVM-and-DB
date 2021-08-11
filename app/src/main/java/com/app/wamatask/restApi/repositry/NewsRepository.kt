package com.app.mytask.repositry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.mediq.networks.ApiClient
import com.app.wamatask.networks.ApiInterface
import com.app.wamatask.restApi.model.NewsResponseModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {
    var call: Call<NewsResponseModel>? = null

    fun callApi(): MutableLiveData<NewsResponseModel> {
        cancelApi()
        val mutableLiveData: MutableLiveData<NewsResponseModel> = MutableLiveData()
        val apiInterface = ApiClient.get().client!!.create(ApiInterface::class.java)
        val call = apiInterface.getNewsList("us","business","444ad0895ee94130972d070cead5fcb3")

        call?.enqueue(object : Callback<NewsResponseModel> {
            override fun onResponse(
                call: Call<NewsResponseModel>,
                response: Response<NewsResponseModel>
            ) {
                Log.e("onRes API ",Gson().toJson(response.body()))
                mutableLiveData.value = response.body()
            }

            override fun onFailure(call: Call<NewsResponseModel>, t: Throwable) {
                mutableLiveData.value = null
            }

        })

        return mutableLiveData
    }

    fun cancelApi() {
        if (call != null && call!!.isExecuted) {
            call!!.cancel()
        }
    }


}