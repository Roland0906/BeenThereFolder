package com.example.beenthere.share

import android.Manifest
import android.app.Activity
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
import android.util.Log
import android.util.Pair
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.beenthere.BuildConfig
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentShareBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.mlkit.BitmapUtils
import com.example.beenthere.mlkit.GraphicOverlay
import com.example.beenthere.mlkit.VisionImageProcessor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import java.util.Locale


class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding

    private val viewModel by viewModels<ShareViewModel> { getVmFactory() }

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
    private var imageProcessor: VisionImageProcessor? = null

    private var recognizedText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentShareBinding.inflate(inflater, container, false)



        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        createImageProcessor()
//        tryReloadAndDetectInImage()

        var image = ""

        binding.btnSearch.setOnClickListener {
            if (checkInternet(requireContext())) {
                val title = binding.editInputBook.text.toString()
                if (title.isNotEmpty()) {

                    binding.bookImageResult.visibility = View.VISIBLE
                    binding.bookTitleResult.visibility = View.VISIBLE
                    binding.authorNameResult.visibility = View.VISIBLE

                    viewModel.getBooks(title, BuildConfig.BOOK_API_KEY)
                    viewModel.myResponse.observe(viewLifecycleOwner) { response ->
                        if (response.isSuccessful) {

                            try {
                                val item = response.body()!!.items[0]

                                binding.bookTitleResult.text = item.volumeInfo?.title
                                binding.authorNameResult.text =
                                    item.volumeInfo?.authors?.get(0) ?: ""

                                Log.i("Book test", item.volumeInfo?.imageLinks?.smallThumbnail.toString())

                                try {
                                    image = item.volumeInfo?.imageLinks?.smallThumbnail.toString()
                                    viewModel.getImage(item.volumeInfo?.imageLinks?.smallThumbnail.toString())
                                } catch (e: Exception) {
                                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {
                                Toast.makeText(this.context, R.string.no_book_found, Toast.LENGTH_SHORT).show()
                            }

                        } else {

                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Title can not be empty", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Please connect to internet", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        val cameraPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        val cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED
        val storagePermissionGranted = ContextCompat.checkSelfPermission(requireContext(), storagePermission) == PackageManager.PERMISSION_GRANTED

        binding.selectImageButton.setOnClickListener { view: View ->
            // Menu for selecting either: a) take new photo b) select from existing

            val popup = PopupMenu(requireContext(), view, Gravity.END)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                val itemId = menuItem.itemId
                if (itemId == R.id.select_images_from_local) {
                    startChooseImageIntentForResult()
                    return@setOnMenuItemClickListener true
                } else if (itemId == R.id.take_photo_using_camera) {

                    if (!cameraPermissionGranted || !storagePermissionGranted) {
                        val permissionsToRequest = mutableListOf<String>()
                        if (!cameraPermissionGranted) {
                            permissionsToRequest.add(cameraPermission)
                        }
                        if (!storagePermissionGranted) {
                            permissionsToRequest.add(storagePermission)
                        }
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            permissionsToRequest.toTypedArray(),
                            3
                        )
                    } else {
                        startCameraIntentForResult()
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.camera_button_menu, popup.menu)
            popup.show()
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



        // fake user id
        val userList = listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson")

        var userId = ""
        var title = ""
        var author = ""
        var situation = ""
        var phrases = ""



        binding.bookTitleResult.doAfterTextChanged {
            title = binding.bookTitleResult.text.toString()
        }

        binding.authorNameResult.doAfterTextChanged {
            author = binding.authorNameResult.text.toString()
        }

        binding.inputSituation.doAfterTextChanged {
            situation = binding.inputSituation.text.toString()
        }

        binding.inputPhrases.doAfterTextChanged {
            phrases = binding.inputPhrases.text.toString()
        }


        binding.btnSend.setOnClickListener {
            userId = userList.random()
            if (title == "" && author == "" && situation == "" && phrases == "") {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addData(userId, title, author, situation, phrases, image, false)

                binding.bookImageResult.visibility = View.GONE
                binding.bookTitleResult.visibility = View.GONE
                binding.authorNameResult.visibility = View.GONE
//                preview = null
//                graphicOverlay!!.clear()
//                preview!!.visibility = View.GONE
//                graphicOverlay!!.visibility = View.GONE
                imageUri = null
                binding.editInputBook.setText("")
                viewModel.getImage("")
                binding.bookTitleResult.text = ""
                binding.authorNameResult.text = ""
                binding.inputSituation.setText("")
                binding.inputPhrases.setText("")
            }
        }

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

//        binding.btnCloseRecognizer.setOnClickListener {
//            preview!!.visibility = View.GONE
//            graphicOverlay!!.visibility = View.GONE
//            preview = null
//            graphicOverlay!!.clear()
//            imageUri = null
//        }


        return binding.root
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_IMAGE_URI, imageUri)
        outState.putInt(KEY_IMAGE_MAX_WIDTH, imageMaxWidth)
        outState.putInt(KEY_IMAGE_MAX_HEIGHT, imageMaxHeight)
        outState.putString(KEY_SELECTED_SIZE, selectedSize)
    }

    private fun startChooseImageIntentForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE)
    }


    private fun startCameraIntentForResult() { // Clean up last time's image
        imageUri = null
        preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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

    // text recognition
    private fun createImageProcessor() {
        imageProcessor = TextRecognitionProcessor(requireContext(), TextRecognizerOptions.Builder().build())
    }

    private fun tryReloadAndDetectInImage() {
        Log.d(TAG, "Try reload and detect image")
        try {
            if (imageUri == null) {
                return
            }

            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
                // UI layout has not finished yet, will reload once it's ready.
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(requireContext().contentResolver, imageUri) ?: return
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

            try {
                preview!!.setImageBitmap(resizedBitmap)
            } catch (e: Exception) {
                Log.i("preview", e.message.toString())
            }

            if (imageProcessor != null) {
                graphicOverlay!!.setImageSourceInfo(
                    resizedBitmap.width,
                    resizedBitmap.height,
                    /* isFlipped= */ false
                )


                // experiment
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())


                val image = InputImage.fromBitmap(resizedBitmap, 0)
                val text = recognizer.process(image).addOnSuccessListener {
                    val strBuilder = StringBuilder()
                    for (block in it.textBlocks) {
                        val blockText = block.text
                        for (line in block.lines) {
                            val lineText = line.text
                            for (element in line.elements) {
                                val elementText = element.text
                                strBuilder.append(elementText).append(" ")
                            }
                        }
                    }
                    recognizedText = strBuilder.toString()
                    binding.inputPhrases.setText(recognizedText)
                }

                imageProcessor!!.processBitmap(resizedBitmap, graphicOverlay)
            } else {
                Log.e(TAG, "Null imageProcessor, please check adb logs for imageProcessor creation error")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error retrieving saved image")
            imageUri = null
        }
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage()
        } else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // In this case, imageUri is returned by the chooser, save it.
            imageUri = data!!.data
            tryReloadAndDetectInImage()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createImageProcessor()
        tryReloadAndDetectInImage()
    }

    public override fun onPause() {
        super.onPause()
        imageProcessor?.run { this.stop() }
        imageUri = null
        binding.inputPhrases.setText("")
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
        imageUri = null
        binding.inputPhrases.setText("")
    }

}