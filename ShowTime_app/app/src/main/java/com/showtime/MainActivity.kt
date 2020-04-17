package com.showtime

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
//        val navView: CustomBottomNavigationView = findViewById(R.id.nav_view)
        //bottom_navigation.inflateMenu(R.menu.bottom_nav_menu)
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
//        val floatingBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton)
//        floatingBtn.setOnClickListener {
//            val tran = supportFragmentManager.beginTransaction()
//            tran.replace(R.id.nav_host_fragment, HomeFragment()).commitAllowingStateLoss()
//        }

//
//            .setOnClickListener {
//            Toast.makeText(this,"??",Toast.LENGTH_SHORT).show()
//            val tran = supportFragmentManager.beginTransaction()
//            tran.replace(R.id.nav_host_fragment, HomeFragment()).commitAllowingStateLoss()



//        val navController = findNavController(R.id.nav_host_fragment)
//        bottom_navigation.setupWithNavController(navController)

        init()
        if(pref.getAlarmFlag() == "true"){
            notificationInit()
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
        super.onResume()
    }

    fun notificationInit(){
        var manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var calendar = Calendar.getInstance()

//        calendar.set(Calendar.MINUTE, 15)
        var intent = Intent(this, NotificationReceiver::class.java)
        var pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d("TRIGGER TIME", "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}")
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            1000,
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
//                if(f is HomeFragment){
//                    tran.detach(f).attach(HomeFragment()).commit()
//                detach(f!!).attach(f).commit()
//                }
            }
        }
        initRecyclerView()

        // Permission Check
        var permissionLitener = object: PermissionListener {
            override fun onPermissionGranted() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionLitener)
            .setRationaleMessage("저장 공간 접근 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한]에서 권한을 허용할 수 있습니다.")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()

        initToolbar()
    }
    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get the ActionBar here to configure the way it behaves.
//        val actionBar = supportActionBar
//        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
//        actionBar.setDisplayShowTitleEnabled(false)
//
//        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
//        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_white) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
//        toolbar.bringToFront()
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
            R.id.action_widget->{
                var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_settings -> {
                drawer_layout.openDrawer(Gravity.RIGHT)
                true
            }
            R.id.action_add ->{
                var intent = Intent(this, AddScheduleActivity::class.java)
                intent.putExtra("tableNum", pref.table)
                startActivity(intent)
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

