package com.fappslab.seedcake.libraries.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
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
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun <T : View> T.postView(postBlock: T.() -> Unit) {
    post { postBlock() }
}

fun String.toHighlightFirstFive(): SpannableString {

    fun SpannableString.toStyleSpan(styleSpan: Any) {
        setSpan(styleSpan, 0, min(a = 5, this.length), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    val boldSpan = StyleSpan(Typeface.BOLD)
    val colorSpan = ForegroundColorSpan(Color.BLUE)

    val spannable = SpannableString(this)
    spannable.toStyleSpan(boldSpan)
    spannable.toStyleSpan(colorSpan)

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

fun Context.gotoPlayStore(appId: String? = null) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=${appId ?: packageName}")
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
