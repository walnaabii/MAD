package com.wael.firebasecrud.models

data class Student(var id:String,var userId:String?, var fname:String?, var lname:String?, var email:String, var password:String) {
    // Required default constructor for Firebase
    constructor() : this("","","","","","")
}
