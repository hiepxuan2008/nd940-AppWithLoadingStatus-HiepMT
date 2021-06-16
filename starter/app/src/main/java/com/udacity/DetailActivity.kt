package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.util.EXTRA_DOWNLOAD_SUCCEED_KEY
import com.udacity.util.EXTRA_FILE_NAME_KEY
import com.udacity.util.NOTIFICATION_DOWNLOAD_COMPLETED_ID
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(NOTIFICATION_DOWNLOAD_COMPLETED_ID)

        val fileName = intent.getStringExtra(EXTRA_FILE_NAME_KEY)
        val downloadStatus = intent.getBooleanExtra(EXTRA_DOWNLOAD_SUCCEED_KEY, false)

        txtFileName.text = fileName
        when (downloadStatus) {
            true -> {
                txtStatus.setTextColor(getColor(R.color.colorSuccess))
                txtStatus.text = getString(R.string.status_succeed)
            }
            else -> {
                txtStatus.setTextColor(getColor(R.color.colorError))
                txtStatus.text = getString(R.string.status_failed)
            }
        }

        btnOK.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
