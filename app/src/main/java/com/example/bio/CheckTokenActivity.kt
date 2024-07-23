package com.example.bio

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bio.data.api.retrofitCheckToken
import com.example.bio.presentation.MainActivity
import com.example.data.SharedPreferencesManager
import com.example.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CheckTokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Mylog", "On create check token activity")
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_token)

        val imageView = findViewById<ImageView>(R.id.image_view)
        val animatorSet = AnimatorInflater.loadAnimator(this, R.animator.scale_animation) as AnimatorSet
        animatorSet.setTarget(imageView)
        animatorSet.start()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Mylog", "On resume check token activity")

        // Начинаем проверку токена и переход на другую активность
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // Выполнение фоновой задачи
                val sharedPreferences = SharedPreferencesManager.getInstance(baseContext)
                val token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)

                try {
                    Log.d("Mylog", "Token = $token")
                    retrofitCheckToken.testGet(token)
                    withContext(Dispatchers.Main) {
                        // Переход на основной поток для запуска следующей активности
                        Log.d("Mylog", "Token is valid, launching MainActivity")
                        val intent = Intent(this@CheckTokenActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mylog", "Exception: ${ex.message}")
                        Log.d("Mylog", "Launching LoginActivity")
                        val intent = Intent(this@CheckTokenActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Mylog", "On start check token activity")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Mylog", "On pause check token activity")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Mylog", "On stop check token activity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Mylog", "On destroy check token activity")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Mylog", "On restart check token activity")
    }
}


