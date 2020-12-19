package jp.a.k.care_support.care_record_app

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.Sort

import kotlinx.android.synthetic.main.activity_date_list.*
import java.util.*

const val EXTRA_RECORD = "jp.a.k.care_support.care_record_app.ConditionRecord"

class DateListActivity : AppCompatActivity() {
    private lateinit var mConditionAdapter: ConditionAdapter
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_list)
        //setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this@DateListActivity, InputActivity::class.java)
            startActivity(intent)
        }

        title = "日付から探す"

        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        // ListViewの設定
        mConditionAdapter = ConditionAdapter(this@DateListActivity)

        // ListViewをタップしたときの処理
        listView1.setOnItemClickListener { parent, view, position, id ->
            // 入力・編集する画面に遷移させる
            val condition = parent.adapter.getItem(position) as ConditionRecord
            val intent = Intent(this@DateListActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_RECORD, condition.id)
            startActivity(intent)
        }

        // ListViewを長押ししたときの処理
        listView1.setOnItemLongClickListener { parent, view, position, id ->
            // タスクを削除する
            val condition = parent.adapter.getItem(position) as ConditionRecord

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@DateListActivity)

            builder.setTitle("削除")
            builder.setMessage(condition.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                val results =
                    mRealm.where(ConditionRecord::class.java).equalTo("id", condition.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                reloadListView()
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }
        // アプリ起動時に表示テスト用のタスクを作成する
        addConditionForTest()

        reloadListView()
    }

    private fun reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        val conditionRealmResults = mRealm.where(ConditionRecord::class.java).findAll().sort("date", Sort.DESCENDING)

        // 上記の結果を、TaskList としてセットする
        mConditionAdapter.conditionList = mRealm.copyFromRealm(conditionRealmResults)

        listView1.adapter = mConditionAdapter
        mConditionAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }

    private fun addConditionForTest() {
        val condition = ConditionRecord()
        condition.title = "様子の記録"
        condition.memo1 = "残さず食べた"
        condition.memo2 = "トイレに何度も起きる"
        condition.content1 = "年末どうするか"
        condition.content2 = "寒さに鈍感みたい"
        condition.date = Date()
        condition.id = 0
        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(condition)
        mRealm.commitTransaction()
    }

}
