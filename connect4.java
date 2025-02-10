import java.util.Scanner;

public class connect4
{
	private static final Scanner input = new Scanner(System.in);
	public static void main(String[] args) {
		String[][] board = loadBoard();
		int[] score = new int[2];
		boolean gameRunning = true;
		String turn = "X";
		// For swapping who goes first
		String lastStartTurn = turn;



		// Main game loop
		while (true) {
			int totalTurns = 1;
			// Individual connect 4 games
			while (gameRunning) {

				displayBoard(board);
				// Ask user where they want to go
				System.out.println("\nPlayer " + turn + " turn");
				System.out.print("What column do you choose (1-7): ");
				int c = input.nextInt();
				// If player can't make move then loop again, else update board
				String[][] verify = verifyAction(c, board, turn);
				if (verify == null) {
					continue;
				} else {
					// Update board and change turns
					board = verify;
					// Check if they won before they change turns
					if (verifyWin(board, totalTurns)) {
							displayBoard(board);
							System.out.println("Player " + turn + " wins!");
							// Update score based on player who won
							if (turn.equals("X")) {
								score[0]++;
							} else {
								score[1]++;
							}

						gameRunning = false;
						continue;
					}
					
					// If tie
					if (!verifyWin(board, totalTurns) && totalTurns == 42) {
                            // If all moves exhausted and no wins
							displayBoard(board);
							System.out.println("Tie, no player wins!");
							gameRunning = false;
					}

					if (turn.equals("X")) {
						turn = "O";
					} else {
						turn = "X";
					}
					totalTurns++;
				}


			}
            displayBoard(board);
			System.out.println("Scores:\n\tPlayer X: " + score[0] + "\n\tPlayer O: " +score[1] );

			System.out.print("Do you wish to play again (y/n): ");
			char choice = input.next().toLowerCase().trim().charAt(0);
			if (choice == 'y') {
				gameRunning = true;
				board = loadBoard();

				// Swap who goes first every time game restarts
				if (lastStartTurn.equals("X")) {
					lastStartTurn = "O";
					turn = "O";
				} else {
					lastStartTurn = "X";
					turn = "X";
				}
				continue;
			} else if (choice == 'n') {
				System.exit(0);
			}

		}
	}
	// Fill board with -
	private static String[][] loadBoard() {
		String[][] board = new String[6][7];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = "-";
			}
		}

		return board;
	}

	private static void displayBoard(String[][] board) {
		// Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();


		// Display board
		System.out.println("  1 2 3 4 5 6 7");
		System.out.println("  ______________");
		for (int i =0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (j == 0) {
					System.out.print(" |");
				}
				System.out.print(board[i][j] + " ");
				if (j == board[i].length-1) {
					System.out.print("|");
				}
			}
			System.out.println();
		}
		System.out.println("  ______________");
	}

	private static void waitForInput() {
		System.out.print("Press enter to continue: ");
		input.nextLine();
		input.nextLine();
	}
	private static String[][] verifyAction(int c, String[][] board, String turn) {
		int column = -1;
		int row = -1;
		// Handle columns
		if (c > 0 && c < 8) {
			column = c-1;
		} else {
			System.out.println("That isn't a valid option!");
			waitForInput();
			return null;
		}

		// Handle rows
		boolean success = false;
		for (int i = 5; i >= 0; i--) {
			if (board[i][column].equals("-")) {
				board[i][column] = turn;
				success = true;
				break;
			}
		}
		// If player can put down turn
		if (success) {
			return board;
		} else { // If entire column is filled
			System.out.println("The entire column is filled!");
			waitForInput();
			return null;
		}


	}
	private static boolean verifyWin(String[][] board, int totalTurns) {
		String players[] = {"X", "O"};
		// Check Horizontal
		for (String player : players) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length-3; j++) {
					if (board[i][j].equals(player) && board[i][j+1].equals(player) && board[i][j+2].equals(player) && board[i][j+3].equals(player)) {
						// If found player won
						return true;
					}
				}
			}
		}
		// Check vertical
		// For each player (X or O)
		for (String player: players) {
			for (int i = 0; i < board.length - 3; i++ ) {
				for (int j = 0; j < board[i].length; j++) { // Check for pattern
					if (board[i][j].equals(player) && board[i+1][j].equals(player) && board[i+2][j].equals(player) && board[i+3][j].equals(player)) {
						// If found player won
						return true;
					}
				}
			}
		}

		// Check up left pattern
		// For each player (X or O)
		for (String player: players) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					try { // Check for pattern
						if (board[i][j].equals(player) && board[i+1][j+1].equals(player) && board[i+2][j+2].equals(player) && board[i+3][j+3].equals(player)) {
							// If found player won
							return true;
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		}
		// Check up right pattern
		// For each player (X or O)
		for (String player: players) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					try { // Check for pattern
						if (board[i][j].equals(player) && board[i+1][j-1].equals(player) && board[i+2][j-2].equals(player) && board[i+3][j-3].equals(player)) {
							// If found player won
							return true;
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		}

		// If it never found any players win
		return false;
	}

}