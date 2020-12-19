package jp.a.k.care_support.care_record_app

import java.io.Serializable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ConditionRecord : RealmObject(), Serializable {
    var title: String = ""
    var date: Date = Date()
    var radio1: Int? = null
    var memo1: String = ""
    var radio2: Int? = null
    var memo2: String = ""
    var content1: String = ""
    var content2: String = ""

    @PrimaryKey
    var id: Int = 0
}