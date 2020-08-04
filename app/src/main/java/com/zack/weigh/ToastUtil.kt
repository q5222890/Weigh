package com.zack.weigh

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object ToastUtil {
    private var sToast: Toast? = null
    fun show(context: Context?, msg: String?, duration: Int): Toast? {
        if (sToast == null) {
            sToast = Toast.makeText(App.instance, msg, duration)
        } else {
            sToast!!.duration = duration
            sToast!!.setText(msg)
        }
        sToast!!.show()
        return sToast
    }

    fun show(msg: String?, duration: Int): Toast? {
        if (sToast == null) {
            sToast = Toast.makeText(App.instance, msg, duration)
        } else {
            sToast!!.duration = duration
            sToast!!.setText(msg)
            sToast!!.setGravity(Gravity.CENTER, 0, 0)
        }
        sToast!!.show()
        return sToast
    }

    fun showLong(msg: String?): Toast? {
        return show(msg, Toast.LENGTH_LONG)
    }

    fun showShort(msg: String?): Toast? {
        return show(msg, Toast.LENGTH_SHORT)
    }

    fun showShort(msgId: Int): Toast? {
        return show(App.instance!!.resources.getString(msgId), Toast.LENGTH_SHORT)
    }

    /**
     * 显示屏幕中间的Toast（短时间）
     * @param context
     * @param msg
     */
    fun showCenterToast(context: Context?, msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 显示屏幕中间的Toast（长时间）
     * @param context
     * @param msg
     */
    fun showCenterToastToLong(context: Context?, msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}