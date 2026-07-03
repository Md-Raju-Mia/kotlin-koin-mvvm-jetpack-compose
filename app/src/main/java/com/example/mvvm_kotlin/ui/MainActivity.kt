package com.example.mvvm_kotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvm_kotlin.ui.theme.Mvvm_kotlinTheme
import com.example.mvvm_kotlin.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mvvm_kotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val post by viewModel.post.observeAsState()
    val postStatus by viewModel.postResponse.observeAsState("")
    val localPosts by viewModel.localPosts.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadLocalPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // GET Section
        post?.let {
            Text(text = "Last Fetched (Remote):", style = MaterialTheme.typography.titleMedium)
            Text(text = it.title)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = { viewModel.fetchPost() }) {
            Text(text = "Execute GET & Save to Room")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // POST Section
        Text(text = "POST Status:", style = MaterialTheme.typography.titleMedium)
        Text(text = postStatus.ifEmpty { "Waiting..." })
        Button(onClick = { viewModel.createPost("New Title", "Body") }) {
            Text(text = "Execute POST")
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Room Section
        Text(text = "Local Posts (Room Database):", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(localPosts) { localPost ->
                Text(text = "ID: ${localPost.id} - ${localPost.title}", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
