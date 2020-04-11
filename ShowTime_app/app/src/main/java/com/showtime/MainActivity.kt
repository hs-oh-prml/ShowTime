package com.showtime

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.showtime.addschedule.AddScheduleActivity
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.ui.dashboard.DashboardFragment
import com.showtime.ui.dashboard.SemesterListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.fragment_dashboard.*

class MainActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)


        init()
    }
    fun init(){
        pref = PreferenceManager(this)
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

            R.id.action_settings -> {
                drawer_layout.openDrawer(Gravity.RIGHT)
                var pref :PreferenceManager= PreferenceManager(this)
                var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                var adapter = SemesterListAdapter(this, pref.myData.semester)
                recycler_view.layoutManager = layoutManager
                recycler_view.adapter = adapter
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

