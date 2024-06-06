package com.example.movie

import android.content.res.Configuration
import android.os.Bundle
import android.view.SurfaceControlViewHost.SurfacePackage
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie.ui.theme.MovieTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                Conversation(messages = messageList)
            }
        }
    }
}}
val messageList: List<Book> = listOf(Book("xd", "xdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"),Book("xd", "xd"),Book("xd", "xd"),Book("xd", "xd"),Book("xd", "xd"),Book("xd", "xd"))

data class Book (val author: String, val book: String)
@Composable
fun Message(book: Book){
    Row {
        Image(
            painter = painterResource(id = R.drawable.favorite_button),
            contentDescription = "xDD",
            modifier = Modifier
                .size(45.dp),
            colorFilter = ColorFilter.tint(Color.Yellow)
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = book.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 5.dp, color = surfaceColor) {
                Text(
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    text = book.book,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Book>){
    LazyColumn {
        items(messages) { message ->
            Message(message)
        }
    }
}
@Preview(name = "Light Theme")
@Preview(name = "Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    )
@Composable
fun PreviewConversation() {
    MovieTheme {
        Surface{
            Conversation(messages = messageList)
        }
    }
}