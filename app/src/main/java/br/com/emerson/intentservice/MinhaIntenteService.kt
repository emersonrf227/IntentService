package br.com.emerson.intentservice

import android.app.IntentService
import android.content.Intent

import android.os.SystemClock
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.text.format.DateFormat
import org.greenrobot.eventbus.EventBus

class MinhaIntenteService : IntentService("MinhaIntenteService") {

    override fun onHandleIntent(intent: Intent?) {

        EventBus.getDefault().post(statusEvent.LOADING)


        val msg = intent?.getStringExtra(MinhaIntenteService.PARAM_ENTRADA)
        SystemClock.sleep(15000)

        val resultado = "$msg ${DateFormat.format("dd/MM/yy H:mm",
            System.currentTimeMillis())}"

        val i = Intent()
        i.action = MINHA_ACTION
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.putExtra(PARAM_SAIDA, resultado)
        sendBroadcast(i)

        EventBus.getDefault().post(statusEvent.SUSCESSO)


    }


    companion object {


        val PARAM_ENTRADA = "entrada"
        val PARAM_SAIDA = "saida"
        val MINHA_ACTION = "br.com.emerson.intentservice.RESPONSE"
    }

}
