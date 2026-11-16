package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.qla_cimol.Tutorial.Tutorial1Fragment
import com.example.qla_cimol.Tutorial.Tutorial2Fragment
import com.example.qla_cimol.Tutorial.Tutorial3Fragment
import com.example.qla_cimol.Tutorial.TutorialFragmentAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotIndicator: DotsIndicator
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.tutorialMessageViewPager)
        dotIndicator = findViewById(R.id.dot_indicator)
        btnNext = findViewById(R.id.btnNext)

        val fragmentsList = listOf(
            Tutorial1Fragment(),
            Tutorial2Fragment(),
            Tutorial3Fragment()
        )

        val adapter = TutorialFragmentAdapter(
            this,
            fragmentsList
        )

        viewPager.adapter = adapter
        dotIndicator.attachTo(viewPager)

        btnNext.setOnClickListener {
            if (viewPager.currentItem < 2) {
                viewPager.currentItem++
            } else {
                bukaRegistrasi()
            }
        }

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    btnNext.text = if (position == 2) {
                        "Mulai"
                    } else {
                        "Lanjut"
                    }
                }
            }
        )

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewPager.currentItem > 0) {
                        viewPager.currentItem--
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun bukaRegistrasi() {
        val intent = Intent(
            this,
            RegistrasiActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}
