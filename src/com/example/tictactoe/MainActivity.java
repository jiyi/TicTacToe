package com.example.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	// for onSaveInstanceState
	private static final String KEY_BOARD = "board";
	private static final String KEY_GAMEOVER = "game_over";
	private static final String KEY_INFO_TEXT_VIEW = "info_text_view";
	private static final String KEY_WINNER = "winner";

	// Represents the internal state of the game
	private TicTacToeGame mGame;

	// Buttons making up the board
	private Button mBoardButtons[];

	// Various text displayed
	private TextView mInfoTextView;

	// Game Over
	Boolean mGameOver;

	int winner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGame = new TicTacToeGame();

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

		if (savedInstanceState != null) {
			mGameOver = savedInstanceState.getBoolean(KEY_GAMEOVER);
			char[] mBoard = savedInstanceState.getCharArray(KEY_BOARD);
			for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
				setMove(mBoard[i], i);
			}
			winner = savedInstanceState.getInt(KEY_WINNER);
			setInfoTextView(winner);
			mInfoTextView.setText(savedInstanceState.getString(KEY_INFO_TEXT_VIEW));
		} else {
			startNewGame();
		}
	}

	// --- Set up the game board.
	private void startNewGame() {
		mGameOver = false;
		mGame.clearBoard();
		// ---Reset all buttons
		for (int i = 0; i < mBoardButtons.length; i++) {
			mBoardButtons[i].setText("");
			mBoardButtons[i].setEnabled(true);
			mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
		}
		// ---Human goes first
		mInfoTextView.setText(R.string.You_go_first);
	}

	private void setInfoTextView(int winner) {
		if (winner == 0) {
			mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
			mInfoTextView.setText(R.string.your_turn);
		} else if (winner == 1) {
			mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
			mInfoTextView.setText(R.string.tie);
			mGameOver = true;
		} else if (winner == 2) {
			mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
			mInfoTextView.setText(R.string.you_won);
			mGameOver = true;
		} else {
			mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
			mInfoTextView.setText(R.string.android_won);
			mGameOver = true;
		}
	}
	
	// ---Handles clicks on the game board buttons
	private class ButtonClickListener implements View.OnClickListener {
		int location;

		public ButtonClickListener(int location) {
			this.location = location;
		}

		@Override
		public void onClick(View v) {

			if (mGameOver == false) {

				if (mBoardButtons[location].isEnabled()) {
					setMove(TicTacToeGame.HUMAN_PLAYER, location);
					// --- If no winner yet, let the computer make a move
					winner = mGame.checkForWinner();
					if (winner == 0) {
						mInfoTextView.setText("It's Android's turn.");
						int move = mGame.getComputerMove();
						setMove(TicTacToeGame.COMPUTER_PLAYER, move);
						winner = mGame.checkForWinner();
					}
					setInfoTextView(winner);
				}
			}
		}
	}

	private void setMove(char player, int location) {
		mGame.setMove(player, location);
		if (player != TicTacToeGame.OPEN_SPOT) {
			mBoardButtons[location].setEnabled(false);
			mBoardButtons[location].setText(String.valueOf(player));
			if (player == TicTacToeGame.HUMAN_PLAYER)
				mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
			else
				mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
		} else {
			mBoardButtons[location].setText("");
			mBoardButtons[location].setEnabled(true);
			mBoardButtons[location].setOnClickListener(new ButtonClickListener(location));
		}
	}

	// --- OnClickListener for Restart a New Game Button
	public void newGame(View v) {
		startNewGame();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharArray(KEY_BOARD, mGame.getBoard());
		outState.putBoolean(KEY_GAMEOVER, mGameOver);
		outState.putString(KEY_INFO_TEXT_VIEW, mInfoTextView.getText().toString());
		outState.putInt(KEY_WINNER, winner);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
