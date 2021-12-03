package com.example.favdish.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.arraySetOf
import com.example.favdish.MainCoroutineRuleTest
import com.example.favdish.getOrAwaitValueTest
import com.example.favdish.repository.FavDishRepository
import com.example.favdish.repository.FavDishRepositoryTest
import com.example.favdish.utiles.Constants
import com.example.favdish.utiles.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class FavDishViewModelTest{

    @get: Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleTest()

    private lateinit var viewModel: FavDishViewModel


    @Before
    fun setup() {
        viewModel = FavDishViewModel(FavDishRepositoryTest())

    }

    @Test
    fun `insert new dish with empty field, return error `() {


     viewModel.validataeDishDataOnInsert(
            "Test","Local","",
            "brakfast","","","50min","")

        val value = viewModel.inserDish.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandle()).isInstanceOf(Resource.Error::class.java)

    }

    @Test
    fun `insert new dish with too long title, return error`() {

        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(i)
            }
        }

        viewModel.validataeDishDataOnInsert("Test","Local",string,
                "Test","Test","Test","Test","Test")

        val value = viewModel.inserDish.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandle()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert dish with valid input, retutn success ` () {

        viewModel.validataeDishDataOnInsert("Test","Test","Test","Test","Test","Test","Test","Test")

        val value = viewModel.inserDish.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandle()).isInstanceOf(Resource.Success::class.java)
    }








}