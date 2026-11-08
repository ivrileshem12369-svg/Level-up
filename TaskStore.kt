package com.levelup.app
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HabitTask(val id:String,var name:String,var description:String,var category:String,var target:String,var xp:Int,var completedDate:String="")
object TaskStore {
 private const val P="levelup_data"; private const val T="tasks"; private const val X="xp"; private const val A="achievements"
 fun today()=SimpleDateFormat("yyyy-MM-dd",Locale.US).format(Date())
 private fun p(c:Context)=c.getSharedPreferences(P,Context.MODE_PRIVATE)
 fun tasks(c:Context):MutableList<HabitTask>{
  val raw=p(c).getString(T,null)
  if(raw==null){ val seed= mutableListOf(
   HabitTask("water","לשתות 8 כוסות מים","לשתות מים לאורך היום","בריאות","8 כוסות",15),
   HabitTask("walk","הליכה של 20 דקות","לצאת להליכה בקצב נוח","תנועה","20 דקות",25),
   HabitTask("read","קריאה","לקרוא ספר או חומר לימודי","למידה","10 עמודים",20),
   HabitTask("sleep","לישון 8 שעות","לתכנן זמן שינה מספק","בריאות","8 שעות",30))
   save(c,seed); return seed }
  return try { val a=JSONArray(raw); MutableList(a.length()){i-> val o=a.getJSONObject(i); HabitTask(o.getString("id"),o.getString("name"),o.optString("description"),o.optString("category"),o.optString("target"),o.optInt("xp",10),o.optString("completedDate"))} } catch(_:Exception){ mutableListOf() }
 }
 fun save(c:Context,ts:List<HabitTask>){ val a=JSONArray(); ts.forEach{a.put(JSONObject().put("id",it.id).put("name",it.name).put("description",it.description).put("category",it.category).put("target",it.target).put("xp",it.xp).put("completedDate",it.completedDate))}; p(c).edit().putString(T,a.toString()).apply(); LevelUpWidget.refresh(c) }
 fun xp(c:Context)=p(c).getInt(X,0)
 fun level(c:Context)=xp(c)/100+1
 fun completed(c:Context)=tasks(c).count{it.completedDate==today()}
 fun toggle(c:Context,id:String){ val ts=tasks(c); val t=ts.firstOrNull{it.id==id}?:return; val done=t.completedDate==today(); p(c).edit().putInt(X,(xp(c)+(if(done)-t.xp else t.xp)).coerceAtLeast(0)).apply(); t.completedDate=if(done) "" else today(); save(c,ts); recalc(c) }
 fun add(c:Context,t:HabitTask){save(c,tasks(c).apply{add(t)})}
 fun update(c:Context,t:HabitTask){val ts=tasks(c); val i=ts.indexOfFirst{it.id==t.id}; if(i<0)return; val old=ts[i]; if(old.completedDate==today())p(c).edit().putInt(X,(xp(c)-old.xp+t.xp).coerceAtLeast(0)).apply(); ts[i]=t; save(c,ts); recalc(c)}
 fun delete(c:Context,id:String){val ts=tasks(c); val t=ts.firstOrNull{it.id==id}; if(t != null && t.completedDate==today())p(c).edit().putInt(X,(xp(c)-t.xp).coerceAtLeast(0)).apply(); save(c,ts.filterNot{it.id==id}); recalc(c)}
 fun achievements(c:Context)=p(c).getStringSet(A,emptySet())?.toSet()?:emptySet()
 private fun recalc(c:Context){ val s=mutableSetOf<String>(); if(completed(c)>=1)s.add("first"); if(completed(c)>=3)s.add("three"); if(xp(c)>=100)s.add("xp100"); if(level(c)>=5)s.add("level5"); p(c).edit().putStringSet(A,s).apply() }
 fun title(c:Context)=p(c).getString("title","מתחיל")?:"מתחיל"
 fun setTitle(c:Context,s:String){p(c).edit().putString("title",s).apply()}
}
