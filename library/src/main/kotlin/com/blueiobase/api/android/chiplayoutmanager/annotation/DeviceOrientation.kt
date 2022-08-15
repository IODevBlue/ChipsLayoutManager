package com.blueiobase.api.android.chiplayoutmanager.annotation

import android.content.res.Configuration
import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef (Configuration.ORIENTATION_LANDSCAPE, Configuration.ORIENTATION_PORTRAIT, Configuration.ORIENTATION_UNDEFINED)
/**
 * Denotes that the annotated [Int] is one of the constants defined in the [Configuration] class
 * representing a device orientation.
 */
annotation class DeviceOrientation