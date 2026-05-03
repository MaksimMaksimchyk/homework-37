package com.example.homework_37

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coil3.compose.AsyncImage
import com.example.homework_37.ui.theme.Homework37Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    companion object {
        const val DOWNLOAD_URL = "https://i.pravatar.cc/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homework37Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainUi(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}

@Composable
fun MainUi(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Создаем лаунчер для запроса разрешения
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Если разрешили — запускаем сервис
            startDownloadService(context)
        }
    }

    Column(
        modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = MainActivity.DOWNLOAD_URL,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Text(
            "Random User",
            modifier = Modifier.padding(top = 22.dp),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Phone number: + 123 456 789",
            modifier = Modifier
                .padding(top = 22.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "City: Moscow",
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        startDownloadService(context)
                    } else {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    startDownloadService(context)
                }
            }, modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Download Avatar Image",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Button(
            onClick = {
                scheduleSync(context)
            }, modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Start scheduleSync",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun startDownloadService(context: Context) {
    val intent = Intent(context, DownloadService::class.java).apply {
        putExtra("URL", MainActivity.DOWNLOAD_URL)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

fun scheduleSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(true)
        .build()

    val syncRequest = PeriodicWorkRequestBuilder<MyWorker>(24, TimeUnit.HOURS)
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailySyncWork",
        ExistingPeriodicWorkPolicy.KEEP,
        syncRequest
    )
}