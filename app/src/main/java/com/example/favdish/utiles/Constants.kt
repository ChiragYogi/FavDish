package com.example.favdish.utiles

object  Constants {

    const val BASE_URL = "https://api.spoonacular.com/"

     const val IMAGE_DIRECTORY = "FavDishImages"

    const val MAX_NAME_LENGTH = 20
    const val MAX_PRICE_LENGTH = 10
    const val GRIDE_SPAN_COUNT = 2

    //key value
    const val TAGE_VALUE = "vegetarian, dessert"
    const val API_ENDPOINTS = "recipes/random"
    const val APIKEY = "APi KEY HERE"
    // KEY PARAMS
    const val API_KEY: String = "apiKey"
    const val TAGS: String = "tags"

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

    const val EXTRA_DISH_DETAILS: String = "DishDetails"

    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"

    const val NOTIFICATION_ID = "FavDish_notification_id"
    const val TAG_VALUE = "FavDish"
    const val Notification_CHANNEL = "FavDish_notification_channel"
    const val NOTIFICATION_NAME = "FavDish"

    /**
     * This function will return the Dish Type List items.
     */
    fun dishTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")
        return list
    }

    /**
     *  This function will return the Dish Category list items.
     */
    fun dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Juices")
        list.add("Sandwich")
        list.add("Tea & Coffee")
        list.add("Wraps")
        list.add("Other")
        return list
    }


    /**
     *  This function will return the Dish Cooking Time list items. The time added is in Minutes.
     */
    fun dishCookTime(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }



}