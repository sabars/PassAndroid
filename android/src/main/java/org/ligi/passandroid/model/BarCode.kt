package org.ligi.passandroid.model

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import com.google.zxing.BarcodeFormat
import org.ligi.passandroid.App
import org.ligi.passandroid.helper.BarcodeHelper
import org.ligi.tracedroid.logging.Log
import java.util.*

class BarCode(val format: BarcodeFormat?, val message: String?) {

    var alternativeText: String? = null

    fun getBitmap(resources: Resources): BitmapDrawable? {
        if (message == null) {
            // no message -> no barcode
            App.component().tracker().trackException("No Barcode in pass - strange", false)
            return null
        }

        if (format == null) {
            Log.w("Barcode format is null - fallback to QR")
            App.component().tracker().trackException("Barcode format is null - fallback to QR", false)
            return BarcodeHelper.generateBitmapDrawable(resources, message, BarcodeFormat.QR_CODE)
        }

        return BarcodeHelper.generateBitmapDrawable(resources, message, format)

    }

    companion object {

        fun getFormatFromString(format: String): BarcodeFormat {
            if (format.contains("417")) {
                return BarcodeFormat.PDF_417
            }

            if (format.toUpperCase(Locale.ENGLISH).contains("AZTEC")) {
                return BarcodeFormat.AZTEC
            }


            if (format.toUpperCase(Locale.ENGLISH).contains("128")) {
                return BarcodeFormat.CODE_128
            }

            /*
        requested but not supported by xing (yet)
        https://github.com/ligi/PassAndroid/issues/43

        if (format.toUpperCase(Locale.ENGLISH).contains("93")) {
            return BarcodeFormat.CODE_93;
        }

        */
            if (format.toUpperCase(Locale.ENGLISH).contains("39")) {
                return BarcodeFormat.CODE_39
            }

            return BarcodeFormat.QR_CODE

        }
    }

}
