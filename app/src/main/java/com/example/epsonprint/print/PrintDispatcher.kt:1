package com.example.epsonprint.print

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.print.PrintHelper

object PrintDispatcher {

    fun print(context: Context, uri: Uri) {
        val mime = tryGetMime(context.contentResolver, uri)
        if (mime != null && mime.startsWith("image/")) {
            printImage(context, uri)
        } else if (mime == "application/pdf") {
            printPdf(context, uri)
        } else {
            printPdf(context, uri)
        }
    }

    private fun printImage(context: Context, uri: Uri) {
        val helper = PrintHelper(context)
        helper.scaleMode = PrintHelper.SCALE_MODE_FIT
        helper.colorMode = PrintHelper.COLOR_MODE_COLOR
        helper.orientation = PrintHelper.ORIENTATION_AUTO
        helper.printBitmap("Image", uri)
    }

    private fun printPdf(context: Context, uri: Uri) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val attrs = PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .build()
        val adapter = PdfFilePrintAdapter(context, uri)
        printManager.print("Document", adapter, attrs)
    }

    private fun tryGetMime(cr: ContentResolver, uri: Uri): String? {
        return try { cr.getType(uri) } catch (_: Exception) { null }
    }
}
