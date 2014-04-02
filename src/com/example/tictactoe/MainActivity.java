package com.example.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
	private Boolean mGameOver;

	private int winner;

	private TextView mCountTextView;

	private int difficulty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", MODE_PRIVATE);

		difficulty = 0;
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
		mCountTextView = (TextView) findViewById(R.id.count_infomation);

		// for count
		StringBuilder builder = new StringBuilder();

		builder.append(pref.getInt("win", 0) + "  ");
		builder.append(pref.getInt("lose", 0) + "  ");
		builder.append(pref.getInt("tie", 0));
		mCountTextView.setText(getString(R.string.your_count) + "\n"
				+ builder.toString());

		if (savedInstanceState != null) {
			mGameOver = savedInstanceState.getBoolean(KEY_GAMEOVER);
			char[] mBoard = savedInstanceState.getCharArray(KEY_BOARD);
			for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
				setMove(mBoard[i], i);
			}
			winner = savedInstanceState.getInt(KEY_WINNER);
			setInfoTextView(winner);
			mInfoTextView.setText(savedInstanceState
					.getString(KEY_INFO_TEXT_VIEW));
		} else {
			startNewGame();
		}
	}

	// --- Set up the game board.
	private void startNewGame() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", MODE_PRIVATE);
		mGameOver = false;
		mGame.clearBoard();
		mGame.setDifficulty(difficulty);
		// ---Reset all buttons
		for (int i = 0; i < mBoardButtons.length; i++) {
			mBoardButtons[i].setText("");
			mBoardButtons[i].setEnabled(true);
			mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
		}

		if ((pref.getInt("win", 0) + pref.getInt("lose", 0) + pref.getInt(
				"tie", 0)) % 2 == 0) {
			// ---Human goes first
			mInfoTextView.setText(R.string.You_go_first);
		} else {
			mInfoTextView.setText(R.string.android_go_first);
			int move = mGame.getComputerMove();
			setMove(TicTacToeGame.COMPUTER_PLAYER, move);
		}
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

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	// ---Handles clicks on the game board buttons
	private class ButtonClickListener implements View.OnClickListener {
		int location;
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", MODE_PRIVATE);
		Editor editor = pref.edit();

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
					if (winner == 1) {
						editor.putInt("tie", pref.getInt("tie", 0) + 1);
						editor.commit();
					} else if (winner == 2) {
						editor.putInt("win", pref.getInt("win", 0) + 1);
						editor.commit();
					} else if (winner == 3) {
						editor.putInt("lose", pref.getInt("lose", 0) + 1);
						editor.commit();
					}
					StringBuilder builder = new StringBuilder();
					builder.append(pref.getInt("win", 0) + "  ");
					builder.append(pref.getInt("lose", 0) + "  ");
					builder.append(pref.getInt("tie", 0));
					mCountTextView.setText(getString(R.string.your_count)
							+ "\n" + builder.toString());
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
			mBoardButtons[location].setOnClickListener(new ButtonClickListener(
					location));
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
		outState.putString(KEY_INFO_TEXT_VIEW, mInfoTextView.getText()
				.toString());
		outState.putInt(KEY_WINNER, winner);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_item_difficulty:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_set_difficulty)
					.setSingleChoiceItems(R.array.difficulty_array,
							mGame.getDifficulty(),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									difficulty = which;
								}
							})
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startNewGame();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									difficulty = mGame.getDifficulty();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									difficulty = mGame.getDifficulty();
								}
							});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		case R.id.menu_item_exit:
			finish();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
