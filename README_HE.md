# Level Up – פרויקט Android

פרויקט מקור לאפליקציית Android מקורית ב-Kotlin ו-Jetpack Compose.

כולל:
- ממשק בעברית ובכיווניות RTL
- יצירה, עריכה ומחיקה של משימות, כולל משימות ברירת מחדל
- סימון וביטול סימון עם החזרה מדויקת של XP
- רמות, הישגים בסיסיים ותארים
- שמירה מקומית במכשיר
- ווידג'ט Android אמיתי עם רשימת משימות ולחצני סימון

## יצירת APK
נדרש Android Studio, JDK 17 ו-Android SDK 35.
פתח את התיקייה כפרויקט Android, המתן ל-Gradle Sync ובחר:
Build > Build Bundle(s) / APK(s) > Build APK(s)

ה-APK יופיע ב-app/build/outputs/apk/debug/app-debug.apk

## בנייה בענן מהטלפון
קיים workflow בתיקייה .github/workflows/android.yml. אפשר להעלות את הפרויקט ל-GitHub, להריץ את הפעולה Build Level Up APK בלשונית Actions ולהוריד את ה-APK כ-artifact.

## מגבלות גרסה 1
זהו פרויקט מקור ולא APK מוכן. הנתונים נשמרים מקומית ואינם מסתנכרנים בין מכשירים. מערכת הרצפים והיסטוריית המשימות עדיין בסיסית, וניתן להרחיב אותן בהמשך.
