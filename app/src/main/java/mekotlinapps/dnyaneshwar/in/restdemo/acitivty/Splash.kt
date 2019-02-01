package mekotlinapps.dnyaneshwar.`in`.restdemo.acitivty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import mekotlinapps.dnyaneshwar.`in`.restdemo.R
import org.jetbrains.anko.toast

class Splash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, Signup::class.java)
            startActivity(mainIntent)
            finish()
            toast("Meeage changes");
        }, 3000)



    }
}