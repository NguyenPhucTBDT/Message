package com.example.message

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.lang.Exception

class PushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(">>>>>>>>", "received mms")
        val resolver = context?.contentResolver
        val mmsInboxUri = Uri.parse("content://mms")
        val mmsInboxCursor = resolver?.query(
            mmsInboxUri,
            arrayOf("_id", "msg_box", "ct_t", "date"),
            "msg_box=1 or msg_box=2",
            null,
            null
        )
        var id = -1
        mmsInboxCursor?.let {
            try {
                if (mmsInboxCursor.moveToFirst()) {
                    id = mmsInboxCursor.getString(0).toInt()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.e("<<<<<<<<<<<", mmsInboxCursor?.getString(1).toString())
        var message = ""
        var bitmap: Bitmap? = null
        val selectionPart = "mid$id"
        val mmsTextUri = Uri.parse("content://mms/part")
        val cursor = resolver?.query(mmsTextUri,null,selectionPart,null,null)
        if(cursor?.moveToFirst()!!) {
            do {
                val partId = cursor.getString(cursor.getColumnIndex("_id"))
                val type = cursor.getString(cursor.getColumnIndex("ct"))
                if("text/plain" == type) {
                    val _data = cursor.getColumnIndex("_data")
                    val data = cursor.getString(_data)
                    if(data != null) {
                        message = partId.toString()
                        Log.e(">>>>>>>>>>>", "Body is this: $message");
                    }
                    else {
                        val text = cursor.getColumnIndex("text")
                        message = cursor.getString(text)
                        Log.e(">>>>>>>>>>>", "Body is this: $message");
                    }
                }
                if("image/jpeg" == type || "image/bmp"== type || "image/gif" == type || "image/jpg" == type ||
                    "image/png" == type) {
                   bitmap = BitmapFactory.decodeByteArray(partId?.toByteArray(),0,
                       partId?.toByteArray()!!.size)
                }
            } while (cursor.moveToNext())
        }
    }
}
