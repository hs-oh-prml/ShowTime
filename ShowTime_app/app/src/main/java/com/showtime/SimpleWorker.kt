package com.showtime

import androidx.work.Worker

class SimpleWorker : Worker() {
    override fun doWork(): Result {

        return Result.SUCCESS
    }
}