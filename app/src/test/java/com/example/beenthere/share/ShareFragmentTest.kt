package com.example.beenthere.share

import android.net.Uri
import com.example.beenthere.data.source.BeenThereRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.beenthere.TestApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import cz.msebera.android.httpclient.client.utils.URIBuilder
import org.junit.Assert.assertTrue

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShareFragmentTest {

    companion object {
        const val REQUEST_CHOOSE_IMAGE = 1001
        const val REQUEST_IMAGE_CAPTURE = 1002
        const val recognizedText = "Know everybody's disapproval, should've worshiped her sooner"

    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var db: FirebaseApp

    private val uri = Uri.parse("https://example.com")

    @Mock
    private lateinit var repository: BeenThereRepository

    @InjectMocks
    private lateinit var viewModel: ShareViewModel
//    @InjectMocks
    private lateinit var shareFragment: ShareFragment

    private lateinit var mockShareViewModel: ShareViewModel

    private lateinit var mockShareFragment: ShareFragment
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        //        viewModel = ShareViewModel(repository)
        mockShareViewModel = mock(ShareViewModel::class.java)

                shareFragment = ShareFragment()
        //        shareFragment = Robolectric.buildFragment(ShareFragment::class)
        mockShareFragment = mock(ShareFragment::class.java)

//        val application = ApplicationProvider.getApplicationContext<TestApplication>()
//        application.onCreate()

//        viewModel = mock<ShareViewModel>()
//        shareFragment.viewModel = viewModel


//        Mockito.`when`(uri.toString()).thenReturn("https://example.com")



    }


    @Test
    fun testTryReloadAndDetectInImageFromAlbum() {
//        shareFragment.viewModel = mockShareViewModel
        shareFragment.tryReloadAndDetectInImage(uri, REQUEST_CHOOSE_IMAGE)
        println(REQUEST_CHOOSE_IMAGE)
        println(uri)
        verify(shareFragment).playBookAnimation()

    }

    @Test
    fun testTryReloadAndDetectInImageTakePhoto() {
//        shareFragment.viewModel = mockShareViewModel
        mockShareFragment.tryReloadAndDetectInImage(uri, REQUEST_IMAGE_CAPTURE)
        verify(mockShareFragment).doNothing()

    }

//    @Test
//    fun testTryReloadAndDetectInImageFromAlbum() {
//        // Call the method you want to test
//        shareFragment.tryReloadAndDetectInImage(null, REQUEST_CHOOSE_IMAGE_UPPER)
//
//        // Check if the playBookAnimation method was invoked
//        val wasPlayBookAnimationCalled = try {
//            // You can use reflection to access private methods, but it's generally not recommended.
//            val method = shareFragment.javaClass.getDeclaredMethod("playBookAnimation")
//            method.isAccessible = true
//            method.invoke(shareFragment)
//            true
//        } catch (e: NoSuchMethodException) {
//            false
//        }
//
//        assertTrue("playBookAnimation should have been called", wasPlayBookAnimationCalled)
//    }

//    @Test
//    fun testTryReloadAndDetectInImageTakePhoto() {
//
//        mockShareFragment.viewModel = viewModel
//        mockShareFragment.tryReloadAndDetectInImage(null, REQUEST_IMAGE_CAPTURE)
//        verify(mockShareViewModel).getRecognizedLowerText(anyString())
//
//    }

//    @Test
//    fun `insert exp with empty field, returns error`() {
//
//        shareFragment.
//        viewModel.addData("","", "", "", "", "", false)
//
//
//    }


}