package com.zj.windmill.data.remote

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class VideoUrlParser(
    private val activity: AppCompatActivity
) : DefaultLifecycleObserver {

    private var currentContinuation: CancellableContinuation<Result<String>>? = null

    private var currentPageUrl: String? = null

    private val webViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            val requestUrl = request.url.toString()
            if (requestUrl.contains("m3u8")) {
                val videoUrl = requestUrl.split("url=").getOrNull(1)
                if (currentContinuation?.isActive == true) {
                    if (videoUrl.isNullOrBlank()) {
                        currentContinuation?.resume(Result.failure(Throwable("解析失败")))
                    } else {
                        currentContinuation?.resume(Result.success(videoUrl))
                    }
                }
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            if (request.url.toString() == currentPageUrl && currentContinuation?.isActive == true) {
                currentContinuation?.resume(Result.failure(Throwable("解析失败")))
            }
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            if (request.url.toString() == currentPageUrl && currentContinuation?.isActive == true) {
                currentContinuation?.resume(Result.failure(Throwable("解析失败")))
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val webView = WebView(activity).apply {
        settings.apply {
            blockNetworkImage = true
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        webViewClient = this@VideoUrlParser.webViewClient
    }

    init {
        activity.lifecycle.addObserver(this)
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        val layoutParams = ViewGroup.LayoutParams(0, 0)
        content.addView(webView, layoutParams)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        content.removeView(webView)
        super.onDestroy(owner)
    }

    @UiThread
    @SuppressLint("SetJavaScriptEnabled")
    suspend fun parseVideoUrl(playPageUrl: String): Result<String> =
        suspendCancellableCoroutine { continuation ->
            currentPageUrl = playPageUrl
            currentContinuation?.cancel()
            currentContinuation = continuation

            continuation.invokeOnCancellation {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(Throwable("解析取消")))
                }
            }
            webView.loadUrl(playPageUrl)
        }
}