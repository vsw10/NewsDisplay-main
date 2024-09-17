package com.husqvarna.newsapp.ui.home

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.husqvarna.newsapp.logs.NDLogs
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.databinding.FragmentHomedetailsBinding

/**
 * A HomeDetails [Fragment] subclass as to display the details of the top headlines.
 */
class HomeDetailsFragment : Fragment() {

    private val TAG = "HomeDetailsFragment"

    private var _binding: FragmentHomedetailsBinding? = null

    private lateinit var urlLink: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomedetailsBinding.inflate(inflater, container, false)
        urlLink = arguments?.getString("url").toString()
        setUiValues()
        initialiseWebView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NDLogs.debug(TAG, " onViewCreated ${arguments?.get("url")} ")
    }

    fun setUiValues() {
        activity?.let {
            it.findViewById<AppCompatImageView>(R.id.global_news_toolbar)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatImageView>(R.id.bookmarked_toolbar)?.let {
                it.isGone = true
                it.isEnabled = true
            }
            it.findViewById<AppCompatImageView>(R.id.search_toolbar)?.let {
                it.isGone = true
                it.isEnabled = true
            }
            it.findViewById<AppCompatEditText>(R.id.search_edit_text)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatTextView>(R.id.bookmarked_title)?.let {
                it.isGone = true
            }
            it.findViewById<AppCompatTextView>(R.id.web_view_url)?.let {
                it.isVisible = true
                it.text = urlLink
            }
        }
    }

    fun initialiseWebView() {
        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(urlLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}