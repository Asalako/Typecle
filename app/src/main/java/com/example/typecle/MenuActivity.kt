package com.example.typecle

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.typecle.notifications.NotificationReceiver
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navView = findViewById(R.id.nav_2)

        //setting default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container, HomeFragment()
            ).commit()
            navView.setCheckedItem(R.id.nav_home)
        }

        //on click listener for the menu, switches fragment to chosen item
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container, HomeFragment()
                    ).commit()
                }
                R.id.nav_options -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container, OptionFragment()
                    ).commit()
                }
                R.id.nav_gallery -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container, ScoresFragment()
                    ).commit()

                }
                //only actions on the menu which logs the user out
                R.id.nav_logout -> {
                    logout()
                }
                else ->  {
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        //toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    //logs the user out
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    //opens selected game mode
    fun openTimeTrial(v: View) {
        val modeIntent = Intent(this, CategoriesActivity::class.java)
        modeIntent.putExtra("mode", "time trial")
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(modeIntent)
    }

    fun openFreeRun(v: View) {
        Toast.makeText(this,"Coming Soon", Toast.LENGTH_SHORT).show()
    }

    fun openKeepUp(v: View) {
        Toast.makeText(this,"Coming Soon", Toast.LENGTH_SHORT).show()
    }

    fun openPerfectRun(v: View) {
        Toast.makeText(this,"Coming Soon", Toast.LENGTH_SHORT).show()
    }

    //schedules a notification when app is in stop
    override fun onStop() {
        super.onStop()
        notification()

    }

    //Schedules notification after 2hrs of inactivity
    private fun notification() {
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        val buffer: Long = 1000 * 7200
        alarmManager.set(AlarmManager.RTC_WAKEUP,time+buffer, actionIntent)
    }

}