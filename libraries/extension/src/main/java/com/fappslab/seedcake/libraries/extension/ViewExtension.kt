package com.fappslab.seedcake.libraries.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Typeface.BOLD
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.min

private const val HIGHLIGHT_LENGTH = 7
const val METADATA_TAG = ":END"

fun ImageView.setTint(@ColorRes colorRes: Int) {
    setColorFilter(context.getColorRes(colorRes))
}

fun Context.getColorRes(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getDrawableRes(@DrawableRes drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableRes)
}

fun Context.isDarkModeActivated(): Boolean {
    return resources.configuration.uiMode and
            UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun <T : View> T.postView(postBlock: T.() -> Unit) {
    post { postBlock() }
}

fun Context.toHighlightBothEndsBeforeMetadata(encryptedSeed: String): SpannableString {
    fun calculateEndHighlightStart(metadataIndex: Int): Int {
        return if (metadataIndex != -1) {
            metadataIndex - HIGHLIGHT_LENGTH
        } else encryptedSeed.length - HIGHLIGHT_LENGTH
    }

    fun shouldApplyEndHighlight(metadataIndex: Int, endHighlightStart: Int): Boolean {
        return endHighlightStart > HIGHLIGHT_LENGTH && metadataIndex != -1
    }

    fun applyStyle(spannable: SpannableString, start: Int, end: Int) {
        val colorRes = getColorRes(R.color.ebcrypted_highlightr)
        spannable.setSpan(StyleSpan(BOLD), start, end, SPAN_INCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(colorRes), start, end, SPAN_INCLUSIVE_EXCLUSIVE)
    }

    val metadataIndex = encryptedSeed.indexOf(METADATA_TAG)
    val endHighlightStart = calculateEndHighlightStart(metadataIndex)

    val spannable = SpannableString(encryptedSeed)

    applyStyle(spannable, start = 0, min(HIGHLIGHT_LENGTH, encryptedSeed.length))

    if (shouldApplyEndHighlight(metadataIndex, endHighlightStart)) {
        applyStyle(spannable, endHighlightStart, metadataIndex)
    }

    if (metadataIndex != -1) {
        val colorRes = getColorRes(R.color.metadata_color)
        val alphaSpan = ForegroundColorSpan(colorRes)
        spannable.setSpan(alphaSpan, metadataIndex, encryptedSeed.length, SPAN_INCLUSIVE_EXCLUSIVE)
    }

    return spannable
}

fun View.hideKeyboard() {
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    im?.hideSoftInputFromWindow(windowToken, 0)
}

fun SearchView.setOnQueryTextListener(submitAction: (query: String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean = false
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let(submitAction::invoke)
            return true
        }
    })
}

fun TextInputLayout.inputError(@StringRes errorRes: Int?) {
    error = errorRes?.let { context.getString(it) }
}

fun View.gone() {
    isVisible = false
}

fun View.show() {
    isVisible = true
}

fun View.invisible() {
    isInvisible = true
}

fun Context.gotoPlayStore(appId: String = "com.fappslab.seedcake") {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=${appId}")
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$appId")
        startActivity(intent)
    }
}

fun Context.openInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    runCatching {
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
