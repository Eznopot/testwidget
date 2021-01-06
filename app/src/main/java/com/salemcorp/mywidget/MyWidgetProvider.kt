package com.salemcorp.mywidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews


class MyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val setence = arrayOf("Bravooo !", "Tu peut le faire !", "T'es le meilleure !")
        val thisWidget = ComponentName(
            context,
            MyWidgetProvider::class.java
        )
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds) {
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_layout
            )
            remoteViews.setTextViewText(R.id.update, setence[i])
            if (i < 2) {
                i++
            } else {
                i = 0
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

    companion object {
        var i = 0
    }
}