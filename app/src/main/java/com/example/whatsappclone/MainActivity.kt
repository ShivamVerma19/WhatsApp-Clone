package com.example.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        viewPager.adapter = ScreenSliderAdapter(this)

        TabLayoutMediator(tabLayout , viewPager , TabLayoutMediator.TabConfigurationStrategy{ tab: TabLayout.Tab, pos: Int ->
              when(pos){
                  0 -> tab.text = "CHAT"
                  1 -> tab.text = "PEOPLE"
              }
        }).attach()
    }
}