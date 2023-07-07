package com.rivkoch.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rivkoch.common.databinding.ActivityStatisticsBinding

open class StatisticsActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticsBinding


    var stringProgress = ""
    var allTheProgress = "The progress:\n"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        allTheProgress += stringProgress
        binding.statTV.text = allTheProgress
    }

    private fun getBundle() {
        // getting the bundle back from the android
        val bundle = intent.extras

        // getting the string back
        stringProgress = bundle!!.getString("stringProgress", "stringProgress")

    }
}