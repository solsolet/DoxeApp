package es.ua.eps.doxeapp

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.ua.eps.doxeapp.databinding.SimActivityBinding

class SimActivity : AppCompatActivity() {
    private lateinit var bindings: SimActivityBinding
    private lateinit var telephonyMan : TelephonyManager
    private lateinit var devicePolicyMan : DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        telephonyMan = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        devicePolicyMan = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        bindings = SimActivityBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        checkPermissions()
        bindings.buttonGetSimData.setOnClickListener { getSimData() }
    }

//    private fun checkPermissions(){
//        val permissions = mutableListOf(Manifest.permission.READ_PHONE_STATE)
//        permissions.add(Manifest.permission.READ_BASIC_PHONE_STATE)
//        permissions.add(Manifest.permission.MANAGE_DEVICE_POLICY_CERTIFICATES)
//
//        val notGranted = permissions.filter {
//            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
//        }
//        if (notGranted.isNotEmpty()) {
//            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), 101)
//        }
//    }
    private fun checkPermissions(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) != PackageManager.PERMISSION_GRANTED){
            val permissions = mutableListOf(Manifest.permission.READ_PHONE_STATE)

            permissions.add(Manifest.permission.READ_BASIC_PHONE_STATE)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 101)
        }
    }

    private fun getSimData(){
        with(bindings) {
            val numState = telephonyMan.simState
            var state = ""
            when(numState){
                TelephonyManager.SIM_STATE_UNKNOWN -> state = "Unknown"
                TelephonyManager.SIM_STATE_ABSENT -> state = "Absent"
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> state = "Pin required"
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> state = "Puk required"
                TelephonyManager.SIM_STATE_NETWORK_LOCKED -> state = "Network locked"
                TelephonyManager.SIM_STATE_READY -> state = "Ready"
                TelephonyManager.SIM_STATE_NOT_READY -> state = "Not ready"
                TelephonyManager.SIM_STATE_PERM_DISABLED -> state = "Perm disabled"
                TelephonyManager.SIM_STATE_CARD_IO_ERROR -> state = "Card IO error"
                TelephonyManager.SIM_STATE_CARD_RESTRICTED -> state = "Card restricted"
            }
            //falta permiso
            //val imsi = telephonyMan.subscriberId
            //val imei = telephonyMan.deviceId
            //val iccid = telephonyMan.simSerialNumber

            val operador = telephonyMan.simOperator // operator name
            val numNetworkType = telephonyMan.dataNetworkType
            var networkType = ""
            when(numNetworkType){
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> networkType = "Unknown"
                TelephonyManager.NETWORK_TYPE_GPRS -> networkType = "GPRS"
                TelephonyManager.NETWORK_TYPE_EDGE -> networkType = "Edge"
                TelephonyManager.NETWORK_TYPE_UMTS -> networkType = "UMTS"
                TelephonyManager.NETWORK_TYPE_CDMA -> networkType = "CDMA"
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> networkType = "EVDO_0"
                TelephonyManager.NETWORK_TYPE_EVDO_A -> networkType = "EVDO_A"
                TelephonyManager.NETWORK_TYPE_1xRTT -> networkType = "1xRTT"
                TelephonyManager.NETWORK_TYPE_HSDPA -> networkType = "HSDPA"
                TelephonyManager.NETWORK_TYPE_HSUPA -> networkType = "HSUPA"
                TelephonyManager.NETWORK_TYPE_HSPA -> networkType = "HSPA"
                TelephonyManager.NETWORK_TYPE_IDEN -> networkType = "IDEN"
                TelephonyManager.NETWORK_TYPE_EVDO_B -> networkType = "EVDO_B"
                TelephonyManager.NETWORK_TYPE_LTE -> networkType = "LTE"
                TelephonyManager.NETWORK_TYPE_EHRPD -> networkType = "EHRPD"
                TelephonyManager.NETWORK_TYPE_HSPAP -> networkType = "HSPAP"
                TelephonyManager.NETWORK_TYPE_GSM -> networkType = "GSM"
                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> networkType = "TD_SCDMA"
                TelephonyManager.NETWORK_TYPE_IWLAN -> networkType = "IWLAN"
                TelephonyManager.NETWORK_TYPE_NR -> networkType = "NR"
            }

            textViewState.append(state) // Estado de los datos
            //textViewImsi.append(imsi)
            //textViewImei.append(imei)
            //textViewIccid.append(iccid)
            textViewOperador.append(operador) // MCC+MNC (mobile country code + mobile network code)
            textViewNetwork.append(networkType) // Tipo de red móbil

            // Valorar: https://source.android.com/devices/tech/config/device-identifiers?hl=es-419
            // https://developer.android.com/identity/user-data-ids?hl=es-419#instance-ids-guids
        }
    }
}