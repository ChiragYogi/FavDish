package com.example.favdish.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.favdish.FavDishApplication
import com.example.favdish.R
import com.example.favdish.databinding.FragmentDishDetailsBinding
import com.example.favdish.models.FavDish
import com.example.favdish.ui.viewmodel.FavDishViewModel
import com.example.favdish.ui.viewmodel.FavDishViewModelProvider
import com.example.favdish.utiles.Constants


class DishDetails : Fragment(R.layout.fragment_dish_details) {

    private var _binding: FragmentDishDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DishDetailsArgs>()
    private var dish: FavDish ?= null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelProvider((activity?.application as FavDishApplication).repository)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDishDetailsBinding.bind(view)

        val dishArgs = args.currentDish!!
        dish = dishArgs

        binding.apply {
            Glide.with(this@DishDetails).load(dishArgs.image).centerCrop()
                .into(binding.ivDishImage)
            tvTitle.text = dishArgs.title
            tvType.text = dishArgs.type
            tvCategory.text = dishArgs.category
            tvIngredients.text = dishArgs.ingredients

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvCookingDirection.text = Html.fromHtml(
                    dishArgs.directionToCook,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                tvCookingDirection.text = Html.fromHtml(dishArgs.directionToCook)
            }
            tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, dishArgs.cookingTime)

            if (dishArgs.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
            } else {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
            }

            binding.ivFavoriteDish.setOnClickListener {
                dishArgs.favoriteDish = !dishArgs.favoriteDish
                mFavDishViewModel.updateDishToDatabase(dishArgs)

                if (dishArgs.favoriteDish) {
                    binding.ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(), R.drawable.ic_favorite_selected
                        )
                    )
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_added_to_favorites),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(), R.drawable.ic_favorite_unselected
                        )
                    )
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_removed_from_favorite),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            setHasOptionsMenu(true)

        }
    }

    private fun deleteDish(dish: FavDish){
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setTitle(resources.getString(R.string.title_delete_dish))
            setMessage(resources.getString(R.string.msg_delete_dish_dialog, dish.title))
            setIcon(android.R.drawable.ic_dialog_alert)
            setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface,_ ->
                mFavDishViewModel.deleteDishToDatabase(favDish = dish)
                dialogInterface.dismiss()
                findNavController().navigateUp()
            }
            setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface,_ ->
                dialogInterface.dismiss()
            }
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun shareDish(){
        val type = "text/plain"
        val subject = "Checkout this dish recipe"
        var extraText = ""
        val shareWith = "Share with"


        dish?.let {
            var image = ""
            if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE){
                image = it.image
            }
            var cookingInstruction = ""
            cookingInstruction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                Html.fromHtml(
                    it.directionToCook,
                    Html.FROM_HTML_MODE_COMPACT
                ).toString()
            }else{
                Html.fromHtml(it.directionToCook).toString()
            }

            extraText =
                "$image \n" +
                        "\n Title: ${it.title} \n\n Type: ${it.type} \n\n" +
                        "Category: ${it.category}" +
                        "\n\n Ingredient:\n ${it.ingredients} \n\n " +
                        "Instructions To Cook: \n $cookingInstruction" +
                        "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."
        }

        val intent = Intent(Intent.ACTION_SEND).let {
            it.type = type
            it.putExtra(Intent.EXTRA_SUBJECT,subject)
            it.putExtra(Intent.EXTRA_TEXT,extraText)
        }
        startActivity(Intent.createChooser(intent,shareWith))

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dish_detail_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.deleteDish -> {

                deleteDish(dish = dish!!)
                return true
            }

            R.id.shareDish -> {

                shareDish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}