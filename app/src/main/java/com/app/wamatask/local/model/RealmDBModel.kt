package com.app.wamatask.local.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmDBModel : RealmObject() {
    @PrimaryKey
    var id: String? = null
    var title: String? = null
    var urlToImage: String? = null
    var url: String? = null
    var publishedAt: String? = null
    var content: String? = null
    var newsstatus: String? = null
}