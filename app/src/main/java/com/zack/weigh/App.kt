package com.zack.weigh

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.util.Log

class App : Application(), Thread.UncaughtExceptionHandler {
    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = this.applicationContext

//        CrashHandler.getInstance().init(getApplicationContext());
        SoundPlayUtil.instance.init(applicationContext)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        val intent = Intent(this, topActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Process.killProcess(Process.myPid())
    }

    val topActivity: Class<*>?
        get() {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            var className: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                className = manager.getRunningTasks(1)[0].topActivity!!.className
            }
            Log.i("App", "getTopActivity class name: $className")
            var cls: Class<*>? = null
            try {
                cls = Class.forName(className!!)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            return cls
        }

    companion object {
        var instance: App? = null
            private set
        var context: Context? = null
            private set

    }
}