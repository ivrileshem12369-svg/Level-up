package com.levelup.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID
private val BG=Color(0xFF10131D); private val CARD=Color(0xFF1A1F2C); private val GREEN=Color(0xFF55DDA2); private val MUTED=Color(0xFF9AA4B5)
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState); setContent{App()}}}
@Composable fun App(){
 val c=androidx.compose.ui.platform.LocalContext.current
 var tick by remember{mutableIntStateOf(0)}; var tab by remember{mutableStateOf("משימות")}; var editor by remember{mutableStateOf(false)}; var selected by remember{mutableStateOf<HabitTask?>(null)}
 val ts=remember(tick){TaskStore.tasks(c)}; val xp=remember(tick){TaskStore.xp(c)}; val level=remember(tick){TaskStore.level(c)}; val done=remember(tick){TaskStore.completed(c)}; val achievements=remember(tick){TaskStore.achievements(c)}
 MaterialTheme(colorScheme=darkColorScheme(primary=GREEN,background=BG,surface=CARD,onSurface=Color.White)){
  CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl){
   Scaffold(containerColor=BG,topBar={Column(Modifier.fillMaxWidth().padding(18.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("⚡",color=GREEN,fontSize=30.sp);Text("LEVEL UP",fontSize=26.sp,fontWeight=FontWeight.Black)}; Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("משימות","הישגים","פרופיל").forEach{FilterChip(selected=tab==it,onClick={tab=it},label={Text(it)})}}}}){pad->
    Column(Modifier.fillMaxSize().padding(pad).padding(horizontal=16.dp)){
     when(tab){
      "משימות"->{Text("יום חדש. עוד צעד קדימה.",color=GREEN,fontSize=24.sp,fontWeight=FontWeight.Bold); BoxCard{Text("הרמה הנוכחית שלך",color=MUTED);Text("LVL $level",color=GREEN,fontSize=34.sp,fontWeight=FontWeight.Black);LinearProgressIndicator(progress={(xp%100)/100f},modifier=Modifier.fillMaxWidth());Text("$xp XP · ${100-xp%100} XP לרמה הבאה",color=MUTED)}
       Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){BoxCard(Modifier.weight(1f)){Text("התקדמות היום",color=MUTED);Text("$done / ${ts.size}",fontSize=22.sp)};BoxCard(Modifier.weight(1f)){Text("נקודות ניסיון",color=MUTED);Text("$xp XP",color=GREEN,fontSize=22.sp)}}
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("המשימות שלי",fontSize=21.sp,fontWeight=FontWeight.Bold);Button(onClick={selected=null;editor=true}){Icon(Icons.Default.Add,null);Text("חדשה")}}
       LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=20.dp)){items(ts, key = { it.id }){t->val isDone=t.completedDate==TaskStore.today(); BoxCard{Row(verticalAlignment=Alignment.CenterVertically){Checkbox(isDone,{TaskStore.toggle(c,t.id);tick++});Column(Modifier.weight(1f)){Text(t.name,fontWeight=FontWeight.Bold);Text(t.description,color=MUTED,fontSize=12.sp);Text("יעד: ${t.target} · ${t.category}",color=MUTED,fontSize=12.sp)};Text("+${t.xp} XP",color=Color(0xFFFFC857))}};Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End){IconButton(onClick={selected=t;editor=true}){Icon(Icons.Default.Edit,null,tint=GREEN)};IconButton(onClick={TaskStore.delete(c,t.id);tick++}){Icon(Icons.Default.Delete,null,tint=Color(0xFFFF7777))}}}}}
      "הישגים"->{Text("🏆 ההישגים שלי",color=GREEN,fontSize=24.sp);listOf(Triple("first","🌱 הצעד הראשון","השלמת משימה ראשונה"),Triple("three","🔥 מתחילים להתחזק","3 משימות ביום"),Triple("xp100","💎 אספן XP","צבירת 100 XP"),Triple("level5","👑 אלוף","הגעה לרמה 5")).forEach{a->BoxCard{Row(verticalAlignment=Alignment.CenterVertically){Text(if(achievements.contains(a.first))"🏅" else "🔒",fontSize=24.sp);Column(Modifier.weight(1f)){Text(a.second,fontWeight=FontWeight.Bold);Text(a.third,color=MUTED,fontSize=12.sp)};if(achievements.contains(a.first))Text("נפתח!",color=GREEN)}}}}
      else->{Text("הפרופיל שלי",color=GREEN,fontSize=24.sp);BoxCard{Text("LEVEL UP PLAYER",fontSize=22.sp,fontWeight=FontWeight.Black);Text("רמה $level · $xp XP");Text("בחירת תואר",color=GREEN);listOf("מתחיל","מתמיד","לוחם","אלוף","אגדה").forEach{TextButton(onClick={TaskStore.setTitle(c,it);tick++}){Text("בחר: $it")}};Text("התואר הנוכחי: ${TaskStore.title(c)}")}}
     }
    }
   }
   if(editor)Editor(selected,{editor=false}){t->if(selected==null)TaskStore.add(c,t)else TaskStore.update(c,t);editor=false;tick++}
  }
 }
}
@Composable private fun BoxCard(modifier:Modifier=Modifier,content:@Composable ColumnScope.()->Unit){Surface(modifier=modifier.fillMaxWidth(),color=CARD,shape=RoundedCornerShape(18.dp)){Column(Modifier.padding(13.dp),verticalArrangement=Arrangement.spacedBy(6.dp),content=content)}}
@Composable private fun Editor(old:HabitTask?,close:()->Unit,save:(HabitTask)->Unit){
 var name by remember{mutableStateOf(old?.name?:"")};var desc by remember{mutableStateOf(old?.description?:"")};var cat by remember{mutableStateOf(old?.category?:"בריאות")};var target by remember{mutableStateOf(old?.target?:"")};var xp by remember{mutableStateOf((old?.xp?:20).toString())}
 AlertDialog(onDismissRequest=close,title={Text(if(old==null)"יצירת משימה" else "עריכת משימה")},text={Column(verticalArrangement=Arrangement.spacedBy(6.dp)){OutlinedTextField(name,{name=it},label={Text("שם המשימה")});OutlinedTextField(desc,{desc=it},label={Text("תיאור")});OutlinedTextField(cat,{cat=it},label={Text("קטגוריה")});OutlinedTextField(target,{target=it},label={Text("יעד מדיד")});OutlinedTextField(xp,{xp=it.filter(Char::isDigit)},label={Text("XP")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number))}},confirmButton={TextButton(enabled=name.isNotBlank()&&target.isNotBlank()&&(xp.toIntOrNull()?:0)>0,onClick={save(HabitTask(old?.id?:UUID.randomUUID().toString(),name,desc,cat,target,(xp.toIntOrNull()?:20).coerceIn(1,500),old?.completedDate?:""))}){Text("שמירה")}},dismissButton={TextButton(onClick=close){Text("ביטול")}})
}
