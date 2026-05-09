package com.example.hitormiss.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hitormiss.R
import com.example.hitormiss.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultBinding.bind(view)

        binding.resultToolbar.title = "Result"
        binding.resultToolbar.setNavigationIcon(R.drawable.hm_logo)

        val correct = arguments?.getInt("correct") ?: 0
        val total = arguments?.getInt("total") ?: 0
        val xp = arguments?.getInt("xp") ?: 0
        val format = arguments?.getString("format")

        binding.accuracyValue.text = "$correct/$total"
        binding.xpValue.text = "XP gained: $xp"

        binding.backToHomeButton.setOnClickListener {

            findNavController().navigate(R.id.action_resultFragment_to_formatFragment)
        }

        binding.playAgainButton.setOnClickListener {
            val b = Bundle().apply { format?.let { putString("format", it) } }
            findNavController().navigate(R.id.homeScreenFragment, b)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

