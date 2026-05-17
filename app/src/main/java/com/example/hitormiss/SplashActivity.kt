package com.example.hitormiss

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hitormiss.utils.AppDependencies
import com.example.hitormiss.utils.getPlayers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            coroutineScope {
                listOf(
                    async { delay(MIN_SPLASH_MS) },
                    async(Dispatchers.IO) {
                        withContext(NonCancellable) {
                            AppDependencies.repository.syncPlayers(getPlayers())
                        }
                    }
                ).awaitAll()
            }
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val MIN_SPLASH_MS = 1200L
    }
}
