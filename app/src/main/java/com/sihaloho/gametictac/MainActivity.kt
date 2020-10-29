package com.sihaloho.gametictac

import android.content.DialogInterface
import android.content.Intent
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        btn_exit.setOnClickListener {
            onBackPressed()
        }
        btn_home.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finishAffinity()
        }
        btn_return.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
        sp = SoundPool.Builder()
            .setMaxStreams(10)
            .build()
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                Toast.makeText(this@MainActivity, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.cardplace, 1)



    }

    fun onClick(view: View){
        val buSelected = view as Button

        var cellID = 0
        when(buSelected.id){
            R.id.bu1->cellID = 1
            R.id.bu2->cellID = 2
            R.id.bu3->cellID = 3
            R.id.bu4->cellID = 4
            R.id.bu5->cellID = 5
            R.id.bu6->cellID = 6
            R.id.bu7->cellID = 7
            R.id.bu8->cellID = 8
            R.id.bu9->cellID = 9
        }
        playgame(cellID,buSelected)

    }

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var activePlayer = 1
    private fun playgame(cellID: Int, buSelected: Button) {

        if (spLoaded) {
            sp.play(soundId, 1f, 1f, 0, 0, 1f)
        }

        if (activePlayer==1){
            buSelected.text = "X"
            buSelected.setBackgroundResource(R.drawable.cardback_red)
            player1.add(cellID)
            activePlayer = 2
        }else{
            buSelected.text = "0"
            buSelected.setBackgroundResource(R.drawable.cardback_green)
            player2.add(cellID)
            activePlayer = 1
        }
        buSelected.isEnabled = false
        checkWinner()

    }

    private fun checkWinner() {


        var winner = -1


        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }

        //col1
        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }
        //col2
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }
        //col3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }
        //colx
        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2
        }
        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2
        }





        if (winner != -1) {


            if (winner == 1) {

                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.winner, null)
                val mulai = view.findViewById<Button>(R.id.btnmulai)
                val keluar = view.findViewById<Button>(R.id.btn_keluar)
                builder.setView(view)
                mulai.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                keluar.setOnClickListener {
                    finish()
                }

                builder.show()
            }

            else {

                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.winner2, null)
                val mulai = view.findViewById<Button>(R.id.btnmulai)
                val keluar = view.findViewById<Button>(R.id.btn_keluar)
                builder.setView(view)
                mulai.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                keluar.setOnClickListener {
                    finish()
                }

                builder.show()
            }


        }
    }
    override fun onBackPressed(){
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah ingin keluar Aplikasi?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener{ dialogInterfac, i ->
                finish()
            })
            .setNegativeButton("Batal", DialogInterface.OnClickListener{ dialogInterface, i ->

            }).show()
    }

}
