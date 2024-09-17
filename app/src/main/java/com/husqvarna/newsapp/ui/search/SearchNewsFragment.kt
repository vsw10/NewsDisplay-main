package com.husqvarna.newsapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.databinding.FragmentSearchBinding

/**
 * A SearchNews [Fragment] subclass as to display the search screen and search the news.
 */
class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUiValues()
        return binding.root

    }

    fun setUiValues() {

        activity?.let {
            it.findViewById<AppCompatImageView>(R.id.global_news_toolbar)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatImageView>(R.id.bookmarked_toolbar)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatImageView>(R.id.search_toolbar)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatEditText>(R.id.search_edit_text)?.let {
                it.isVisible = true
            }
            it.findViewById<AppCompatTextView>(R.id.bookmarked_title)?.let {
                it.isGone = true
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}