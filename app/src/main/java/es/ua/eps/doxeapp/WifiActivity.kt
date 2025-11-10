package es.ua.eps.doxeapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.DhcpInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiInfo.FREQUENCY_UNITS
import android.net.wifi.WifiInfo.LINK_SPEED_UNITS
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.ua.eps.doxeapp.databinding.WifiActivityBinding

class WifiActivity : AppCompatActivity() {
    private lateinit var bindings : WifiActivityBinding
    private lateinit var wifiMan : WifiManager
    private lateinit var wifiInf : WifiInfo
    private lateinit var dhcpInfo: DhcpInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindings = WifiActivityBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        checkPermissions()
        bindings.buttonGetWifiData.setOnClickListener { getWifiData() }
    }
    private fun checkPermissions(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES)
            != PackageManager.PERMISSION_GRANTED)
        ) {
            val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 100)
        }
    }
    private fun getWifiData() {
        wifiMan = getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiInf = wifiMan.connectionInfo
        dhcpInfo = wifiMan.dhcpInfo

        with(bindings) {
            if(wifiInf.ssid != WifiManager.UNKNOWN_SSID) {
                // Connection WifiInfo
                textViewSSID.append(wifiInf.ssid)
                textViewBSSID.append(wifiInf.bssid)

                val strength : String = WifiManager.calculateSignalLevel(wifiInf.rssi, 5).toString()
                val speed : String = wifiInf.linkSpeed.toString()
                val frequency : String = wifiInf.frequency.toString()
                val ipDecimal = wifiInf.ipAddress
                val ip : String = getFormattedIp(ipDecimal, "decimal")      // se puede poner modo=hex
                val hidden : String = wifiInf.hiddenSSID.toString()

                textViewStrength.append(strength)
                textViewSpeed.append(speed+LINK_SPEED_UNITS)
                textViewFrec.append(frequency+FREQUENCY_UNITS)
                textViewIP.append(ip)
                textViewHidden.append(hidden)

                // Server DHCP
                val gateDecimal = dhcpInfo.gateway
                val gate = getFormattedIp(gateDecimal, "decimal")// se puede poner modo=hex
                val mask : String = dhcpInfo.netmask.toString()

                textViewGateway.append(gate)
                textViewMask.append(mask)

            } else {
                textViewWifiTitle.text = getString(R.string.infoUnavailable)
            }
        }
    }
    private fun getFormattedIp(ipDecimal: Int, modo: String): String { // formato Hexadecimal
        var res : String = ""
        // Extrae octetos dirección IP
        val octet1 = (ipDecimal and 0xFF000000.toInt()) shr 24
        val octet2 = (ipDecimal and 0x00FF0000.toInt()) shr 16
        val octet3 = (ipDecimal and 0x0000FF00.toInt()) shr 8
        val octet4 = ipDecimal and 0x000000FF

        when(modo){
            "hex" -> res = String.format("%02X:%02X:%02X:%02X", octet1, octet2, octet3, octet4) // octeto a formato hex (dos dígitos, con 0 a la izquierda si es necesario)
            "decimal" -> res = "$octet1.$octet2.$octet3.$octet4"
        }
        return res
    }


}