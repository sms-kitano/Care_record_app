package jp.a.k.care_support.care_record_app

import android.app.Application
import io.realm.Realm

class RecordApp: Application()  {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}