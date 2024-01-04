package com.wael.firebasecrud.models

data class MealLog(var userId:String,var name:String, var calories:Int, var time:String,var date:Long){
    constructor() : this("","",0,"",0)
}
