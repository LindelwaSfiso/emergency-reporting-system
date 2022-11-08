package org.xhanka.ndm_project.ui.report_emergency.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.xhanka.ndm_project.utils.getErrorCode
import java.util.*

class ReportEmergencyViewModel : ViewModel(), RecognitionListener {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    private var _results: MutableLiveData<String> = MutableLiveData()
    val results: LiveData<String> = _results

    private var _errors: MutableLiveData<String> = MutableLiveData(null)
    val errors: LiveData<String> = _errors

    fun initializeRecognizer(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.setRecognitionListener(this)
    }

    fun startListening() {
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }

    override fun onReadyForSpeech(p0: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(p0: Float) {}

    override fun onBufferReceived(p0: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(p0: Int) {
        _errors.postValue(p0.getErrorCode())
    }

    fun  clearErrors() {
        _errors.postValue("")
    }

    override fun onResults(bundle: Bundle?) {
        val results = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        _results.postValue(results?.get(0))
    }

    override fun onPartialResults(p0: Bundle?) {}

    override fun onEvent(p0: Int, p1: Bundle?) {}


    /*fun setUpModel(model: Model?){
        this.model = model
    }

    fun setState(state: STATE) {
        this._state.postValue(state)
    }

    fun startMicrophoneRecognition() {
        try {
            val rec = Recognizer(model, 16000.0f)
            speechService = SpeechService(rec, 16000.0f)
            speechService!!.startListening(this)
            pause(true)
        } catch (exception: IOException) {
        }
    }


    fun pause(checked: Boolean) {
        if (speechService != null) {
            speechService!!.setPause(checked)
        }
    }

    enum class STATE(state: String) {
        STATE_START("Preparing voice recognizer!"),
        STATE_READY("Ready. Start speaking."),
        STATE_DONE(""),
        STATE_MIC("State your emergency."),
        STATE_UNPACKING("Failed to unpack the model!")
    }

    override fun onPartialResult(hypothesis: String?) {
        Log.d("TAG", "ON PARTIAL RESULT" + hypothesis.toString())
    }

    override fun onResult(hypothesis: String?) {
        hypothesis?.let { translateText += it }
        _results.postValue(translateText)
    }

    override fun onFinalResult(hypothesis: String?) {
        Log.d("TAG", "ON FINAL RESULTS" + hypothesis.toString())
    }

    override fun onError(exception: Exception?) {
        Log.d("TAG", "ON ERROR" + exception?.message.toString())
    }

    override fun onTimeout() {
        Log.d("TAG", "TIMED OUT")
    }

    fun clearTranslatedText() {
        translateText = ""
        _results.postValue(translateText)
    }

    fun onDestroy() {
        if (speechService != null) {
            speechService!!.stop()
            speechService!!.shutdown()
        }
    }*/

    /*private val startCountDown =
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
    }*/
}