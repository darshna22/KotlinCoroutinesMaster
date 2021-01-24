package com.example.kotlincorotines.di.entities

data class TypeCodeResponse(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)