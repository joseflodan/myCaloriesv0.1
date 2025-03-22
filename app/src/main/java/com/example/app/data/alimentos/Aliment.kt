package com.example.app.data.alimentos

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alimentos")
class Aliment (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id : Int,
    @NonNull
    @ColumnInfo(name = "nombre")
    val nombre : String,
    @NonNull
    @ColumnInfo(name = "Kcal")
    val Kcal : Int,
    @NonNull
    @ColumnInfo(name = "porcion")
    val porcion : String
    )