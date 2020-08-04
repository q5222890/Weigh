package com.zack.weigh

import android.util.Log
import org.winplus.serial.utils.SerialPort
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.experimental.xor

/**
 * 串口工具类
 */
class SerialPortUtil private constructor() {
    private val TAG = SerialPortUtil::class.java.simpleName
    private var mSerialPort: SerialPort? = null
    private var mOutputStream: OutputStream? = null
    private var mInputStream: InputStream? = null
    private var mReadThread: ReadThread? = null
    private val path = "/dev/ttyS4" //瑞芯微s
    private val baudrate = 9600
    private var onDataReceiveListener: OnDataReceiveListener? = null
    private var isStop = false

    interface OnDataReceiveListener {
        fun onDataReceive(buffer: ByteArray?, size: Int)
    }

    fun setOnDataReceiveListener(dataReceiveListener: OnDataReceiveListener?) {
        onDataReceiveListener = dataReceiveListener
    }

    /**
     * 初始化串口信息
     */
    fun onCreate() {
        try {
            if (mSerialPort == null) {
                mSerialPort = SerialPort(File(path), baudrate, 0)
                mOutputStream = mSerialPort!!.outputStream
                mInputStream = mSerialPort!!.inputStream
                isStop = false
                mReadThread = ReadThread()
                mReadThread!!.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置新的锁地址
     *
     * @param address
     */
    fun setAddress(address: String): Boolean {
        Log.v(TAG, "setAddress: $address")
        val data = ByteArray(6)
        data[0] = 0x01
        data[1] = 0x55.toByte()
        data[2] = 0x01
        data[3] = 0x24
        val i = address.toInt()
        data[4] = (i and 0xff).toByte() //锁编号
        data[5] = (data[0] xor data[1] xor data[2] xor data[3] xor data[4])
        return sendBuffer(data)
    }

    fun sendBuffer(mBuffer: ByteArray?): Boolean {
        if (mSerialPort == null) {
            Log.e(TAG, "mSerialPort is null")
            onCreate()
        }
        var result = true
        try {
            if (mOutputStream != null) {
                mOutputStream!!.write(mBuffer)
            } else {
                result = false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            result = false
        }
        return result
    }

    //读取子弹重量
    fun readWeight(no: String): Boolean {
        Log.v(TAG, "readWeight: $no")
        val readWeight = byteArrayOf(0x00, 0x55, 0x01, 0x2d, 0x00)
        readWeight[0] = (no.toInt() and 0xff).toByte()
        readWeight[4] = (readWeight[0] xor readWeight[1] xor readWeight[2] xor readWeight[3]) as Byte
        return sendBuffer(readWeight)
    }

    //设置皮重
    fun setTare(no: String): Boolean {
        Log.v(TAG, "setTare: $no")
        val setTare = byteArrayOf(0x00, 0x55, 0x01, 0x27, 0x00)
        setTare[0] = (no.toInt() and 0xff).toByte()
        setTare[4] = (setTare[0] xor setTare[1] xor setTare[2] xor setTare[3]) as Byte
        return sendBuffer(setTare)
    }

    private inner class ReadThread : Thread() {
        override fun run() {
            super.run()
            Log.v(TAG, "run ReadThread: ")
            while (!isStop && !isInterrupted) {
                var size: Int
                try {
                    if (mInputStream == null) {
                        return
                    }
                    sleep(50)
                    val nCount = mInputStream!!.available()
                    if (nCount == 0) {
//                        Log.i(TAG, "run nCount == 0: ");
                        continue
                    }
                    //                    Log.i(TAG, "run  nCount: " + nCount);
                    val buffer = ByteArray(nCount)
                    size = mInputStream!!.read(buffer)
                    //                    Log.i(TAG, "run size: " + size);
                    if (size > 0) {
                        if (null != onDataReceiveListener) {
                            Log.i(TAG, "run onDataReceiveListener is not null: ")
                            onDataReceiveListener!!.onDataReceive(buffer, size)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    fun close() {
        try {
            isStop = true
            if (mReadThread != null && !mReadThread!!.isInterrupted) {
                mReadThread!!.interrupt()
            }
            if (mSerialPort != null) {
                mSerialPort!!.close()
                mSerialPort = null
            }
            if (onDataReceiveListener != null) {
                onDataReceiveListener = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var portUtil: SerialPortUtil? = null
        val instance: SerialPortUtil?
            get() {
                if (null == portUtil) {
                    portUtil = SerialPortUtil()
                }
                return portUtil
            }
    }

    init {
        onCreate()
    }
}