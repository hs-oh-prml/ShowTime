package com.showtime.data

data class MajorItem(
    var depart:String,
    var major:String?,
    var code:String?,
    var type:Int        // Type: 0 Department has Child Major Type: 1 Major End Node
) {
}