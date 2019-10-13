package com.example.friki;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import edu.harding.tictactoe.TicTacToeGame;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        startNewGame();
        mGameOver = false;
        return true;
    }

    boolean mGameOver;

    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button[] mBoardButtons;

    private int[] history;

    // Various text displayed
    private TextView mInfoTextView, HumanWon, TiesNumber, AndroidWon;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = (TextView) findViewById(R.id.information);

        HumanWon = (TextView) findViewById(R.id.human_won);
        TiesNumber = (TextView) findViewById(R.id.finish_tie);
        AndroidWon = (TextView) findViewById(R.id.android_won);

        history = new int[]{0, 0, 0};

        mGame = new TicTacToeGame();
        startNewGame();
        mGameOver = false;
    }

    void startNewGame(){
        mGame.clearBoard();

        for(int i = 0; i < mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        mInfoTextView.setText(R.string.first_human);
        boolean androidFirst = new Random().nextBoolean();
        if (androidFirst){
            int move = mGame.getComputerMove();
            System.out.println("Getting computer move.");
            mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            mBoardButtons[move].setEnabled(false);
            mBoardButtons[move].setText(String.valueOf(TicTacToeGame.COMPUTER_PLAYER));
            mBoardButtons[move].setTextColor(Color.rgb(200,0,0));
            mInfoTextView.setText(R.string.turn_human);
        }

    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        ButtonClickListener(int i) {
            this.location = i;
        }

        @Override
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()){
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = mGame.checkForWinner();
                if (winner == 0){
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    System.out.println("Getting computer move.");
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if(winner == 0)
                    mInfoTextView.setText(R.string.turn_human);
                else if ( winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                    TiesNumber.setText(String.valueOf(++history[0]));
                }else if ( winner == 2) {
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                    HumanWon.setText(String.valueOf(++history[1]));
                }else if ( winner == 3) {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    AndroidWon.setText(String.valueOf(++history[2]));
                    mGameOver = true;
                }
            }
        }

        private void setMove(char player, int location) {
            if (mGameOver)
                return;
            mGame.setMove(player, location);
            mBoardButtons[location].setEnabled(false);
            mBoardButtons[location].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            else
                mBoardButtons[location].setTextColor(Color.rgb(200,0,0));
        }
    }



}
