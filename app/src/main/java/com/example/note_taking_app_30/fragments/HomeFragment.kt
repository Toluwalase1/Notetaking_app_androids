package com.example.note_taking_app_30.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Query
import com.example.note_taking_app_30.MainActivity
import com.example.note_taking_app_30.R
import com.example.note_taking_app_30.adapter.NoteAdapter
import com.example.note_taking_app_30.databinding.FragmentHomeBinding
import com.example.note_taking_app_30.model.Note
import com.example.note_taking_app_30.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var notesAdapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

//       notesViewModel = (activity as MainActivity).noteViewModel
        notesViewModel = ( requireActivity() as MainActivity).noteViewModel
        setupHomeRecyclerView()

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment2_to_addNoteFragment2)
        }
    }

    private fun updateUI(note: List<Note>?){
        if(note != null){
            if(note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        notesAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }

        activity?.let{
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){ note ->
                notesAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private  fun searchNote(query: String?){
        val searchQuery = "%$query"

//        notesViewModel.searchNote(searchQuery).observe(this) { list ->
//            notesAdapter.differ.submitList(list)
//
//        }

        notesViewModel.searchNote(searchQuery).observe(viewLifecycleOwner) { list ->
            notesAdapter.differ.submitList(list)
            updateUI(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            searchNote(newText)
        } else {
            // go back to showing all notes
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
                notesAdapter.differ.submitList(notes)
                updateUI(notes)
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView

        menuSearch.isSubmitButtonEnabled = false

        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
       return false
    }

}