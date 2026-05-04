package com.example.hitormiss.ui.game

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hitormiss.R
import com.example.hitormiss.data.viewmodel.GameViewModel
import com.example.hitormiss.databinding.FragmentGameBinding
import com.example.hitormiss.utils.engine.StatCategory

class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private var selectedSide: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGameBinding.bind(view)

        val categoryString = arguments?.getString("category")
        val category = StatCategory.valueOf(categoryString!!)

        setupClicks()
        observeState()

        viewModel.startGame(category)
    }

    private fun setupClicks() {

        binding.cardViewSelectA.setOnClickListener {
            selectedSide = "A"
        }

        binding.cardViewSelectB.setOnClickListener {
            selectedSide = "B"
        }

        binding.confirmButton.setOnClickListener {

            val question = viewModel.state.value?.question ?: return@setOnClickListener

            val selectedPlayer =
                if (selectedSide == "A") question.playerA else question.playerB

            val isCorrect = checkAnswer(question, selectedSide!!)

            viewModel.updateScore(isCorrect)

            showResult(isCorrect)

            binding.root.postDelayed({
                viewModel.nextQuestion()
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

    private fun checkAnswer(question: com.example.hitormiss.utils.engine.GameQuestion, selected: String): Boolean {

        val a = question.playerAStats
        val b = question.playerBStats

        val correct = when (question.category) {

            StatCategory.RUNS -> a.runs > b.runs
            StatCategory.SIXES -> a.sixes > b.sixes
            StatCategory.FOURS -> a.fours > b.fours
            StatCategory.WICKETS -> a.wickets > b.wickets
            StatCategory.STRIKE_RATE -> a.strikeRate > b.strikeRate
            StatCategory.ECONOMY -> a.economy < b.economy
        }

        return if (selected == "A") correct else !correct
    }

    private fun showResult(correct: Boolean) {

        val color = if (correct) "#4CAF50" else "#F44336"

        binding.cardViewSelectA.setCardBackgroundColor(Color.parseColor(color))
        binding.cardViewSelectB.setCardBackgroundColor(Color.parseColor(color))
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