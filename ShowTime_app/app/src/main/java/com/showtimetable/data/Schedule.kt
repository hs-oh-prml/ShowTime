package com.showtimetable.data

data class Schedule(
    var isLecture:Boolean,
    var name:String,
    var place:String,
    var time:ArrayList<TimeCell>,
    var credit: Int,
    var score:String?
) {
}