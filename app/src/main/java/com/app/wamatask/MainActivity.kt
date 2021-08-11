package com.app.wamatask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.wamatask.databinding.ActivityMainBinding
import com.app.wamatask.restApi.RestApiActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.btnDemo.setOnClickListener {
            val intent = Intent(this, RestApiActivity::class.java)
            startActivity(intent)
        }

    }
}