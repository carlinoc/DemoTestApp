package com.demo.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.demo.testapp.data.NotificationData
import com.demo.testapp.data.PushNotification
import com.demo.testapp.databinding.ActivityMainBinding
import com.demo.testapp.utils.Constants.Companion.DESCRIPTION_NOTIFICATION
import com.demo.testapp.utils.Constants.Companion.TITLE_NOTIFICATION
import com.demo.testapp.utils.Constants.Companion.TOPIC
import com.demo.testapp.utils.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.message.observe(this@MainActivity, Observer {
            val newMessage = it ?: return@Observer
            binding.message.setText(newMessage)
        })

        binding.changeMessage.setOnClickListener {
            mainViewModel.changeMessageText()
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.showNotification.setOnClickListener{
            PushNotification(
                NotificationData(TITLE_NOTIFICATION, DESCRIPTION_NOTIFICATION), TOPIC
            ).also {
                sendNotification(it)
            }
        }
    }

    /**
     * Get instance of Retrofit and post notification
     *
     * @param notification PushNotification data class.
     */
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            RetrofitInstance.api.postNotification(notification)
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}