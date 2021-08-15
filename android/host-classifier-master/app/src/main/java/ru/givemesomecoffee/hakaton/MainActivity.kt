package ru.givemesomecoffee.hakaton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private var confirmCheckButton: Button? = null
    private var hostFieldView: TextInputLayout? = null
    private lateinit var hostFieldInput: TextInputEditText
    private var result: TextView? = null
    private var loadingView: ProgressBar? = null

    private fun init() {
        confirmCheckButton = findViewById(R.id.host_check_confirm_button)
        hostFieldView = findViewById(R.id.host_field)
        hostFieldInput = findViewById(R.id.host_input_field)
        result = findViewById(R.id.host_check_result)
        loadingView = findViewById(R.id.progressBar)
        mainActivityViewModel.data.observe(this, Observer(::showResult))
        mainActivityViewModel.error.observe(this, Observer(::showConnectionError))
        mainActivityViewModel.validationState.observe(this, Observer(::onValidationChanged))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        confirmCheckButton?.setOnClickListener {
            result?.text = null
            mainActivityViewModel.validateFinalHostName(hostFieldView?.editText?.text.toString())
        }

        hostFieldInput.doOnTextChanged { text, _, _, _ ->
            mainActivityViewModel.checkCurrentHostName(
                text.toString()
            )
        }
    }

    private fun onValidationChanged(validationState: ValidationState?) {
        when (validationState?.status) {
            ValidationState.Status.FAILED -> showValidationError(validationState.msg)
            ValidationState.Status.READY -> hostFieldView?.error = null
            ValidationState.Status.VALIDATED -> checkHostType(validationState.host)
        }
    }

    private fun checkHostType(host: String?) {
        loadingView?.visibility = View.VISIBLE
        mainActivityViewModel.checkHostType(host)
    }

    private fun showValidationError(error: String?) {
        hostFieldView?.error = error
    }

    private fun showResult(resultMessage: String?) {
        loadingView?.visibility = View.INVISIBLE
        result?.text = resultMessage
    }

    private fun showConnectionError(error: String) {
        loadingView?.visibility = View.INVISIBLE
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}