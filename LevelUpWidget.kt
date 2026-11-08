package com.levelup.app
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class LevelUpWidget:AppWidgetProvider(){
 override fun onUpdate(c:Context,m:AppWidgetManager,ids:IntArray){ids.forEach{update(c,m,it)}}
 override fun onReceive(c:Context,i:Intent){super.onReceive(c,i); if(i.action==ACTION){i.getStringExtra(ID)?.let{TaskStore.toggle(c,it)}; refresh(c)}}
 companion object{
  const val ACTION="com.levelup.app.TOGGLE_TASK"; const val ID="task_id"
  fun refresh(c:Context){val m=AppWidgetManager.getInstance(c); m.getAppWidgetIds(ComponentName(c,LevelUpWidget::class.java)).forEach{update(c,m,it)}}
  private fun update(c:Context,m:AppWidgetManager,w:Int){
   val v=RemoteViews(c.packageName,R.layout.levelup_widget)
   v.setTextViewText(R.id.widget_xp,"${TaskStore.xp(c)} XP · רמה ${TaskStore.level(c)}")
   val svc=Intent(c,WidgetListService::class.java).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,w)
   v.setRemoteAdapter(R.id.widget_tasks,svc)
   val bi=PendingIntent.getBroadcast(c,w,Intent(c,LevelUpWidget::class.java).setAction(ACTION),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
   v.setPendingIntentTemplate(R.id.widget_tasks,bi)
   val open=PendingIntent.getActivity(c,0,Intent(c,MainActivity::class.java),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
   v.setOnClickPendingIntent(R.id.widget_title,open); v.setOnClickPendingIntent(R.id.widget_footer,open)
   m.updateAppWidget(w,v); m.notifyAppWidgetViewDataChanged(w,R.id.widget_tasks)
  }
 }
}
