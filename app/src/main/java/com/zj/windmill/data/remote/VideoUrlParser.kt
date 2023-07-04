package com.zj.windmill.data.remote

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.annotation.UiThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class VideoUrlParser(
    private val activity: ComponentActivity
) : DefaultLifecycleObserver {

    private var currentContinuation: CancellableContinuation<String?>? = null

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            val requestUrl = request.url.toString()
            if (requestUrl.contains("m3u8")) {
                if (currentContinuation?.isActive == true) {
                    currentContinuation?.resume(requestUrl)
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val webView = WebView(activity).apply {
        settings.apply {
            blockNetworkImage = true
            javaScriptEnabled = true
            domStorageEnabled = true
        }
    }

    private fun init() {
        activity.lifecycle.addObserver(this)
        webView.webViewClient = webViewClient
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        val layoutParams = ViewGroup.LayoutParams(0, 0)
        content.addView(webView, layoutParams)
    }

    init {
        init()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        webView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        webView.onPause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        content.removeView(webView)
        super.onDestroy(owner)
    }

    @UiThread
    @SuppressLint("SetJavaScriptEnabled")
    suspend fun parseVideoUrl(playPageUrl: String) = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            if (continuation.isActive) {
                continuation.resume(null)
            }
        }
        currentContinuation = continuation
        webView.loadUrl(playPageUrl)
    }
}