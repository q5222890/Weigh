package com.zack.weigh

import android.content.Context
import android.content.SharedPreferences

/**
 * sp工具类
 */
object SharedUtils {
    private const val Name = "intelligent"
    private val shared: SharedPreferences
        private get() = App.instance!!.getSharedPreferences(Name,
                Context.MODE_PRIVATE)

    val editor: SharedPreferences.Editor
        get() = shared.edit()

    fun putString(key: String?, content: String?) {
        editor.putString(key, content).commit()
    }

    fun getString(key: String?): String? {
        return shared.getString(key, "")
    }

    fun putFloat(key: String?, content: Float) {
        editor.putFloat(key, content).apply()
    }

    fun getFloat(key: String?): Float {
        return shared.getFloat(key, 0f)
    }

    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String?): Int {
        return shared.getInt(key, 0)
    }

    fun putBoolean(key: String?, `val`: Boolean) {
        editor.putBoolean(key, `val`).apply()
    }

    fun getBoolean(key: String?): Boolean {
        return shared.getBoolean(key, false)
    }

    // 温度
    fun saveTemperatureValue(temp: Float) {
        editor.putFloat("temperature_value", temp).apply()
    }

    val temperatureValue: Float
        get() = shared.getFloat("temperature_value", 26.00f)

    // 湿度
    fun saveHumidityValue(humidity: Float) {
        editor.putFloat("humidity_value", humidity).apply()
    }

    val humidityValue: Float
        get() = shared.getFloat("humidity_value", 50.00f)

    // 保存MAC地址
    fun saveMacAddress(mac: String) {
        editor.putString("mac_addr", mac).apply()
    }

    //获取MAC地址
    val macAddress: String?
        get() = shared.getString("mac_addr", "D2:66:E8:6C:50:25")

    /**
     * 设置波特率
     * @param baudrate
     */
    // 保存MAC地址
    fun saveBaudRate(baudrate: String) {
        editor.putString("baud_rate", baudrate).apply()
    }
    val baudRate: String?
        get() = shared.getString("baud_rate", "9600")
}