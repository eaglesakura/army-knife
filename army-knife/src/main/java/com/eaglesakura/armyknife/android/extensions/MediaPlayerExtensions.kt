package com.eaglesakura.armyknife.android.extensions

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.media.AudioAttributesCompat


/**
 * Set attributes with appcompat.
 *
 * Sets the audio attributes for this MediaPlayer.
 * See {@link AudioAttributes} for how to build and configure an instance of this class.
 * You must call this method before {@link #prepare()} or {@link #prepareAsync()} in order
 * for the audio attributes to become effective thereafter.
 * @param attributes a non-null set of audio attributes
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun MediaPlayer.setAudioAttributes(attributes: AudioAttributesCompat) {
    this.setAudioAttributes(AudioAttributes.Builder().also { builder ->
        builder.setUsage(attributes.usage)
        builder.setLegacyStreamType(attributes.legacyStreamType)
        builder.setFlags(attributes.flags)
        builder.setContentType(attributes.contentType)
    }.build())
}