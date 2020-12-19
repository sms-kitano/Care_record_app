package jp.a.k.care_support.care_record_app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.content_date_list.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private lateinit var mToolbar: Toolbar
    private var mItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)

        included.button1.setOnClickListener(this)
        included.button2.setOnClickListener(this)

        // ナビゲーションドロワーの設定
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        when(v!!.id){
            R.id.button1 -> intent = Intent(this, InputActivity::class.java)
            R.id.button2 -> intent = Intent(this, InputCareActivity::class.java)

        }

        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var intent: Intent? = null

        if (id == R.id.nav_date) {
            //mToolbar.title = "日付から探す"
            mItem = 1
            intent = Intent(this, DateListActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_concerned) {
            //mToolbar.title = "気になること"
            mItem = 2
        } else if (id == R.id.nav_well) {
            //mToolbar.title = "うまくいったこと"
            mItem = 3
        } else if (id == R.id.nav_consult) {
            //mToolbar.title = "相談したいこと"
            mItem = 4
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


}
