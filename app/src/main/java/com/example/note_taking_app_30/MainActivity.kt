package com.example.note_taking_app_30

import NoteViewModelFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.note_taking_app_30.database.NoteDatabase
import com.example.note_taking_app_30.repository.NoteRepository
import com.example.note_taking_app_30.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {


    lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProvider = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProvider)[NoteViewModel::class.java]
    }
}