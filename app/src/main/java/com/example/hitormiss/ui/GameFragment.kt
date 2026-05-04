package com.example.hitormiss.ui.game

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hitormiss.R
import com.example.hitormiss.data.repository.PlayerRepository
import com.example.hitormiss.data.viewmodel.GameViewModel
import com.example.hitormiss.data.viewmodel.GameViewModelFactory
import com.example.hitormiss.databinding.FragmentGameBinding
import com.example.hitormiss.utils.engine.GameEngine
import com.example.hitormiss.utils.engine.StatCategory

class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var selectedSide: String? = null

    // ⚠️ TEMP: replace with DI later (Hilt recommended)
    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(
            GameEngine(
                PlayerRepository(
                    api = (requireActivity().application as MyApp).api,
                    playerDao = (requireActivity().application as MyApp).playerDao,
                    statsDao = (requireActivity().application as MyApp).statsDao
                )
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGameBinding.bind(view)

        val categoryString = arguments?.getString("category")
        val category = categoryString?.let {
            StatCategory.valueOf(it)
        } ?: return

        setupClicks(category)
        observeState()

        viewModel.startGame(category)
    }

    private fun setupClicks(category: StatCategory) {

        binding.cardViewSelectA.setOnClickListener {
            selectedSide = "A"
        }

        binding.cardViewSelectB.setOnClickListener {
            selectedSide = "B"
        }

        binding.confirmButton.setOnClickListener {

            val state = viewModel.state.value ?: return@setOnClickListener
            val question = state.question ?: return@setOnClickListener

            if (selectedSide == null) return@setOnClickListener

            val isCorrect = viewModel.checkAnswer(
                question,
                selectedSide!!
            )

            viewModel.updateScore(isCorrect)

            showResult(isCorrect, selectedSide!!)

            binding.root.postDelayed({
                viewModel.loadQuestion(category)
                resetUI()
            }, 1000)
        }
    }

    private fun observeState() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            binding.progressBar.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            state.question?.let { q ->

                binding.questionTV.text = q.questionText

                binding.selectTvA.text = q.playerA.name
                binding.selectTvB.text = q.playerB.name

                binding.chipPlayerNameA.text = q.playerA.name
                binding.chipPlayerNameB.text = q.playerB.name

                binding.chipCountryA.text = q.playerA.country
                binding.chipCountryB.text = q.playerB.country
            }
        }
    }

    private fun showResult(correct: Boolean, selected: String) {

        val correctColor = Color.parseColor("#4CAF50")
        val wrongColor = Color.parseColor("#F44336")

        if (correct) {
            if (selected == "A") {
                binding.cardViewSelectA.setCardBackgroundColor(correctColor)
            } else {
                binding.cardViewSelectB.setCardBackgroundColor(correctColor)
            }
        } else {
            if (selected == "A") {
                binding.cardViewSelectA.setCardBackgroundColor(wrongColor)
            } else {
                binding.cardViewSelectB.setCardBackgroundColor(wrongColor)
            }
        }
    }

    private fun resetUI() {
        binding.cardViewSelectA.setCardBackgroundColor(Color.WHITE)
        binding.cardViewSelectB.setCardBackgroundColor(Color.WHITE)
        selectedSide = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}