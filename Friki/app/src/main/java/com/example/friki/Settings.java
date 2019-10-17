package com.example.friki;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import edu.harding.tictactoe.TicTacToeGame;

public class Settings extends PreferenceActivity {

    TicTacToeGame mGame;

    public void setGame(TicTacToeGame game) {
        mGame = game;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
