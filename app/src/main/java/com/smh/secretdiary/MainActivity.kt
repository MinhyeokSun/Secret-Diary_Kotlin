package com.smh.secretdiary

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy { // lazy로 설정해주는 이유: onCreate가 된 후 View에 접근을 해야하기때문.
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {    //  기기저장 방식 : 1. 로컬DB, 2. 파일에 관리 (sharedPrefereces)

            if(changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)  // 모드설정으로 나만 사용 가능함. 다른 앱 x
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                //패스워드 성공
                // TODO 다이어리 페이지 열기
                //startActivity()
            } else {
                //패스워드 실패
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {
                //번호를 저장하는 기능
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)



            } else {
                // changePasswordMode 활성화 :: 비밀번호 맞는지 체크해야함.
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {

                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)

                } else {
                    showErrorAlertDialog()
                }

            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()

    }
}