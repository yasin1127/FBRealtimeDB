package com.example.fbrealtimedatabase.Repository

import androidx.lifecycle.MutableLiveData
import com.example.fbrealtimedatabase.Model.Data
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("students")

    fun fetchData(): MutableLiveData<List<Data>> {
        val dataList = MutableLiveData<List<Data>>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = mutableListOf<Data>()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(Data::class.java)
                    data?.id = snapshot.key
                    list.add(data!!)
                }
                dataList.value = list

            }

            override fun onCancelled(databaseError: DatabaseError) {
                dataList.value = null
                handleErrors(databaseError)
            }

        })
        return dataList
    }

    private fun handleErrors(databaseError: DatabaseError) {
        databaseError.toException().printStackTrace()

    }

    fun addData(data: Data): Task<Void> {
        val key = databaseReference.push().key
        data.id = key
        return databaseReference.child(key!!).setValue(data)

    }

    fun updateData(data: Data) {
        databaseReference.child(data.id!!).setValue(data)

    }

    fun deleteData(data: Data) {
        databaseReference.child(data.id!!).removeValue()
    }
}