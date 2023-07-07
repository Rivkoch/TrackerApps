package com.rivkoch.common

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.rivkoch.common.databinding.ActivityHomeBinding
import java.util.*

open class HomeActivity : ComponentActivity() {
    open lateinit var binding: ActivityHomeBinding
    lateinit var dataHelper: DataHelper

    private val timer = Timer()
    private var time: Long = 0

    var stringProgress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeQuestion.visibility = View.GONE
        binding.pagesET.visibility = View.GONE
        binding.homeAnswer.visibility = View.GONE
        binding.saveButton.visibility = View.GONE

        dataHelper = DataHelper(applicationContext)


        binding.startButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener { resetAction() }
        binding.yesButton.setOnClickListener { showEditText() }
        binding.noButton.setOnClickListener { returnTimer() }
        binding.saveButton.setOnClickListener { checkProgressAdded() }
        binding.homeBTNStats.setOnClickListener { setBundleOpenStats() }

        if (dataHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
    }

    private fun checkProgressAdded() {
        if(binding.pagesET.text.toString().isEmpty()){
            Toast.makeText(this,"can't save empty.", Toast.LENGTH_SHORT).show()
        }
        else{
            saveProgress()
        }
    }

    private fun setBundleOpenStats() {
        // creating the instance of the bundle
        val bundle = Bundle()
        // storing the string value in the bundle
        // which is mapped to key
        bundle.putString("stringProgress", stringProgress)

        // creating a intent
        intent = Intent(this@HomeActivity, StatisticsActivity::class.java)

        // passing a bundle to the intent
        intent.putExtras(bundle)

        // starting the activity by passing the intent to it.
        startActivity(intent)

    }

    private fun showEditText() {
        binding.pagesET.visibility = View.VISIBLE
        binding.homeAnswer.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
    }

    private fun returnTimer() {
        binding.homeQuestion.visibility = View.GONE
        binding.homeTimer.visibility = View.VISIBLE
        binding.homeAnswer.visibility = View.GONE

    }

    private fun saveProgress() {

        // set the time to val
        //set the val to tv
        stringProgress +=
            "It takes you " + timeStringFromLong(time) + " " +
                    getString(R.string.to) + " " +
                    binding.pagesET.text.toString() + " " +
                    getString(R.string.reach) + ".\n"

        //todo -> save stringProgress to shared preferences? bundle?

        resetAction()

        binding.pagesET.setText("")

        binding.homeQuestion.visibility = View.GONE
        binding.pagesET.visibility = View.GONE
        binding.homeAnswer.visibility = View.GONE
        binding.saveButton.visibility = View.GONE

        binding.homeTimer.visibility = View.VISIBLE

    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (dataHelper.timerCounting()) {
                time = Date().time - dataHelper.startTime()!!.time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }
    }

    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }

    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }

    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.done)
    }

    private fun startStopAction() {
        if (dataHelper.timerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()

            //show yes no question
            binding.homeQuestion.visibility = View.VISIBLE
            binding.homeTimer.visibility = View.GONE


        } else {
            if (dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}