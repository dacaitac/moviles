package com.example.friki;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import edu.harding.tictactoe.*;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_QUIT_ID = 1;

    private TicTacToeGame mGame;
    private boolean mSound;
    private PreferenceManager mPrefs;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                mGameOver = false;
                System.out.println(mGame.getDifficultyLevel());
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }


    boolean mGameOver;
    // Buttons making up the board

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    Handler handler = new Handler();

    private int[] history;

    // Various text displayed
    private TextView mInfoTextView, HumanWon, TiesNumber, AndroidWon;
    private BoardView mBoardView;
    private int winner;

    private OnTouchListener mTouchListener = new OnTouchListener() {
        private boolean setMove(char player, int location) {
            mGame.setMove(player, location);
            mBoardView.setGame(mGame);
            mBoardView.invalidate();
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            System.out.println("Human moving to pos: " + pos);

            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                if(mSound) mHumanMediaPlayer.start();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        mBoardView.setGame(mGame);
                        winner = mGame.checkForWinner();
                        if (winner == 0) {
                            mInfoTextView.setText("It's Android's turn.");
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            mBoardView.invalidate();
                            if(mSound) mComputerMediaPlayer.start();
                            winner = mGame.checkForWinner();
                        }

                        if (winner == 0)
                            mInfoTextView.setText("It's your turn.");
                        else if (winner == 1)
                            mInfoTextView.setText("It's a tie!");
                        else if (winner == 2) {
                            mInfoTextView.setText("You won!");
                            mGameOver = true;
                        } else {
                            mInfoTextView.setText("Android won!");
                            mGameOver = true;
                        }

                    }
                }, 750);

            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CANCELED){
            mSound = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if(difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if(difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        mInfoTextView = (TextView) findViewById(R.id.information);
        HumanWon = (TextView) findViewById(R.id.human_won);
        TiesNumber = (TextView) findViewById(R.id.finish_tie);
        AndroidWon = (TextView) findViewById(R.id.android_won);

        history = new int[]{0, 0, 0};

        mGame = new TicTacToeGame();
        startNewGame();
        mGameOver = false;

        mPrefs = PreferenceManager.getSharedPreferences(this);
        mSound = mPrefs.getBoolean("sound", true);

        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));

        if(difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if(difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
    }

    void startNewGame(){
        mGame.clearBoard();
        mBoardView.invalidate();
        mInfoTextView.setText(R.string.first_human);
    }

}