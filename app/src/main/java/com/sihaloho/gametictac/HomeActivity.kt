package com.sihaloho.gametictac

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.SoundPool
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false


    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        MobileAds.initialize(this){}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-1757230288277348/8194549249"
        mInterstitialAd.loadAd(AdRequest.Builder().build())



        btn_multi.setOnClickListener {

            sp.autoPause()
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        btn_vscom.setOnClickListener {

            if(mInterstitialAd.isLoaded)
                mInterstitialAd.show()

            val options = arrayOf<CharSequence>(
                "Gampang Banget",
                "Susah Banget"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Level?")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, pos ->
                if (pos == 0){

                    sp.autoPause()

                    startActivity(Intent(this, VSComEasyActivity::class.java))
                    finishAffinity()

                }
                if (pos==1){

                    sp.autoPause()
                    startActivity(Intent(this, VSComExpertActivity::class.java))
                    finishAffinity()
                }
            })
            builder.show()
        }

        btn_exit.setOnClickListener {
            onBackPressed()
        }

        btn_mute.setOnClickListener {
            sp.autoPause()
            btn_mute.visibility = View.GONE
            btn_on.visibility = View.VISIBLE
        }
        btn_on.setOnClickListener {
            sp.autoResume()

            btn_mute.visibility = View.VISIBLE
            btn_on.visibility = View.GONE
        }

        sp = SoundPool.Builder()
            .setMaxStreams(10)
            .build()
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = true
                sp.play(soundId, 0.5f, 0.5f, 0, -1, 1f)
            } else {
                Toast.makeText(this, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.back, 1)



    }


    override fun onBackPressed(){
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah ingin keluar Aplikasi?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener{dialogInterfac, i ->
                finish()
                sp.stop(soundId)
            })
            .setNegativeButton("Batal",DialogInterface.OnClickListener{dialogInterface, i ->

            }).show()
    }


}
