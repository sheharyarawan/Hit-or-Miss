package com.example.hitormiss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.hitormiss.R
import com.example.hitormiss.databinding.FragmentHomeScreenBinding
import com.example.hitormiss.utils.engine.StatCategory

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private var format: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeScreenBinding.bind(view)
        format = arguments?.getString("format")

        setupClicks()
    }

    private fun setupClicks() {

        binding.cardViewRuns.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.RUNS) }
        }

        binding.cardViewSixes.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.SIXES) }
        }

        binding.cardViewFours.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.FOURS) }
        }

        binding.cardViewWickets.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.WICKETS) }
        }

        binding.cardViewSR.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.STRIKE_RATE) }
        }

        binding.cardViewEconomy.setOnClickListener { v ->
            popAndNavigate(v) { openGame(StatCategory.ECONOMY) }
        }
    }

    private fun popAndNavigate(view: View, navigate: () -> Unit) {
        // small "pop" animation, then navigate
        view.animate()
            .scaleX(0.96f)
            .scaleY(0.96f)
            .setDuration(80)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(120)
                    .withEndAction { navigate() }
                    .start()
            }
            .start()
    }

    private fun openGame(category: StatCategory) {

        val bundle = Bundle().apply {
            putString("category", category.name)
            format?.let { putString("format", it) }
        }

        findNavController().navigate(
            R.id.action_homeScreenFragment_to_gameFragment,
            bundle
        )
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}