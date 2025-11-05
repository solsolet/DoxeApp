package es.ua.eps.doxeapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.TelephonyManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import es.ua.eps.doxeapp.databinding.SimActivityBinding

class SimActivity : AppCompatActivity() {
    private lateinit var bindings: SimActivityBinding
    private lateinit var telephonyMan : TelephonyManager

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        enableEdgeToEdge()

        telephonyMan= getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        bindings = SimActivityBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        checkPermissions()
        bindings.buttonGetSimData.setOnClickListener { getSimData() }
    }

    private fun checkPermissions(){
        val permissions = mutableListOf(Manifest.permission.READ_PHONE_STATE)
        // READ_BASIC_PHONE_STATE exists from API 33
        permissions.add(Manifest.permission.READ_BASIC_PHONE_STATE)

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), 101)
        }
    }

    private fun getSimData(){
        with(bindings) {
            val state = telephonyMan.simState.toString()
            //falta permiso
            val imei = telephonyMan.deviceId
            val imsi = telephonyMan.subscriberId
            val iccid = telephonyMan.simSerialNumber
            val operador = telephonyMan.simOperator // operator name
            //val networkType = telephonyMan.dataNetworkType.toString()

            textViewState.append(state)
            textViewImei.append(imei)
            textViewImsi.append(imsi)
            textViewIccid.append(iccid)
            textViewOperador.append(operador)
            //textViewNetwork.append(networkType)
        }
    }
}