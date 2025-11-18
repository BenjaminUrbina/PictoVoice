package com.example.saacapp.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.saacapp.adapter.PictogramAdapter
import com.example.saacapp.data.PictogramRepository
import com.example.saacapp.databinding.FragmentPictogramBinding

class PictogramFragment : Fragment() {

    private var _binding: FragmentPictogramBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictogramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pictograms = PictogramRepository.loadPictograms(requireContext())
        val adapter = PictogramAdapter(pictograms)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
