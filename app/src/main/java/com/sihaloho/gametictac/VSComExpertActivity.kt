package com.sihaloho.gametictac

import android.content.DialogInterface
import android.content.Intent
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_vscom_expert.*

class VSComExpertActivity : AppCompatActivity() {
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    var board = Board()
    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vscom_expert)


        loadBoard()

        keawal.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        exit.setOnClickListener {
            onBackPressed()
        }

        sp = SoundPool.Builder()
            .setMaxStreams(10)
            .build()
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                Toast.makeText(this, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.cardplace, 1)

        button_restart.setOnClickListener {
            //creating a new board instance
            //it will empty every cell
            board = Board()

            //setting the result to empty
            text_view_result.text = ""

            //this function will map the internal board
            //to the visual board
            mapBoardToUi()
        }

    }

    private fun mapBoardToUi() {
        if (spLoaded) {
            sp.play(soundId, 1f, 1f, 0, 0, 1f)
        }
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.baseline_lens_black_48dp)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.crossblack)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {

        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 200
                    height = 200
                    bottomMargin = 2
                    topMargin = 2
                    leftMargin = 2
                    rightMargin = 2
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class CellClickListener(
        val i: Int,
        val j: Int
    ) : View.OnClickListener {

        override fun onClick(p0: View?) {

            //checking if the game is not over
            if (!board.isGameOver) {

                //creating a new cell with the clicked index
                val cell = Cell(i, j)

                //placing the move for player
                board.placeMove(cell, Board.PLAYER)

                //calling minimax to calculate the computers move
                board.minimax(0, Board.COMPUTER)

                //performing the move for computer
                board.computersMove?.let {
                    board.placeMove(it, Board.COMPUTER)
                }

                //mapping the internal board to visual board
                mapBoardToUi()
            }

            //Displaying the results
            //according to the game status
            when {
                board.hasComputerWon() -> text_view_result.text = "Computer Won"
                board.hasPlayerWon() -> text_view_result.text = "Player Won"
                board.isGameOver -> text_view_result.text = "Game Tied"
            }


        }

    }

    override fun onBackPressed(){
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah ingin keluar Aplikasi?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener{ dialogInterfac, i ->
                finish()
            })
            .setNegativeButton("Batal", DialogInterface.OnClickListener{ dialogInterface, i ->

            }).show()
    }
}
