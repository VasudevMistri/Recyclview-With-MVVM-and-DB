package com.app.wamatask

import android.app.Application
import android.content.Context
import com.app.wamatask.network.ConnectivityTools
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    var connectivityTools: ConnectivityTools? = null
    var checkInternetConnectivity: ConnectivityTools.CheckInternetConnectivity? = null

    companion object {
        private var instance: App? = null
        var appContext: Context? = null
        fun getinstance(): App? {
            return instance
        }

        fun appContext(): Context = instance!!.applicationContext

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        connectivityTools = ConnectivityTools(this, checkInternetConnectivity)
        connectivityTools!!.registerInternetCheckReceiver()
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .name("news.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)
    }


}