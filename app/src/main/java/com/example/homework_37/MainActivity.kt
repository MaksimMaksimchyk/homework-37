package com.example.homework_37

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.homework_37.ui.theme.Homework37Theme

class MainActivity : ComponentActivity() {
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
    Column(
        modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://i.pravatar.cc/",
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
            onClick = {}, modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Download Avatar Image",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}