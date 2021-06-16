package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.util.NotificationUtils
import com.udacity.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedUrl: String = ""
    private var selectedTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGlide -> {
                    selectedUrl = GLIDE_URL
                    selectedTitle = getString(R.string.file_name_glide)
                }
                R.id.rbUdacity -> {
                    selectedUrl = UDACITY_URL
                    selectedTitle = getString(R.string.file_name_udacity)
                }
                R.id.rbRetrofit -> {
                    selectedUrl = RETROFIT_URL
                    selectedTitle = getString(R.string.file_name_retrofit)
                }
            }
        }

        btnDownload.setOnClickListener {
            onBtnDownloadClicked()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent?.action
            if (downloadID != id || !action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) return
            val query = DownloadManager.Query().setFilterById(downloadID)
            val downloadManager =
                getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                if (cursor.count > 0) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val title =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val success = status == DownloadManager.STATUS_SUCCESSFUL
                    btnDownload.updateButtonState(ButtonState.Completed)
                    NotificationUtils.sendNotificationDownloadCompleted(context, title, success)
                }
            }
            cursor.close()
        }
    }

    private fun onBtnDownloadClicked() {
        when {
            txtCustomUrl.text.toString().isNotEmpty() -> {
                download(txtCustomUrl.text.toString(), getString(R.string.file_name_custom))
            }
            selectedUrl.isNotEmpty() -> {
                download(selectedUrl, selectedTitle)
            }
            else -> {
                Toast.makeText(this, R.string.no_selection_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun download(url: String, title: String) {
        if (!Utils.isUrlValid(url)) {
            Toast.makeText(this, getString(R.string.url_not_valid_message), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (!Utils.isNetworkAvailable(this)) {
            NotificationUtils.sendNotificationDownloadCompleted(this, title, false)
            return
        }
        btnDownload.updateButtonState(ButtonState.Loading)
        downloadID = Utils.enqueueDownload(this, url, title)
    }

    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"
    }
}
