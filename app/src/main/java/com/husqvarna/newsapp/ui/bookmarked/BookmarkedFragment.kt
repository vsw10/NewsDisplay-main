package com.husqvarna.newsapp.ui.bookmarked

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.husqvarna.newsapp.extras.Extras
import com.husqvarna.newsapp.model.NewsArticleDetails
import com.husqvarna.newsapp.ui.adapter.NewsBookmarkedAdapter
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.databinding.FragmentBookmarkedBinding
import io.realm.OrderedRealmCollection
import io.realm.Realm

/**
 * A Bookmarked [Fragment] subclass as to display the bookmarked news.
 */
class BookmarkedFragment : Fragment() {

    val TAG = "BookmarkedFragment"

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }
    private var _binding: FragmentBookmarkedBinding? = null

    lateinit var newsBookmarkedAdapter: NewsBookmarkedAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkedBinding.inflate(inflater, container, false)
        setUiValues()
        initNewsListAdapter()
        return binding.root

    }

    fun setUiValues() {

        activity?.let {
            it.findViewById<AppCompatImageView>(R.id.global_news_toolbar)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatImageView>(R.id.bookmarked_toolbar)?.let {
                it.isVisible = true
                it.isEnabled = false
            }
            it.findViewById<AppCompatImageView>(R.id.search_toolbar)?.let {
                it.isVisible = true
                it.isEnabled = false
            }
            it.findViewById<AppCompatEditText>(R.id.search_edit_text)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatTextView>(R.id.bookmarked_title)?.let {
                it.isVisible = true
            }
        }

    }

    fun initNewsListAdapter() {

        val number: Int? = 1

        val newsList = realm.where(NewsArticleDetails::class.java)
            .equalTo(Extras.IS_BOOKMARKED, number)
            .findAllAsync()

        newsBookmarkedAdapter = NewsBookmarkedAdapter(
            requireContext(),
            newsList as OrderedRealmCollection<NewsArticleDetails>
        )

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsBookmarkedAdapter
        }
        with(binding.newsRecyclerView.itemAnimator) {
            if (this is SimpleItemAnimator) {
                this.supportsChangeAnimations = true
            }
        }

        //setUpView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}