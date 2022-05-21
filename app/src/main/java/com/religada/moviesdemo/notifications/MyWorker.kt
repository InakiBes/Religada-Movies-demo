package com.religada.moviesdemo.notifications

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.religada.moviesdemo.utils.log

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        log("Performing long running task in scheduled job")
        // (developer): add long running task here.
        return ListenableWorker.Result.success()
    }

}