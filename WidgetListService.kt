package com.levelup.app
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
class WidgetListService:RemoteViewsService(){override fun onGetViewFactory(i:Intent)=TaskFactory(applicationContext)}
class TaskFactory(private val c:android.content.Context):RemoteViewsService.RemoteViewsFactory{
 private var ts=TaskStore.tasks(c).take(5)
 override fun onCreate(){}
 override fun onDataSetChanged(){ts=TaskStore.tasks(c).take(5)}
 override fun onDestroy(){}
 override fun getCount()=ts.size
 override fun getViewAt(p:Int):RemoteViews{val t=ts[p]; val v=RemoteViews(c.packageName,R.layout.widget_task_row); val done=t.completedDate==TaskStore.today(); v.setTextViewText(R.id.task_check,if(done)"✓" else "○"); v.setTextViewText(R.id.task_name,t.name); v.setOnClickFillInIntent(R.id.task_check,Intent().putExtra(LevelUpWidget.ID,t.id)); v.setOnClickFillInIntent(R.id.task_name,Intent().putExtra(LevelUpWidget.ID,t.id)); return v}
 override fun getLoadingView():RemoteViews?=null
 override fun getViewTypeCount()=1
 override fun getItemId(p:Int)=ts[p].id.hashCode().toLong()
 override fun hasStableIds()=true
}
