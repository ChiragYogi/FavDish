package com.example.favdish.ui.viewmodel


import androidx.lifecycle.*
import com.example.favdish.models.FavDish
import com.example.favdish.repository.FavDishImpl
import com.example.favdish.repository.FavDishRepository
import com.example.favdish.utiles.Constants
import com.example.favdish.utiles.Event
import com.example.favdish.utiles.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavDishViewModel
@Inject constructor(
    private val repository: FavDishImpl
) : ViewModel() {

    val allDishData: LiveData<List<FavDish>> = repository.getAllDish().asLiveData()
    val favoriteDishData: LiveData<List<FavDish>> = repository.getFavoriteDish().asLiveData()

    private val _insertDishStatus = MutableLiveData<Event<Resource<FavDish>>>()
    val inserDish: LiveData<Event<Resource<FavDish>>> get() = _insertDishStatus



    fun insertDishToDatabase(favDish: FavDish) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDish(favDish)
        }
    }

    fun updateDishToDatabase(favDish: FavDish) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDish(favDish)
        }
    }

    fun deleteDishToDatabase(favDish: FavDish) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDish(favDish)
        }
    }


    fun validataeDishDataOnInsert(
        image: String, imageSource: String,
        title: String, type: String, category: String,
        ingredients: String, cookingTime: String, diractionToCook: String)
    {

        if (title.isEmpty() || type.isEmpty() || category.isEmpty() ||
            ingredients.isEmpty() || cookingTime.isEmpty() || diractionToCook.isEmpty()
        ) {
            _insertDishStatus.postValue(Event(
                Resource.Error(
                    "title must be not grater than ${Constants.MAX_NAME_LENGTH} words",
                    null
                ))
            )
            return

        } else if (title.length > Constants.MAX_NAME_LENGTH) {
            _insertDishStatus.postValue(Event(
                Resource.Error(
                    "title must be not grater than ${Constants.MAX_NAME_LENGTH} words",
                    null
                ))
            )
            return
        } else {

            val favDish = FavDish(
                image,
                imageSource,
                title,
                type,
                category,
                ingredients,
                cookingTime,
                diractionToCook,
                false
            )

            insertDishToDatabase(favDish)
            _insertDishStatus.postValue(Event(Resource.Success(favDish)))

        }
    }

  /*  fun validateAndAddDishInDataBase(
        image: String, imageSource: String,
        title: String, type: String, category: String,
        ingredients: String, cookingTime: String, diractionToCook: String){

        if (title.isEmpty() || type.isEmpty() || category.isEmpty() ||

        }

        if (title.length > Constants.MAX_NAME_LENGTH){
            _insertDishStatus.postValue(Resource.Error("title must be not grater than ${Constants.MAX_NAME_LENGTH} words", null))
            return
        }

        if (image.isEmpty() && imageSource.isEmpty()){
            _insertDishStatus.postValue(Resource.Error("Please Select Image",null))
        }



    }*/


    fun getDishByType(query: String): LiveData<List<FavDish>> =
        repository.getDishByType(query).asLiveData()

}















