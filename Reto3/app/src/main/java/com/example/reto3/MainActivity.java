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

import com.example.lib.TicTacToeGame;

public class MainActivity extends AppCompatActivity {
    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startNewGame();
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

        startNewGame();
    }

    void startNewGame(){
        mGame.clearBoard();

        for(int i = 0; i < mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        mInfoTextView.setText("You go First.");
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
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if(winner == 0)
                    mInfoTextView.setText("It's your turn.");
                else if ( winner == 1)
                    mInfoTextView.setText("It's a tie!");
                else if ( winner == 2)
                    mInfoTextView.setText("You won!");
                else if ( winner == 3)
                    mInfoTextView.setText("Android Won!");
            }
        }

        private void setMove(char player, int location) {
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
