package com.example.favdish.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favdish.FavDishApplication
import com.example.favdish.R
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.databinding.FragmentAllDishBinding
import com.example.favdish.models.FavDish
import com.example.favdish.ui.adepter.CustomListItemAdepter
import com.example.favdish.ui.adepter.FavDishAdepter
import com.example.favdish.ui.viewmodel.FavDishViewModel
import com.example.favdish.utiles.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDishFragment : Fragment(R.layout.fragment_all_dish), FavDishAdepter.OnItemClick {

    private var _binding: FragmentAllDishBinding? = null
    private val binding get() = _binding!!
    private val mAdepter = FavDishAdepter(this)
    private lateinit var mCustomDialog: Dialog
    private  val mFavDishViewModel: FavDishViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAllDishBinding.bind(view)
         binding.rvDishList.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = mAdepter
            setHasFixedSize(true)

        }

        observeAllDishes()

        setHasOptionsMenu(true)

    }

    private fun observeAllDishes() {
        mFavDishViewModel.allDishData.observe(viewLifecycleOwner,{ dishesList ->

            if (dishesList.isNotEmpty()){
                binding.rvDishList.visibility = View.VISIBLE
                binding.tvNoDishAddedYet.visibility = View.GONE
                mAdepter.submitList(dishesList)
            } else {
                binding.rvDishList.visibility = View.GONE
                binding.tvNoDishAddedYet.visibility = View.VISIBLE

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_dish_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){

           R.id.addUpdateDishActivity ->{
               startActivity(
                   Intent(
                       requireActivity(), AddUpdateDishActivity::class.java
                   )
               )
               return true
              }
           R.id.action_filter_dish -> {
               filterListDialog()
               return true
           }
                  }

        return super.onOptionsItemSelected(item)


    }

    override fun onDestroyView() {
        binding.rvDishList.adapter = null
        _binding = null
        super.onDestroyView()


    }

    override fun onDishItemClick(favDish: FavDish) {

        val action = AllDishFragmentDirections.actionNavigationAllDishesToDishDetails(favDish)
        findNavController().navigate(action)
    }

    override fun onDishEditClick(favDish: FavDish) {
        val intent = Intent(context,AddUpdateDishActivity::class.java)
        intent.putExtra(Constants.EXTRA_DISH_DETAILS,favDish)
        startActivity(intent)
    }

    fun filterItemSelection(selection: String){
        mCustomDialog.dismiss()

       mFavDishViewModel.getDishByType(selection).observe(viewLifecycleOwner){ dishList ->
                dishList.let {
                    if (dishList.isNotEmpty()) {
                        binding.rvDishList.visibility = View.VISIBLE
                        binding.tvNoDishAddedYet.visibility = View.GONE
                        mAdepter.submitList(dishList)
                    } else {
                        binding.rvDishList.visibility = View.GONE
                        binding.tvNoDishAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }


    private fun filterListDialog(){
        mCustomDialog = Dialog(requireActivity())
        val mBinding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomDialog.setContentView(mBinding.root)
        mBinding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishType = Constants.dishTypes()
        mBinding.rvList.layoutManager = LinearLayoutManager(requireActivity())

        val adepter = CustomListItemAdepter(requireActivity(),this,dishType,Constants.FILTER_SELECTION)
        mBinding.rvList.adapter = adepter
        mCustomDialog.show()
    }


}