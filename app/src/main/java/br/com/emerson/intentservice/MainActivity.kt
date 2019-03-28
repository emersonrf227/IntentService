package br.com.emerson.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {

    var receiver = ResponseReceiver()
    var receiverUSB = ResponseUSBReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MinhaIntenteService::class.java)
        intent.putExtra(MinhaIntenteService.PARAM_ENTRADA, "AGORA É: ")
        startService(intent)
        registrarReceiver()
        powerStatus()
    }


//  Toast.makeText(this, event, Toast.LENGTH_LONG).show()

    private fun registrarReceiver() {
        val filter = IntentFilter(MinhaIntenteService.MINHA_ACTION)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(receiver, filter)
    }


    private fun powerStatus() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(receiverUSB, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    inner class ResponseReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {

            tvResultado.text = intent?.getStringExtra(MinhaIntenteService.PARAM_SAIDA)

        }
    }

    inner class ResponseUSBReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(Intent.ACTION_POWER_CONNECTED)) {
                Toast.makeText(this@MainActivity, "Cabo Conectado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Cabo Desconectado", Toast.LENGTH_LONG).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: statusEvent) {
        //  Toast.makeText(this, event, Toast.LENGTH_LONG).show()

        when (event) {

            statusEvent.ERROR -> {
                containerLoading.visibility = View.GONE

            }
            statusEvent.LOADING -> {
                containerLoading.visibility = View.VISIBLE

            }
            statusEvent.SUSCESSO -> {
                containerLoading.visibility = View.GONE

            }


        }
    };
}
