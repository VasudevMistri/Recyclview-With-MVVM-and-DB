package com.app.wamatask.local

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.app.wamatask.local.model.RealmDBModel
import io.realm.Realm
import io.realm.RealmResults


class RealmController(application: Application?) {
    var realm: Realm

    //Refresh the realm istance
    fun refresh() {
        realm.refresh()
    }

    fun RealmController(application: Application?) {
        realm = Realm.getDefaultInstance()
    }

    fun clearAll() {
        realm.beginTransaction()
        realm.delete(RealmDBModel::class.java)
        realm.commitTransaction()
    }

    fun getAllNews(): RealmResults<RealmDBModel?>? {
        return realm.where(RealmDBModel::class.java).findAll()
    }


    companion object {
        var instance: RealmController? = null
            private set

        fun with(fragment: Fragment): RealmController? {
            if (instance == null) {
                instance = RealmController(
                    fragment.requireActivity().application
                )
            }
            return instance
        }

        fun with(activity: Activity): RealmController? {
            if (instance == null) {
                instance = RealmController(activity.application)
            }
            return instance
        }

        fun with(application: Application?): RealmController? {
            if (instance == null) {
                instance = RealmController(application)
            }
            return instance
        }
    }

    init {
        realm = Realm.getDefaultInstance()
    }
}