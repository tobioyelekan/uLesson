package com.example.ulesson.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

@ExperimentalCoroutinesApi
@Before
fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
}

@ExperimentalCoroutinesApi
@After
fun tearDownDispatcher() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
}