package com.yc.ycmvvm.utils
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.renderscript.Type
import java.nio.ByteBuffer

class YuvToRgbConverter(context: Context) {
    @Suppress("DEPRECATION")
    private val rs = RenderScript.create(context)

    @Suppress("DEPRECATION")
    private val scriptYuvToRgb =
        ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))

    private var yuvBits: ByteBuffer? = null
    private var bytes: ByteArray = ByteArray(0)

    @Suppress("DEPRECATION")
    private var inputAllocation: Allocation? = null

    @Suppress("DEPRECATION")
    private var outputAllocation: Allocation? = null

    @Synchronized
    fun yuvToRgb(image: Image, output: Bitmap) {
        try {
            val yuvBuffer = YuvByteBuffer(image, yuvBits)
            yuvBits = yuvBuffer.buffer

            if (needCreateAllocations(image, yuvBuffer)) {
                createAllocations(image, yuvBuffer)
            }

            yuvBuffer.buffer.get(bytes)
            @Suppress("DEPRECATION")
            inputAllocation!!.copyFrom(bytes)

            @Suppress("DEPRECATION")
            scriptYuvToRgb.setInput(inputAllocation)
            @Suppress("DEPRECATION")
            scriptYuvToRgb.forEach(outputAllocation)
            @Suppress("DEPRECATION")
            outputAllocation!!.copyTo(output)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to convert YUV to RGB", e)
        }
    }

    private fun needCreateAllocations(image: Image, yuvBuffer: YuvByteBuffer): Boolean {
        @Suppress("DEPRECATION")
        return inputAllocation?.type?.x != image.width ||
                inputAllocation?.type?.y != image.height ||
                inputAllocation?.type?.yuv != yuvBuffer.type
    }

    private fun createAllocations(image: Image, yuvBuffer: YuvByteBuffer) {
        @Suppress("DEPRECATION")
        val yuvType = Type.Builder(rs, Element.U8(rs))
            .setX(image.width)
            .setY(image.height)
            .setYuvFormat(yuvBuffer.type)
        @Suppress("DEPRECATION")
        inputAllocation = Allocation.createTyped(
            rs,
            yuvType.create(),
            Allocation.USAGE_SCRIPT
        )
        bytes = ByteArray(yuvBuffer.buffer.capacity())
        @Suppress("DEPRECATION")
        val rgbaType = Type.Builder(rs, Element.RGBA_8888(rs))
            .setX(image.width)
            .setY(image.height)
        @Suppress("DEPRECATION")
        outputAllocation = Allocation.createTyped(
            rs,
            rgbaType.create(),
            Allocation.USAGE_SCRIPT
        )
    }

    @Suppress("DEPRECATION")
    fun release() {
        inputAllocation?.destroy()
        outputAllocation?.destroy()
        scriptYuvToRgb.destroy()
        rs.destroy()
    }
}