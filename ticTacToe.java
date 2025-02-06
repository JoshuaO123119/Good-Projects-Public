import java.util.Scanner;
import java.util.Arrays;

public class ticTacToe
{
	private static final Scanner input = new Scanner(System.in);
	public static void main(String[] args) {
		String[][] board = new String[3][3];
		
		boolean gameRunning = true;
		
		while (true) {
		int column = -1;
		int row = -1;
		String turn = "X";
		int turns = 1;

		// Fill Array
		board = loadBoard(board);
		// Main loop
		while (gameRunning) {

			// Print board to user
			displayBoard(board);
			// Allow user to type into board

			System.out.println("Type a column (a/b/c): ");
			char c = input.next().toLowerCase().charAt(0);

			if (c == 'a') {
				row = 0;
			} else if (c == 'b') {
				row = 1;
			} else if (c == 'c') {
				row = 2;
			} else {
				System.out.println("That isn't a valid choice!");
				waitForInput();
				continue;
			}

			System.out.println("Type a row (1/2/3): ");
			int r = input.nextInt();
			if (r < 4 && r > 0) {
				column = r-1;
			} else {
				System.out.println("That isn't a valid choice!");
				waitForInput();
				continue;
			}

			if (board[column][row].equals("X") || board[column][row].equals("O")) {
				System.out.println("You can't play there!");
				waitForInput();
				continue;
			} else {
				board[column][row] = turn;

			}



			if (turn.equals("X")) {
				turn = "O";
			} else {
				turn = "X";
			}
			turns++;
			// Check if user won
			if (validateWin(board)) {
				System.out.println("Game Over!");
				gameRunning = false;
			}
			
			// Check if tie
			if (!validateWin(board) && turns == 10) {
				displayBoard(board);
				System.out.println("Tie!");
				System.out.println("Game Over!");
				gameRunning = false;
			}
		}
		System.out.println("Do you want to play again (y/n): ");
		char choice = input.next().toLowerCase().trim().charAt(0);
		
		if (choice == 'y') {
			gameRunning = true;
		} else {
			System.exit(0);
		}
		}

	}

	public static void displayBoard(String[][] board) {
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

	public static String[][] loadBoard(String[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = "-";
			}

		}

		return board;
	}

	public static void waitForInput() {
		System.out.print("Press enter to continue: ");
		input.nextLine();
		input.nextLine();
	}

	public static boolean validateWin(String[][] board) {
		String[] players = {"X", "O"};
		// Check all horizontal wins
		for (String player : players) {
			for (int i = 0; i < board.length; i++) {
				if (board[i][0].equals(player) && board[i][1].equals(player) && board[i][2].equals(player)) {
					displayBoard(board);
					System.out.println("Player " + player + " won!");
					return true;
				}
			}
		}
		// Check all vertical wins
		for (String player : players) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (board[0][j].equals(player) && board[1][j].equals(player) && board[2][j].equals(player)) {
						displayBoard(board);
						System.out.println("Player " + player + " won!");
						return true;
					}
				}
			}
		}
		// Check both diagonal wins
		for (String player : players) {
			if (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) {
				displayBoard(board);
				System.out.println("Player " + player + " won!");
				return true;
			}
			
			if (board[2][0].equals(player) && board[1][1].equals(player) && board[0][2].equals(player)) {
				displayBoard(board);
				System.out.println("Player " + player + " won!");
				return true;
			}
		}
		return false;
	}
}