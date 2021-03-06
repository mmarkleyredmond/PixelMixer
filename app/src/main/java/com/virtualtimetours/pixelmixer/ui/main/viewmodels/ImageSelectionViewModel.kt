package com.virtualtimetours.pixelmixer.ui.main.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pixelmixer.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.virtualtimetours.pixelmixer.PixelMixerApplication
import com.virtualtimetours.pixelmixer.ui.main.fragments.ImageSelectionFragment

class ImageSelectionViewModel : ViewModel(), Target {

    val imageBitmap = MutableLiveData<Bitmap?>(null)
    val attributeButtonVisibility = MutableLiveData(View.GONE)
    val splashPhotographerName = MutableLiveData("")
    val splashPhotoUrl = MutableLiveData("")
    val splashPhotoPublishDate = MutableLiveData("")
    val attributesVisibility = MutableLiveData(View.GONE)
    val progressVisibility = MutableLiveData(View.GONE)
    val gameInfoText = MutableLiveData(PixelMixerApplication.context.getString(R.string.info_text_preload))

    val bitmapLoadError = MutableLiveData(false)

    fun clearLoadError() {
        bitmapLoadError.postValue(false)
    }

    fun setImage(photo: UnsplashPhoto) {
        progressVisibility.postValue(View.VISIBLE)
        bitmapLoadError.postValue(false)
        Picasso.get().load(photo.urls.full).into(this)
        splashPhotographerName.postValue(photo.user.name)
        splashPhotoUrl.postValue(photo.urls.full)
        splashPhotoPublishDate.postValue(photo.created_at)
    }

    /**
     * Callback for Picasso to notify the application that the image has been loaded
     */
    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        progressVisibility.postValue(View.GONE)
        if(null == bitmap) {
            return
        }
        if(bitmap.byteCount > 86936160) {
            bitmapLoadError.postValue(true)
            return
        }
        imageBitmap.postValue(bitmap)
        attributeButtonVisibility.postValue(View.VISIBLE)
        gameInfoText.postValue(PixelMixerApplication.context.getString(R.string.info_text_after_load))
    }

    /**
     * Callback for Picasso to notify the application that the image load failed for some
     * reason.
     */
    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
        progressVisibility.postValue(View.GONE)
        bitmapLoadError.postValue(true)
        Log.i(TAG, "failed to load bitmap ${e?.message}")
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        Log.i(TAG, "prepare to load")
    }

    companion object {
        val TAG: String = ImageSelectionFragment::javaClass.name
    }

    /**
     * Method to display the attributions of the image.
     */
    fun onFABClick() {
        attributesVisibility.postValue(when(attributesVisibility.value) {
            View.VISIBLE -> View.GONE
            else -> View.VISIBLE
        })
    }
}
