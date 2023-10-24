//package com.example.beenthere.share
//
//import com.example.beenthere.data.source.BeenThereRepository
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.beenthere.databinding.FragmentShareBinding
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mock
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.verify
//import org.mockito.MockitoAnnotations
//
//
////@RunWith(RobolectricTestRunner::class)
//@RunWith(AndroidJUnit4::class)
//class ShareFragmentTest {
//
//    companion object {
//        const val REQUEST_CHOOSE_IMAGE = 1001
//        const val REQUEST_IMAGE_CAPTURE = 1002
//        const val recognizedText = "Know everybody's disapproval, should've worshiped her sooner"
//    }
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//
//
//    @Mock
//    private lateinit var repository: BeenThereRepository
//
//    @Mock
//    private lateinit var viewModel: ShareViewModel
//    private lateinit var shareFragment: ShareFragment
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//
//        shareFragment = ShareFragment()
//        viewModel = ShareViewModel(repository)
//
//    }
//
//
//    @Test
//    fun testTryReloadAndDetectInImageFromAlbum() {
//
//        shareFragment.tryReloadAndDetectInImage(null, REQUEST_CHOOSE_IMAGE)
//        verify(viewModel).getRecognizedUpperText(anyString())
//
//    }
//
//    @Test
//    fun testTryReloadAndDetectInImageTakePhoto() {
//        db =
//
//        shareFragment.tryReloadAndDetectInImage(null, REQUEST_IMAGE_CAPTURE)
//        verify(viewModel).getRecognizedLowerText(anyString())
//
//    }
//
////    @Test
////    fun `insert exp with empty field, returns error`() {
////
////        shareFragment.
////        viewModel.addData("","", "", "", "", "", false)
////
////
////    }
//
//
//}