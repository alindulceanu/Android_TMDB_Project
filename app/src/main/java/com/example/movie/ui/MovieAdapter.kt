package com.example.movie.ui

import com.example.movie.ui.tools.DateUtils
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.movie.R
import com.example.movie.data.remote.HttpRoutes
import com.example.movie.database.model.MovieEntity

class MovieAdapter (
    private var movies: List<MovieEntity>,
    private val onItemClick: (MovieEntity) -> Unit
): RecyclerView.Adapter<MovieAdapter.MyViewHolder>(){

    fun submitList(movies: List<MovieEntity>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView : ImageView = view.findViewById(R.id.image_view)
        private val progressBar : ProgressBar = view.findViewById(R.id.progress_bar)
        private val progressText : TextView = view.findViewById(R.id.progress_bar_value)
        private val titleBox : TextView = view.findViewById(R.id.title_box)
        private val dateBox : TextView = view.findViewById(R.id.date_box)

        init {
            progressBar.rotation = -90f
        }

        fun bind(item: MovieEntity){
            titleBox.text = item.title
            dateBox.text = DateUtils.toDisplayDateString(item.releaseDate)
            val progress = (item.voteAverage * 10).toInt()
            progressBar.progress = progress
            progressText.text = formatProgressWithSuperscript(progress)
            val progressBackgroundColor = getProgressColor(progress, true)
            val progressProgressColor = getProgressColor(progress, false)

            (progressBar.progressDrawable as? LayerDrawable)?.let { layerDrawable ->
                layerDrawable.findDrawableByLayerId(R.id.progress_ring)?.setTint(progressProgressColor)
                layerDrawable.findDrawableByLayerId(R.id.background_ring)?.setTint(progressBackgroundColor)
            }

            try {
                Glide.with(this.imageView)
                    .load(HttpRoutes.BASE_URL_IMAGE + HttpRoutes.ORIGINAL_SIZE + item.posterPath)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
                    .into(imageView)
            }
            catch (e: Exception){
                println("[LOG] No image for ${item.title}")
            }
        }

        private fun getProgressColor(progress: Int, forBackground: Boolean) : Int {
            val color : Int = when(forBackground){
                true -> {
                    when(progress) {
                        in 0..39 -> Color.rgb(80,0,0)
                        in 40..59 -> Color.rgb(80, 80, 0)
                        else -> Color.rgb(0, 80, 0)
                    }
                }

                false -> {
                    when(progress){
                        in 0..39 -> Color.rgb(200,0,0)
                        in 40..59 -> Color.rgb(200, 200, 0)
                        else -> Color.rgb(0, 200, 0)
                    }
                }
            }
            return color
        }
        private fun formatProgressWithSuperscript(progress: Int): SpannableString {
            val progressString = "$progress%"
            val spannableString = SpannableString(progressString)

            val superscriptSpan = SuperscriptSpan()

            val relativeSizeSpan = RelativeSizeSpan(0.5f) // 50% of the original size
            spannableString.setSpan(relativeSizeSpan, progressString.length - 1, progressString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(superscriptSpan, progressString.length - 1, progressString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return spannableString
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        val viewHolder = MyViewHolder(v)
        v.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(movies[position])
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieAdapter.MyViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}