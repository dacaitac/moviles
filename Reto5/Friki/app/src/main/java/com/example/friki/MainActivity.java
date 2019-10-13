package com.example.friki;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import edu.harding.tictactoe.*;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    private TicTacToeGame mGame;

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
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                startNewGame();
                mGameOver = false;
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        System.out.println(id);
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
                int selected = mGame.getDifficultyLevel().getValue();
                // Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
                                // Set the diff level of mGame based on which item was selected.
                                // Display the selected difficulty level
                                System.out.println(item);
                                switch (item){
                                    case 0:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;
                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;
                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
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
                mHumanMediaPlayer.start();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        mBoardView.setGame(mGame);
                        winner = mGame.checkForWinner();
                        if (winner == 0) {
                            mInfoTextView.setText("It's Android's turn.");
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            mBoardView.invalidate();
                            mComputerMediaPlayer.start();
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
    }

    void startNewGame(){
        mGame.clearBoard();
        mBoardView.invalidate();
        mInfoTextView.setText(R.string.first_human);
    }

}