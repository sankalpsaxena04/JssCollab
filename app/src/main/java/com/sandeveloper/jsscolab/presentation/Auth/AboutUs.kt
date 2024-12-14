package com.sandeveloper.jsscolab.presentation.Auth

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityAboutUsBinding
import io.noties.markwon.Markwon

class AboutUs : AppCompatActivity() {
    private  val binding: ActivityAboutUsBinding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Set up the logo and about us text
        val aboutUsContent = """
            ### Introducing *JSSCollab* – your go-to app for simplifying daily tasks and making student life more affordable and convenient!

            Struggling to reach the free delivery limit when ordering online? Need to exchange your quantum books at the end of the semester? Want to travel to the station but find cab fares too high? JSSConnect has got you covered!

            **Key Features:**
            - **Co-Order with Neighbors:** Partner with people nearby to place joint orders, eliminating delivery fees and saving you money.
            - **Exchange Quantums:** Easily exchange quantum books with fellow students at the end of each semester, keeping your study materials up-to-date.
            - **Group Plan Purchases:** Purchase premium subscriptions as a group, reducing the per capita cost and making premium plans more accessible.
            - **Shared Cab Rides:** Travel to popular destinations by sharing cab rides with others, cutting down on expenses while making the journey more enjoyable.

            JSSConnect brings all these features together in one seamless app, designed to make your student life at JSS simpler and more affordable. Join the JSSConnect community and experience a smarter way to manage your daily tasks!

            Created by [**Sankalp Saxena**](https://www.linkedin.com/in/sankalpsaxena04) (Android Developer) and [**Unnat Kumar Agarwal**](https://www.linkedin.com/in/unnat-kumar-agarwal-5a1969257/) (Backend Developer).
            Developed under [**Nibble Computer Society**](https://www.instagram.com/hackncs/) and hosted on their [**Play Store**](https://play.google.com/store/apps/dev?id=8880167874592698362&hl=en_IN).
        """.trimIndent()

        // Set up Markwon to parse and display Markdown content
        val markwon = Markwon.create(this)
        markwon.setMarkdown(binding.aboutUsText, aboutUsContent)
        binding.topbar.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}