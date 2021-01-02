package com.salemcorp.mywidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import okhttp3.*
import java.io.IOException
import java.util.*



class MyWidgetProvider : AppWidgetProvider() {
    private val client = OkHttpClient()

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val thisWidget = ComponentName(
            context,
            MyWidgetProvider::class.java
        )
        lateinit var text : String
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {

            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_layout
            )
            run(remoteViews)
            Log.w("WidgetExample", text)

            val intent = Intent(context, MyWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

    }

    fun run(remoteViews : RemoteViews) {
        val request = Request.Builder()
            .url("https://intra.epitech.eu")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    remoteViews.setTextViewText(R.id.update, response.body!!.string())
                }
            }
        })
    }

    companion object {
        private const val ACTION_CLICK = "ACTION_CLICK"
    }
}