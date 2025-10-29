package es.ua.eps.doxeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.view.View
import es.ua.eps.doxeapp.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindings : MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindings = MainActivityBinding.inflate(layoutInflater)
        with(bindings) {
            setContentView(root)

            buttonSim.setOnClickListener { showSim() }
            buttonWifi.setOnClickListener { showWifi() }
        }
    }
    private fun showSim(){
        val simIntent = Intent(this@MainActivity, SimActivity::class.java)
        startActivity(simIntent)
    }
    private fun showWifi(){
        val wifiIntent = Intent(this@MainActivity, WifiActivity::class.java)
        startActivity(wifiIntent)
    }
}