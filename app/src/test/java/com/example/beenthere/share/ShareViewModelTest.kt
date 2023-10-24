package com.example.beenthere.share


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.camera.core.impl.CameraConfig.RequiredRule
import androidx.lifecycle.Observer
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.getOrAwaitValueTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

//@RunWith(AndroidJUnit4::class)
class ShareViewModelTest {

    @Mock
    private lateinit var repository: BeenThereRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getRecognizedUpperText_giveString_liveDataChangeAccordingly() {
        // Given a fresh viewModel
        val shareViewModel = ShareViewModel(repository)

        // Observe the LiveData forever
//        shareViewModel.upperText.

        // When getting new recognized text
        shareViewModel.getRecognizedUpperText("knows everybody's disapproval")

        // Then the getText function is triggered
        val value = shareViewModel.upperText.getOrAwaitValueTest()
        assertEquals(value, "knows everybody's disapproval")
    }

//    @Test
//    fun getRecognizedUpperText_giveInt_returnError() {
//        // Given a fresh viewModel
//        val shareViewModel = ShareViewModel(repository)
//
//        // Observe the LiveData forever
////        shareViewModel.upperText.
//
//        // When getting new recognized text
//        shareViewModel.getRecognizedUpperText(0)
//
//        // Then the getText function is triggered
//        val value = shareViewModel.upperText.getOrAwaitValueTest()
//        assertEquals(value, "knows everybody's disapproval")
//    }

}
