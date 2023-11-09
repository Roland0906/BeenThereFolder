package com.example.beenthere.share

import android.net.Uri
import android.view.View
import com.example.beenthere.data.source.BeenThereRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.beenthere.MainActivity
import com.example.beenthere.TestApplication
import com.example.beenthere.home.HomeFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import cz.msebera.android.httpclient.client.utils.URIBuilder
import org.junit.Assert.assertEquals
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

    private val uri = Uri.parse("https://example.com")

    private lateinit var activity: MainActivity

    private lateinit var homeFragment: HomeFragment
    private lateinit var shareFragment: ShareFragment

    @Mock
    private lateinit var repository: BeenThereRepository

    private lateinit var shareViewModel: ShareViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        activity = Robolectric.buildActivity(MainActivity::class.java).create().start().get()

        homeFragment = HomeFragment()
        shareFragment = ShareFragment()
        activity.supportFragmentManager.beginTransaction().add(homeFragment, null).commit()
        activity.supportFragmentManager.beginTransaction().add(shareFragment, null).commit()
        shareViewModel = ShareViewModel(repository)

    }


    @Test
    fun testTryReloadAndDetectInImageFromAlbum() {

        shareFragment.tryReloadAndDetectInImage(uri, REQUEST_CHOOSE_IMAGE)
        println(REQUEST_CHOOSE_IMAGE)
        println(uri)
        val visibility = shareFragment.binding.bookImageResult.visibility
        assertEquals(View.VISIBLE, visibility)

    }

    @Test
    fun testTryReloadAndDetectInImageTakePhoto() {

        shareFragment.tryReloadAndDetectInImage(uri, REQUEST_IMAGE_CAPTURE)
        verify(shareViewModel).getRecognizedLowerText(anyString())

    }


}