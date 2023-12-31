package com.example.beenthere.share

import android.Manifest
import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Pair
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.beenthere.BuildConfig
import com.example.beenthere.R
import com.example.beenthere.data.User
import com.example.beenthere.databinding.FragmentShareBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.mlkit.BitmapUtils
import com.example.beenthere.mlkit.GraphicOverlay
import com.example.beenthere.mlkit.VisionImageProcessor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


class ShareFragment : Fragment() {

    lateinit var binding: FragmentShareBinding

    val viewModel by viewModels<ShareViewModel> { getVmFactory() }

    private var preview: ImageView? = null
    private var graphicOverlay: GraphicOverlay? = null

    //    private var selectedMode = OBJECT_DETECTION
    private var selectedSize: String? = SIZE_SCREEN
    private var isLandScape = false
    private var imageUri: Uri? = null

    // Max width (portrait mode)
    private var imageMaxWidth = 0

    // Max height (portrait mode)
    private var imageMaxHeight = 0
    private val imageProcessor: VisionImageProcessor by lazy {
        TextRecognitionProcessor(requireContext(), TextRecognizerOptions.Builder().build())
    }

    private var recognizedText = ""

    private val cameraPermissionGranted: Boolean
        get() {
            return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

    private val storagePermissionGranted: Boolean
        get() {
            return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }


    companion object {
        //        private const val TAG = "StillImageActivity"
        private const val TAG = "ShareFragment"
//        private const val OBJECT_DETECTION = "Object Detection"

        private const val SIZE_SCREEN = "w:screen" // Match screen width
        private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
        private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio
        private const val SIZE_ORIGINAL = "w:original" // Original image size
        private const val KEY_IMAGE_URI = "com.beenthere.mlkit.KEY_IMAGE_URI"
        private const val KEY_IMAGE_MAX_WIDTH = "com.beenthere.mlkit.KEY_IMAGE_MAX_WIDTH"
        private const val KEY_IMAGE_MAX_HEIGHT = "com.beenthere.mlkit.KEY_IMAGE_MAX_HEIGHT"
        private const val KEY_SELECTED_SIZE = "com.beenthere.mlkit.KEY_SELECTED_SIZE"
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CHOOSE_IMAGE = 1002
        private const val REQUEST_CHOOSE_IMAGE_UPPER = 1003
        private const val REQUEST_CHOOSE_IMAGE_LOWER = 1004

    }


    //    private var recognizedBook = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentShareBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner



        var image = ""

        binding.btnSearch.setOnClickListener {
            if (checkInternet(requireContext())) {

                // original, input
//                val title = binding.editInputBook.text.toString()

                // test, change to OCR
                startChooseImageIntentForResult()

            } else {
                showReminderDialog(getString(R.string.no_internet))
            }
        }

        viewModel.myResponse.observe(viewLifecycleOwner) { books ->
            try {
                val item = books.items[0]
                item.volumeInfo?.let { volume ->
                    with(volume) {
                        binding.bookTitleResult.text = title
                        binding.authorNameResult.text = authors[0]
                        imageLinks?.smallThumbnail?.let { thumbnail ->
                            try {
                                image = thumbnail
                                viewModel.getImage(thumbnail)
                                Log.i("Book test2", thumbnail)
                            } catch (e: Exception) {
                                showReminderDialog(e.message.toString())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                showReminderDialog(e.message.toString())
            }
        }


        binding.selectImageButton.setOnClickListener { view: View ->

            // Menu for selecting either: a) take new photo b) select from existing

            // one line??
            if (!cameraPermissionGranted || !storagePermissionGranted) {
                val permissionsToRequest = mutableListOf<String>()
                if (!cameraPermissionGranted) {
                    permissionsToRequest.add(Manifest.permission.CAMERA)
                }
                if (!storagePermissionGranted) {
                    permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toTypedArray(),
                    3
                )
            } else {
                startCameraIntentForResult()
//                        return@setOnMenuItemClickListener true
            }
//                }
//                false
//            }
//            val inflater = popup.menuInflater
//            inflater.inflate(R.menu.camera_button_menu, popup.menu)
//            popup.show()
//            viewModel.lowerText = true
        }

        preview = binding.preview
        graphicOverlay = binding.graphicOverlay



        isLandScape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(KEY_IMAGE_URI)
            imageMaxWidth = savedInstanceState.getInt(KEY_IMAGE_MAX_WIDTH)
            imageMaxHeight = savedInstanceState.getInt(KEY_IMAGE_MAX_HEIGHT)
            selectedSize = savedInstanceState.getString(KEY_SELECTED_SIZE)
        }


        // in repo??
        // fake user id
        val userList = listOf(
            "PollyYana",
            "Jammy",
            "Jayson",
            "Terry",
            "Elephant",
            "Timothy",
            "Bryant",
            "Jackson"
        )

//        val jayson = User(
//            userId = "",
//            userName = "Jayson",
//            userAvatar = R.drawable.avatar
//        )

        var userId = ""
        var title = ""
        var author = ""
        var situation = ""
        var phrases = ""


        // binding.apply { ??
//        binding.bookTitleResult.doAfterTextChanged {
//            title = binding.bookTitleResult.text.toString()
//        }
//
//        binding.authorNameResult.doAfterTextChanged {
//            author = binding.authorNameResult.text.toString()
//        }
//
//        binding.inputSituation.doAfterTextChanged {
//            situation = binding.inputSituation.text.toString()
//        }
//
//        binding.inputPhrases.doAfterTextChanged {
//            phrases = binding.inputPhrases.text.toString()
//        }

        binding.apply {
            bookTitleResult.update {
                title = it.toString()
            }

            authorNameResult.doAfterTextChanged {
                author = it.toString()
            }

            inputSituation.doAfterTextChanged {
                situation = it.toString()
            }

            inputPhrases.doAfterTextChanged {
                phrases = it.toString()
            }
        }


        binding.btnSend.setOnClickListener {
            userId = userList.random()
            if (title == "" && author == "" && situation == "" && phrases == "") {
                showReminderDialog(getString(R.string.error))
            } else {
                playAnalyzerAnimation()
                viewModel.addData(userId, title, author, situation, phrases, image, false)

                clearInput()
            }
        }

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            showReminderDialog(message.toString())
        }

        // naming or alternative
        viewModel.upperText.observe(viewLifecycleOwner) { text ->
            binding.editInputBook.setText(text)
        }

        viewModel.lowerText.observe(viewLifecycleOwner) { text ->
            binding.inputPhrases.setText(text)
        }


        val rootView = binding.root
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    imageMaxWidth = rootView.width
                    imageMaxHeight = rootView.height
                    if (SIZE_SCREEN == selectedSize) {
                        tryReloadAndDetectInImage()
                    }
                }
            }
        )

//        val settingsButton = binding.settingsButton
//        settingsButton.setOnClickListener {
//            val intent = Intent(applicationContext, SettingsActivity::class.java)
//            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.STILL_IMAGE)
//            startActivity(intent)
//        }

        binding.btnCloseRecognizer.setOnClickListener {
            preview!!.visibility = View.GONE
            graphicOverlay!!.visibility = View.GONE
            preview = null
            graphicOverlay!!.clear()
            imageUri = null
        }



        binding.inputSituation.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(binding.inputSituation)

                return@setOnKeyListener true
            }

            false
        }

        binding.inputPhrases.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(binding.inputPhrases)

                return@setOnKeyListener true
            }

            false
        }


        return binding.root
    }

    private fun clearInput() {
        binding.bookImageResult.visibility = View.GONE
        binding.bookTitleResult.visibility = View.GONE
        binding.authorNameResult.visibility = View.GONE
        imageUri = null
        binding.editInputBook.setText("")
        viewModel.getImage("")
        binding.bookTitleResult.text = ""
        binding.authorNameResult.text = ""
        binding.inputSituation.setText("")
        binding.inputPhrases.setText("")
    }

    private fun showReminderDialog(text: String) {

        AlertDialog.Builder(this.context)
            .setTitle(text)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun hideKeyboard(editText: EditText) {
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_IMAGE_URI, imageUri)
        outState.putInt(KEY_IMAGE_MAX_WIDTH, imageMaxWidth)
        outState.putInt(KEY_IMAGE_MAX_HEIGHT, imageMaxHeight)
        outState.putString(KEY_SELECTED_SIZE, selectedSize)
    }

    private fun startChooseImageIntentForResult(requestCode: Int = REQUEST_CHOOSE_IMAGE) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
    }


    private fun startCameraIntentForResult() { // Clean up last time's image
        imageUri = null
        preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            Log.i(TAG, "startCamera imageUri = $imageUri")

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private val targetedWidthHeight: Pair<Int, Int>
        get() {
            val targetWidth: Int
            val targetHeight: Int
            when (selectedSize) {
                SIZE_SCREEN -> {
                    targetWidth = imageMaxWidth
                    targetHeight = imageMaxHeight
                }

                SIZE_640_480 -> {
                    targetWidth = if (isLandScape) 640 else 480
                    targetHeight = if (isLandScape) 480 else 640
                }

                SIZE_1024_768 -> {
                    targetWidth = if (isLandScape) 1024 else 768
                    targetHeight = if (isLandScape) 768 else 1024
                }

                else -> throw IllegalStateException("Unknown size")
            }
            return Pair(targetWidth, targetHeight)
        }

    fun tryReloadAndDetectInImage(
        uri: Uri? = null,
        requestCode: Int? = null) {

        Log.d(TAG, "Try reload and detect image")
        try {

//            if (imageUri == null) {
//                Log.i("Share Frag", "tryReload imageUrl == null")
//                return
//            }
            if (uri == null) {
                Log.i("Share Frag", "tryReload imageUrl == null")
                return
            }

//            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
//                Log.i("Share Frag", "tryReload screen size wrong")
//                // UI layout has not finished yet, will reload once it's ready.
//                return
//            }

            // 把uri轉成bitmap

            val imageBitmap =
                BitmapUtils.getBitmapFromContentUri(requireContext().contentResolver, uri)
                    ?: return
            Log.i("Share Frag", "image bitmap = $imageBitmap")
            // Clear the overlay first
            graphicOverlay!!.clear()

            val resizedBitmap: Bitmap
            resizedBitmap =
                if (selectedSize == SIZE_ORIGINAL) {
                    imageBitmap
                } else {
                    // Get the dimensions of the image view
                    val targetedSize: Pair<Int, Int> = targetedWidthHeight

                    // Determine how much to scale down the image
                    val scaleFactor =
                        Math.max(
                            imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                            imageBitmap.height.toFloat() / targetedSize.second.toFloat()
                        )
                    Bitmap.createScaledBitmap(
                        imageBitmap,
                        (imageBitmap.width / scaleFactor).toInt(),
                        (imageBitmap.height / scaleFactor).toInt(),
                        true
                    )
                }


//            try {
            preview!!.setImageBitmap(resizedBitmap)
//            } catch (e: Exception) {
//                Log.i("preview", e.message.toString())
//            }

            graphicOverlay!!.setImageSourceInfo(
                resizedBitmap.width,
                resizedBitmap.height,
                /* isFlipped= */ false
            )


            // experiment
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())


            val image = InputImage.fromBitmap(resizedBitmap, 0)
            recognizer.process(image).addOnSuccessListener {
                val strBuilder = StringBuilder()
                for (block in it.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            val elementText = element.text
                            strBuilder.append(elementText).append(" ")
                        }
                    }
                }
                recognizedText = strBuilder.toString()
//                    recognizedBook = strBuilder.toString()
//                    binding.inputPhrases.setText(recognizedText)
//                    binding.editInputBook.setText(recognizedBook)

                Log.i("Share frag", "book title = $recognizedText")

                requestCode?.let { requestCode ->
                    when(requestCode) {

                        REQUEST_CHOOSE_IMAGE -> {
                            Log.i("Share frag", "choose image, text = $recognizedText")
                            viewModel.getBooks(recognizedText, BuildConfig.BOOK_API_KEY)
                            viewModel.getRecognizedUpperText(recognizedText)

                            playBookAnimation()

                            binding.bookImageResult.visibility = View.VISIBLE
                            binding.bookTitleResult.visibility = View.VISIBLE
                            binding.authorNameResult.visibility = View.VISIBLE

                        }

                        REQUEST_IMAGE_CAPTURE -> {
                            Log.i("Share frag", "take photo, text = $recognizedText")
                            viewModel.getRecognizedLowerText(recognizedText)

                        }

                    }

                }
            }

            imageProcessor.processBitmap(resizedBitmap, graphicOverlay)

        } catch (e: IOException) {
            Log.e(TAG, "Error retrieving saved image")
            imageUri = null
        }
    }

    fun playBookAnimation() {

        binding.cover.visibility = View.VISIBLE
        binding.lottieBookSearch.visibility = View.VISIBLE
        binding.lottieBookSearch.playAnimation()

        binding.lottieBookSearch.addAnimatorListener(object :
            Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {
                binding.lottieBookSearch.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator) {
                binding.lottieBookSearch.visibility = View.GONE
                binding.cover.visibility = View.GONE
            }
            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun playAnalyzerAnimation() {


        binding.lottieAnalyzer.visibility = View.VISIBLE
        binding.lottieAnalyzer.playAnimation()

        binding.lottieAnalyzer.addAnimatorListener(object :
            Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {
                binding.lottieAnalyzer.visibility = View.VISIBLE
                binding.coverLottieAnalyzer.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator) {
                binding.lottieAnalyzer.visibility = View.GONE
                binding.coverLottieAnalyzer.visibility = View.GONE
                showReminderDialog(getString(R.string.share_btn_dialog))

            }
            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }





    private fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage(imageUri, requestCode = requestCode)
        } else if (requestCode == REQUEST_CHOOSE_IMAGE &&
            resultCode == Activity.RESULT_OK) {
            // In this case, imageUri is returned by the chooser, save it.
            Log.i("Share Frag", "intent received")
            imageUri = data?.data
            tryReloadAndDetectInImage(uri = data?.data, requestCode)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("Roland", "onRequestPermissionsResult: " +
                "$requestCode, camera: $cameraPermissionGranted, storage: $storagePermissionGranted")
        if (requestCode == 3 && cameraPermissionGranted && storagePermissionGranted) {
            startCameraIntentForResult()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        tryReloadAndDetectInImage()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor.run { this.stop() }
        binding.editInputBook.setText("")
//        imageUri = null
//        binding.inputPhrases.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor.run { this.stop() }
//        `imageUri = null
//        binding.inputPhrases.setText("")`
    }

}


fun TextView.update(callback: (Editable?) -> Unit) {
    doAfterTextChanged {
        callback.invoke(it)
    }
}

