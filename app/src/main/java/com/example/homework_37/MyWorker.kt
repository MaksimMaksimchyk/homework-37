package com.example.homework_37

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.net.HttpURLConnection
import java.net.URL

class MyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val url = URL("https://jsonplaceholder.typicode.com/posts/1")
            val connection = url.openConnection() as HttpURLConnection
            val jsonText = connection.inputStream.bufferedReader().use { it.readText() }

            val sharedPrefs = applicationContext.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putString("last_post_data", jsonText).apply()
            Log.d("WorkManager", "Данные синхронизированы: $jsonText")
            Log.d("TEST WORKER", jsonText)
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Ошибка синхронизации", e)
            Result.retry()
        }
    }
}