package com.example.favdish.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.favdish.getOrAwaitValue
import com.example.favdish.models.FavDish
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class FavDishDaoTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test.db")
    lateinit var database: FavDishDatabase
    private lateinit var dao: FavDishDao


    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.getFavDishDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertDish() = runBlockingTest {

        val dish = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)

        dao.insertDish(dish)

    val allDish = dao.getAllDish().asLiveData().getOrAwaitValue()

     assertThat(allDish).contains(dish)

    }

    @Test
    fun deleteDish() = runBlockingTest {

        val dish = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)

        dao.insertDish(dish)
        dao.deleteDish(dish)

        val allDish = dao.getAllDish().asLiveData().getOrAwaitValue()

        assertThat(allDish).doesNotContain(dish)

    }


    @Test
    fun updateDish() = runBlockingTest {

        val dish = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)
        val updatedDish = FavDish("Test.jpeg","Local","Hello Test Update","Test",
            "Test","Test","Test","Test",false,1)
        dao.insertDish(dish)
        dao.updateDish(updatedDish)

        val allDish = dao.getAllDish().asLiveData().getOrAwaitValue()

        assertThat(allDish).contains(updatedDish)

    }

    @Test
    fun obseravFavoriteDish() = runBlockingTest {
        val dish = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)
        val dish2 = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",true,1)
        val dish3 = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)

        dao.insertDish(dish)
        dao.insertDish(dish2)
        dao.insertDish(dish3)

        val value = dao.getFavoriteDish().asLiveData().getOrAwaitValue()

        assertThat(value).contains(dish2)



    }

    @Test
    fun obseravQueryedDishDish() = runBlockingTest {
        val dish = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",false,1)
        val dish2 = FavDish("Test.jpeg","Local","Test","Test",
            "Test","Test","Test","Test",true,1)
        val dish3 = FavDish("Test.jpeg","Local","Test","Lunch",
            "Test","Test","Test","Test",false,1)

        dao.insertDish(dish)
        dao.insertDish(dish2)
        dao.insertDish(dish3)

        val query = "Lunch"

        val value = dao.getDishByType(query).asLiveData().getOrAwaitValue()

        assertThat(value).contains(dish3)



    }




}