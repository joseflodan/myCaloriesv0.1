package com.example.app.model

import com.google.gson.annotations.SerializedName


data class FoodResult (
    @SerializedName("product") var product: Product? = Product()
)


data class Product(
    @SerializedName("brands") var brands: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("product_name") var productName: String? = null,
    @SerializedName("nutriments") var nutriments: Nutriments? = Nutriments()
)


data class Nutriments(
    @SerializedName("energy-kcal") var energyKcal: Double? = null
)