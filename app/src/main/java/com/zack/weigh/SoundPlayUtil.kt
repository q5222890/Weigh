package com.zack.weigh

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.util.Log
import java.io.IOException

/**
 *
 */
class SoundPlayUtil private constructor() {
    private var soundPool: SoundPool? = null
    private var mContext: Context? = null
    fun init(context: Context?) {
        mContext = context
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i(TAG, "sdk >= 21")
            val builder = SoundPool.Builder()
            builder.setMaxStreams(1)
            val attrBuild = AudioAttributes.Builder()
            attrBuild.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            val audioAttributes = attrBuild.build()
            builder.setAudioAttributes(audioAttributes)
            builder.build()
        } else {
            Log.i(TAG, "sdk < 21")
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
    }

    fun play(fileName: String?): Int {
        val assetManager = mContext!!.assets
        var assetFileDescriptor: AssetFileDescriptor? = null
        try {
            assetFileDescriptor = assetManager.openFd(fileName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val load = soundPool!!.load(assetFileDescriptor, 1)
        soundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status ->
            //                Log.i(TAG, "onLoadComplete: "+sampleId+":"+status);
            val play = soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f)
            //                Log.i(TAG, "onLoadComplete play: "+play);
        }
        return load
    }

    fun play(rawId: Int): Int {
        val load = soundPool!!.load(mContext, rawId, 1)
        //        Log.i(TAG, "play load: "+load);
        soundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status ->
            //                Log.i(TAG, "onLoadComplete: "+sampleId+":"+status);
            val play = soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f)
            //                Log.i(TAG, "onLoadComplete play: "+play);
        }
        return load
    }

    fun pause(streamID: Int) {
        soundPool!!.pause(streamID)
    }

    fun stop(streamID: Int) {
        soundPool!!.stop(streamID)
    }

    fun release() {
        if (soundPool != null) {
            soundPool!!.release()
        }
    }

    companion object {
        private const val TAG = "SoundPlayUtil"
        val instance = SoundPlayUtil()
    }
}