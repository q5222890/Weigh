package com.zack.weigh

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1.
        SerialPortUtil.instance!!.onCreate()
        SerialPortUtil.instance!!.setOnDataReceiveListener(object : SerialPortUtil.OnDataReceiveListener {
            override fun onDataReceive(buffer: ByteArray?, size: Int) {
                var hexString = TransformUtil.binaryToHexString(buffer)
                Log.v(tag, "run  buffer:$hexString")
                if (Utils.isXorValue(buffer)) {
                    Log.v(tag, "run 校验值正确: ")
                    var split = hexString.split(" ")
                    var position = Integer.parseInt(split[1], 16)
                    Log.v(tag, "run  position: $position size: ${split.size}")
                    if (split.size == 10) { //查询重量
                        val kilobit = split[4].toInt(16).toString()
                        val hundreds = split[5].toInt(16).toString()
                        val tens = split[6].toInt(16).toString()
                        val units = split[7].toInt(16).toString()
                        var weight =kilobit+hundreds+tens+units
                        val intWeight = weight.toInt()
                        Log.i(tag, "String 类型重量：$weight")
                        Log.i(tag, "int 类型 重量：$intWeight")
                        main_tv_msg.setText("$intWeight 克")
                    }
                } else {
                    Log.v(tag, "run 校验值错误: ")
                }
            }
        })
        main_edt_address.setText("1")
    }

    /**
     * 设置地址
     * @param view
     */
    fun setAddress(view: View) {
        var address = main_edt_address.text.toString()
        Log.i(tag, "address $address")
        if(address.isNotEmpty()) {
            SerialPortUtil.instance!!.setAddress(address)
        }
    }

    /**
     * 设置皮重
     * @param view
     */
    fun setTare(view: View) {
        var address = main_edt_address.text.toString()
        Log.i(tag, "address $address")
        if(address.isNotEmpty()) {
            SerialPortUtil.instance!!.setTare(address)
        }
    }

    /**
     * 读取重量
     * @param view
     */
    fun readWeight(view: View) {
        var address = main_edt_address.text.toString()
        Log.i(tag, "address $address")
        if(address.isNotEmpty()){
            SerialPortUtil.instance!!.readWeight(address)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SerialPortUtil.instance!!.close()
    }
}
