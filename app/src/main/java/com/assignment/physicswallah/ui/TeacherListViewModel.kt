package com.assignment.physicswallah.ui

import TeacherAdapter
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.assignment.physicswallah.database.teachers.Teacher
import org.json.JSONArray
import org.json.JSONObject

class TeacherListViewModel: ViewModel()
{

    private var _teachersList= MutableLiveData<ArrayList<Teacher?>>()
    val teachersList: LiveData<ArrayList<Teacher?>>
        get() = _teachersList

    var pageNumber=-1
    var isLoading = false
    var isLoadingDialogVisible= MutableLiveData<Boolean>()


    init {

    }

    fun getTeacherList()
    {
        var link:String?=null

        link="https://my-json-server.typicode.com/easygautam/data/users"
        AndroidNetworking.get(link)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response != null) {
                        parseJSONTeachers(response.get("data") as JSONArray)

                    }
                    //_isLoading.value = false
                }

                override fun onError(anError: ANError?) {
                    Log.d("div", "TeachersListViewModel L50 API error: ${anError?.response} --- ${anError?.errorCode} --- ${anError?.errorBody} --- ${anError?.errorDetail} "
                    )
                }

            })
    }

    private fun parseJSONTeachers(teachersListData: JSONArray)
    {
        Log.d("div","TeachersListViewModel L59 ${teachersListData.length()}")
        val list=ArrayList<Teacher?>()
        var i=0
        while (i< teachersListData.length())
        {
            val obj=teachersListData.get(i) as JSONObject
            Log.d("div","TeachersListViewModel L116 $obj")
            list.add(Teacher(
                obj.getLong("id"),
                obj.getString("name"),
                obj.getJSONArray("subjects"),
                obj.getJSONArray("qualification"),
                obj.getString("profileImage")
                ))
            i++
        }
    }



}