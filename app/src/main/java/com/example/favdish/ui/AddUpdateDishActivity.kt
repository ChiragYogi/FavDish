package com.example.favdish.ui


import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.FavDishApplication
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.databinding.DialogCustomeImageSelectionBinding
import com.example.favdish.models.FavDish
import com.example.favdish.ui.adepter.CustomListItemAdepter
import com.example.favdish.ui.viewmodel.FavDishViewModel
import com.example.favdish.utiles.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*



@AndroidEntryPoint
class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityAddUpdateDishBinding
    private lateinit var mCustomListDialog: Dialog
    private var mImagePath: String = ""
    private lateinit var imageView: ImageView

    private  var mFavDishDetail: FavDish? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels()




    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            resultForCamera(result.resultCode,result.data)
        }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            resultFromGallery(result.resultCode,result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)){
            mFavDishDetail = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }




        if (mFavDishDetail != null && mFavDishDetail!!.id != 0){
            supportActionBar?.title = resources.getString(R.string.title_edit_dish)
        }else{
            supportActionBar?.title = resources.getString(R.string.title_add_dish)
        }
        setDataToUi()


        binding.ivAddDishImage.setOnClickListener(this)
        binding.etType.setOnClickListener(this)
        binding.etDishCookingTime.setOnClickListener(this)
        binding.etCategory.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)
        binding.ivEditDishImage.setOnClickListener(this)

        imageView = binding.ivDishImage
    }

    private fun setDataToUi(){
        mFavDishDetail?.let {

            if (it.id != 0){
                binding.apply {
                    mImagePath = it.image
                    if (mImagePath.isNotEmpty()){
                        Glide.with(this@AddUpdateDishActivity).load(mImagePath).fitCenter().into(ivDishImage)
                        ivAddDishImage.visibility = View.GONE
                        binding.addImageTxt.visibility = View.GONE
                        ivEditDishImage.visibility = View.VISIBLE
                    }else{
                        ivAddDishImage.visibility = View.VISIBLE
                        binding.addImageTxt.visibility = View.VISIBLE
                        ivEditDishImage.visibility = View.GONE
                    }

                    etTitle.setText(it.title)
                    etType.setText(it.type)
                    etCategory.setText(it.category)
                    etIngredient.setText(it.ingredients)
                    etDishCookingTime.setText(it.cookingTime)
                    etDishCookingDirection.setText( it.directionToCook)
                    btnAddDish.text = resources.getString(R.string.lbl_update_dish)
                }
            }
        }
    }

    override fun onClick(v: View?) {
       if (v != null){
           when(v.id){
               R.id.iv_add_dish_image -> {
                   customImageSelectionDialog()
               }
               R.id.iv_edit_dish_image -> {
                   customImageSelectionDialog()
               }

               R.id.et_type -> {
                   customItemListDialog(resources.getString(R.string.title_select_dish_type),
                       Constants.dishTypes(),Constants.DISH_TYPE)
                   return
               }
               R.id.et_category -> {
                   customItemListDialog(resources.getString(R.string.title_select_dish_category),
                       Constants.dishCategories(),Constants.DISH_CATEGORY)
                   return
               }
               R.id.et_dish_cooking_time -> {
                   customItemListDialog(resources.getString(R.string.title_select_dish_cooking_time),
                       Constants.dishCookTime(),Constants.DISH_COOKING_TIME)
                   return
               }
               R.id.btn_add_dish ->{
                   addDishToDatabase()
               }

           }
       }
    }

    private fun addDishToDatabase() {

        val title = binding.etTitle.text.toString().trim {it <= ' '}
        val type = binding.etType.text.toString().trim{it <= ' '}
        val category = binding.etCategory.text.toString().trim{it <= ' '}
        val ingredient = binding.etIngredient.text.toString().trim{it <= ' '}
        val cookingTimeInMinutes = binding.etDishCookingTime.text.toString().trim{it <= ' '}
        val cookingDirection = binding.etDishCookingDirection.text.toString().trim{it <= ' '}

        when {
            mImagePath.isEmpty() -> {
                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_select_dish_image,Toast.LENGTH_LONG).show()
            }
            title.isEmpty() -> {

                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_enter_dish_title,Toast.LENGTH_LONG).show()
            }
            type.isEmpty() -> {

                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_select_dish_type,Toast.LENGTH_LONG).show()
            }
            category.isEmpty() -> {

                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_select_dish_category,Toast.LENGTH_LONG).show()
            }
            ingredient.isEmpty() -> {

                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_enter_dish_ingredients,Toast.LENGTH_LONG).show()
            }
            cookingTimeInMinutes.isEmpty() -> {
                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_select_dish_cooking_time,Toast.LENGTH_LONG).show()
            }
            cookingDirection.isEmpty() -> {

                Toast.makeText(this@AddUpdateDishActivity,
                    R.string.err_msg_enter_dish_cooking_instructions,Toast.LENGTH_LONG).show()
            }
            else -> {

                var favDishId = 0
                var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                var favoriteDish = false

                mFavDishDetail?.let {
                    if (it.id != 0){
                        favDishId = it.id!!
                        imageSource = it.imageSource
                        favoriteDish = it.favoriteDish
                    }
                }
                   val favDishDetail = FavDish(mImagePath,
                    imageSource,title,type,category,
                    ingredient,cookingTimeInMinutes,cookingDirection,favoriteDish,favDishId)

                if (favDishId == 0){
                   mFavDishViewModel.insertDishToDatabase(favDishDetail)
                    Toast.makeText(this,"Dish Added SuceesFully",Toast.LENGTH_LONG).show()
                }else{
                  mFavDishViewModel.updateDishToDatabase(favDishDetail)
                    Toast.makeText(this,"Dish Updated SuceesFully",Toast.LENGTH_LONG).show()
                }
                    finish()


            }


        }
    }




    private fun customItemListDialog(title: String, itemList: List<String>, selection: String){

        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title

        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adepter = CustomListItemAdepter(this,null,itemList,selection)
        binding.rvList.adapter = adepter
        mCustomListDialog.show()

    }

    fun selectedListItem(item: String, selection: String){
        when(selection){
            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                binding.etType.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                binding.etCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME -> {
                mCustomListDialog.dismiss()
                binding.etDishCookingTime.setText(item)
            }


        }
    }


    private fun customImageSelectionDialog() {

        mCustomListDialog = Dialog(this)

        val dialogBinding: DialogCustomeImageSelectionBinding =
            DialogCustomeImageSelectionBinding.inflate(layoutInflater)
            mCustomListDialog.setContentView(dialogBinding.root)

        dialogBinding.tvCamera.setOnClickListener {

            Dexter.withContext(this).withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object : MultiplePermissionsListener{


                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()){
                            val intent = Intent(ACTION_IMAGE_CAPTURE)
                            cameraResult.launch(intent)

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialog()
                }
            }
            )






            mCustomListDialog.dismiss()


             }
        dialogBinding.tvGallery.setOnClickListener{

            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener( object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryResult.launch(intent)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateDishActivity,"You Have Not Granted Permission",Toast.LENGTH_LONG).show()

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialog()
                    }
                }).onSameThread().check()

            mCustomListDialog.dismiss()
        }

        mCustomListDialog.show()
    }



    private fun saveImageToInternalStorage(bitmap: Bitmap): String{

        val wrapper = ContextWrapper(applicationContext)

        //setting file

        var file = wrapper.getDir(Constants.IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun showRationalDialog() {

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this).apply {
            setMessage("It Look Like You turned off permission require " +
                    "for this feature. It can be enable under application setting" )
            setPositiveButton("Go To Setting") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@AddUpdateDishActivity, "$e", Toast.LENGTH_LONG).show()
                }
            }
            setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }

        }
        dialog.show()
    }


    private fun resultForCamera(
        resultCode: Int,
        data: Intent?
    ) {
        when (resultCode) {
            RESULT_OK -> {

                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras?.get("data") as Bitmap

                    Glide.with(this)
                        .load(thumbnail)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.d("FavFoodDish", mImagePath)

                    binding.ivAddDishImage.visibility = View.GONE
                    binding.addImageTxt.visibility = View.GONE
                    binding.ivEditDishImage.visibility = View.VISIBLE
                }
            }
            RESULT_CANCELED -> {
                Log.d("FavFoodDish", "User Canceled the operation")
            }

        }
    }

    private fun resultFromGallery(
        resultCode: Int,
        data: Intent?
    ) {
        when(resultCode) {

            RESULT_OK -> {
                data?.let {
                    val selectedImage = data.data

                    Glide.with(this)
                        .load(selectedImage)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                    Log.d("FavFoodDish", mImagePath)
                                }
                                return false
                            }

                        })
                        .into(binding.ivDishImage)
                    //binding.ivDishImage.setImageURI(selectedImage)


                    binding.ivAddDishImage.visibility = View.GONE
                    binding.addImageTxt.visibility = View.GONE
                    binding.ivEditDishImage.visibility = View.VISIBLE
                }
            }
            RESULT_CANCELED -> {
                Log.d("FavFoodDish", "User Canceled the operation")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("imagePath",mImagePath)
        Log.d("imagePath", mImagePath)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mImagePath = savedInstanceState.getString("imagePath").toString()
        if (mImagePath.isNotEmpty()){
            Glide.with(this).load(mImagePath).fitCenter().into(binding.ivDishImage)
            binding.addImageTxt.visibility = View.GONE
            binding.ivAddDishImage.visibility = View.GONE
            binding.ivEditDishImage.visibility = View.VISIBLE

        }
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }



}












