package com.showtime.setting
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.utils.Utils.init
import com.showtime.CustomToast
import com.showtime.R
import com.showtime.sharedpreference.PreferenceManager
import com.showtime.widget.WidgetSettingActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.FileOutputStream


class SettingActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    val BIAS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()
    }
    val licenseStr="com.github.PhilJay:MPAndroidChart:v3.1.0\n" +
            "\n" +
            "Copyright 2019 Philipp Jahoda\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n" +
            "\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.\n" +
            "\n" +
            "\n" +
            "com.google.code.gson:gson:2.8.6\n" +
            "\n" +
            " Apache License\n" +
            "Version 2.0, January 2004\n" +
            "http://www.apache.org/licenses/\n" +
            "\n" +
            "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
            "\n" +
            "1. Definitions.\n" +
            "\n" +
            "\"License\" shall mean the terms and conditions for use, reproduction,\n" +
            "and distribution as defined by Sections 1 through 9 of this document.\n" +
            "\n" +
            "\"Licensor\" shall mean the copyright owner or entity authorized by\n" +
            "the copyright owner that is granting the License.\n" +
            "\n" +
            "\"Legal Entity\" shall mean the union of the acting entity and all\n" +
            "other entities that control, are controlled by, or are under common\n" +
            "control with that entity. For the purposes of this definition,\n" +
            "\"control\" means (i) the power, direct or indirect, to cause the\n" +
            "direction or management of such entity, whether by contract or\n" +
            "otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
            "outstanding shares, or (iii) beneficial ownership of such entity.\n" +
            "\n" +
            "\"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
            "exercising permissions granted by this License.\n" +
            "\n" +
            "\"Source\" form shall mean the preferred form for making modifications,\n" +
            "including but not limited to software source code, documentation\n" +
            "source, and configuration files.\n" +
            "\n" +
            "\"Object\" form shall mean any form resulting from mechanical\n" +
            "transformation or translation of a Source form, including but\n" +
            "not limited to compiled object code, generated documentation,\n" +
            "and conversions to other media types.\n" +
            "\n" +
            "\"Work\" shall mean the work of authorship, whether in Source or\n" +
            "Object form, made available under the License, as indicated by a\n" +
            "copyright notice that is included in or attached to the work\n" +
            "(an example is provided in the Appendix below).\n" +
            "\n" +
            "\"Derivative Works\" shall mean any work, whether in Source or Object\n" +
            "form, that is based on (or derived from) the Work and for which the\n" +
            "editorial revisions, annotations, elaborations, or other modifications\n" +
            "represent, as a whole, an original work of authorship. For the purposes\n" +
            "of this License, Derivative Works shall not include works that remain\n" +
            "separable from, or merely link (or bind by name) to the interfaces of,\n" +
            "the Work and Derivative Works thereof.\n" +
            "\n" +
            "\"Contribution\" shall mean any work of authorship, including\n" +
            "the original version of the Work and any modifications or additions\n" +
            "to that Work or Derivative Works thereof, that is intentionally\n" +
            "submitted to Licensor for inclusion in the Work by the copyright owner\n" +
            "or by an individual or Legal Entity authorized to submit on behalf of\n" +
            "the copyright owner. For the purposes of this definition, \"submitted\"\n" +
            "means any form of electronic, verbal, or written communication sent\n" +
            "to the Licensor or its representatives, including but not limited to\n" +
            "communication on electronic mailing lists, source code control systems,\n" +
            "and issue tracking systems that are managed by, or on behalf of, the\n" +
            "Licensor for the purpose of discussing and improving the Work, but\n" +
            "excluding communication that is conspicuously marked or otherwise\n" +
            "designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
            "\n" +
            "\"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
            "on behalf of whom a Contribution has been received by Licensor and\n" +
            "subsequently incorporated within the Work.\n" +
            "\n" +
            "2. Grant of Copyright License. Subject to the terms and conditions of\n" +
            "this License, each Contributor hereby grants to You a perpetual,\n" +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
            "copyright license to reproduce, prepare Derivative Works of,\n" +
            "publicly display, publicly perform, sublicense, and distribute the\n" +
            "Work and such Derivative Works in Source or Object form.\n" +
            "\n" +
            "3. Grant of Patent License. Subject to the terms and conditions of\n" +
            "this License, each Contributor hereby grants to You a perpetual,\n" +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
            "(except as stated in this section) patent license to make, have made,\n" +
            "use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
            "where such license applies only to those patent claims licensable\n" +
            "by such Contributor that are necessarily infringed by their\n" +
            "Contribution(s) alone or by combination of their Contribution(s)\n" +
            "with the Work to which such Contribution(s) was submitted. If You\n" +
            "institute patent litigation against any entity (including a\n" +
            "cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
            "or a Contribution incorporated within the Work constitutes direct\n" +
            "or contributory patent infringement, then any patent licenses\n" +
            "granted to You under this License for that Work shall terminate\n" +
            "as of the date such litigation is filed.\n" +
            "\n" +
            "4. Redistribution. You may reproduce and distribute copies of the\n" +
            "Work or Derivative Works thereof in any medium, with or without\n" +
            "modifications, and in Source or Object form, provided that You\n" +
            "meet the following conditions:\n" +
            "\n" +
            "(a) You must give any other recipients of the Work or\n" +
            "Derivative Works a copy of this License; and\n" +
            "\n" +
            "(b) You must cause any modified files to carry prominent notices\n" +
            "stating that You changed the files; and\n" +
            "\n" +
            "(c) You must retain, in the Source form of any Derivative Works\n" +
            "that You distribute, all copyright, patent, trademark, and\n" +
            "attribution notices from the Source form of the Work,\n" +
            "excluding those notices that do not pertain to any part of\n" +
            "the Derivative Works; and\n" +
            "\n" +
            "(d) If the Work includes a \"NOTICE\" text file as part of its\n" +
            "distribution, then any Derivative Works that You distribute must\n" +
            "include a readable copy of the attribution notices contained\n" +
            "within such NOTICE file, excluding those notices that do not\n" +
            "pertain to any part of the Derivative Works, in at least one\n" +
            "of the following places: within a NOTICE text file distributed\n" +
            "as part of the Derivative Works; within the Source form or\n" +
            "documentation, if provided along with the Derivative Works; or,\n" +
            "within a display generated by the Derivative Works, if and\n" +
            "wherever such third-party notices normally appear. The contents\n" +
            "of the NOTICE file are for informational purposes only and\n" +
            "do not modify the License. You may add Your own attribution\n" +
            "notices within Derivative Works that You distribute, alongside\n" +
            "or as an addendum to the NOTICE text from the Work, provided\n" +
            "that such additional attribution notices cannot be construed\n" +
            "as modifying the License.\n" +
            "\n" +
            "You may add Your own copyright statement to Your modifications and\n" +
            "may provide additional or different license terms and conditions\n" +
            "for use, reproduction, or distribution of Your modifications, or\n" +
            "for any such Derivative Works as a whole, provided Your use,\n" +
            "reproduction, and distribution of the Work otherwise complies with\n" +
            "the conditions stated in this License.\n" +
            "\n" +
            "5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
            "any Contribution intentionally submitted for inclusion in the Work\n" +
            "by You to the Licensor shall be under the terms and conditions of\n" +
            "this License, without any additional terms or conditions.\n" +
            "Notwithstanding the above, nothing herein shall supersede or modify\n" +
            "the terms of any separate license agreement you may have executed\n" +
            "with Licensor regarding such Contributions.\n" +
            "\n" +
            "6. Trademarks. This License does not grant permission to use the trade\n" +
            "names, trademarks, service marks, or product names of the Licensor,\n" +
            "except as required for reasonable and customary use in describing the\n" +
            "origin of the Work and reproducing the content of the NOTICE file.\n" +
            "\n" +
            "7. Disclaimer of Warranty. Unless required by applicable law or\n" +
            "agreed to in writing, Licensor provides the Work (and each\n" +
            "Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
            "implied, including, without limitation, any warranties or conditions\n" +
            "of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
            "PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
            "appropriateness of using or redistributing the Work and assume any\n" +
            "risks associated with Your exercise of permissions under this License.\n" +
            "\n" +
            "8. Limitation of Liability. In no event and under no legal theory,\n" +
            "whether in tort (including negligence), contract, or otherwise,\n" +
            "unless required by applicable law (such as deliberate and grossly\n" +
            "negligent acts) or agreed to in writing, shall any Contributor be\n" +
            "liable to You for damages, including any direct, indirect, special,\n" +
            "incidental, or consequential damages of any character arising as a\n" +
            "result of this License or out of the use or inability to use the\n" +
            "Work (including but not limited to damages for loss of goodwill,\n" +
            "work stoppage, computer failure or malfunction, or any and all\n" +
            "other commercial damages or losses), even if such Contributor\n" +
            "has been advised of the possibility of such damages.\n" +
            "\n" +
            "9. Accepting Warranty or Additional Liability. While redistributing\n" +
            "the Work or Derivative Works thereof, You may choose to offer,\n" +
            "and charge a fee for, acceptance of support, warranty, indemnity,\n" +
            "or other liability obligations and/or rights consistent with this\n" +
            "License. However, in accepting such obligations, You may act only\n" +
            "on Your own behalf and on Your sole responsibility, not on behalf\n" +
            "of any other Contributor, and only if You agree to indemnify,\n" +
            "defend, and hold each Contributor harmless for any liability\n" +
            "incurred by, or claims asserted against, such Contributor by reason\n" +
            "of your accepting any such warranty or additional liability.\n" +
            "\n" +
            "END OF TERMS AND CONDITIONS\n" +
            "\n" +
            "APPENDIX: How to apply the Apache License to your work.\n" +
            "\n" +
            "To apply the Apache License to your work, attach the following\n" +
            "boilerplate notice, with the fields enclosed by brackets \"[]\"\n" +
            "replaced with your own identifying information. (Don't include\n" +
            "the brackets!)  The text should be enclosed in the appropriate\n" +
            "comment syntax for the file format. We also recommend that a\n" +
            "file or class name and description of purpose be included on the\n" +
            "same \"printed page\" as the copyright notice for easier\n" +
            "identification within third-party archives.\n" +
            "\n" +
            "Copyright [yyyy] [name of copyright owner]\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "you may not use this file except in compliance with the License.\n" +
            "You may obtain a copy of the License at\n" +
            "\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software\n" +
            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "See the License for the specific language governing permissions and\n" +
            "limitations under the License.\n" +
            "\n" +
            "gun0912.ted:tedpermission:2.2.3\n" +
            "\n" +
            "Copyright 2017 Ted Park\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "you may not use this file except in compliance with the License.\n" +
            "You may obtain a copy of the License at\n" +
            "\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software\n" +
            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "See the License for the specific language governing permissions and\n" +
            "limitations under the License."+
            "\nCopyright (c) 2012, LatinoType (luciano@latinotype.com), with Reserved Font Names 'Julius'\n" +
            "\n" +
            "This Font Software is licensed under the SIL Open Font License, Version 1.1.\n" +
            "This license is copied below, and is also available with a FAQ at:\n" +
            "http://scripts.sil.org/OFL\n" +
            "\n" +
            "\n" +
            "-----------------------------------------------------------\n" +
            "SIL OPEN FONT LICENSE Version 1.1 - 26 February 2007\n" +
            "-----------------------------------------------------------\n" +
            "\n" +
            "PREAMBLE\n" +
            "The goals of the Open Font License (OFL) are to stimulate worldwide\n" +
            "development of collaborative font projects, to support the font creation\n" +
            "efforts of academic and linguistic communities, and to provide a free and\n" +
            "open framework in which fonts may be shared and improved in partnership\n" +
            "with others.\n" +
            "\n" +
            "The OFL allows the licensed fonts to be used, studied, modified and\n" +
            "redistributed freely as long as they are not sold by themselves. The\n" +
            "fonts, including any derivative works, can be bundled, embedded, \n" +
            "redistributed and/or sold with any software provided that any reserved\n" +
            "names are not used by derivative works. The fonts and derivatives,\n" +
            "however, cannot be released under any other type of license. The\n" +
            "requirement for fonts to remain under this license does not apply\n" +
            "to any document created using the fonts or their derivatives.\n" +
            "\n" +
            "DEFINITIONS\n" +
            "\"Font Software\" refers to the set of files released by the Copyright\n" +
            "Holder(s) under this license and clearly marked as such. This may\n" +
            "include source files, build scripts and documentation.\n" +
            "\n" +
            "\"Reserved Font Name\" refers to any names specified as such after the\n" +
            "copyright statement(s).\n" +
            "\n" +
            "\"Original Version\" refers to the collection of Font Software components as\n" +
            "distributed by the Copyright Holder(s).\n" +
            "\n" +
            "\"Modified Version\" refers to any derivative made by adding to, deleting,\n" +
            "or substituting -- in part or in whole -- any of the components of the\n" +
            "Original Version, by changing formats or by porting the Font Software to a\n" +
            "new environment.\n" +
            "\n" +
            "\"Author\" refers to any designer, engineer, programmer, technical\n" +
            "writer or other person who contributed to the Font Software.\n" +
            "\n" +
            "PERMISSION & CONDITIONS\n" +
            "Permission is hereby granted, free of charge, to any person obtaining\n" +
            "a copy of the Font Software, to use, study, copy, merge, embed, modify,\n" +
            "redistribute, and sell modified and unmodified copies of the Font\n" +
            "Software, subject to the following conditions:\n" +
            "\n" +
            "1) Neither the Font Software nor any of its individual components,\n" +
            "in Original or Modified Versions, may be sold by itself.\n" +
            "\n" +
            "2) Original or Modified Versions of the Font Software may be bundled,\n" +
            "redistributed and/or sold with any software, provided that each copy\n" +
            "contains the above copyright notice and this license. These can be\n" +
            "included either as stand-alone text files, human-readable headers or\n" +
            "in the appropriate machine-readable metadata fields within text or\n" +
            "binary files as long as those fields can be easily viewed by the user.\n" +
            "\n" +
            "3) No Modified Version of the Font Software may use the Reserved Font\n" +
            "Name(s) unless explicit written permission is granted by the corresponding\n" +
            "Copyright Holder. This restriction only applies to the primary font name as\n" +
            "presented to the users.\n" +
            "\n" +
            "4) The name(s) of the Copyright Holder(s) or the Author(s) of the Font\n" +
            "Software shall not be used to promote, endorse or advertise any\n" +
            "Modified Version, except to acknowledge the contribution(s) of the\n" +
            "Copyright Holder(s) and the Author(s) or with their explicit written\n" +
            "permission.\n" +
            "\n" +
            "5) The Font Software, modified or unmodified, in part or in whole,\n" +
            "must be distributed entirely under this license, and must not be\n" +
            "distributed under any other license. The requirement for fonts to\n" +
            "remain under this license does not apply to any document created\n" +
            "using the Font Software.\n" +
            "\n" +
            "TERMINATION\n" +
            "This license becomes null and void if any of the above conditions are\n" +
            "not met.\n" +
            "\n" +
            "DISCLAIMER\n" +
            "THE FONT SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
            "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO ANY WARRANTIES OF\n" +
            "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT\n" +
            "OF COPYRIGHT, PATENT, TRADEMARK, OR OTHER RIGHT. IN NO EVENT SHALL THE\n" +
            "COPYRIGHT HOLDER BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,\n" +
            "INCLUDING ANY GENERAL, SPECIAL, INDIRECT, INCIDENTAL, OR CONSEQUENTIAL\n" +
            "DAMAGES, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING\n" +
            "FROM, OUT OF THE USE OR INABILITY TO USE THE FONT SOFTWARE OR FROM\n" +
            "OTHER DEALINGS IN THE FONT SOFTWARE.\n"
    fun init(){

        pref = PreferenceManager(this)
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
        //위젯설정
        setWidget.setOnClickListener {
            var intent = Intent(this, WidgetSettingActivity::class.java)
            startActivity(intent)
        }

        set_push_alarm.setOnClickListener {
            if(alarm_detail.visibility == VISIBLE){
                alarm_detail.visibility = GONE
            } else {
                alarm_detail.visibility = VISIBLE
            }
        }
        save_d_day.setOnClickListener {
            var d_day = d_day_alarm.value - BIAS
            var d_hour = hour_alarm.value
            Log.d("D_DAY_HOUR", "${d_day} ${d_hour}")
            pref.setAlarmTime(d_day, d_hour)
            alarm_detail.visibility = GONE

        }

        //테마변경
        setColorTheme.setOnClickListener {
            if(theme_detail.visibility == GONE){
                theme_detail.visibility = VISIBLE
            }else{
                theme_detail.visibility = GONE
            }
        }
        //테마선택시
        theme_group.setOnCheckedChangeListener { radioGroup, i ->

            when(i){
                R.id.theme1->{
                    pref.setTheme(R.array.theme1)
                    val str = theme1.text.toString()+"으로 변경되었습니다."
                    CustomToast(this, str).show()
                }
                R.id.theme2->{
                    pref.setTheme(R.array.theme2)
                    val str = theme2.text.toString()+"으로 변경되었습니다."
                    CustomToast(this, str).show()
                }
                else->{
                    pref.setTheme(R.array.theme3)
                    val str = theme3.text.toString()+"으로 변경되었습니다."
                    CustomToast(this, str).show()
                }
            }
        }

        license.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("라이센스 정보")
            builder.setMessage(licenseStr)
            builder.setNeutralButton("닫기") { _, _ ->
            }
            val dialog:AlertDialog = builder.create()
            dialog.show()

        }
        //뒤로가기
        setting_close.setOnClickListener {
            this.finish()
        }
        //앱 사용설명서
        guide.setOnClickListener {

        }

        makeColor()
    }


    fun str2Bitmap(encodedStr:String): Bitmap {
        var encodeByte = Base64.decode(encodedStr, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        return bitmap
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
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
