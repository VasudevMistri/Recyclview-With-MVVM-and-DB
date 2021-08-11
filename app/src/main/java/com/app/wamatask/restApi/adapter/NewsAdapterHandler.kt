package com.app.wamatask.restApi.adapter

import android.widget.Toast
import com.app.wamatask.local.model.RealmDBModel
import com.app.wamatask.restApi.RestApiActivity
import io.realm.Realm

class NewsAdapterHandler(val context: RestApiActivity) {

    fun onClickLikes(position: Int) {

        if (context.itemArrayList.get(position).status.equals("0")
        ) {
            context.itemArrayList.get(position).status = "1"
            Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
        } else {
            context.itemArrayList.get(position).status = "0"
            Toast.makeText(context, "Disliked", Toast.LENGTH_SHORT).show()
        }
        val mRealm: Realm = Realm.getDefaultInstance()
        mRealm.beginTransaction()
        val draftOrderProductId: RealmDBModel
        draftOrderProductId =
            mRealm.where(RealmDBModel::class.java)
                ?.equalTo("id", context.itemArrayList.get(position).id)?.findFirst()!!
        draftOrderProductId.newsstatus = context.itemArrayList.get(position).status
        mRealm.commitTransaction()
        mRealm.close()
    }
}