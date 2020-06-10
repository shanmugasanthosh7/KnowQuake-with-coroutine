package com.sample.knowquake.ui.earthquakes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.knowquake.api.ApiService
import com.sample.knowquake.coroutine.CoroutineContextProviderTest
import com.sample.knowquake.provider.ResourceProvider
import com.sample.knowquake.vo.EarthQuake
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Test
import com.sample.knowquake.result.EventObserver
import com.sample.knowquake.rule.TestCoroutineRule
import com.sample.knowquake.util.LiveDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockito.Mockito.doReturn


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class EarthQuakeViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var apiService: ApiService

    private lateinit var earthQuakeViewModel: EarthQuakeViewModel

    @Mock
    lateinit var response: EarthQuake

    @Mock
    lateinit var resourceProvider: ResourceProvider

    @Before
    fun setUp() { // This method is call before each test run.
        earthQuakeViewModel =
            EarthQuakeViewModel(apiService, CoroutineContextProviderTest(), resourceProvider)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun earthquake_feature_test() {
        testCoroutineRule.runBlockingTest {
            doReturn(response)
                .`when`(apiService)
                .earthQuakeFeatures(limit = 1, offset = 1)

            earthQuakeViewModel.isProgressShown.observeForever(
                EventObserver {
                    if (it) {
                        assertTrue("Loading Success", true)
                    } else {
                        assertFalse("Loading Finished", it)
                    }
                })

            earthQuakeViewModel.earthQuakeFeatures(1, 1, false)

            assertEquals(response, LiveDataTestUtil.getValue(earthQuakeViewModel.earthQuake))
        }
    }

}