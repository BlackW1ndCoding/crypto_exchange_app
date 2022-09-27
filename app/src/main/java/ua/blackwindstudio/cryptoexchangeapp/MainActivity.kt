package ua.blackwindstudio.cryptoexchangeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.blackwindstudio.cryptoexchangeapp.coin.data.CoinRepository
import ua.blackwindstudio.cryptoexchangeapp.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        GlobalScope.launch {
            CoinRepository.initializeRepository(
                COIN_LIST_SIZE,
                "USD",
                this@MainActivity.lifecycleScope
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val COIN_LIST_SIZE = 50
    }
}