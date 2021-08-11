package com.app.wamatask.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.mytask.viewmodel.NewsViewModel


class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private var single = ViewModelFactory()

        fun getInstance(): ViewModelFactory {
            if (single == null) {
                single = ViewModelFactory()
            }
            return single
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {

            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                return NewsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}