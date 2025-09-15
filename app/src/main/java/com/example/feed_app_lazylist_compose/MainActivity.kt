package com.example.feed_app_lazylist_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feed_app_lazylist_compose.ui.theme.Feed_App_LazyList_ComposeTheme
import androidx.compose.runtime.*



//data model
//simple data for feed items
data class FeedItem(val id:Int,val title:String, val description:String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //makes ui draw behind system bars

        enableEdgeToEdge()
        setContent {

            //root composable where we define ui
            MaterialTheme {   //theme for consistent color , typography
                FeedScreenHost()  //call main screen composable
            }

        }
    }
}


@Preview
@Composable
fun FeedScreenHost() {



    // Create 10 sample feed items using List generator
    val feedItems = List(10) { index ->
        FeedItem(
            id = index,
            title = "Title $index",
            description = "This is the description for item #$index. Add whatever details you like."
        )
    }

    val context = LocalContext.current  // Required for Toast
    var isVertical by remember { mutableStateOf(true) } // Toggle LazyColumn / LazyRow

    Column(modifier = Modifier.fillMaxSize()) {

        // Row with two buttons (Vertical / Horizontal)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, top = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // spacing between buttons
        ) {
            Button(onClick = { isVertical = true }) { Text("Vertical (LazyColumn)") }
            Button(onClick = { isVertical = false }) { Text("Horizontal (LazyRow)") }
        }

        Divider() // simple horizontal line

        // Conditionally render vertical or horizontal feed
        Box(modifier = Modifier.fillMaxSize()) {
            if (isVertical) {
                VerticalFeed(feedItems = feedItems) { item ->
                    Toast.makeText(context, "Clicked ${item.title}", Toast.LENGTH_SHORT).show()
                }
            } else {
                HorizontalFeed(feedItems = feedItems) { item ->
                    Toast.makeText(context, "Clicked ${item.title}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}




@Composable
fun FeedCard(item: FeedItem, onClick: (FeedItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // occupy full width of parent (useful in vertical list)
            .clickable { onClick(item) } // handle click
            .padding(4.dp), // margin around card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // shadow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, fontSize = 18.sp, color = Color.Black) // title
            Spacer(modifier = Modifier.height(6.dp)) // space
            Text(text = item.description, fontSize = 14.sp, color = Color.Gray) // description
        }
    }
}



@Composable
fun VerticalFeed(feedItems: List<FeedItem>, onItemClick: (FeedItem) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp), // padding for whole list
        verticalArrangement = Arrangement.spacedBy(8.dp) // spacing between items
    ) {
        // items = feedItems list
        // key = unique identifier (id) for better performance & stability
        items(items = feedItems, key = { it.id }) { item ->
            FeedCard(item = item, onClick = onItemClick)
        }
    }
}




@Composable
fun HorizontalFeed(feedItems: List<FeedItem>, onItemClick: (FeedItem) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), // fixed height for horizontal cards
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // space between horizontal cards
    ) {
        items(items = feedItems, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .width(300.dp) // each card fixed width
                    .clickable { onItemClick(item) }, // handle click
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.title, fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = item.description, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

