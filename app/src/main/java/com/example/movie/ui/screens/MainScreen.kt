package com.example.movie.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.movie.data.remote.HttpRoutes
import com.example.movie.database.model.MovieEntity
import com.example.movie.ui.tools.DateUtils
import com.example.movie.ui.tools.ProgressBar
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.MovieEvents
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun MainScreen(nav: NavController,
               username: String,
               modifier: Modifier = Modifier,
               movieViewModel: MovieViewModel = hiltViewModel()){

    val state by movieViewModel.state.collectAsState()
    var showButtons by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (state.isLoaded) {
        Column(modifier = modifier.fillMaxWidth()) {
            SortTabs(
                tabs = sortTabs,
                {
                    movieViewModel.onEvent(MovieEvents.FilterMovies(it))
                },
            )
            state.message?.let { errorMessage ->
                Text(text = errorMessage, color = Color.Red)
            }
            Spacer(modifier = modifier.height(100.dp))
            MovieList(state.movies, { nav.navigate("itemDetail/${it}") })
        }
        ProfileTab(
            username = username,
            showButtons = showButtons,
            onDisconnectClick = {
                coroutineScope.launch {
                    nav.navigate("loginScreen")
                    movieViewModel.disconnectUser(username)
                }
            },
            onToggleClick = { showButtons = !showButtons }
        )
    } else {
        Text(text = "loading...")
    }
}

@Composable
fun ProfileTab(username: String, showButtons: Boolean, onDisconnectClick: () -> Unit, onToggleClick: () -> Unit, modifier: Modifier = Modifier){
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = modifier
            .absoluteOffset(x = 300.dp, y = 100.dp)
            .then(
                if (showButtons) {
                    Modifier
                        .background(Color.White, shape)
                        .border(BorderStroke(1.dp, Color.Black), shape)
                        .clip(shape)
                } else {
                    Modifier
                }
            )
    ) {
        Column {
            Button(
                onClick = { onToggleClick() },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Text(
                    text = username,
                    color = Color(0xFF4A148C)
                )
            }

            if (showButtons) {
                Button(
                    onClick = { onDisconnectClick() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                ) {
                    Text(
                        text = "Logout",
                        color = Color(0xFF4A148C),
                    )
                }
            }
        }
    }
}
@Composable
fun MovieList(movies: List<MovieEntity>, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, { onClick(movie.idDatabase) }, modifier.width(250.dp))
        }
    }
}

val sortTabs: List<SortTabs> = listOf(
    SortTabs("Popularity" , FilterType.POPULARITY),
    SortTabs("Rating", FilterType.RATING),
    SortTabs("Favorites", FilterType.FAVORITES),
    SortTabs("New", FilterType.RELEASE_DATE),
)
data class SortTabs(
    val name: String,
    val query: FilterType
)

@Composable
fun SortTabs(tabs: List<SortTabs>, onClick: (FilterType) -> Unit, modifier: Modifier = Modifier){
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    Column(
        modifier = modifier
            .height(50.dp)
    ) {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                        onClick(item.query)
                    },
                    text = {
                        Text(text = item.name)
                    }
                )
            }
        }
    }
}

@Composable
fun MovieItem(movie : MovieEntity, onClick: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .width(250.dp)
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.Start
    ){
        GlideImageComposable(HttpRoutes.BASE_URL_IMAGE + HttpRoutes.ORIGINAL_SIZE + movie.posterPath,
            progress = movie.voteAverage.toFloat(),
            cornerRadius = 16
        )
        Spacer(modifier = modifier.height(35.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = modifier.height(5.dp))
        Text(
            text = DateUtils.toDisplayDateString(movie.releaseDate)!!,
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            fontSize = 20.sp
        )
    }
}
@Composable
fun GlideImageComposable(
    imageUrl: String,
    progress: Float,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 16,
    progressBarSize: Dp = 20.dp
) {
    Box(
        modifier = modifier
            .aspectRatio(9 / 16f)
    ) {
        GlideImage(
            imageModel = imageUrl,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shadow(8.dp),
            contentScale = ContentScale.Crop,
            loading = {
                Box(Modifier.matchParentSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            },
            failure = {
                Box(Modifier.matchParentSize()) {
                    Text(
                        text = "Image load failed",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = progressBarSize, y = progressBarSize)
        ) {
            ProgressBar(
                percentage = progress * 10f,
                radius = progressBarSize,
                fontSize = 16.sp,
                strokeWidth = 3.dp
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview(){
    val modifier = Modifier
    val movies: List<MovieEntity> = listOf(MovieEntity(0,false,false,"/fqv8v6AycXKsivp1T5yKtLbGXce.jpg", listOf(1,2,3),3123,"en","test", "testoverview", 7.88, "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg", "2022-05-21", "testtitleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",false, 7.45,1231),MovieEntity(0,false,false,"/fqv8v6AycXKsivp1T5yKtLbGXce.jpg", listOf(1,2,3),3123,"en","test", "testoverview", 7.88, "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg", "2022-05-21", "testtitle",false, 7.45,1231))
    Column(modifier = modifier.fillMaxWidth()) {
        SortTabs(tabs = sortTabs,{}, modifier = modifier)
        Spacer(modifier = modifier.height(100.dp))
        MovieList(movies, {})
    }
    ProfileTab(
        username = "Alin",
        showButtons = true,
        onDisconnectClick = {},
        onToggleClick = {},
    )
}
