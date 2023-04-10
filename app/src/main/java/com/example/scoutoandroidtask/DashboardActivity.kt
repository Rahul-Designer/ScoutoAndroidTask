package com.example.scoutoandroidtask

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scoutoandroidtask.adapter.DashboardItemListAdapter
import com.example.scoutoandroidtask.api.JsonService
import com.example.scoutoandroidtask.api.RetrofitHelper
import com.example.scoutoandroidtask.data.Car
import com.example.scoutoandroidtask.data.CarDatabase
import com.example.scoutoandroidtask.databinding.ActivityDashboardBinding
import com.example.scoutoandroidtask.model.DashboardListview
import com.example.scoutoandroidtask.model.MakerList
import com.example.scoutoandroidtask.repository.JsonRepository
import com.example.scoutoandroidtask.viewModel.DashboardViewModel
import com.example.scoutoandroidtask.viewModel.DashboardViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity(), DashboardItemListAdapter.OnItemClickListener {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var database: CarDatabase
    var arrMakerList = ArrayList<MakerList>()
    var arrSpinnerOne = ArrayList<String>()
    var arrMakerId = ArrayList<Int>()
    var arrCar = ArrayList<DashboardListview>()
    lateinit var adapter: DashboardItemListAdapter
    var IMAGE_REQUEST_CODE = 100
    var imagePos: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putBoolean("flag", false)
            editor?.apply()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        database = CarDatabase.getDatabase(this)
        arrMakerList.add(MakerList(0, "Select Make"))
        arrSpinnerOne.add("Select Make")
        arrMakerId.add(0)


        val makerAPI = RetrofitHelper.getInstance().create(JsonService::class.java)
        val repository = JsonRepository(makerAPI)

        dashboardViewModel = ViewModelProvider(
            this,
            DashboardViewModelFactory(repository)
        ).get(DashboardViewModel::class.java)

        val progressDialog = ProgressDialog(this@DashboardActivity)
        progressDialog.setTitle("Please Wait..")
        progressDialog.setMessage("Application is loading, please wait")
        progressDialog.show()
        dashboardViewModel.makers.observe(this, Observer {
            progressDialog.dismiss()
            updateArray(it.Results)
        })

    }

    private fun updateArray(results: List<MakerList>) {
        arrMakerList.addAll(results)
        results.forEach {
            arrSpinnerOne.addAll(listOf(it.Make_Name))
            arrMakerId.addAll(listOf(it.Make_ID))

        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrSpinnerOne)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.makerSpinner.adapter = adapter

        binding.makerSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getModelDetails(position, arrMakerId, arrSpinnerOne[position])

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun getModelDetails(pos: Int, arrMakerId: ArrayList<Int>, s: String) {
        val modelApi = RetrofitHelper.getInstance().create(JsonService::class.java)
        val progressDialog = ProgressDialog(this@DashboardActivity)
        progressDialog.setTitle("Please Wait..")
        progressDialog.setMessage("Application is loading, please wait")
        progressDialog.show()
        GlobalScope.launch(Dispatchers.Main) {
            val arrSpinnerTwo = ArrayList<String>()
            arrSpinnerTwo.add("Select Model")
            if (pos != 0) {
                val result = modelApi.getModelData(arrMakerId[pos].toString())
                if (result.body() != null) {
//                Log.d("Rahul", result.body().toString())
                    result.body()!!.Results.forEach {
                        arrSpinnerTwo.add(it.Model_Name.toString())

                    }
                }
            }

            val adapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                arrSpinnerTwo
            )
            progressDialog.dismiss()
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            binding.modelSpinner.adapter = adapter


            binding.modelSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    getdetails(s, arrSpinnerTwo[position])

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        }

    }

    private fun getdetails(s: String, s1: String) {
        var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_car)
        binding.addCar.setOnClickListener {
            arrCar.add(DashboardListview(bitmap, s, s1))
            GlobalScope.launch {
                database.carDao().insertCar(Car(null, s, s1))
            }
            binding.carRecyclerview.layoutManager = LinearLayoutManager(this)
            adapter = DashboardItemListAdapter(this, arrCar, this)
            binding.carRecyclerview.adapter = adapter

        }

    }

    override fun deleteItem(position: Int) {
        arrCar.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun addImage(position: Int) {
        this.imagePos = position
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri: Uri? = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            arrCar[imagePos].Car_Image = bitmap
            adapter.notifyItemChanged(imagePos)
        }
    }

}