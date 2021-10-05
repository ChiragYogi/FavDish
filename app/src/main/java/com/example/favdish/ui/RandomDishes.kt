package com.example.favdish.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.favdish.FavDishApplication
import com.example.favdish.R
import com.example.favdish.databinding.FragmentRandomDishesBinding
import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.ui.viewmodel.RandomDishViewModel
import com.example.favdish.ui.viewmodel.FavDishViewModelProvider
import com.example.favdish.utiles.Constants
import com.example.favdish.utiles.Resource


class RandomDishes : Fragment(R.layout.fragment_random_dishes) {

    private var _binding: FragmentRandomDishesBinding? = null
    private val binding get() = _binding!!
    private var mProgressDialog: Dialog? = null
    private val mRandomDishViewModel: RandomDishViewModel by viewModels {
        FavDishViewModelProvider((activity?.application as FavDishApplication).repository)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRandomDishesBinding.bind(view)

        binding.swipeToRefresh.setOnRefreshListener {
            mRandomDishViewModel.getRandomDish()
            binding.ivAddDish.visibility = View.VISIBLE
        }


        mRandomDishViewModel.randomDish.observe(viewLifecycleOwner,{ responce ->

            when(responce){

                is Resource.Success -> {
                    responce.data?.let {

                      binding.swipeToRefresh.isRefreshing = false

                        setDataToUI(it)
                        hideCustomeDialog()
                    }

                }
                is Resource.Error -> {
                    hideCustomeDialog()
                    binding.swipeToRefresh.isRefreshing = false
                    Toast.makeText(context,"${responce.message}",Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showCustomDialog()
                }
            }

        })

    }


    private fun showCustomDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custome_progess_bar)
            it.show()
        }

    }

    private fun hideCustomeDialog(){
        mProgressDialog?.dismiss()
    }

    private fun setDataToUI(recipe: RandomRecipesResponce) {

        binding.apply {
            val randomDish = recipe.recipes[0]
            Glide.with(requireContext()).load(randomDish.image).into(ivDishImage)

            var dishType = "other"
            if (randomDish.dishTypes.isNotEmpty()) {
                dishType = randomDish.dishTypes[0]
                tvType.text = dishType
            }

            tvCategory.text = "other"

            var ingredient = ""
            for (value in randomDish.extendedIngredients) {
                ingredient = if (ingredient.isEmpty()) {
                    value.original
                } else {
                    ingredient + ", \n" + value.original
                }
            }
            tvIngredients.text = ingredient


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvCookingDirection.text = Html.fromHtml(
                    randomDish.instructions, Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                tvCookingDirection.text = Html.fromHtml(randomDish.instructions)
            }

            tvCookingTime.text =
                resources.getString(
                    R.string.lbl_estimate_cooking_time,
                    randomDish.readyInMinutes.toString()
                )

            val dishId = randomDish.id

            val addDishToDb = FavDish(
                randomDish.image,
                Constants.DISH_IMAGE_SOURCE_ONLINE,
                randomDish.title,
                dishType,
                "other",
                ingredient,
                randomDish.readyInMinutes.toString(),
                randomDish.instructions,
                false
            )

            ivAddDish.setOnClickListener {

               try {
                    mRandomDishViewModel.addDishTODatabase(addDishToDb)
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_dish_added_to_db),
                        Toast.LENGTH_SHORT
                    ).show()
                    ivAddDish.visibility = View.GONE
                }catch (e: Exception){
                    Toast.makeText(
                        requireActivity(),"$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}