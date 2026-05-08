package com.example.hitormiss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.cardview.widget.CardView
import com.example.hitormiss.R

class FormatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_format, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardT20 = view.findViewById<CardView>(R.id.formatCardT20)
        val cardOdi = view.findViewById<CardView>(R.id.formatCardOdi)
        val cardTest = view.findViewById<CardView>(R.id.formatCardTest)

        fun go(format: String) {
            val b = Bundle().apply {
                putString("format", format)
            }
            findNavController().navigate(R.id.action_formatFragment_to_homeScreenFragment, b)
        }

        cardT20.setOnClickListener { go("t20i") }
        cardOdi.setOnClickListener { go("odi") }
        cardTest.setOnClickListener { go("test") }
    }
}