package ru.givemesomecoffee.hakaton

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

const val ERROR_EMPTY_FIELD = "Поле не может быть пустым"
const val ERROR_WHITESPACES = "В адресе не может быть пробелов"
const val ERROR_WRONG_HOST = "Такого хоста не существует"

class MainActivityViewModel : ViewModel() {
    val data: LiveData<String> get() = _data
    private val _data = MutableLiveData<String>()

    val error: LiveData<String> get() = _error
    private val _error = MutableLiveData<String>()

    val validationState: LiveData<ValidationState> get() = _validationState
    private val _validationState = MutableLiveData<ValidationState>()

    private suspend fun getHostType(host: String): String {
        return HostApi.retrofitService.getHostType(host)
    }

    private fun validateHostName(host: String): Boolean {
        var validated = false
     /*   val geo = host.split(".")
        val zone = geo.last()
        val pattern = Pattern.compile(IANA_TOP_LEVEL_DOMAINS)*/
        when {
            host.isEmpty() -> _validationState.postValue(ValidationState.error(ERROR_EMPTY_FIELD))
            host.contains(" ") ->
                _validationState.postValue(ValidationState.error(ERROR_WHITESPACES))
            !Patterns.WEB_URL.matcher(host).matches() ->
                _validationState.postValue(ValidationState.error(ERROR_WRONG_HOST))
           /* !pattern.matcher(zone).matches() ->
                _validationState.postValue(ValidationState.error(ERROR_WRONG_HOST))*/
            else -> validated = confirmValidation()
        }
        return validated
    }

    private fun confirmValidation(): Boolean {
        _validationState.postValue(ValidationState.READY)
        return true
    }


    fun checkCurrentHostName(host: String) {
        validateHostName(host)
    }

    fun validateFinalHostName(host: String) {
        if (validateHostName(host)) {
            _validationState.postValue(ValidationState.host(host))
        }
    }

    fun checkHostType(host: String?) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            _error.postValue(throwable.message)
        }
        viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                val response = host?.let { getHostType(it) }
                val result = response?.replace(", ", "\n")
                _data.postValue("$host - $result")
            }
        }
    }
}
