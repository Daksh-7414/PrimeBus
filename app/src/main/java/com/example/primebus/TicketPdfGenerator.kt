package com.example.primebus

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object TicketPdfGenerator {

    fun generateAndSave(context: Context, bus: Bus, booking: Booking): Boolean {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = document.startPage(pageInfo)

            drawTicket(page.canvas, bus, booking)

            document.finishPage(page)
            val fileName = "${bus.source}_to_${bus.destination}_journey_ticket.pdf"
                .replace(" ", "_")
                .lowercase()

            val saved = saveToDownloads(context, document, fileName)
            document.close()
            saved
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun drawTicket(canvas: Canvas, bus: Bus, booking: Booking) {
        val width = 595f
        val margin = 40f

        // Background
        val bgPaint = Paint().apply { color = Color.WHITE }
        canvas.drawRect(0f, 0f, width, 842f, bgPaint)

        // Bus Name
        val titlePaint = Paint().apply {
            color = Color.parseColor("#00236F")
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        canvas.drawText(bus.busName, margin, 60f, titlePaint)

        // Bus type
        val subPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
            isAntiAlias = true
        }
        canvas.drawText(bus.type, margin, 82f, subPaint)

        // CONFIRMED badge
        val badgePaint = Paint().apply {
            color = Color.parseColor("#287145")
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        canvas.drawText("✓ CONFIRMED", width - 140f, 70f, badgePaint)

        // Divider line
        val linePaint = Paint().apply { color = Color.LTGRAY; strokeWidth = 1f }
        canvas.drawLine(margin, 100f, width - margin, 100f, linePaint)

        // Route: FROM to TO
        val labelPaint = Paint().apply {
            color = Color.GRAY; textSize = 11f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }
        val valuePaint = Paint().apply {
            color = Color.DKGRAY; textSize = 18f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }

        canvas.drawText("FROM", margin, 130f, labelPaint)
        canvas.drawText(bus.source, margin, 155f, valuePaint)

        canvas.drawText("TO", width - margin - 80f, 130f, labelPaint)
        canvas.drawText(bus.destination, width - margin - 80f, 155f, valuePaint)

        // Date
        val dateStr = booking.journeyDate?.let {
            SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .format(Date(it))
        } ?: "N/A"

        val datePaint = Paint().apply {
            color = Color.BLACK; textSize = 13f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }
        canvas.drawText("Date: $dateStr", margin, 185f, datePaint)

        // Departure / Duration / Arrival
        canvas.drawText("DEPARTURE", margin, 225f, labelPaint)
        canvas.drawText(bus.departureTime, margin, 248f, valuePaint)

        val centerX = width / 2
        canvas.drawText("DURATION", centerX - 35f, 225f, labelPaint)
        canvas.drawText(bus.duration, centerX - 25f, 248f, subPaint.apply { textSize = 15f; typeface = Typeface.DEFAULT_BOLD })

        canvas.drawText("ARRIVAL", width - margin - 80f, 225f, labelPaint)
        val arrivalPaint = Paint().apply {
            color = Color.parseColor("#712AE2"); textSize = 18f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }
        canvas.drawText(bus.arrivalTime, width - margin - 80f, 248f, arrivalPaint)

        // Divider
        canvas.drawLine(margin, 270f, width - margin, 270f, linePaint)

        // Boarding / Dropping
        canvas.drawText("BOARDING POINT", margin, 295f, labelPaint)
        canvas.drawText(bus.boardingPoint, margin, 315f, subPaint.apply { textSize = 14f; color = Color.BLACK })

        canvas.drawText("DROPPING POINT", margin, 345f, labelPaint)
        canvas.drawText(bus.droppingPoint, margin, 365f, subPaint)

        // Divider
        canvas.drawLine(margin, 385f, width - margin, 385f, linePaint)

        // Passengers
        val sectionPaint = Paint().apply {
            color = Color.BLACK; textSize = 16f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }
        canvas.drawText("Passenger Details", margin, 410f, sectionPaint)

        var yPos = 435f
        booking.passengers.forEachIndexed { i, p ->
            canvas.drawText("${i + 1}. ${p.name}  |  ${p.gender}, ${p.age} Yrs  |  Seat: ${p.seatNumber}", margin, yPos, subPaint.apply { color = Color.DKGRAY; textSize = 13f })
            yPos += 22f
        }

        // Divider
        canvas.drawLine(margin, yPos + 10f, width - margin, yPos + 10f, linePaint)
        yPos += 30f

        // Fare Summary
        canvas.drawText("Fare Summary", margin, yPos, sectionPaint)
        yPos += 25f

        val gst = com.example.primebus.core.utils.Constants.gst
        val convFee = com.example.primebus.core.utils.Constants.convenienceFee
        val baseFare = booking.totalAmount - gst - convFee

        canvas.drawText("Base Fare:         ₹$baseFare", margin, yPos, subPaint.apply { color = Color.DKGRAY; textSize = 14f })
        yPos += 22f
        canvas.drawText("GST & Taxes:      ₹$gst", margin, yPos, subPaint)
        yPos += 22f
        canvas.drawText("Convenience Fee: ₹$convFee", margin, yPos, subPaint)
        yPos += 10f
        canvas.drawLine(margin, yPos + 5f, width - margin, yPos + 5f, linePaint)
        yPos += 22f

        val totalPaint = Paint().apply {
            color = Color.parseColor("#3F51B5"); textSize = 16f; typeface = Typeface.DEFAULT_BOLD; isAntiAlias = true
        }
        canvas.drawText("Total Paid:         ₹${booking.totalAmount}", margin, yPos, totalPaint)

        // Booking ID footer
        val footerPaint = Paint().apply {
            color = Color.GRAY; textSize = 11f; isAntiAlias = true
        }
        canvas.drawText("Booking ID: ${booking.bookingId}", margin, 810f, footerPaint)
    }

    private fun saveToDownloads(context: Context, document: PdfDocument, fileName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29+ - use MediaStore (no permission needed)
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
            ) ?: return false

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                document.writeTo(stream)
            }
            true
        } else {
            // API 28 and below - direct file write
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsDir.mkdirs()
            val file = java.io.File(downloadsDir, fileName)
            document.writeTo(file.outputStream())
            true
        }
    }
}