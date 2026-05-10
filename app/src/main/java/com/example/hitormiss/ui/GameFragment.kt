package com.example.hitormiss.ui.game

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hitormiss.R
import com.example.hitormiss.data.viewmodel.GameViewModel
import com.example.hitormiss.data.viewmodel.GameViewModelFactory
import com.example.hitormiss.databinding.FragmentGameBinding
import com.example.hitormiss.utils.AppDependencies
import com.example.hitormiss.utils.MatchFormat
import com.example.hitormiss.utils.engine.StatCategory
import com.example.hitormiss.utils.toMatchFormatOrNull

class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var selectedSide: String? = null
    private lateinit var category: StatCategory
    private lateinit var format: MatchFormat
    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(AppDependencies.generateQuestionUseCase)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGameBinding.bind(view)

        category = arguments?.getString("category")
            ?.let { StatCategory.valueOf(it) }
            ?: return
        format = arguments?.getString("format").toMatchFormatOrNull() ?: MatchFormat.TEST
        binding.gameToolbar.title = format.name
        binding.gameToolbar.subtitle = ""
        binding.gameToolbar.setNavigationIcon(R.drawable.hm_logo)

        setupClicks()
        observeState()

        viewModel.startGame(category, format)
    }

    private fun categoryLabel(category: StatCategory): String =
        when (category) {
            StatCategory.RUNS -> "Runs"
            StatCategory.SIXES -> "Sixes"
            StatCategory.FOURS -> "Fours"
            StatCategory.WICKETS -> "Wickets"
            StatCategory.STRIKE_RATE -> "Strike Rate"
            StatCategory.ECONOMY -> "Economy"
        }

    private fun setupClicks() {

        binding.cardViewSelectA.setOnClickListener {
            selectedSide = "A"
            highlightSelection("A")
            showCurrentStatsToast("A")
        }

        binding.cardViewSelectB.setOnClickListener {
            selectedSide = "B"
            highlightSelection("B")
            showCurrentStatsToast("B")
        }

        binding.confirmButton.setOnClickListener {

            val state = viewModel.state.value ?: return@setOnClickListener
            if (state.isLoading) return@setOnClickListener
            val question = state.question ?: return@setOnClickListener
            val selected = selectedSide ?: return@setOnClickListener

            val isCorrect = viewModel.checkAnswer(question, selected)
            viewModel.updateScore(isCorrect)
            showResult(isCorrect, selected)

            binding.root.postDelayed({
                val updated = viewModel.state.value
                if (updated != null && updated.wrongCount >= 2) {
                    val b = Bundle().apply {
                        putInt("correct", updated.correctAnswered)
                        putInt("total", updated.totalAnswered)
                        putInt("xp", updated.xp)
                        putString("format", arguments?.getString("format"))
                    }
                    findNavController().navigate(R.id.action_gameFragment_to_resultFragment, b)
                } else {
                    viewModel.nextQuestion()
                    resetUI()
                }
            }, 1000)
        }
    }

    private fun observeState() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            updateLives(state.wrongCount)

            state.question?.let { q ->

                binding.questionTV.text = q.questionText

                binding.selectTvA.text = q.playerA.name
                binding.selectTvB.text = q.playerB.name

                binding.chipPlayerNameA.text = q.playerA.name
                binding.chipPlayerNameB.text = q.playerB.name

                binding.chipCountryA.text = q.playerA.country
                binding.chipCountryB.text = q.playerB.country

                Glide.with(requireContext())
                    .load(q.playerA.imageUrl)
                    .into(binding.playerImageA)

                Glide.with(requireContext())
                    .load(q.playerB.imageUrl)
                    .into(binding.imgPlayerB)
            }

            state.error?.let { }
        }
    }

    private fun updateLives(wrongCount: Int) {

        binding.lifeStar1.setImageResource(
            if (wrongCount >= 2) android.R.drawable.btn_star_big_off else android.R.drawable.btn_star_big_on
        )
        binding.lifeStar2.setImageResource(
            if (wrongCount >= 1) android.R.drawable.btn_star_big_off else android.R.drawable.btn_star_big_on
        )
    }

    private fun highlightSelection(side: String) {
        clearSelectionBorders()
        val selectedColor = Color.parseColor("#2196F3")
        if (side == "A") {
            binding.cardViewSelectA.strokeColor = selectedColor
        } else {
            binding.cardViewSelectB.strokeColor = selectedColor
        }
    }

    private fun showResult(correct: Boolean, selected: String) {

        val correctColor = Color.parseColor("#4CAF50")
        val wrongColor = Color.parseColor("#F44336")

        val color = if (correct) correctColor else wrongColor
        if (selected == "A") {
            binding.cardViewSelectA.strokeColor = color
        } else {
            binding.cardViewSelectB.strokeColor = color
        }
    }

    private fun resetUI() {
        clearSelectionBorders()
        selectedSide = null
    }

    private fun clearSelectionBorders() {
        val defaultStroke = Color.parseColor("#22000000")
        binding.cardViewSelectA.strokeColor = defaultStroke
        binding.cardViewSelectB.strokeColor = defaultStroke
    }

    private fun showCurrentStatsToast(selected: String) {
        val question = viewModel.state.value?.question ?: return

        val (aStat, bStat, label) = when (question.category) {
            StatCategory.RUNS -> Triple(
                getFormatRuns(question.playerAStats),
                getFormatRuns(question.playerBStats),
                "${format.name} Runs"
            )

            StatCategory.SIXES -> Triple(
                getFormatSixes(question.playerAStats),
                getFormatSixes(question.playerBStats),
                "${format.name} Sixes"
            )

            StatCategory.FOURS -> Triple(
                getFormatFours(question.playerAStats),
                getFormatFours(question.playerBStats),
                "${format.name} Fours"
            )

            StatCategory.WICKETS -> Triple(
                getFormatWickets(question.playerAStats),
                getFormatWickets(question.playerBStats),
                "${format.name} Wickets"
            )

            StatCategory.STRIKE_RATE -> Triple(
                getFormatStrikeRate(question.playerAStats),
                getFormatStrikeRate(question.playerBStats),
                "${format.name} Strike Rate"
            )

            StatCategory.ECONOMY -> Triple(
                getFormatEconomy(question.playerAStats),
                getFormatEconomy(question.playerBStats),
                "${format.name} Economy"
            )
        }

        val message = "A: $aStat\nB: $bStat\n($label)"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getFormatRuns(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testRuns.toString()
            MatchFormat.ODI -> s.odiRuns.toString()
            MatchFormat.T20I -> s.t20iRuns.toString()
        }

    private fun getFormatFours(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testFours.toString()
            MatchFormat.ODI -> s.odiFours.toString()
            MatchFormat.T20I -> s.t20iFours.toString()
        }

    private fun getFormatSixes(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testSixes.toString()
            MatchFormat.ODI -> s.odiSixes.toString()
            MatchFormat.T20I -> s.t20iSixes.toString()
        }

    private fun getFormatWickets(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testWickets.toString()
            MatchFormat.ODI -> s.odiWickets.toString()
            MatchFormat.T20I -> s.t20iWickets.toString()
        }

    private fun getFormatStrikeRate(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testStrikeRate.toString()
            MatchFormat.ODI -> s.odiStrikeRate.toString()
            MatchFormat.T20I -> s.t20iStrikeRate.toString()
        }

    private fun getFormatEconomy(s: com.example.hitormiss.data.entity.PlayerStatsSummary): String =
        when (format) {
            MatchFormat.TEST -> s.testEconomy.toString()
            MatchFormat.ODI -> s.odiEconomy.toString()
            MatchFormat.T20I -> s.t20iEconomy.toString()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}