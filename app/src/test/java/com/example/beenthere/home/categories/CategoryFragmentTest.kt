package com.example.beenthere.home.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.home.catogories.CategoryFragment
import com.example.beenthere.home.catogories.CategoryVM
import com.example.beenthere.share.ShareViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class CategoryFragmentTest {

    private val expList = listOf(Experience(userId= "Bryant", title= "12 Rules for Life", author= "Jordan B. Peterson", situation= "Don't know what to do in my life, everything seems meaningless, every job I do I wonder why am I doing this, phrases=Everything you do matters, you should learn to write, because that equals thinking. And if you can write and think, and make a presentation, people give you money, they give you jobs, you have influence. It's like, the most powerful weapon you can possibly have as a human being.", image= "http://books.google.com/books/content?id=u8w_DwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api", isProcessed= true, category= "LIFE_MEANING"), Experience(userId="Jammy", title="Zorba the Greek", author="Nikos Kazantzakis", situation= "cares too much how others think about me, phrases=life is trouble, only death is not, to be alive is to unfasten the belt and look for trouble", image= "http://books.google.com/books/content?id=72iabSl3b78C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api", isProcessed= true, category="LIFE_MEANING"), Experience(userId= "Bryant", title= "12 Rules for Life", author= "Jordan B. Peterson", situation= "Don't know what to do in my life, everything seems meaningless, every job I do I wonder why am I doing this, phrases=Everything you do matters, you should learn to write, because that equals thinking. And if you can write and think, and make a presentation, people give you money, they give you jobs, you have influence. It's like, the most powerful weapon you can possibly have as a human being.", image= "http://books.google.com/books/content?id=u8w_DwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api", isProcessed= true, category= "RELATIONSHIP"))

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CategoryVM
    private lateinit var categoryFragment: CategoryFragment

    @Mock
    private lateinit var repository: BeenThereRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        categoryFragment = CategoryFragment()
        viewModel = CategoryVM(repository)
    }

    @Test
    fun testSubmitListContent() = runBlocking {

        Mockito.`when`(viewModel.allExp()).thenReturn(flow { emit(expList) })
        viewModel.allExp().collect()

//        val flowTestObserver = FlowTestObserver<List<Experience>>
//        viewModel.allExp().observeForever(flowTestObserver)
//
//        flowTestObserver.assertValues(expList)
//
//        verify(categoryFragment)

    }


}