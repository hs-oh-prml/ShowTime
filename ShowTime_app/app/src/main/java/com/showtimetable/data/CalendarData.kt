package com.showtimetable.data

class CalendarData (
    var calendarItemList: ArrayList<CalendarItem>
) {
    data class CalendarItem(
        var color:String,
        var content:String
    ) {
    }
}