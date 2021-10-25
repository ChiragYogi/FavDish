package com.example.favdish.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.FavDishApplication
import com.example.favdish.R
import com.example.favdish.databinding.FragmentFavoriteDishBinding
import com.example.favdish.models.FavDish
import com.example.favdish.ui.adepter.FavDishAdepter
import com.example.favdish.ui.viewmodel.FavDishViewModel
import com.example.favdish.utiles.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteDishFragment : Fragment(R.layout.fragment_favorite_dish), FavDishAdepter.OnItemClick {

    private var _binding: FragmentFavoriteDishBinding? = null
    private val binding get() = _binding!!
    val mAdepter = FavDishAdepter(this)
    private  val mFavDishViewModel: FavDishViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteDishBinding.bind(view)


        binding.rvFavDishList.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = mAdepter
            setHasFixedSize(true)

        }

        observeFavoriteDishes()

    }

    private fun observeFavoriteDishes() {
       mFavDishViewModel.favoriteDishData.observe(viewLifecycleOwner,{ favDishList ->

           if (favDishList.isNotEmpty()) {
               binding.rvFavDishList.visibility = View.VISIBLE
               binding.tvNoDishAddedYet.visibility = View.GONE
                mAdepter.submitList(favDishList)
           } else {
               binding.rvFavDishList.visibility = View.GONE
               binding.tvNoDishAddedYet.visibility = View.VISIBLE
           }

       })
    }

    override fun onDestroyView() {
        binding.rvFavDishList.adapter = null
        _binding = null
        super.onDestroyView()



    }

    override fun onDishItemClick(favDish: FavDish) {

        val action = FavoriteDishFragmentDirections.actionNavigationFavoriteToDishDetails(favDish)
        findNavController().navigate(action)
    }

    override fun onDishEditClick(favDish: FavDish) {
        val intent = Intent(context,AddUpdateDishActivity::class.java)
        intent.putExtra(Constants.EXTRA_DISH_DETAILS,favDish)
        startActivity(intent)
    }

}