package com.showtimetable.setting
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.sharedpreference.PreferenceManager
import com.showtimetable.tutorial.TutorialActivity
import com.showtimetable.widget.WidgetSettingActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.random.Random


class SettingActivity : AppCompatActivity(),
    BillingProcessor.IBillingHandler
{
    override fun onBillingInitialized() {
//        TODO("Not yet implemented")
        pref.setNoADFlag((bp.isPurchased(products[0])||bp.isPurchased(products[1])||bp.isPurchased(products[2])))
    }

    override fun onPurchaseHistoryRestored() {
//        TODO("Not yet implemented")
        pref.setNoADFlag((bp.isPurchased(products[0])||bp.isPurchased(products[1])||bp.isPurchased(products[2])))
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
//        TODO("Not yet implemented")
        if(productId == products[0]){
            pref.setNoADFlag(true)
        } else if (productId == products[0]){
            pref.setNoADFlag(true)
        } else {
            pref.setNoADFlag(true)
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        TODO("Not yet implemented")
        Log.d("BILLING", error.toString())
    }

    lateinit var bp: BillingProcessor
    lateinit var pref: PreferenceManager
    val BIAS = 5

    var licenseStr = ""
    var GOOGLEPLAYLICENSEKEY = ""

    lateinit var products: Array<String>
    lateinit var appreciate: Array<String>

    override fun onDestroy() {
        if(bp != null){
            bp.release()
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(!bp.handleActivityResult(requestCode, resultCode, data)){
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        products = resources.getStringArray(R.array.product)
        appreciate = resources.getStringArray(R.array.appreciate)
        pref = PreferenceManager(this)

        //  init billing
        GOOGLEPLAYLICENSEKEY = resources.getString(R.string.google_play_license_key)
        bp = BillingProcessor(this, GOOGLEPLAYLICENSEKEY, this);
        bp.initialize();

        initLicense()
        init()
    }

    fun initLicense(){
        var input_stream = resources.openRawResource(R.raw.opensourcelicense)
        var baos = ByteArrayOutputStream()
        try {
            var i = input_stream.read()
            while(i != -1){
                baos.write(i)
                i = input_stream.read()
            }

        } catch (e:IOException){

        }
        licenseStr = baos.toString()
    }

    fun init(){

        push_switch.isChecked = pref.getAlarmFlag() == "true"

        d_day_alarm.minValue = 0
        d_day_alarm.maxValue = 5
        d_day_alarm.displayedValues = arrayOf("D-5", "D-4", "D-3", "D-2", "D-1", "D-Day")
        d_day_alarm.wrapSelectorWheel = false
        d_day_alarm.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        hour_alarm.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        hour_alarm.minValue = 0
        hour_alarm.maxValue = 23
        hour_alarm.displayedValues = arrayOf("12 AM",
            "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM",
            "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM","6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM")
        var setAlarm = pref.getAlarmTime()!!.split(" ")
        Log.d("ALARM INFO", setAlarm.toString())
        d_day_alarm.value = setAlarm[0].toInt() + BIAS
        hour_alarm.value = setAlarm[1].toInt()
        Log.d("ALARM INFO", (setAlarm[0].toInt() + BIAS).toString())

        //푸시알람
        push_switch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                pref.setAlarmFlag("true")
                val str = "푸시 알람을 설정합니다."
                CustomToast(this, str).show()
                alarm_detail.visibility = VISIBLE
            } else {
                pref.setAlarmFlag("false")
                val str = "푸시 알람을 해제합니다."
                CustomToast(this, str).show()
                alarm_detail.visibility = GONE
            }
        }
        set_push_alarm.setOnClickListener {
            if(alarm_detail.visibility == VISIBLE){
                alarm_detail.visibility = GONE
            } else {
                if(push_switch.isChecked)
                    alarm_detail.visibility = VISIBLE
                else
                    alarm_detail.visibility = GONE
            }
        }

        //위젯설정
        setWidget.setOnClickListener {
            var intent = Intent(this, WidgetSettingActivity::class.java)
            startActivity(intent)
        }


        save_d_day.setOnClickListener {
            var d_day = d_day_alarm.value - BIAS
            var d_hour = hour_alarm.value
            Log.d("D_DAY_HOUR", "${d_day} ${d_hour}")
            pref.setAlarmTime(d_day, d_hour)
            alarm_detail.visibility = GONE
            val str = d_day.toString()+", "+d_hour.toString()+" 으로 설정되었습니다."
            CustomToast(this, str).show()

        }

        //테마변경
        setColorTheme.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
        }
        //테마선택시
//        theme_group.setOnCheckedChangeListener { radioGroup, i ->
//
//            val is_no_AD = pref.getNoADFlag()
//            if(!is_no_AD){
//                mInterstitialAd.show()
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.")
//            }
//
//            when(i){
//                R.id.theme1->{
//                    pref.setTheme(R.array.theme1)
//                    val str = "테마가 ["+theme1.text.toString()+"] 으로 변경되었습니다."
//                    CustomToast(this, str).show()
//                }
//                R.id.theme2->{
//                    pref.setTheme(R.array.theme2)
//                    val str = "테마가 ["+theme2.text.toString()+"] 으로 변경되었습니다."
//                    CustomToast(this, str).show()
//                }
//                else->{
//                    pref.setTheme(R.array.theme3)
//                    val str = "테마가 ["+theme3.text.toString()+"] 으로 변경되었습니다."
//                    CustomToast(this, str).show()
//                }
//            }
//        }

        license.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("오픈소스 라이센스 정보")
            builder.setMessage(licenseStr)
            builder.setNeutralButton("닫기") { _, _ ->
            }
            val dialog: android.app.AlertDialog = builder.create()
            dialog.show()

        }
        //뒤로가기
        setting_close.setOnClickListener {
            this.finish()
        }
        //앱 사용설명서
        guide.setOnClickListener {
            var intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }

        remove_ad.setOnClickListener {
            if (pref.getNoADFlag()) {
                val str = "이미 구입하였습니다."
                CustomToast(this, str).show()

            } else {
                bp.purchase(this, products[0]);
            }
        }
        gift_coffee.setOnClickListener {
            if (pref.getNoADFlag()) {

                val idx = (appreciate.indices).random()
                val str = appreciate[idx]
                CustomToast(this, str).show()
            } else {
                bp.purchase(this, products[1]);
            }

        }
        gift_chicken.setOnClickListener {
            if (pref.getNoADFlag()) {
                val idx = (appreciate.indices).random()
                val str = appreciate[idx]
                CustomToast(this, str).show()

            } else {
                bp.purchase(this, products[2]);
            }

        }
//        makeColor()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }


}
