package com.yc.ycmvvm.utils.permission

import com.hjq.permissions.permission.PermissionLists

/**
 * com.github.getActivity:XXPermissions权限框架适配
 */
object XXPermissionUtil {
    /**
     * 蓝牙权限
     * 由于XXPermissions框架设定（蓝牙权限只需下方三种，申请时候(AndroidManifest.xml还是要加的)不能添加
     * Manifest.permission.BLUETOOTH，android.permission.ACCESS_COARSE_LOCATION，android.permission.ACCESS_FINE_LOCATION
     * 否则会报错）
     */
    val BLUETOOTH = arrayOf(
        PermissionLists.getBluetoothScanPermission(),
        PermissionLists.getBluetoothConnectPermission(),
        PermissionLists.getBluetoothAdvertisePermission()
    )

    /**
     * 适配分区存储
     */
    val STORAGE =
        arrayOf(
            PermissionLists.getReadMediaImagesPermission(), PermissionLists.getReadMediaVideoPermission(), PermissionLists.getReadMediaAudioPermission(),
            PermissionLists.getReadMediaVisualUserSelectedPermission(), PermissionLists.getWriteExternalStoragePermission()
        )

    /**
     * 定位权限
     */
    val LOCATION = arrayOf(PermissionLists.getAccessCoarseLocationPermission(), PermissionLists.getAccessFineLocationPermission())

    /**
     * 后台定位权限(申请时如果用户未选择“始终允许”，而选择“仅在使用期间”，此情况无法再次申请，只能用户手动修改)
     */
    val LOCATION_BACKGROUND = arrayOf(PermissionLists.getAccessBackgroundLocationPermission())

    /**
     * 照相机权限
     */
    val CAMERA = PermissionLists.getCameraPermission()

    /**
     * 麦克风权限(即录像和录音需要)
     */
    val RECORD_AUDIO = PermissionLists.getRecordAudioPermission()

    /**
     * 联系人权限
     */
    val CONTACTS = PermissionLists.getReadContactsPermission()

    /**
     * 读取电话状态
     * 需要注意的是：这个权限在某些手机上面是没办法获取到的，因为某些系统禁止应用获得该权限
     * 所以你要是申请了这个权限之后没有弹授权框，而是直接回调授权失败方法
     * 请不要惊慌，这个不是 Bug、不是 Bug、不是 Bug，而是正常现象
     * 后续情况汇报：有人反馈在 iQOO 手机上面获取不到该权限，在清单文件加入下面这个权限就可以了（这里只是做记录，并不代表这种方式就一定有效果）
     */
    val READ_PHONE_STATE = PermissionLists.getReadPhoneStatePermission()

    /**
     * 通知权限权限
     */
    val POST_NOTIFICATIONS = PermissionLists.getNotificationServicePermission()
}