package jp.a.k.care_support.care_record_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ConditionAdapter(context: Context): BaseAdapter() {
    private val mLayoutInflater: LayoutInflater
    var conditionList = mutableListOf<ConditionRecord>()

    init {
        this.mLayoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null)

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        val textView2 = view.findViewById<TextView>(android.R.id.text2)

        // 情報を取得
        textView1.text = conditionList[position].title

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
        val date = conditionList[position].date
        textView2.text = simpleDateFormat.format(date)

        return view
    }

    override fun getItem(position: Int): Any {
        return conditionList[position]
    }

    override fun getItemId(position: Int): Long {
        return conditionList[position].id.toLong()
    }

    override fun getCount(): Int {
        return conditionList.size
    }
}