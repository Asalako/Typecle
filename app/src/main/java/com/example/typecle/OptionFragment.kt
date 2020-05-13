package com.example.typecle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment

class OptionFragment: Fragment() {

    //opens user guide fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_options, container, false)

        //displays the user guide
        val webView: WebView = root.findViewById(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/index.html")
        return root
    }

}