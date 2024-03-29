package com.example.tictactoe;

/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 * 
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O. 
 */

import java.util.Random;

public class TicTacToeGame {

	// Characters used to represent the human, computer, and open spots
	public static final char HUMAN_PLAYER = 'X';
	public static final char COMPUTER_PLAYER = 'O';
	public static final char OPEN_SPOT = ' ';
	public static final int BOARD_SIZE = 9;
	private char mBoard[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * The constructor of the TicTacToeGame have to be remove some code as
	 * follows
	 */
	private Random mRand;

	private int difficulty = 0;

	public TicTacToeGame() {

		// Seed the random number generator
		mRand = new Random();
	}

	/** Clear the board of all X's and O's. */
	public void clearBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			mBoard[i] = OPEN_SPOT;
		}
	}

	public char[] getBoard() {
		return mBoard;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public int getDifficulty() {
		return difficulty;
	}

	/** Set the given player at the given location on the game board * */
	public void setMove(char player, int location) {
		mBoard[location] = player;
	}

	/**
	 * Return the best move for the computer to make. You must call setMove() to
	 * actually make the computer move to that location.
	 * 
	 * @return The best move for the computer to make (0-8).
	 */
	public int getComputerMove() {
		int move;
		if (difficulty < 2) {
			// First see if there's a move O can make to win
			for (int i = 0; i < BOARD_SIZE; i++) {
				if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
					char curr = mBoard[i];
					mBoard[i] = COMPUTER_PLAYER;
					if (checkForWinner() == 3) {
						System.out.println("Computer is moving to " + (i + 1));
						return i;
					} else
						mBoard[i] = curr;
				}
			}
		}
		if (difficulty < 1) {
			// See if there's a move O can make to block X from winning
			for (int i = 0; i < BOARD_SIZE; i++) {
				if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
					char curr = mBoard[i]; // Save the current number
					mBoard[i] = HUMAN_PLAYER;
					if (checkForWinner() == 2) {
						mBoard[i] = COMPUTER_PLAYER;
						System.out.println("Computer is moving to " + (i + 1));
						return i;
					} else
						mBoard[i] = curr;
				}
			}
		}
		// Generate random move
		do {
			move = mRand.nextInt(BOARD_SIZE);
		} while (mBoard[move] == HUMAN_PLAYER
				|| mBoard[move] == COMPUTER_PLAYER);

		mBoard[move] = COMPUTER_PLAYER;
		return move;
	}

	// Check for a winner. Return
	// 0 if no winner or tie yet
	// 1 if it's a tie
	// 2 if X won
	// 3 if O won
	public int checkForWinner() {

		// Check horizontal wins
		for (int i = 0; i <= 6; i += 3) {
			if (mBoard[i] == HUMAN_PLAYER && mBoard[i + 1] == HUMAN_PLAYER
					&& mBoard[i + 2] == HUMAN_PLAYER)
				return 2;
			if (mBoard[i] == COMPUTER_PLAYER
					&& mBoard[i + 1] == COMPUTER_PLAYER
					&& mBoard[i + 2] == COMPUTER_PLAYER)
				return 3;
		}

		// Check vertical wins
		for (int i = 0; i <= 2; i++) {
			if (mBoard[i] == HUMAN_PLAYER && mBoard[i + 3] == HUMAN_PLAYER
					&& mBoard[i + 6] == HUMAN_PLAYER)
				return 2;
			if (mBoard[i] == COMPUTER_PLAYER
					&& mBoard[i + 3] == COMPUTER_PLAYER
					&& mBoard[i + 6] == COMPUTER_PLAYER)
				return 3;
		}

		// Check for diagonal wins
		if ((mBoard[0] == HUMAN_PLAYER && mBoard[4] == HUMAN_PLAYER && mBoard[8] == HUMAN_PLAYER)
				|| (mBoard[2] == HUMAN_PLAYER && mBoard[4] == HUMAN_PLAYER && mBoard[6] == HUMAN_PLAYER))
			return 2;
		if ((mBoard[0] == COMPUTER_PLAYER && mBoard[4] == COMPUTER_PLAYER && mBoard[8] == COMPUTER_PLAYER)
				|| (mBoard[2] == COMPUTER_PLAYER
						&& mBoard[4] == COMPUTER_PLAYER && mBoard[6] == COMPUTER_PLAYER))
			return 3;

		// Check for tie
		for (int i = 0; i < BOARD_SIZE; i++) {
			// If we find a number, then no one has won yet
			if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
				return 0;
		}

		// If we make it through the previous loop, all places are taken, so
		// it's a tie
		return 1;
	}
}