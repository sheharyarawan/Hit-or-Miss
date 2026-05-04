package com.example.hitormiss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.hitormiss.R
import com.example.hitormiss.databinding.FragmentHomeScreenBinding
import com.example.hitormiss.utils.engine.StatCategory

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeScreenBinding.bind(view)

        setupClicks()
    }

    private fun setupClicks() {

        binding.cardViewRuns.setOnClickListener {
            openGame(StatCategory.RUNS)
        }

        binding.cardViewSixes.setOnClickListener {
            openGame(StatCategory.SIXES)
        }

        binding.cardViewFours.setOnClickListener {
            openGame(StatCategory.FOURS)
        }

        binding.cardViewWickets.setOnClickListener {
            openGame(StatCategory.WICKETS)
        }

        binding.cardViewSR.setOnClickListener {
            openGame(StatCategory.STRIKE_RATE)
        }

        binding.cardViewEconomy.setOnClickListener {
            openGame(StatCategory.ECONOMY)
        }
    }

    private fun openGame(category: StatCategory) {

        val bundle = Bundle().apply {
            putString("category", category.name)
        }

        /*        findNavController().navigate(
            R.id.action_homeFragment_to_gameFragment,
            bundle
        )*/
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}