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
import okhttp3.internal.http.promisesBody
import java.io.IOException



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
        Log.e("========>", "LAUNCH")
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_layout
            )
            try {
                get("/orgs/EpitechIT2020/repos")
            } catch (e : IOException) {
                e.printStackTrace();
            }
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

    fun get(route : String) {
        val request = Request.Builder()
                .url(api + route + "?access_token=" + token)
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("accept", "application/vnd.github.v3+json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.e("=====>", response.body!!.string())
                }
            }
        })
    }


//843e7b1e3bb15b678e93427df91f8737d81e0ddd

    companion object {
        private const val api = "https://api.github.com"
        private const val token = "843e7b1e3bb15b678e93427df91f8737d81e0ddd"
        private const val ACTION_CLICK = "ACTION_CLICK"
    }
}