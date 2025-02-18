package com.yc.ycmvvm.data.constans

import androidx.annotation.StringDef
import com.yc.ycmvvm.R

object YcPlateNumData {
    /**
     * 车牌数字键盘：删除键
     */
    const val PLATE_NUM_KEYBOARD_TYPE_DEL = "plate_num_keyboard_type_del"

    /**
     * 车牌数字键盘:确定键
     */
    const val PLATE_NUM_KEYBOARD_TYPE_OK = "PLATE_NUM_KEYBOARD_TYPE_OK"

    /**
     * 车牌数字键盘：数据键
     */
    const val PLATE_NUM_KEYBOARD_TYPE_DATA = "PLATE_NUM_KEYBOARD_TYPE_DATA"

    /**
     * 车牌数字键盘 按键功能类型
     */
    @Target(AnnotationTarget.TYPE)
    @StringDef(
        PLATE_NUM_KEYBOARD_TYPE_DEL,
        PLATE_NUM_KEYBOARD_TYPE_OK,
        PLATE_NUM_KEYBOARD_TYPE_DATA
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PlateNumKeyboardType

    /**
     *  车牌-未选中
     */
    const val PLATE_NUM_TYPE_NONE = "0"

    /**
     *  车牌-蓝色
     */
    const val PLATE_NUM_TYPE_BLUE = "蓝"

    /**
     *  车牌-黄色
     */
    const val PLATE_NUM_TYPE_YELLOW = "黄"

    /**
     *  车牌-绿色
     */
    const val PLATE_NUM_TYPE_GREEN = "绿"

    /**
     *  车牌-白色
     */
    const val PLATE_NUM_TYPE_WHITE = "白"

    /**
     *  车牌-黑色
     */
    const val PLATE_NUM_TYPE_BLACK = "黑"

    /**
     *  车牌-黄绿色
     */
    const val PLATE_NUM_TYPE_YELLOW_GREEN = "黄绿"


    /**
     * 车牌颜色类型
     */
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
    @StringDef(
        PLATE_NUM_TYPE_NONE,
        PLATE_NUM_TYPE_BLUE,
        PLATE_NUM_TYPE_YELLOW,
        PLATE_NUM_TYPE_GREEN,
        PLATE_NUM_TYPE_WHITE,
        PLATE_NUM_TYPE_BLACK,
        PLATE_NUM_TYPE_YELLOW_GREEN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class YcPlateNumColorType

    /**
     * 根据车牌号获取车牌颜色类型
     */
    fun getYcPlateNumColorType(plateNum: String): List<@YcPlateNumColorType String> {
        val plateNum6 = plateNum[6]
        when (plateNum6) {
            '港', '澳', '领' -> {
                return listOf(PLATE_NUM_TYPE_BLACK)
            }

            '学' -> {
                return listOf(PLATE_NUM_TYPE_YELLOW)
            }

            '警' -> {
                return listOf(PLATE_NUM_TYPE_WHITE)
            }

            else -> {
                if (plateNum.length == 7) {
                    //燃油车
                    return listOf(PLATE_NUM_TYPE_BLUE, PLATE_NUM_TYPE_YELLOW)
                } else {
                    //新能源车
                    val plateNum7 = plateNum[7]
                    return if (plateNum7.toString().matches("[a-zA-Z]".toRegex())) {
                        listOf(PLATE_NUM_TYPE_YELLOW_GREEN, PLATE_NUM_TYPE_GREEN)
                    } else {
                        listOf(PLATE_NUM_TYPE_BLUE, PLATE_NUM_TYPE_YELLOW)
                    }
                }
            }
        }
    }

    fun getYcPlateNumFrameBg(plateNumIndex: Int, plateNumColorType: @YcPlateNumColorType String, hasSelect: Boolean, hasDateEmpty: Boolean): Int {
        if (hasDateEmpty) {
            return R.drawable.yc_plate_num_frame_un_data
        }
        return when (plateNumColorType) {
            PLATE_NUM_TYPE_BLUE -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_blue_select
                } else {
                    R.drawable.yc_plate_num_frame_blue_select
//                    R.drawable.yc_plate_num_frame_blue_select_un
                }
            }

            PLATE_NUM_TYPE_YELLOW -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_yellow_select
                } else {
                    R.drawable.yc_plate_num_frame_yellow_select
//                    R.drawable.yc_plate_num_frame_yellow_select_un
                }
            }

            PLATE_NUM_TYPE_GREEN -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_green_select
                } else {
                    R.drawable.yc_plate_num_frame_green_select
//                    R.drawable.yc_plate_num_frame_green_select_un
                }
            }

            PLATE_NUM_TYPE_WHITE -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_white_select
                } else {
                    R.drawable.yc_plate_num_frame_white_select
//                    R.drawable.yc_plate_num_frame_white_select_un
                }
            }

            PLATE_NUM_TYPE_BLACK -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_black_select
                } else {
                    R.drawable.yc_plate_num_frame_black_select
//                    R.drawable.yc_plate_num_frame_black_select_un
                }
            }

            PLATE_NUM_TYPE_YELLOW_GREEN -> {
                if (plateNumIndex <= 1) {
                    if (hasSelect) {
                        R.drawable.yc_plate_num_frame_yellow_select
                    } else {
//                        R.drawable.yc_plate_num_frame_yellow_select_un
                        R.drawable.yc_plate_num_frame_yellow_select
                    }
                } else {
                    if (hasSelect) {
                        R.drawable.yc_plate_num_frame_green_select
                    } else {
//                        R.drawable.yc_plate_num_frame_green_select_un
                        R.drawable.yc_plate_num_frame_green_select
                    }
                }
            }

            else -> {
                if (hasSelect) {
                    R.drawable.yc_plate_num_frame_blue_select
                } else {
//                    R.drawable.yc_plate_num_frame_blue_select_un
                    R.drawable.yc_plate_num_frame_blue_select
                }
            }
        }
    }

    const val PLATE_NUM_CONTENT_PROVINCE = "粤京冀沪津晋蒙辽吉黑苏浙皖闽赣鲁豫鄂湘桂琼渝川贵云藏陕甘青宁新"
    const val PLATE_NUM_CONTENT_SPECIAL = "港澳学领警"
    const val PLATE_NUM_CONTENT_LETTER = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    const val PLATE_NUM_CONTENT_DIGIT = "0123456789"
}