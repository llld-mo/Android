<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical"
android:padding="16dp">

<TextView
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="历史得分"
android:textSize="24sp"
android:textStyle="bold"
android:layout_gravity="center_horizontal"
android:layout_marginTop="20dp"/>

<ListView
android:id="@+id/scoreListView"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:layout_marginTop="20dp"/>
</LinearLayout>