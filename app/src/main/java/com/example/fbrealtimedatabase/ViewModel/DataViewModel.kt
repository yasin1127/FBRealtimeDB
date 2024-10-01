package com.example.fbrealtimedatabase.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fbrealtimedatabase.Model.Data
import com.example.fbrealtimedatabase.Repository.DataRepository


class DataViewModel: ViewModel()  {
    private val repository = DataRepository()
    private val _data : MutableLiveData<List<Data>> = repository.fetchData()
    val data: LiveData<List<Data>> = _data

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    fun addData(data: Data,onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        repository.addData(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                _error.value = it.message
                onFailure(it)
            }
    }

    fun deleteData(data: Data, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        repository.deleteData(data)
        onSuccess()
    }
    fun handleError(errorMesssage: String?) {
        _error.value = errorMesssage
    }

    fun updateData(data: Data) {
        repository.updateData(data)

    }

}