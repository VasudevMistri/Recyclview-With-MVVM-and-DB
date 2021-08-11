package com.app.wamatask.restApi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.app.mytask.viewmodel.NewsViewModel
import com.app.wamatask.R
import com.app.wamatask.databinding.ActivityRestapiBinding
import com.app.wamatask.local.RealmController
import com.app.wamatask.local.model.RealmDBModel
import com.app.wamatask.network.ViewModelFactory
import com.app.wamatask.restApi.adapter.NewsAdapter
import com.app.wamatask.restApi.model.Article
import com.app.wamatask.utils.CommonUtils
import io.realm.Realm
import io.realm.RealmResults


class RestApiActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityRestapiBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var itemArrayList = ArrayList<Article>()
    var mRealm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_restapi)
        mRealm = RealmController.with(this)?.realm

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance()
        ).get(NewsViewModel::class.java)

        if (RealmController.with(this)?.getAllNews()!!.size > 0) {
            readRecords(RealmController.with(this)?.getAllNews()!!)
        } else {
            getEmployeeData()
        }

    }

    private fun getEmployeeData() {
        itemArrayList.clear()
        mBinding.progressCircular.visibility = View.VISIBLE
        viewModel.getEmployeeData().observe(this) { baseModel ->
            if (baseModel != null) {
                if (baseModel.articles != null && baseModel.articles!!.size > 0) {
                    mBinding.progressCircular.visibility = View.GONE
                    setDatabase(baseModel.articles!!)
                    readRecords(RealmController.with(this)?.getAllNews()!!)
                } else {
                    mBinding.progressCircular.visibility = View.GONE
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            } else {
                mBinding.progressCircular.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun readRecords(allNews: RealmResults<RealmDBModel?>) {
        itemArrayList.clear()
        for (th in allNews.indices) {
            val article = Article()
            article.id = allNews.get(th)?.id
            article.title = allNews.get(th)?.title
            article.content = allNews.get(th)?.content
            article.publishedAt = CommonUtils.convertDate(allNews.get(th)?.publishedAt)
            article.urlToImage = allNews.get(th)?.urlToImage
            article.status = allNews.get(th)?.newsstatus
            itemArrayList.add(article)
        }
        if (itemArrayList.size > 0) {
            newsAdapter = NewsAdapter(this, itemArrayList)
            mBinding.rvLocal.setHasFixedSize(true)
            mBinding.rvLocal.adapter = newsAdapter
        }
    }


    fun RecyclerView.disableItemAnimator() {
        (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun setDatabase(articles: List<Article>) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    for (inth in articles.indices) {
                        val realmDBModel = RealmDBModel()
                        realmDBModel.id = CommonUtils.getRandomNumberString()
                        realmDBModel.content = articles.get(inth).content
                        realmDBModel.newsstatus = "0"
                        realmDBModel.publishedAt = articles.get(inth).publishedAt
                        realmDBModel.title = articles.get(inth).title
                        realmDBModel.url = articles.get(inth).url
                        realmDBModel.urlToImage = articles.get(inth).urlToImage
                        realm.copyToRealm(realmDBModel)
                    }
                }
            })
        } finally {
            if (realm != null) {
                realm.close()
            }
        }
    }
}