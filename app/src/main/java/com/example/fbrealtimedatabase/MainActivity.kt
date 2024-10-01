package com.example.fbrealtimedatabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fbrealtimedatabase.Adapter.DataAdapter
import com.example.fbrealtimedatabase.Model.Data
import com.example.fbrealtimedatabase.ViewModel.DataViewModel
import com.example.fbrealtimedatabase.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.initialize


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DataViewModel by viewModels()
    private lateinit var adapter: DataAdapter


    private fun clearFields() {
        binding.idEtxt.text?.clear()
        binding.nameEtxt.text?.clear()
        binding.emailEtxt.text?.clear()
        binding.subjectEtxt.text?.clear()
        binding.birthdateEtxt.text?.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Firebase.initialize(this)

        adapter= DataAdapter(listOf(),this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =LinearLayoutManager(this)
        viewModel.data.observe(this, Observer {
            if (it!=null){
                adapter.updateData(it)
            }else{
                Toast.makeText(this@MainActivity, "Error in fetching data", Toast.LENGTH_SHORT).show()
            }

        })
        viewModel.error.observe(this, Observer {errorMessage->
            errorMessage?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }


        })
        binding.addBtn.setOnClickListener {
            val stuId = binding.idEtxt.text.toString()
            val name = binding.nameEtxt.text.toString()
            val email = binding.emailEtxt.text.toString()
            val subject = binding.subjectEtxt.text.toString()
            val birthdate = binding.birthdateEtxt.text.toString()

            if (stuId.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() &&
                subject.isNotEmpty() && birthdate.isNotEmpty()){

                val data = Data(stuId,name,email,subject,birthdate)
                viewModel.addData(data, onSuccess = {
                    Toast.makeText(this@MainActivity, "Data added successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                }, onFailure = {
                    Toast.makeText(this@MainActivity, "Failed to add data", Toast.LENGTH_SHORT).show()
                })
            }
        }


    }

    fun onEditItemClick(data: Data) {
        binding.idEtxt.setText(data.stuId)
        binding.nameEtxt.setText(data.name)
        binding.emailEtxt.setText(data.email)
        binding.subjectEtxt.setText(data.subject)
        binding.birthdateEtxt.setText(data.birthdate)

        binding.addBtn.setOnClickListener {
            val updateData = Data(data.id, binding.idEtxt.text.toString(), binding.nameEtxt.text.toString(),
                binding.emailEtxt.text.toString(), binding.subjectEtxt.text.toString(), binding.birthdateEtxt.text.toString())
            viewModel.updateData(updateData)
            clearFields()
            Toast.makeText(this@MainActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun onDeleteItemClick(data: Data) {
        AlertDialog.Builder(this).apply {
            setTitle("Delete Confirmation")
            setMessage("Are you sure you want to delete this item?")
            setPositiveButton("Yes") { _, _ ->
                viewModel.deleteData(data, onSuccess = {
                    Toast.makeText(this@MainActivity, "Data deleted successfully", Toast.LENGTH_SHORT).show()},
                    onFailure = {
                        Toast.makeText(this@MainActivity, "Failed to delete data", Toast.LENGTH_SHORT).show()
                    })
            }
            setNegativeButton("No", null)
        }

    }

}