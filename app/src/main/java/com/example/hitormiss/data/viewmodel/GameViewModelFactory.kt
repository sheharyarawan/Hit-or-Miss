package com.example.hitormiss.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hitormiss.domain.usecase.GenerateQuestionUseCase

class GameViewModelFactory(
    private val useCase: GenerateQuestionUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(useCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}