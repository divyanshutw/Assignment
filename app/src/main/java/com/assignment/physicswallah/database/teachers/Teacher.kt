package com.assignment.physicswallah.database.teachers


import org.json.JSONArray

data class Teacher (
    var id:Long=1,

    var name:String?=null,

    var subjects: JSONArray,

    var qualification: JSONArray,

    var profileImage:String? = null

)