package com.example.movie.ui.screens

import android.widget.ProgressBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie.R
import com.example.movie.data.remote.HttpRoutes
import com.example.movie.database.model.MovieEntity
import com.example.movie.ui.tools.ProgressBar
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.MovieEvents
import com.skydoves.landscapist.glide.GlideImage
import dagger.Module

@Composable
fun MainScreen(modifier: Modifier = Modifier){
    val movieViewModel = hiltViewModel<MovieViewModel>()
    val state by movieViewModel.state.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        SortTabs(tabs = sortTabs, {
            movieViewModel.onEvent(MovieEvents.FilterMovies(it))
            },
            modifier = modifier
        )
        Spacer(modifier = modifier.height(100.dp))
        MovieList(state.movies, {})
    }
}
@Composable
fun MovieList(movies: List<MovieEntity>, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, { onClick(movie.id) }, modifier.width(250.dp))
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
fun MovieItem(movie : MovieEntity, onClick: (Int) -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .width(250.dp)
            .padding(16.dp),
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
            text = movie.releaseDate,
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
    ){
    Box(
        modifier = modifier
            .aspectRatio(9 / 16f)

    ) {
        GlideImage(
            imageModel = imageUrl,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shadow(8.dp), // Match the size of the parent box
            contentScale = ContentScale.Crop // Scale the image to fill the box, cropping as necessary
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
    val movies: List<MovieEntity> = listOf(MovieEntity(0,false,false,"/fqv8v6AycXKsivp1T5yKtLbGXce.jpg", listOf(1,2,3),3123,"en","test", "testoverview", 7.88, "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg", "testdate", "testtitleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",false, 7.45,1231),MovieEntity(0,false,false,"/fqv8v6AycXKsivp1T5yKtLbGXce.jpg", listOf(1,2,3),3123,"en","test", "testoverview", 7.88, "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg", "testdate", "testtitle",false, 7.45,1231))
    Column(modifier = modifier.fillMaxWidth()) {
        SortTabs(tabs = sortTabs,{}, modifier = modifier)
        Spacer(modifier = modifier.height(100.dp))
        MovieList(movies, {})
    }
}
