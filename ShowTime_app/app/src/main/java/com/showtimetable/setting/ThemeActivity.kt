package com.showtimetable.setting

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RadioButton
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.activity_theme.*

class ThemeActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        init()
    }

    fun init(){
        pref = PreferenceManager(this)
// Init AD
        val is_no_AD = pref.getNoADFlag()

        if(!is_no_AD){
            MobileAds.initialize(this) {}
            mInterstitialAd = InterstitialAd(this)
            mInterstitialAd.adUnitId = resources.getString(R.string.test_whole_ad_unit_id)
            mInterstitialAd.loadAd(AdRequest.Builder().build())
            mInterstitialAd.show()

        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }

        //시간표 무지개색상추가
        makeColor()

        //기존 시간표 색상 체크
        val currentTheme: RadioButton
        when(pref.getTheme()){
            R.array.theme1->{
                currentTheme = findViewById(R.id.theme1)
            }
            R.array.theme2->{
                currentTheme = findViewById(R.id.theme2)
            }
            R.array.theme3->{
                currentTheme = findViewById(R.id.theme3)
            }
            else ->{
                currentTheme = findViewById(R.id.theme1)
            }
        }
        currentTheme.isChecked = true

        //기존 배경 색상 체크
        val currentBackground: RadioButton
        when(pref.getTableBackgroundColor()){
            R.color.white->{
                currentBackground = findViewById(R.id.background0)
            }
            R.color.light_pink->{
                currentBackground = findViewById(R.id.background1)
            }
            R.color.light_blue->{
                currentBackground = findViewById(R.id.background2)
            }
            R.color.mint->{
                currentBackground = findViewById(R.id.background3)
            }
            else ->{
                currentBackground = findViewById(R.id.background0)
            }
        }
        currentBackground.isChecked = true

        //기존 글씨 색상 체크
        val currentFontColor: RadioButton
        when(pref.getTableFontColor()){
            R.color.black->{
                currentFontColor = findViewById(R.id.fontcolor1)
            }
            R.color.dark_grey->{
                currentFontColor = findViewById(R.id.fontcolor2)
            }
            R.color.colorPrimary->{
                currentFontColor = findViewById(R.id.fontcolor3)
            }
            R.color.dark_pink->{

                currentFontColor = findViewById(R.id.fontcolor4)
            }
            else ->{
                currentFontColor = findViewById(R.id.fontcolor1)
            }
        }
        currentFontColor.isChecked = true

        //취소 버튼 클릭
        cancelThemeBtn.setOnClickListener {
            this.finish()
        }

        //테마변경 버튼 클릭
        changeThemeBtn.setOnClickListener {
            saveTheme()
            this.finish()
            CustomToast(this, "테마가 변경되었습니다.").show()
        }
    }

    private fun saveTheme() {
        var theme=-1//시간표
        var background=-1//배경
        var font=-1//글씨
        var border=-1//테두리

        theme = when(theme_group.checkedRadioButtonId){
            R.id.theme1->R.array.theme1
            R.id.theme2->R.array.theme2
            R.id.theme3->R.array.theme3
            else->R.array.theme1
        }

        background = when(background_group.checkedRadioButtonId){
            R.id.background0->{
                border = R.color.table_border
                R.color.white
            }
            R.id.background1->{
                border = R.color.table_pink
                R.color.light_pink
            }
            R.id.background2->{
                border = R.color.table_blue
                R.color.light_blue
            }
            R.id.background3->{
                border = R.color.table_mint
                R.color.mint
            }
            else->{
                border = R.color.table_border
                R.color.white
            }
        }

        font = when(fontcolor_group.checkedRadioButtonId){
            R.id.fontcolor1->R.color.black
            R.id.fontcolor2->R.color.dark_grey
            R.id.fontcolor3->R.color.colorPrimary
            R.id.fontcolor4->R.color.dark_pink
            else->R.color.black
        }

        pref.setTheme(theme)
        pref.setTableBackgroundColor(background)
        pref.setTableFontColor(font)
        pref.setTableBorder(border)
    }

    fun makeColor(){
        val layout1 = findViewById<LinearLayout>(R.id.theme_layout1)
        val layout2 = findViewById<LinearLayout>(R.id.theme_layout2)
        val layout3 = findViewById<LinearLayout>(R.id.theme_layout3)

        val t1 = this.resources.getStringArray(R.array.theme1)
        for(i in 0..t1.size-1){
            val view = layout1.getChildAt(i)
            val color = t1[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

        val t2 = this.resources.getStringArray(R.array.theme2)
        for(i in 0..t2.size-1){
            val view = layout2.getChildAt(i)
            val color = t2[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

        val t3 = this.resources.getStringArray(R.array.theme3)
        for(i in 0..t3.size-1){
            val view = layout3.getChildAt(i)
            val color = t3[i]
            view.setBackgroundColor(Color.parseColor(color))
        }

    }
}
