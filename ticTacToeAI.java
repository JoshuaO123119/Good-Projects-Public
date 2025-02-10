import java.util.Scanner;
import java.util.Random;
public class ticTacToeAI
{
	private static final Scanner input = new Scanner(System.in);
	private static final Random generator = new Random();
	public static void main(String[] args) {
		// Initilize board and score
		String[][] board = new String[3][3];
		int[] score = new int[2];

		boolean gameRunning = true;
		// Main loop of entire game (restart entire tic tac toe game)
		while (true) {
			int column = -1;
			int row = -1;
			String turn = "X";
			int turns = 1;

			// Fill Array
			board = loadBoard(board);
			// Main tic tac toe game loop
			while (gameRunning) {

				// Print board to user
				displayBoard(board);
				// Player turn
				if (turn.equals("X")) {
					// Allow user to type into board
					System.out.println("Type a column (a/b/c): ");
					char r = input.next().toLowerCase().charAt(0);

					if (r == 'a') {
						row = 0;
					} else if (r == 'b') {
						row = 1;
					} else if (r == 'c') {
						row = 2;
					} else {
						System.out.println("That isn't a valid choice!");
						waitForInput();
						continue;
					}

					// Let user choose what column they want
					System.out.println("Type a row (1/2/3): ");
					int c = input.nextInt();
					if (c < 4 && c > 0) {
						// Compensate for arrays starting at 0 instead of 1
						column = c-1;
					} else { // If user chooses a number that isn't available
						System.out.println("That isn't a valid choice!");
						waitForInput();
						continue;
					}

					// If user tries to playe somewhere that's already taken
					if (board[column][row].equals("X") || board[column][row].equals("O")) {
						System.out.println("You can't play there!");
						waitForInput();
						continue;
					} else {
						board[column][row] = turn;
					}
				}
				// AI player
				if (turn.equals("O")) {
				    while (true) {
				        int randRow = generator.nextInt(3);
				        int randCol = generator.nextInt(3);
				        if (board[randCol][randRow].equals("-")) {
				            board[randCol][randRow] = "O";
				            break;
				        }
				    }
				}

                // Check if someone won
				String win = validateWin(board);

				

                
				// Swap turns
				if (turn.equals("X")) {
					turn = "O";
				} else {
					turn = "X";
				}
				turns++;
                
                
                // Check if user won
				if (win != null) {
					displayBoard(board);
					System.out.println("Player " + win + " won!");
					score = updateScore(score, win);
					System.out.println("Game Over!");
					gameRunning = false;
				}

				// Check if tie
				if (win == null && turns == 10) {
					displayBoard(board);
					System.out.println("Tie!");
					System.out.println("Game Over!");
					score = updateScore(score, "No Player Won");
					gameRunning = false;
				}

			}
			// Allow player to restart game
			System.out.println("Do you want to play again (y/n): ");
			char choice = input.next().toLowerCase().trim().charAt(0);

			if (choice == 'y') {
				gameRunning = true;
			} else {
				System.exit(0);
			}
		}
	}
	private static void displayBoard(String[][] board) {
		// Clear Console
		System.out.print("\033[H\033[2J");
		System.out.flush();

		System.out.println("  A B C");
		for (int i = 0; i < board.length; i++) {
			System.out.print(i+1 + " ");
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	// Set board to default
	private static String[][] loadBoard(String[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = "-";
			}
		}
		return board;
	}
	private static void waitForInput() {
		System.out.print("Press enter to continue: ");
		/* Need double input.nextLine() to eat up last input
		and create new one     */
		input.nextLine();
		input.nextLine();
	}
	// If win it returns the player, else it returns null
	private static String validateWin(String[][] board) {
		String[] players = {"X", "O"};
		// Check all horizontal wins by checking each row individually (all 3 at once) at a time
		// Check for each player (X or O)
		for (String player : players) {
			for (int i = 0; i < board.length; i++) {
				if (board[i][0].equals(player) && board[i][1].equals(player) && board[i][2].equals(player)) {
					return player;
				}
			}
		}
		// Check all vertical wins by checking each column individually (all 3 at oonce) at a time
		// Check for each player (X or O)
		for (String player : players) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (board[0][j].equals(player) && board[1][j].equals(player) && board[2][j].equals(player)) {
						return player;
					}
				}
			}
		}
		// Check both diagonal wins
		// Check for reach player (X or O)
		for (String player : players) {
			if (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) {
				return player;
			}

			if (board[2][0].equals(player) && board[1][1].equals(player) && board[0][2].equals(player)) {
				return player;
			}
		}

		// If no one won at all, just return null
		return null;
	}
	private static int[] updateScore(int[] score, String player) {
		// Update Score
		if (player.equals("X")) {
			score[0]++;
		}
		if (player.equals("O")) {
			score[1]++;
		}

		// Display wins
		System.out.println("Score:\n\tPlayer X: " + score[0] + "\n\tPlayer O: " + score[1]);
		return score;
	}
}