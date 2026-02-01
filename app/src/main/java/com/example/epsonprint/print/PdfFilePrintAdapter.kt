package com.example.epsonprint.print

import android.content.Context
import android.net.Uri
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.InputStream
import java.io.OutputStream

// Pass-through PDF to Android print framework
class PdfFilePrintAdapter(
    private val context: Context,
    private val uri: Uri
) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: android.print.PrintAttributes?,
        newAttributes: android.print.PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: android.os.Bundle?
    ) {
        if (cancellationSignal?.isCanceled == true) {
            callback?.onLayoutCancelled()
            return
        }
        val info = PrintDocumentInfo.Builder("document.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .build()
        callback?.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        if (destination == null) {
            callback?.onWriteFailed("No destination")
            return
        }
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = context.contentResolver.openInputStream(uri)
            output = ParcelFileDescriptor.AutoCloseOutputStream(destination)
            if (input == null) {
                callback?.onWriteFailed("Failed to open source")
                return
            }
            val buf = ByteArray(8192)
            while (true) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onWriteCancelled()
                    return
                }
                val read = input.read(buf)
                if (read <= 0) break
                output.write(buf, 0, read)
            }
            output.flush()
            callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: Exception) {
            callback?.onWriteFailed(e.message)
        } finally {
            try { input?.close() } catch (_: Exception) {}
            try { output?.close() } catch (_: Exception) {}
        }
    }
}
