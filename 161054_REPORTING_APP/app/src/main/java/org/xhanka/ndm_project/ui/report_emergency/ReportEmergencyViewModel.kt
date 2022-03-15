package org.xhanka.ndm_project.ui.report_emergency

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportEmergencyViewModel : ViewModel() {
    private var _countDown: MutableLiveData<Int> = MutableLiveData(TOTAL_TIME_SEC)
    val countDown: LiveData<Int> get() = _countDown

    private fun updateCountdown(downTime: Int) {
        _countDown.postValue(downTime)
    }

    fun startCounter() {
        this.startCountDown.start()
    }

    fun stopCounter() {
        this.startCountDown.cancel()
    }

    private val startCountDown =
        object : CountDownTimer(TOTAL_TIME_MIL, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountdown((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {

            }
        }

    companion object {
        const val TOTAL_TIME_MIL: Long = 5_000
        const val INTERVAL: Long = 1_000

        const val TOTAL_TIME_SEC: Int = (TOTAL_TIME_MIL / INTERVAL).toInt()
    }
}