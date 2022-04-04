package com.assignment.physicswallah.ui

import TeacherAdapter
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.assignment.physicswallah.AppNetworkStatus
import com.assignment.physicswallah.R
import com.assignment.physicswallah.DialogsUtil
import com.assignment.physicswallah.database.teachers.Teacher
import com.assignment.physicswallah.databinding.FragmentTeacherListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class TeacherListFragment : Fragment() {

    private lateinit var binding: FragmentTeacherListBinding

    private var list = ArrayList<Teacher>()

    private val viewModelJob = Job()

    // Coroutine init
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // For controlling loading dialog
    private val isLoadingDialogVisible= MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initialiseBindingAndViewModel(inflater, container)

        // To show back button in supportActionBar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);

        (activity as AppCompatActivity).supportActionBar!!.title=getString(R.string.teachers)

        fetchDataFromApi()

        val adapter = TeacherAdapter(list)
        binding.recyclerView.adapter = adapter


        return binding.root
    }

    private fun fetchDataFromApi() {

        // Checking if network available
        if(AppNetworkStatus.getInstance(requireContext()).isOnline) {
            isLoadingDialogVisible.value=true
            showLoadingDialog()
            Log.d("div", "TeacherListFragment L68 ")
            uiScope.launch {
                AndroidNetworking.get("https://my-json-server.typicode.com/easygautam/data/users")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(object : JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray?) {
                            Log.d("div", "TeacherListFragment L75 $response")
                            if (response != null) {
                                Log.d("div", response.toString())
                                parseData(response)
                            }
                        }

                        override fun onError(anError: ANError?) {
                            Log.e("div", "TeacherListFragment L84 $anError")
                        }
                    })
            }
        }
        else{
            Snackbar.make(binding.layout,"No internet connection", Snackbar.LENGTH_LONG)
                .setAction("RETRY"){
                    fetchDataFromApi()
                }.show()
        }
    }

    private fun parseData(data: JSONArray) {
        // Parsing data and converting response from API into an array of teacher object
        var i = 0
        list.clear()
        while (i < data.length()) {
            val obj = data.get(i) as JSONObject
            list.add(
                Teacher(
                    obj.getLong("id"),
                    obj.getString("name"),
                    obj.getJSONArray("subjects"),
                    obj.getJSONArray("qualification"),
                    obj.getString("profileImage")
                )
            )
            i++
        }

        isLoadingDialogVisible.value=false

        val adapter = TeacherAdapter(list)
        binding.recyclerView.adapter = adapter
    }

    private fun initialiseBindingAndViewModel(inflater: LayoutInflater, container: ViewGroup?) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_list, container, false)
        binding.lifecycleOwner=this

    }



    private fun showLoadingDialog() {
        val dialog = DialogsUtil(requireContext())
        dialog.showDialog("Loading, please wait",
            "Back",
            object : DialogsUtil.DialogListener {
                override fun onDialogButtonClicked(dialog: Dialog) {
                    dialog.dismiss()
                    findNavController().navigateUp()
                }
            })
        isLoadingDialogVisible.observe(viewLifecycleOwner, Observer {
            if(!it)
            {
                dialog.dismissDialog()
            }
        })
        val handler= Handler()
        handler.postDelayed(Runnable{
            dialog.dismissDialog()
        },R.string.loadingDuarationInMillis.toLong())

    }
}