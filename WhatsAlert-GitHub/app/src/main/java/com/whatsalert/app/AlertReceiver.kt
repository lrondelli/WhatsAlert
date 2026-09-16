package com.whatsalert.app

import android.app.*
import android.content.*
import android.os.Build

class AlertReceiver: BroadcastReceiver(){ override fun onReceive(context: Context, intent: Intent){ val channel="alerts"; val nm=context.getSystemService(NotificationManager::class.java); if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(NotificationChannel(channel,"Alertas",NotificationManager.IMPORTANCE_HIGH)); val n=Notification.Builder(context,channel).setSmallIcon(android.R.drawable.ic_dialog_alert).setContentTitle("WhatsAlert").setContentText(intent.getStringExtra("message") ?: "Alerta").setAutoCancel(true).build(); nm.notify((System.currentTimeMillis()%100000).toInt(),n) } }
