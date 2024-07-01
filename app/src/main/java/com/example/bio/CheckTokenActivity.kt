package com.example.bio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bio.data.api.retrofitCheckToken
import com.example.bio.presentation.MainActivity
import com.example.data.SharedPreferencesManager
import com.example.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckTokenActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        Log.d("Mylog", "On resume check token activity")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Mylog", "On restart check token activity")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Mylog", "On start check token activity")

        lifecycleScope.launch {
            val sharedPreferences = SharedPreferencesManager.getInstance(baseContext)
            val token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)

            try {
                Log.d("Mylog", "Token = $token")
                retrofitCheckToken.testGet(token)
                val intent = Intent(this@CheckTokenActivity, MainActivity::class.java)
                startActivity(intent)

            } catch (ex: Exception) {
                Log.d("Mylog", "Exception main activity = ${ex.message}")
                val intent = Intent(this@CheckTokenActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Mylog", "On create check token activity")
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_token)

    }

}