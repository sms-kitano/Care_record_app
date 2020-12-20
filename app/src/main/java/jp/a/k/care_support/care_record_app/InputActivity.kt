package jp.a.k.care_support.care_record_app

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_input.*
import java.util.*

class InputActivity : AppCompatActivity() {

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mConditionRecord: ConditionRecord? = null

    private val mOnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)
                date_button.text = dateString
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private val mOnDoneClickListener = View.OnClickListener { view ->
        addTask()
/*
        Snackbar.make(view, "保存しました", Snackbar.LENGTH_LONG)
            .show()
*/
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        title = "記録をする"

        // ActionBarを設定する TODO:nullじゃないか確認
        if (findViewById<View>(R.id.toolbar) != null) {
            val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }

        // UI部品の設定
        date_button.setOnClickListener(mOnDateClickListener)
        save_button.setOnClickListener(mOnDoneClickListener)

        // EXTRA_TASK から Task の id を取得して、 id から Task のインスタンスを取得する
        val intent = intent
        val conditionId = intent.getIntExtra(EXTRA_RECORD, -1)
        val realm = Realm.getDefaultInstance()
        mConditionRecord = realm.where(ConditionRecord::class.java).equalTo("id", conditionId).findFirst()
        realm.close()

        if (mConditionRecord == null) {
            // 新規作成の場合
            val calendar = Calendar.getInstance()
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDay = calendar.get(Calendar.DAY_OF_MONTH)

            val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)

            date_button.text = dateString

        } else {
            // 更新の場合
            editText.setText(mConditionRecord!!.memo1)
            editText2.setText(mConditionRecord!!.memo2)
            editText3.setText(mConditionRecord!!.content1)
            editText4.setText(mConditionRecord!!.content2)
            if (mConditionRecord!!.radio1 != null) {
                radio1.check(mConditionRecord!!.radio1!!.toInt())
            }
            if (mConditionRecord!!.radio2 != null) {
                radio2.check(mConditionRecord!!.radio2!!.toInt())
            }

            val calendar = Calendar.getInstance()
            calendar.time = mConditionRecord!!.date
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDay = calendar.get(Calendar.DAY_OF_MONTH)

            val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)

            date_button.text = dateString
        }
    }

    private fun addTask() {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if (mConditionRecord == null) {
            // 新規作成の場合
            mConditionRecord = ConditionRecord()

            val taskRealmResults = realm.where(ConditionRecord::class.java).findAll()

            val identifier: Int =
                if (taskRealmResults.max("id") != null) {
                    taskRealmResults.max("id")!!.toInt() + 1
                } else {
                    0
                }
            mConditionRecord!!.id = identifier
        }

        val title = title.toString()
        val memo1 = editText.text.toString()
        val memo2 = editText2.text.toString()
        val content1 = editText3.text.toString()
        val content2 = editText4.text.toString()
        //選択されているラジオボタンのIDをRealmに保存
        val radio1 = findViewById<RadioGroup>(R.id.radio1)
        val radio2 = findViewById<RadioGroup>(R.id.radio2)
        val id1 = radio1.checkedRadioButtonId.toInt()
        val id2 = radio2.checkedRadioButtonId.toInt()

        mConditionRecord!!.title = title
        mConditionRecord!!.memo1 = memo1
        mConditionRecord!!.memo2 = memo2
        mConditionRecord!!.content1 = content1
        mConditionRecord!!.content2 = content2

        mConditionRecord!!.radio1 = id1
        mConditionRecord!!.radio2 = id2


        val calendar = GregorianCalendar(mYear, mMonth, mDay)
        val date = calendar.time
        mConditionRecord!!.date = date

        realm.copyToRealmOrUpdate(mConditionRecord!!)
        realm.commitTransaction()

        realm.close()
    }
}
