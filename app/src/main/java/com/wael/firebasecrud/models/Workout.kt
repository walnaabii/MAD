package com.wael.firebasecrud.models

data class Workout(var id:String,var userId:String,var finished:Int=0,var total:Int=0,val totalTime:Int=0,
                   var walking:Int=0,var walkingCal:Int=0,var running:Int=0,var runningCal:Int=0,
                   var cycling:Int=0,var cyclingCal: Int=0,var weight:Int=0,var weightCal: Int=0,
                   var cardio:Int=0,var cardioCal: Int=0,var date:Long){
    constructor():this("","",0,0,0,0,0,0,0,0,0,0,0,0,0,0)
}
