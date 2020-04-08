package com.showtime.data

data class Schedule(
    var isLecture:Boolean,
    var name:String,
    var time:ArrayList<TimeCell>,
    var credit: Int,
    var score:String?
) {
}