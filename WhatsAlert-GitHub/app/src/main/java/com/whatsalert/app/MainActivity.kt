package com.whatsalert.app

import android.app.*
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.widget.*

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("settings", MODE_PRIVATE) }
    private lateinit var status: TextView; private lateinit var monitorLabel: TextView; private lateinit var termsText: TextView
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main)
        status=findViewById(R.id.statusText); monitorLabel=findViewById(R.id.monitorLabel); termsText=findViewById(R.id.termsText)
        val sw: Switch=findViewById(R.id.monitorSwitch); val input: EditText=findViewById(R.id.termInput); val mins: EditText=findViewById(R.id.minutesInput)
        sw.isChecked=prefs.getBoolean("enabled",false); update(sw.isChecked)
        findViewById<Button>(R.id.addButton).setOnClickListener { val t=input.text.toString().trim(); if(t.isNotEmpty()){ val old=prefs.getStringSet("terms", emptySet())!!.toMutableSet(); old.add(t); prefs.edit().putStringSet("terms",old).apply(); input.text.clear(); refreshTerms() } }
        sw.setOnCheckedChangeListener { _, checked -> prefs.edit().putBoolean("enabled",checked).apply(); update(checked); if(checked) startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        findViewById<Button>(R.id.accessButton).setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        mins.setOnFocusChangeListener { _, hasFocus -> if(!hasFocus) prefs.edit().putLong("minutes", mins.text.toString().toLongOrNull() ?: 2L).apply() }
        findViewById<Button>(R.id.testButton).setOnClickListener { val m=(mins.text.toString().toLongOrNull() ?: 2L).coerceAtLeast(0); AlertScheduler.schedule(this,"Teste do WhatsAlert",m) }
        refreshTerms()
    }
    private fun update(on:Boolean){ status.text=if(on) "Ativo · ${prefs.getStringSet("terms",emptySet())?.size ?: 0} termos" else "Pausado · ${prefs.getStringSet("terms",emptySet())?.size ?: 0} termos"; monitorLabel.text=if(on) "Ativado" else "Desativado" }
    private fun refreshTerms(){ termsText.text=prefs.getStringSet("terms",emptySet())!!.sorted().joinToString("\n") { "• $it" }.ifEmpty { "Nenhum termo cadastrado." }; update(prefs.getBoolean("enabled",false)) }
}
