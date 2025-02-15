import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class battleshipAI
{
	private static final Scanner input = new Scanner(System.in);
	private static final Random generator = new Random();

	private static Boolean gameRunning = true;
	// s = stupid easy, h = hard
	private static char difficulty = 'n';
	private static boolean entireGameRunning = true;
	private static int[] score = new int[2];


	public static void main(String[] args) {
		while (true) {
			while (entireGameRunning) {

				// P = "Player", E = "Enemy"
				String turn = "p";
				int[] ships = {5, 4, 3, 3, 2};

				String[][] playerBoard = loadBoard();
				String[][] aiBoard = loadBoard();
				String[][] verifyPlace = null;
				while (true) {
					clearConsole();
					// Player choosing difficulty
					System.out.println("What difficulty do you want the bot to be:\n\t1. Stupid Easy (AI shoots at random)\n\t2. Hard (Shoots random until it finds ship, then shoots around ship)\n\t3. Impossible (Has cheats and beams found ships)");
					System.out.print("\nDifficulty (1-3): ");
					char choice = input.next().trim().toLowerCase().charAt(0);
					if (choice == '1' || choice == 's') {
						difficulty = 's';
					} else if (choice == '2' || choice == 'h') {
						difficulty = 'h';
					} else if (choice == '3' || choice == 'i') {
					    difficulty = 'i';
					} 
					else {
						System.out.println("That isn't a valid option!");
						waitForInput();
						continue;
					}
					break;
				}

				// Placing down ships
				for (int i = 0; i < ships.length; i++) {
					displayBoards(playerBoard, aiBoard, turn, false);
					System.out.println("Placing ship of size " + ships[i]);
					// If can't place down and turn is player (print)
					if (turn.equals("p")) {
						verifyPlace = placeShip(playerBoard, ships[i], turn);

						if (verifyPlace == null) {
							System.out.println("You can't put the ship there!");
							waitForInput();
							i--;
							continue;
						} else {
							playerBoard = verifyPlace;
						}
					}

					turn = "e";

					if (turn.equals("e")) {
						// If can't place down and turn is enemy (dont print)
						verifyPlace = placeShip(aiBoard, ships[i], turn);
						if (verifyPlace == null && turn.equals("e")) {
							i--;
							continue;
						} else {
							aiBoard = verifyPlace;
						}
					}

					turn = "p";

				}
				// Main game loop
				while (gameRunning) {

					// Player's turn
					if (turn == "p") {
						displayBoards(playerBoard, aiBoard, turn, false);
						String[][] attackAI = verifyAttack(aiBoard, turn);
						// If attack to AI successful
						if (attackAI != null) {
							aiBoard = attackAI;
						}

						// If attack to AI unsuccessful
						else {
							continue;
						}
					}

					// Check for win

					String playerWon = verifyWin(playerBoard, aiBoard);
					if (playerWon != null) {
						displayBoards(playerBoard, aiBoard, turn, true);
						gameRunning = false;
						entireGameRunning = false;
					    System.out.println("Scores:\n\tPlayer: " + score[0] + "\n\tAI: " + score[1]);
						System.out.println(playerWon + " won!");
						continue;
					}
					// AI's turn
					if (turn == "e") {
						displayBoards(playerBoard, aiBoard, turn, false);
						String[][] attackPlayer = verifyAttack(playerBoard, turn);
						// If attack to Player successful
						if (attackPlayer != null) {
							playerBoard = attackPlayer;
						}
						// If attack to Player unsuccessful
						else {
							continue;
						}
					}

					playerWon = verifyWin(playerBoard, aiBoard);
					if (playerWon!=null) {
						displayBoards(playerBoard, aiBoard, turn, true);
						gameRunning = false;
						entireGameRunning = false;
						System.out.println("Scores:\n\tPlayer: " + score[0] + "\n\tAI: " + score[1]);
						System.out.println(playerWon + " won!");
						continue;
					}


					// Swap turns, P = "Player", E = "Enemy"
					if (turn == "p") {
						turn = "e";
					} else {
						turn = "p";
					}
				}
			}
			System.out.println("Do you want to play again (y/n): ");
				char choice = input.next().toLowerCase().trim().charAt(0);
				if (choice == 'y') {
					gameRunning = true;
					entireGameRunning = true;
					continue;
				} else if (choice == 'n') {
					System.exit(0);
				} else {
					System.out.println("That isn't a valid option!");
					waitForInput();
					continue;
				}

		}
	}

	private static String[][] loadBoard() {
		String[][] arr = new String[10][10];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = "~";
			}
		}

		return arr;
	}

	private static void displayBoards(String[][] player, String[][] aiBoard, String turn, boolean gameOver) {
		clearConsole();
		
		boolean fiveShipFound = false;
		boolean fourShipFound = false;
		int threeShipCount = 0;
		boolean twoShipFound = false;		
		
		// Display sunken ships (optimize this)
		System.out.print("Enemy Ships Alive: ");
		for (int i = 0; i < aiBoard.length; i++) {
		    for (int j = 0; j < aiBoard[i].length; j++) {
		        if (aiBoard[i][j].equals("5")) {
		            fiveShipFound = true;
		        }
		        if (aiBoard[i][j].equals("4")) {
		            fourShipFound = true;
		        }
		        if (aiBoard[i][j].equals("3")) {
		            threeShipCount++;
		        }
		        if (aiBoard[i][j].equals("2")) {
		            twoShipFound = true;
		        }
		    }
		}
		// Have to be a better way to do this
		if (fiveShipFound) {System.out.print("5 ");}
		if (fourShipFound) {System.out.print("4 ");}
		if (threeShipCount > 0) {
		    if (threeShipCount > 3) {System.out.print("3 3 ");}
		    else {System.out.print("3 ");}}
		if (twoShipFound) {System.out.print("2");}
		
		System.out.println();
		
		
		// Display Player and AI's board side by side
		System.out.println("____________________________________________________");
		System.out.println("|       PLAYER BOARD     ||       ENEMY BOARD      |");
		System.out.println("|________________________||________________________|");
		System.out.println("|   A B C D E F G H I J  ||   A B C D E F G H I J  |");
		// Display Player board
		for (int i = 0; i < player.length; i++) {
			System.out.print("|");
			for(int j = 0; j < player[i].length; j++) {
				if (j == 0) {
					System.out.print(i +"  ");
				}
				System.out.print(player[i][j] + " ");
			}

			// Display Enemey Board Side
			System.out.print(" ||");
			for (int k = 0; k < aiBoard[i].length; k++) {
				if (k == 0) {
					System.out.print(i + "  ");
				}
				// If game not over and if number found (ship), then display as water instead
				if (!aiBoard[i][k].equals("~") && !aiBoard[i][k].equals("H") && !aiBoard[i][k].equals("M") && !gameOver) {
					System.out.print("~" + " ");
				} else {
					System.out.print( aiBoard[i][k] + " ");
				}
			}
			System.out.print(" |");
			System.out.println();
		}

		// Print bottom _______
		for (int i = 0; i < 52; i++) {
			System.out.print("-");
		}

		// Printing to use whose turn it is
		if (turn.equals("p")) {
			turn = "Player";
		}
		if (turn.equals("e")) {
			turn = "Enemy";
		}

		System.out.println("\nTurn: " + turn);


	}

	private static String[][] verifyAttack(String[][] board, String turn) {
		int column = -1;
		int row = -1;
		// If player's turn, allow choice to go
		if (turn.equals("p")) {
			System.out.println("Enter a valid column (A-J): ");
			char c = input.next().trim().toLowerCase().charAt(0);

			switch (c) {
			case 'a':
				column = 0;
				break;
			case 'b':
				column = 1;
				break;
			case 'c':
				column = 2;
				break;
			case 'd':
				column = 3;
				break;
			case 'e':
				column = 4;
				break;
			case 'f':
				column = 5;
				break;
			case 'g':
				column = 6;
				break;
			case 'h':
				column = 7;
				break;
			case 'i':
				column = 8;
				break;
			case 'j':
				column = 9;
				break;
			default:
				System.out.println("That isn't a valid option!");
				waitForInput();
				return null;
			}


			System.out.println("Enter a valid row (0-9): ");
			// Catch if user inputs anything other than integer
			try {
				row = input.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("That isn't a valid option!");
				waitForInput();
				return null;
			}

			if (!(row < 10 && row >= 0)) {

				System.out.println("That isn't a valid option!");
				waitForInput();
				return null;
			}
			// Miss
			if (board[row][column].equals("~")) {
				board[row][column] = "M";

			}
			// Hit
			if (!board[row][column].equals("~") && !board[row][column].equals("H") && !board[row][column].equals("M")) {
				board[row][column] = "H";
			}

		}
		// If AI, randomly, choose place
		if (turn.equals("e")) {
			// Normal difficulty (shoots at random)
			if (difficulty == 's') {
				row = generator.nextInt(10);
				column = generator.nextInt(10);
			}

			// hard difficulty (smart shooting, if found ship beam it, else randomly shoot)
			if (difficulty == 'h') {
				// Found hit ship with number next to it
				int nextX = -1;
				int nextY = -1;
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[i].length; j++) {
						// If found hit ship
						if (board[i][j].equals("H")) {
							// Search up, left, right, down for any numbers (ship remains)
							try {
								if (!board[i][j+1].equals("H") && !board[i][j+1].equals("M")) {
									nextX = i;
									nextY = j+1;
								}
							} catch (Exception e) {

							}

							try {
								if (!board[i][j-1].equals("H") && !board[i][j-1].equals("M")) {
									nextX = i;
									nextY = j-1;
								}
							} catch (Exception e) {
							}


							try {
								if (!board[i+1][j].equals("H") && !board[i+1][j].equals("M")) {
									nextX = i+1;
									nextY = j;
								}
							} catch (Exception e) {
							}

							try {
								if (!board[i-1][j].equals("H") && !board[i-1][j].equals("M")) {
									nextX = i-1;
									nextY = j;
								}
							} catch (Exception e) {
							}
						}
					}
				}

				// If never found hit ship
				if (nextX == -1) {
					row = generator.nextInt(10);
					column = generator.nextInt(10);
				} else { // If found hit ship, put next coordinates
					row = nextX;
					column = nextY;
				}
			}
			
			if (difficulty == 'i') {
				// Found hit ship with number next to it
				int nextX = -1;
				int nextY = -1;
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[i].length; j++) {
						// If found hit ship
						if (board[i][j].equals("H")) {
							// Search up, left, right, down for any numbers (ship remains)
							try {
								if (!board[i][j+1].equals("H") && !board[i][j+1].equals("M") && !board[i][j+1].equals("~")) {
									nextX = i;
									nextY = j+1;
								}
							} catch (Exception e) {

							}

							try {
								if (!board[i][j-1].equals("H") && !board[i][j-1].equals("M") && !board[i][j-1].equals("~")) {
									nextX = i;
									nextY = j-1;
								}
							} catch (Exception e) {
							}


							try {
								if (!board[i+1][j].equals("H") && !board[i+1][j].equals("M") && !board[i+1][j].equals("~")) {
									nextX = i+1;
									nextY = j;
								}
							} catch (Exception e) {
							}

							try {
								if (!board[i-1][j].equals("H") && !board[i-1][j].equals("M") && !board[i-1][j].equals("~")) {
									nextX = i-1;
									nextY = j;
								}
							} catch (Exception e) {
							}
						}
					}
				}

				// If never found hit ship
				if (nextX == -1) {
					row = generator.nextInt(10);
					column = generator.nextInt(10);
				} else { // If found hit ship, put next coordinates
					row = nextX;
					column = nextY;
				}
			}

			// Miss
			if (board[row][column].equals("~")) {
				board[row][column] = "M";
			}
			// Hit
			if (!board[row][column].equals("~") && !board[row][column].equals("H") && !board[row][column].equals("M")) {
				board[row][column] = "H";
			}
		}

		return board;

	}

	private static void waitForInput() {
		System.out.print("Press enter to continue: ");
		input.nextLine();
		input.nextLine();
	}

	private static String[][] placeShip(String[][] board, int size, String turn) {
		int column = -1;
		int row = -1;
		int direction = -1;
		// If player's turn, don't ask
		if (turn.equals("p")) {
			// Find row and column to place ship

			// Column of player's choice
			System.out.println("Enter a valid column (A-J): ");
			char c = input.next().trim().toLowerCase().charAt(0);

			switch (c) {
			case 'a':
				column = 0;
				break;
			case 'b':
				column = 1;
				break;
			case 'c':
				column = 2;
				break;
			case 'd':
				column = 3;
				break;
			case 'e':
				column = 4;
				break;
			case 'f':
				column = 5;
				break;
			case 'g':
				column = 6;
				break;
			case 'h':
				column = 7;
				break;
			case 'i':
				column = 8;
				break;
			case 'j':
				column = 9;
				break;
			default:
				System.out.println("That isn't a valid option!");
				waitForInput();
				return null;

			}
			// Row of player's choice
			System.out.println("Enter a valid row (0-9): ");
			// If user inputs anything other than an integer
			try {
				row = input.nextInt();
			} catch (InputMismatchException e) {
				return null;
			}

			if (!(row < 10 && row >= 0)) {

				System.out.println("That isn't a valid option!");
				waitForInput();
				return null;
			}
			// Find direction to place ship
			System.out.println("What direction to you want to place the ship:\n\t1. Up\n\t2. Down\n\t3. Left\n\t4. Right");
			System.out.print("Direction (1-4): ");
			try {
				direction = input.nextInt();
			} catch (Exception e) { // If user puts anything other than an integer
				return null;
			}
			// If valid choice from user
			if (!(direction > 0 && direction < 5 )) {
				return null;
			}
		}
		// if ai's turn
		if (turn.equals("e")) {
			row = generator.nextInt(10);
			column = generator.nextInt(10);
			direction  = generator.nextInt(4) + 1;
		}

		try {
			// up
			if (direction == 1) {
				for (int i = 0; i < size; i++) {
					// If spot already taken
					if (!board[row-i][column].equals("~")) {
						return null;
					}
				}
				// Actually place ship down if there are no previous ships
				for (int i = 0; i < size; i++) {
					board[row-i][column] = Integer.toString(size);
				}
			}
			// down
			if (direction == 2) {
				for (int i = 0; i < size; i++) {
					// If spot already taken
					if (!board[row+i][column].equals("~")) {
						return null;
					}
				}
				// Actually place ship down if there are no previous ships
				for (int i = 0; i < size; i++) {
					board[row+i][column] = Integer.toString(size);
				}
			}
			// left
			if (direction == 3) {
				// Check first to make sure ship isn't there
				for (int i = 0; i < size; i++) {
					// If spot already taken
					if (!board[row][column-i].equals("~")) {
						return null;
					}
				}
				// Actually place ship down if there are no previous ships
				for (int i = 0; i < size; i++) {
					board[row][column-i] = Integer.toString(size);
				}


			}
			// right
			if (direction == 4) {
				// Check first to make sure ship isn't there
				for (int i = 0; i < size; i++) {
					// If spot already taken
					if (!board[row][column+i].equals("~")) {
						return null;
					}
				}
				// Actually place ship down if there are no previous ships
				for (int i = 0; i < size; i++) {
					board[row][column+i] = Integer.toString(size);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}

		return board;
	}
	// null = no win, p = player won, e = AI Won
	private static String verifyWin(String[][] playerBoard, String[][] aiBoard) {
		boolean playerLoss = true;
		boolean aiLoss = true;
		// Check to see if any numbers left over in array for player
		for (int i = 0; i < playerBoard.length; i++) {
			for (int j = 0; j < playerBoard[i].length; j++) {
				// If it never comes across number (ship) never change to false
				if (!playerBoard[i][j].equals("~") && !playerBoard[i][j].equals("H") && !playerBoard[i][j].equals("M")) {
					playerLoss = false;
				}
			}
		}

		//Check to see if any numbers (ships) left over in array for AI
		for (int i = 0; i < aiBoard.length; i++) {
			for (int j = 0; j < aiBoard[i].length; j++) {
				if (!aiBoard[i][j].equals("~") && !aiBoard[i][j].equals("H") && !aiBoard[i][j].equals("M")) {
					aiLoss = false;
				}
			}
		}



		// If player lost, return winner as AI
		if (playerLoss) {
			score[1]++;
			return "ENEMY";
		}
		// If ai lost, return winner as Player
		if (aiLoss) {
			score[0]++;
			return "PLAYER";
		}
		// If neither players lose return null
		return null;

	}

	private static void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

}