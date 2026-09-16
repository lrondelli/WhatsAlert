package com.whatsalert.app

import android.app.*
import android.content.*

object AlertScheduler {
    fun schedule(context: Context, message: String, minutes: Long) {
        val intent=Intent(context, AlertReceiver::class.java).putExtra("message",message)
        val pi=PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarm=context.getSystemService(AlarmManager::class.java); alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+minutes*60_000,pi)
    }
}
