package com.example.hw_03_m7.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model")
data class MedicinesModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int ?= null,
    var title: String,
    var description: String,
    var time: String,
    var quantity: Int,
    var date: String? = null,
)
