package com.sandeveloper.jsscolab.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sandeveloper.jsscolab.domain.Interfaces.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
   // val repository: Repository,
application: Application
) :AndroidViewModel(application) {

    companion object {
        val TAG = "StartScreenViewModel"
    }

}