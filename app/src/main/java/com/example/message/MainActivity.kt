package com.example.message

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.READ_SMS
        )

        if (checkSelfPermission(READ_SMS_PERMISSION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                READ_MMS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(READ_PERMISSION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            receiveMsg()
        }
    }

    private fun receiveMsg() {
        val br = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
                        Toast.makeText(
                            p0,
                            "Phone Number : " + sms.displayOriginatingAddress + "|Message : " + sms.displayMessageBody,
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(
                            ">>>>>>>>>",
                            "Phone Number : " + sms.displayOriginatingAddress + "|Message : " + sms.displayMessageBody
                        )
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        registerReceiver(
            PushReceiver(),
            IntentFilter("android.provider.Telephony.WAP_PUSH_RECEIVED")
        )
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
        private const val READ_SMS_PERMISSION = Manifest.permission.RECEIVE_SMS
        private const val READ_MMS_PERMISSION = Manifest.permission.RECEIVE_MMS
        private const val READ_PERMISSION = Manifest.permission.READ_SMS
    }
}
