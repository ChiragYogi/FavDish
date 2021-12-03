package com.example.favdish

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@ExperimentalCoroutinesApi
class MainCoroutineRuleTest(
    private val dispater: CoroutineDispatcher = TestCoroutineDispatcher()
): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispater) {


    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispater)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}