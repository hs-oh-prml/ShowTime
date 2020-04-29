package com.showtimetable.data

data class MyData(
    var semester:ArrayList<Semester>
) {
    data class Semester(
        var semester: String,
        var dayMode:Int,        // 5일, 6일, 7일
        var schedules:ArrayList<Schedule>
    )
}