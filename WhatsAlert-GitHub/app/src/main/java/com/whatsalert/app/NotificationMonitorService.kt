package com.whatsalert.app

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationMonitorService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.whatsapp" && sbn.packageName != "com.whatsapp.w4b") return
        val p=getSharedPreferences("settings", MODE_PRIVATE); if(!p.getBoolean("enabled",false)) return
        val terms=p.getStringSet("terms",emptySet())!!.map { it.lowercase() }; if(terms.isEmpty()) return
        val n=sbn.notification; val extras=n.extras; val text=listOf(extras.getCharSequence(Notification.EXTRA_TITLE),extras.getCharSequence(Notification.EXTRA_TEXT),extras.getCharSequence(Notification.EXTRA_BIG_TEXT)).filterNotNull().joinToString(" ").lowercase()
        if(terms.any { text.contains(it) }) { val mins=p.getLong("minutes",2L).coerceAtLeast(0); AlertScheduler.schedule(this,"Termo identificado no WhatsApp",mins) }
    }
}
