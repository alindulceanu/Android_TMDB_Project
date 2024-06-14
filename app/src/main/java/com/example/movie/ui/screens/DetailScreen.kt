package com.example.movie.ui.screens

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import com.example.movie.R
import com.example.movie.data.remote.HttpRoutes
import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.DetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.security.MessageDigest
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.core.graphics.toColor
import com.example.movie.database.model.getGenreNameById
import com.example.movie.ui.GenreColors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Composable
fun DetailScreen(nav: NavController,
                 movieId: String,
                 modifier: Modifier = Modifier,
                 viewModel: DetailViewModel = hiltViewModel(),
                 genreColors: GenreColors
                 ) {
    LaunchedEffect(movieId) {
        viewModel.fetchItemById(movieId.toInt())
        viewModel.getAllGenres()  // Assuming you also want to load genres
    }
    val movie by viewModel.movie.observeAsState()
    val genres by viewModel.genres.observeAsState(emptyList())

    if (movie != null) {
        DrawScreen(
            movie = movie!!,
            genres = genres,
            onBackClick = { nav.navigateUp() },
            onFavoriteClick = {},
            genreColors = genreColors
        )
    } else {
        Text("Loading...", modifier = modifier.fillMaxSize())
    }
}

@Composable
fun DrawScreen(movie: MovieEntity,
               genres: List<GenreEntity>,
               modifier: Modifier = Modifier,
               onBackClick: () -> Unit,
               onFavoriteClick: () -> Unit,
               genreColors: GenreColors
) {
    GlideImg (
        imageUrl = HttpRoutes.BASE_URL_IMAGE + HttpRoutes.ORIGINAL_SIZE + movie.backdropPath,
        modifier = modifier
            .fillMaxWidth()
            .height(450.dp)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ){
        item {
            Spacer(modifier = modifier.height(50.dp))
            Row (
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.back_button2),
                    contentDescription = "back button",
                    modifier = modifier
                        .size(35.dp)
                        .background(Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBackClick() },
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = modifier.width(230.dp))
                Image(
                    painterResource(id = R.drawable.favorite_button),
                    contentDescription = "favorite button",
                    modifier = modifier
                        .size(35.dp)
                        .background(Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { },
                    contentScale = ContentScale.Fit
                )
            }
        }
        item {
            Spacer(modifier = modifier.height(250.dp))
        }

        item {
            Text(
                text = movie.title,
                style = TextStyle(
                    fontSize = 45.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = modifier.fillMaxWidth(),
                color = Color(0xFF4A148C)
            )
        }

        item {
            Spacer(modifier = modifier.height(15.dp))
        }

        item {
            LazyRow (
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                item {
                    Image(
                        painterResource(id = R.drawable.favorite_button_full),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp),
                        colorFilter = ColorFilter.tint(color = Color(0xFFFFD600))
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    Text(
                        text = "${(movie.voteAverage * 10).toInt()}%",
                        style = TextStyle(
                            fontSize = 30.sp
                        ),
                        color = Color(0xFF4A148C)
                    )
                }

                item {
                    Text(
                        text = movie.originalLanguage,
                        style = TextStyle(
                            fontSize = 30.sp
                        ),
                        color = Color(0xFF4A148C)
                    )
                }
                
                item {
                    movie.genreIds.forEach { id ->
                        val genreName = getGenreNameById(genres, id) ?: "Unknown"
                        val textMeasurer = rememberTextMeasurer()
                        val genreColor = Color(
                            genreColors.getColorForGenre(genreName)
                                ?: Color.Gray.toArgb()
                        )

                        val textLayoutResult = textMeasurer.measure(
                            text = genreName,
                            style = TextStyle(fontSize = 8.sp)
                        )

                        val textWidth = textLayoutResult.size.width

                        Box(
                            modifier = modifier
                                .padding(horizontal = 10.dp)
                                .width(textWidth.dp + 40.dp)
                                .height(30.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Canvas(
                                modifier = modifier
                                    .matchParentSize()
                            ) {
                                drawRoundRect(
                                    color = genreColor,
                                    cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx())
                                )
                            }
                            Canvas(
                                modifier = modifier
                                    .matchParentSize()
                            ) {
                                drawRoundRect(
                                    color = Color.Black,
                                    cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx()),
                                    style = Stroke(2.dp.toPx())
                                )
                            }
                            Box (
                                modifier = modifier,
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Canvas(
                                    modifier = modifier
                                        .size(10.dp),
                                ) {
                                    drawCircle(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                genreColor.darken(),
                                                genreColor.lighten()
                                            ),
                                        ),
                                        radius = 10.dp.toPx()
                                    )

                                    drawCircle(
                                        color = Color.Black,
                                        style = Stroke(1.dp.toPx()),
                                        radius = 10.dp.toPx()
                                    )
                                }

                                Text(
                                    modifier = modifier
                                        .padding(end = 10.dp, start = 20.dp),
                                    text = genreName,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                    ),
                                    color = Color(0xFFFFFFFF)
                                )
                            }
                        }
                    }
                }

                item {
                    TODO("Story line")
                }

                item {
                    TODO("Overview")
                }
            }
        }
    }
}

fun Color.darken(factor: Float = 0.8f): Color {
    return Color(
        red = (this.red * factor).coerceIn(0f, 1f),
        green = (this.green * factor).coerceIn(0f, 1f),
        blue = (this.blue * factor).coerceIn(0f, 1f),
        alpha = this.alpha
    )
}

fun Color.lighten(factor: Float = 1.2f): Color {
    return Color(
        red = (this.red * factor).coerceIn(0f, 1f),
        green = (this.green * factor).coerceIn(0f, 1f),
        blue = (this.blue * factor).coerceIn(0f, 1f),
        alpha = this.alpha
    )
}

@Composable
fun GlideImg(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            imageModel = imageUrl,
            requestBuilder = {
                Glide.with(context)
                    .load(imageUrl)
                    .apply(RequestOptions().transform(GradientTransformation()))
            },
            modifier = Modifier
                .matchParentSize(),
            contentScale = ContentScale.Crop
        )
    }
}

class GradientTransformation : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return applyGradientMask(pool, toTransform)
    }

    private fun applyGradientMask(pool: BitmapPool, source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val result = pool.get(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)
        canvas.drawBitmap(source, 0f, 0f, null)

        val paint = Paint()
        val shader = LinearGradient(
            0f, height / 2f, 0f, height.toFloat(),
            0xFFFFFFFF.toInt(), 0x00FFFFFF,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawRect(0f, height / 2f, width.toFloat(), height.toFloat(), paint)

        return result
    }
    override fun equals(other: Any?): Boolean {
        return other is GradientTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        private const val ID = "com.example.yourapp.GradientTransformation"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    }
}

@Preview
@Composable
fun PreviewDetailScreen() {
    val movie = MovieEntity(
        0,
        false,
        false,
        "/fqv8v6AycXKsivp1T5yKtLbGXce.jpg",
        listOf(1,2,3),
        3123,
        "en",
        "test",
        "testoverview",
        7.88,
        "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg",
        "2022-05-21",
        "testtitleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
        false,
        7.45,
        1231
    )

    val genres: List<GenreEntity> = listOf(
        GenreEntity(1, "Action"),
        GenreEntity(2, "Adventure"),
        GenreEntity(3, "Crime")
    )

    val genreColors = GenreColors(Application())

    DrawScreen(movie = movie, genres = genres, onBackClick = {}, onFavoriteClick = {}, genreColors = genreColors)
}