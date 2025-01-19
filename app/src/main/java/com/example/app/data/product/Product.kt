package com.example.app.data.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val barcode: String = "",
    val brands: String = "",
    val imageUrl: String = "",
    val productName: String = "",
    val energyKcal: Double = 0.0

){}