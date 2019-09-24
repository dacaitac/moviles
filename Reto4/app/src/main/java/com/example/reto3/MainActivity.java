package com.example.reto3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

import com.example.lib.TicTacToeGame;

public class MainActivity extends AppCompatActivity {
    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView, HumanWon, TiesNumber, AndroidWon;
    boolean mGameOver;

    private int[] history;

    public enum DifficultyLevel { Easy, Harder, Expert};

    public DifficultyLevel getmDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setmDifficultyLevel(DifficultyLevel mDifficultyLevel) {
        this.mDifficultyLevel = mDifficultyLevel;
    }

    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startNewGame();
        mGameOver = false;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.button);
        mBoardButtons[1] = (Button) findViewById(R.id.button2);
        mBoardButtons[2] = (Button) findViewById(R.id.button3);
        mBoardButtons[3] = (Button) findViewById(R.id.button4);
        mBoardButtons[4] = (Button) findViewById(R.id.button5);
        mBoardButtons[5] = (Button) findViewById(R.id.button6);
        mBoardButtons[6] = (Button) findViewById(R.id.button7);
        mBoardButtons[7] = (Button) findViewById(R.id.button8);
        mBoardButtons[8] = (Button) findViewById(R.id.button9);

        mInfoTextView = (TextView) findViewById(R.id.textView);
        mGame = new TicTacToeGame();

        //HumanWon = (TextView) findViewById(R.id.human_won);
        //TiesNumber = (TextView) findViewById(R.id.finish_tie);
        //AndroidWon = (TextView) findViewById(R.id.android_won);

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

        mInfoTextView.setText("Your turn");
        boolean androidFirst = new Random().nextBoolean();
        if (androidFirst){
            int move = mGame.getComputerMove();
            System.out.println("Getting computer move.");
            mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            mBoardButtons[move].setEnabled(false);
            mBoardButtons[move].setText(String.valueOf(TicTacToeGame.COMPUTER_PLAYER));
            mBoardButtons[move].setTextColor(Color.rgb(200,0,0));
            mInfoTextView.setText("Your turn");
        }


    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int i) {
            this.location = i;
        }

        @Override
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()){
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = mGame.checkForWinner();
                if (winner == 0){
                    //mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    System.out.println("Getting computer move.");
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if(winner == 0)
                    mInfoTextView.setText("");
                else if ( winner == 1) {
                    mInfoTextView.setText("Its a tie");
                    mGameOver = true;
                    //TiesNumber.setText(String.valueOf(++history[0]));
                }else if ( winner == 2) {
                    mInfoTextView.setText("You win");
                    mGameOver = true;
                    //HumanWon.setText(String.valueOf(++history[1]));
                }else if ( winner == 3) {
                    mInfoTextView.setText("Computer wins.");
                    //AndroidWon.setText(String.valueOf(++history[2]));
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
