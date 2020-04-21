package com.showtime

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.showtime.addschedule.AddScheduleActivity
import com.showtime.setting.NotificationReceiver
import com.showtime.setting.SettingActivity
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.tutorial.TutorialActivity
import com.showtime.ui.dashboard.DashboardFragment
import com.showtime.ui.home.HomeFragment
import com.showtime.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    lateinit var listener: SemesterListAdapter.SemesterListener
    lateinit var tran:FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.inflateMenu(R.menu.bottom_nav_menu)
        bottom_navigation.selectedItemId = R.id.navigation_dashboard
        tran = supportFragmentManager.beginTransaction()
        tran.replace(R.id.nav_host_fragment, HomeFragment()).commitAllowingStateLoss()
        bottom_navigation.setOnNavigationItemSelectedListener {
            val tran = supportFragmentManager.beginTransaction()
            when(it.itemId){
                R.id.navigation_home->{
                    tran.replace(R.id.nav_host_fragment, DashboardFragment()).commit()
                    true
                }
                R.id.navigation_dashboard->{

                    tran.replace(R.id.nav_host_fragment, HomeFragment()).commit()
                    true
                }
                R.id.navigation_notifications->{
                    tran.replace(R.id.nav_host_fragment, NotificationsFragment()).commit()
                    true
                }
                else->{
                    true
                }
            }
        }

        permissionCheck()
        init()
        if(pref.getAlarmFlag() == "true"){
            notificationInit()
        }

        if(pref.getIsFirstFlag()){
            pref.setIsFirstFlag()
            var intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
    }

    fun initRecyclerView(){
        pref = PreferenceManager(this)

        var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var adapter = SemesterListAdapter(
            this,
            pref.myData.semester,
            supportFragmentManager,
            listener
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
    }

    override fun onResume() {
        initRecyclerView()
        if(pref.getAlarmFlag() == "true"){
            notificationInit()
        }
        var f = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        tran = supportFragmentManager.beginTransaction()
        tran.detach(f!!).attach(f).commit()
        super.onResume()
    }

    fun notificationInit(){

        var alarmInfo = pref.getAlarmTime()
        var list = alarmInfo!!.split(" ")

        var hasSchedule = Calendar.getInstance()
        hasSchedule.add(Calendar.DATE, -(list[0].toInt()))

        var y= hasSchedule.get(Calendar.YEAR)
        var m= hasSchedule.get(Calendar.MONTH) + 1
        var d = hasSchedule.get(Calendar.DATE)
        var str = pref.getDaySchedule(y, m, d)
        if(str == null || str == ""){
            return
        }

        var manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, list[1].toInt())
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

//        calendar.set(Calendar.MINUTE, 15)
        var intent = Intent(this, NotificationReceiver::class.java)
        var pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
             AlarmManager.INTERVAL_DAY,
            pi);
    }

    fun init(){
        listener = object: SemesterListAdapter.SemesterListener{
            override fun refresh() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                initRecyclerView()
                var f = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                tran = supportFragmentManager.beginTransaction()
                tran.detach(f!!).attach(f).commit()
            }
        }
        initRecyclerView()

        initToolbar()

        setting_btn.setOnClickListener {
            var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }
    fun permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d("BUILD_VERSION_SDK_INT", Build.VERSION.SDK_INT.toString())
            // Permission Check
            var permissionLitsener = object: PermissionListener {
                override fun onPermissionGranted() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    CustomToast(applicationContext, "권한 허가").show()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    CustomToast(applicationContext, "권한 거부").show()
                }
            }
            TedPermission.with(this)
                .setPermissionListener(permissionLitsener)
                .setRationaleMessage("저장 공간 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
        } else {
            Log.d("BUILD_VERSION_SDK_INT", Build.VERSION.SDK_INT.toString())
        }
    }

    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                drawer_layout.openDrawer(Gravity.RIGHT)
                true
            }
            R.id.action_add ->{
                var intent = Intent(this, AddScheduleActivity::class.java)
                intent.putExtra("tableNum", pref.table)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() { //뒤로가기 처리
        if(drawer_layout.isDrawerOpen(Gravity.RIGHT)){
            drawer_layout.closeDrawers()
        } else{
            super.onBackPressed()
        }
    }
}

