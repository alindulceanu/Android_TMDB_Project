//package com.example.movie.ui
//
//
//import android.animation.ObjectAnimator
//import android.app.Dialog
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.LinearGradient
//import android.graphics.Paint
//import android.graphics.PorterDuff
//import android.graphics.PorterDuffXfermode
//import android.graphics.Shader
//import android.graphics.drawable.ClipDrawable
//import android.graphics.drawable.GradientDrawable
//import android.graphics.drawable.LayerDrawable
//import android.os.Bundle
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.DialogFragment
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
//import com.example.movie.R
//import com.example.movie.data.remote.HttpRoutes
//import com.example.movie.database.model.MovieEntity
//import java.security.MessageDigest
//import com.bumptech.glide.load.Key
//import com.example.movie.viewmodel.MovieViewModel
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.Observer
//import com.example.movie.viewmodel.events.MovieEvents
//
//class DetailDialogFragment() : DialogFragment(){
//
//    class GradientTransformation : BitmapTransformation() {
//
//        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//            messageDigest.update(ID_BYTES)
//        }
//
//        override fun transform(
//            pool: BitmapPool,
//            toTransform: Bitmap,
//            outWidth: Int,
//            outHeight: Int
//        ): Bitmap {
//            return applyGradientMask(pool, toTransform)
//        }
//
//        private fun applyGradientMask(pool: BitmapPool, source: Bitmap): Bitmap {
//            val width = source.width
//            val height = source.height
//            val result = pool.get(width, height, Bitmap.Config.ARGB_8888)
//
//            val canvas = Canvas(result)
//            canvas.drawBitmap(source, 0f, 0f, null)
//
//            val paint = Paint()
//            val shader = LinearGradient(
//                0f, height / 2f, 0f, height.toFloat(),
//                0xFFFFFFFF.toInt(), 0x00FFFFFF.toInt(),
//                Shader.TileMode.CLAMP
//            )
//            paint.shader = shader
//            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
//            canvas.drawRect(0f, height / 2f, width.toFloat(), height.toFloat(), paint)
//
//            return result
//        }
//
//        override fun equals(other: Any?): Boolean {
//            return other is GradientTransformation
//        }
//
//        override fun hashCode(): Int {
//            return ID.hashCode()
//        }
//
//        companion object {
//            private const val ID = "com.example.yourapp.GradientTransformation"
//            private val ID_BYTES = ID.toByteArray(Key.CHARSET)
//        }
//    }
//
//    private val viewModel: MovieViewModel by activityViewModels()
//
//    companion object {
//        private const val ARG_MOVIE = "arg_movie"
//
//        fun newInstance(movie: MovieEntity): DetailDialogFragment{
//            val args = Bundle().apply {
//                putSerializable(ARG_MOVIE, movie)
//            }
//            val fragment = DetailDialogFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.dialog_box, container, false)
//        val movie: MovieEntity? = arguments?.getSerializable(ARG_MOVIE) as? MovieEntity
//
//        val titleBox: TextView = view.findViewById(R.id.dialog_title_box)
//        val genreLayout: LinearLayout = view.findViewById(R.id.dialog_genre_layout)
//        val ratingBox: TextView = view.findViewById(R.id.dialog_rating_box)
//        val overviewBox: TextView = view.findViewById(R.id.dialog_overview_box)
//        val dropbackImage: ImageView = view.findViewById(R.id.dialog_image_box)
//        val languageBox: TextView = view.findViewById(R.id.dialog_language_box)
//        val closeButton: ImageButton = view.findViewById(R.id.dialog_close_button)
//        val favoriteButton: ImageButton = view.findViewById(R.id.dialog_favorite_button)
//
//        movie?.let { movieEntity ->
//            titleBox.text = movieEntity.title
//            ratingBox.text = "${(movieEntity.voteAverage * 10).toInt()}%"
//            overviewBox.text = movieEntity.overview
//            languageBox.text = movieEntity.originalLanguage
//
//            viewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
//                genreLayout.removeAllViews()
//                movieEntity.genreIds.forEach { genreId ->
//                    val genreView = inflater.inflate(R.layout.genre_layout, genreLayout, false)
//                    val genreText: TextView = genreView.findViewById(R.id.genre_text)
//                    val genreName = genres.find { it.id == genreId }?.name ?: "Unknown"
//                    val circleImage: ImageView = genreView.findViewById(R.id.genre_circle)
//                    val genreLayoutBg: RelativeLayout = genreView.findViewById(R.id.genre_layout_bg)
//                    val genreColor = GenreColors.getColorForGenre(genreName)
//                    val strokeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.genre_background) as GradientDrawable
//                    strokeDrawable.setStroke(9, genreColor!!)
//                    genreLayoutBg.background = strokeDrawable
//
//                    circleImage.setColorFilter(genreColor, PorterDuff.Mode.SRC_IN)
//                    genreText.text = genreName
//
//                    genreLayout.addView(genreView)
//                }
//            })
//
//            setStarState(favoriteButton, movieEntity.favourite)
//
//            closeButton.setOnClickListener {
//                dismiss()
//            }
//
//            favoriteButton.setOnClickListener {
//                viewModel.onEvent(MovieEvents.FavoriteMovie(movieEntity.idDatabase))
//                movieEntity.favourite = !movieEntity.favourite
//                animateStar(favoriteButton, movieEntity.favourite)
//            }
//
//            try {
//                Glide.with(dropbackImage)
//                    .load(HttpRoutes.BASE_URL_IMAGE + HttpRoutes.ORIGINAL_SIZE + movieEntity.backdropPath)
//                    .transform(GradientTransformation())
//                    .into(dropbackImage)
//            } catch (e: Exception) {
//                println("[LOGXD] No backdrop image found for ${movieEntity.title}")
//            }
//        }
//
//        return view
//    }
//
//    private fun setStarState(view: ImageButton, isFavorite: Boolean) {
//        val layerDrawable = view.drawable as? LayerDrawable
//        val clipDrawable = layerDrawable?.findDrawableByLayerId(R.id.clip_drawable) as? ClipDrawable
//        clipDrawable?.level = if (isFavorite) 10000 else 0  // Set initial state
//    }
//
//    private fun animateStar(view: ImageButton, isFavorite: Boolean) {
//        val layerDrawable = view.drawable as? LayerDrawable
//        val clipDrawable = layerDrawable?.findDrawableByLayerId(R.id.clip_drawable) as? ClipDrawable
//
//        clipDrawable?.let {
//            val endLevel = if (isFavorite) 10000 else 0  // ClipDrawable uses levels from 0 to 10000
//            val animator = ObjectAnimator.ofInt(it, "level", it.level, endLevel)
//            animator.duration = 500  // Adjust animation duration as needed
//            animator.start()
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)  // Adjust dialog width and height
//        return dialog
//    }
//
//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.setLayout(
//            (resources.displayMetrics.widthPixels * 0.95).toInt(),
//            (resources.displayMetrics.heightPixels * 0.95).toInt()
//        )
//        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
//        dialog?.window?.setGravity(Gravity.CENTER)
//    }
//}