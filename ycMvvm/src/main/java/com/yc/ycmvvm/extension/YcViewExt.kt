package com.yc.ycmvvm.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.signature.ObjectKey
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.utils.YcColorUtil
import com.yc.ycmvvm.utils.YcSoftInputUtil
import com.yc.ycmvvm.utils.YcUI
import java.io.File

/**
 * Creator: yc
 * Date: 2021/2/2 21:03
 * UseDes:
 */
/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun AppCompatActivity.ycCreateResultLauncher(crossinline success: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            success(it)
        }
    }

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun AppCompatActivity.ycCreateResultLauncher(
    crossinline success: ((result: ActivityResult) -> Unit),
    crossinline fail: ((result: ActivityResult) -> Unit)
): ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
        success(it)
    } else {
        fail(it)
    }
}

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun Fragment.ycCreateResultLauncher(crossinline success: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            success(it)
        }
    }

/**
 * 创建跳转回调的launcher（必须在onCreate生命周期里创建）
 */
inline fun Fragment.ycCreateResultLauncher(
    crossinline success: ((result: ActivityResult) -> Unit),
    crossinline fail: ((result: ActivityResult) -> Unit)
): ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
        success(it)
    } else {
        fail(it)
    }
}

fun Activity?.ycShowToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this == null || this.isFinishing) {
        return
    } else {
        (this as Context).ycShowToast(msg, duration)
    }
}

fun FragmentActivity?.ycShowToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this == null || this.isFinishing) {
        return
    } else {
        (this as Context).ycShowToast(msg, duration)
    }
}

fun Fragment?.ycShowToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this == null || this.isDetached) {
        return
    } else {
        this.requireContext().ycShowToast(msg, duration)
    }
}

fun Context?.ycShowToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this != null) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast.makeText(this, msg, duration).show()
        } else {
            try {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, msg, duration).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/**
 * 点击非EditText位置，隐藏输入法
 */
fun Activity.ycTouchOutsideHideSoftInput(event: MotionEvent) {
    YcSoftInputUtil.clickNoEditHideSoftInput(currentFocus, event)
}

/**
 * 隐藏输入法
 */
fun View.ycHideSoftInput() {
    YcSoftInputUtil.hideSoftInput(context, windowToken)
}

/**
 * 隐藏输入法
 */
fun Activity.ycHideSoftInput() {
    window.peekDecorView()?.ycHideSoftInput()
}

/**
 * 隐藏输入法
 */
fun Fragment.ycHideSoftInput() {
    view?.ycHideSoftInput()
}

@ColorInt
fun Fragment.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@ColorInt
fun Activity.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@ColorInt
fun Context.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

fun View.ycGetFont(resId: Int): Typeface? {
    return ResourcesCompat.getFont(context, resId)
}

fun Context.ycGetFont(resId: Int): Typeface? {
    return ResourcesCompat.getFont(this, resId)
}

@ColorInt
fun View.ycGetColorRes(resId: Int): Int {
    return resources.getColor(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun View.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Activity.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.ycGetDrawable(resId: Int): Drawable {
    return resources.getDrawable(resId)
}

fun View.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Fragment.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Activity.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun Context.ycGetString(resId: Int): String {
    return resources.getString(resId)
}

fun View.ycGetStringArray(resId: Int): Array<String> {
    return resources.getStringArray(resId)
}

fun View.ycGetStringList(resId: Int): List<String> {
    return mutableListOf(*resources.getStringArray(resId))
}

fun Context.ycDpToPx(dp: Float): Int {
    return (resources.displayMetrics.density * dp + 0.5f).toInt()
}

fun View.ycDpToPx(dp: Float): Int {
    return (resources.displayMetrics.density * dp + 0.5f).toInt()
}


/**
 * 等同于getDimension()得到值进行四舍五入
 */
fun View.ycGetDimensionPixelSize(resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

fun Context.ycGetDimensionPixelSize(resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

fun View.ycGetDimension(resId: Int): Float {
    return resources.getDimension(resId)
}

fun Context.ycGetDimension(resId: Int): Float {
    return resources.getDimension(resId)
}

fun TextView.ycSetTextSizeRes(@DimenRes idRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(idRes))
}

fun TextView.ycSetTextSize(textSize: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
}

fun TextView.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(context.resources.getColor(textColorRes))
}


fun Button.ycSetTextColorRes(@ColorRes textColorRes: Int) {
    this.setTextColor(context.resources.getColor(textColorRes))
}

fun View.ycBgRgb(red: Int, green: Int, blue: Int) {
    setBackgroundColor(YcColorUtil.rgb(red, green, blue))
}

fun TextView.ycTextColorRgb(red: Int, green: Int, blue: Int) {
    setTextColor(YcColorUtil.rgb(red, green, blue))
}

fun View.ycBgArgb(alpha: Int, red: Int, green: Int, blue: Int) {
    setBackgroundColor(YcColorUtil.argb(alpha, red, green, blue))
}

fun ImageView.ycSetColorFilter(@ColorRes colorRes: Int) {
    this.setColorFilter(context.resources.getColor(colorRes))
}

fun RecyclerView.ycInitLinearLayoutManage(orientation: Int = RecyclerView.VERTICAL) {
    this.layoutManager = LinearLayoutManager(context, orientation, false)
}

fun RecyclerView.ycInitGridLayoutManage(spanCount: Int = 2) {
    this.layoutManager = GridLayoutManager(context, spanCount)
}

/**
 * 加载网络图片
 */
fun ImageView.ycLoadImageNet(imageNet: String?, placeholderImg: Int = YcInit.mInstance.mImgIdResLoading, errorImg: Int = YcInit.mInstance.mImgIdResFail) {
    Glide.with(context)
        .asBitmap()
        .placeholder(placeholderImg)
        .error(errorImg)
        .load(imageNet)
        .into(this)
}

/**
 * 加载网络图片(设置长宽)
 */
fun ImageView.ycLoadImageNetWithSize(
    imageNet: String?, width: Int = 180, height: Int = 180,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail

) {
    Glide.with(context)
        .load(imageNet)
        .override(maxWidth, maxHeight)
        .apply(RequestOptions().placeholder(placeholderImg))
        .error(errorImg)
        .into(this)
}

fun ImageView.ycLoadImageNetLong(
    imageNet: String?,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .asBitmap()
        .placeholder(placeholderImg)
        .error(errorImg)
        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
        .load(imageNet)
        .into(this)
}

fun ImageView.ycLoadImageHeader(imageNet: String?, headerMap: Map<String, String>) {
    Glide.with(context)
        .asBitmap()
        .load(GlideUrl(imageNet) { headerMap })
        .into(this)
}

/**
 * 加载网络图片（圆形）
 */
fun ImageView.ycLoadImageNetCircle(
    imageNet: String?,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .asBitmap()
        .load(imageNet)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}

/**
 * 加载网络图片（圆）
 */
fun ImageView.ycLoadImageNetCircle(
    imgNetUrl: String?,
    imageUpdateTime: String,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .circleCrop()
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}

/**
 * 加载圆角图片(自定义弧度dp)
 * roundingRadius单位是dp
 * RoundedCorners（px）
 */
fun ImageView.ycLoadImageNetFilletDp(
    imageNet: String?,
    roundingRadius: Float,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .asBitmap()
        .load(imageNet)
        .transform(CenterCrop(), RoundedCorners(YcUI.dpToPx(roundingRadius)))
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}

/**
 * 加载圆角图片(自定义弧度px)
 * roundingRadius单位是px
 */
fun ImageView.ycLoadImageNetFilletPx(
    imageNet: String?,
    roundingRadius: Int,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .asBitmap()
        .load(imageNet)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}

/**
 * 加载网络图片（用时间来区分地址相同，图片内容不相同情况）
 */
fun ImageView.ycLoadImageNet(
    imgNetUrl: String?, imageUpdateTime: String,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .applyDefaultRequestOptions(RequestOptions().signature(ObjectKey(imageUpdateTime)))
        .asBitmap()
        .load(imgNetUrl)
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}


fun ImageView.ycLoadImagePath(imageFilePath: String) {
    this.ycLoadImageFile(File(imageFilePath))
}

fun ImageView.ycLoadImagePathCircle(imageFilePath: String) {
    this.ycLoadImageFileCircle(File(imageFilePath))
}

fun ImageView.ycLoadImagePathCircle(imageFilePath: String, roundingRadius: Int) {
    this.ycLoadImageFileCircle(File(imageFilePath), roundingRadius)
}

/**
 * 加载本地图片
 */
fun ImageView.ycLoadImageFile(imageFile: File) {
    Glide.with(context)
        .load(imageFile)
        .into(this)
}

fun ImageView.ycLoadImageFileCircle(imageFile: File) {
    Glide.with(context)
        .load(imageFile)
        .circleCrop()
        .into(this)
}

fun ImageView.ycLoadImageFileCircle(imageFile: File, roundingRadius: Int) {
    Glide.with(context)
        .load(imageFile)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))//px
        .into(this)
}

/**
 * 加载资源图片
 */
fun ImageView.ycLoadImageRes(@DrawableRes imgRes: Int) {
    this.setImageResource(imgRes)
}

fun ImageView.ycLoadImageViewTarget(
    url: String,
    onLoadStartedCallBack: ((Drawable?) -> Unit)? = null,
    onLoadFailedCallBack: ((Drawable?) -> Unit)? = null,
    setResourceCallBack: ((Bitmap?) -> Unit)? = null
) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(object : ImageViewTarget<Bitmap?>(this) {
            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
                onLoadStartedCallBack?.invoke(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                onLoadFailedCallBack?.invoke(errorDrawable)
            }

            override fun setResource(resource: Bitmap?) {
                setResourceCallBack?.invoke(resource)
            }
        })

}

fun ImageView.ycLoadBitmapImageViewTarget(
    url: String, width: Int = 180, height: Int = 180, sizeMultiplier: Float = 0.5f,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail,
    setResourceCallBack: ((Bitmap?) -> Unit)? = null,
) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .override(width, height)
        .centerCrop()
        .sizeMultiplier(sizeMultiplier)
        .apply(RequestOptions().placeholder(placeholderImg))
        .error(errorImg)
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setResourceCallBack?.invoke(resource)
            }
        })

}

fun ImageView.ycLoadAsGifImage(
    url: String,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .asGif()
        .load(url)
        .placeholder(placeholderImg)
        .error(errorImg)
        .into(this)
}

fun ImageView.ycLoadGridImage(
    url: String, width: Int = 180, height: Int = 180,
    placeholderImg: Int = YcInit.mInstance.mImgIdResLoading,
    errorImg: Int = YcInit.mInstance.mImgIdResFail
) {
    Glide.with(context)
        .load(url)
        .override(width, height)
        .centerCrop()
        .apply(RequestOptions().placeholder(placeholderImg))
        .error(errorImg)
        .into(this)
}


fun View?.ycSetVisible(visibleOrGone: Boolean? = true) {
    if (visibleOrGone == true)
        this?.ycSetVisible()
    else {
        this?.ycSetGone()
    }
}

fun View?.ycSetGone() {
    this?.visibility = View.GONE
}

fun View?.ycSetVisible() {
    this?.visibility = View.VISIBLE
}

fun View?.ycSetInvisible() {
    this?.visibility = View.INVISIBLE
}