import java.util.Random;
import java.util.Scanner;

public class tourneyBotOriginal {
    private static final int SIZE = 4;
    private static final char EMPTY = '.';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static char[][] board = new char[SIZE][SIZE];
    private static int winsX = 0;
    private static int winsO = 0;
   
    public static void main(String[] args) {
        for (int game = 0; game < 100; game++) {
            initializeBoard();
            boolean xTurn = game % 2 == 0;
           
            while (true) {
                if (xTurn) {
                    cpuMoveX(); // Team X - Replace this function with your logic
                } else {
                    cpuMoveO();
                }
               
                char currentPlayer = PLAYER_X;
                if (!xTurn) {
                    currentPlayer = PLAYER_O;
                }
               
                if (checkWin(currentPlayer)) {
                    if (currentPlayer == PLAYER_X) {
                        winsX++;
                    } else {
                        winsO++;
                    }
                    System.out.println(currentPlayer + " wins!");
                    break;
                }
                if (isBoardFull()) {
                    System.out.println("It's a draw!");
                    break;
                }
               
                xTurn = !xTurn;
            }
            printBoard();
        }
       
        // Update score based on wins
        int scoreX = 0;
        int scoreO = 0;
        if (winsX > winsO) {
            scoreX = 3;
        } else {
            if (winsO > winsX) {
                scoreO = 3;
            } else {
                scoreX = 1;
                scoreO = 1;
            }
        }
        System.out.println("Final Score: X = " + winsX + ", O = " + winsO);
        System.out.println("Final Score: X = " + scoreX + ", O = " + scoreO);
    }

    private static void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    // My bot: Plays defensive if it sees enemy already has 2 in a row, otherwise play attack
    private static char[][] cpuMoveX() {
		// ~ ~ Allow easy access to change priorities ~ ~
    	// 0 - Defensive (~77% win, ~0.003% loss, ~23.0% tie) 1 - Offensive (~84.5% win, ~0.3% loss, ~15.2% tie)
    	int priority = 0; // Default = 0
    	
    	// Check to see if player is about to win
		// Priorities
		char[] players = {PLAYER_X, PLAYER_O};
    	
		for (char p : players) {
		// ~ ~ ~ ~ ~ Check first for any possible wins for player_x ~ ~ ~ ~ ~ 
		for (int k =-2; k<1; k++) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					// VERTICAL
					try {
						if (board[i][j-k] == EMPTY && board[i][j-k+1] == p && board[i][j-k+2] == p && board[i][j-k+3] == p) {
							board[i][j-k] = PLAYER_X;
							return board;
						}
					}catch (Exception e) {}
					try {
						if (board[i][j-k] == p && board[i][j-k+1] == EMPTY && board[i][j-k+2] == p && board[i][j-k+3] == p) {
							board[i][j-k+1] = PLAYER_X;
							return board;
						}
					}catch (Exception e) {}
					try {
						if (board[i][j-k] == p && board[i][j-k+1] == p && board[i][j-k+2] == EMPTY && board[i][j-k+3] == p) {
							board[i][j-k+2] = PLAYER_X;
							return board;
						}
					}catch (Exception e) {}
					
					try {
						if (board[i][j-k] == p && board[i][j-k+1] == p && board[i][j-k+2] == p && board[i][j-k+3] == EMPTY) {
							board[i][j-k+3] = PLAYER_X;
							return board;
						}
					}catch (Exception e) {}
					
					// HORIZONTAL
					try {
						if (board[i-k][j] == EMPTY && board[i-k+1][j] == p && board[i-k+2][j] == p && board[i-k+3][j] == p) {
							board[i-k][j] = PLAYER_X;
							return board;
						}
					}catch(Exception e) {}
					
					try {
						if (board[i-k][j] == p && board[i-k+1][j] == EMPTY && board[i-k+2][j] == p && board[i-k+3][j] == p) {
							board[i-k+1][j] = PLAYER_X;
							return board;
						}
					}catch(Exception e) {}
					
					try {
						if (board[i-k][j] == p && board[i-k+1][j] == p && board[i-k+2][j] == EMPTY && board[i-k+3][j] == p) {
							board[i-k+2][j] = PLAYER_X;
							return board;
						}
					}catch(Exception e) {}
					
					try {
						if (board[i-k][j] == p && board[i-k+1][j] == p && board[i-k+2][j] == p && board[i-k+3][j] == EMPTY) {
							board[i-k+3][j] = PLAYER_X;
							return board;
						}
					}catch(Exception e) {}
				}
			}
		}
		
		
		// CHECK IF PLAYER IS ABOUT TO WIN BOTTOM LEFT TO TOP RIGHT
		// DIAGONAL BOTTOM LEFT TO UP RIGHT
		if (board[0][0] == EMPTY && board[1][1] == p && board[2][2] == p && board[3][3] == p) {
			board[0][0] = PLAYER_X;
			return board;
		}
		if (board[0][0] == p && board[1][1] == EMPTY && board[2][2] == p && board[3][3] == p) {
			board[1][1] = PLAYER_X;
			return board;
		}
		if (board[0][0] == p && board[1][1] == p && board[2][2] == EMPTY && board[3][3] == p) {
			board[2][2] = PLAYER_X;
			return board;
		}
		if (board[0][0] == p && board[1][1] == p && board[2][2] == p && board[3][3] == EMPTY) {
			board[3][3] = PLAYER_X;
			return board;
		}
		
		
		// DIAGONAL BOTTOM RIGHT TO UP LEFT
		if (board[3][0] == EMPTY && board[2][1] == p && board[1][2] == p && board[0][3] == p) {
			board[3][0] = PLAYER_X;
			return board;
		}
		if (board[3][0] == p && board[2][1] == EMPTY && board[1][2] == p && board[0][3] == p) {
			board[2][1] = PLAYER_X;
			return board;
		}
		if (board[3][0] == p && board[2][1] == p && board[1][2] == EMPTY && board[0][3] == p) {
			board[1][2] = PLAYER_X;
			return board;
		}
		if (board[3][0] == p && board[2][1] == p && board[1][2] == p && board[0][3] == EMPTY) {
			board[0][3] = PLAYER_X;
			return board;
		}
		}
		
		// ~ ~ ~ IF NO POSSIBLE WINS CHECK FOR DEFENSIVE AND OFFENSIVE MANUEVERS ~ ~ ~ 
		
		// Transfer priorities
		// PLAYER_O, PLAYER_X = Highly defensive, loses less games, ties more often, opponent scores almost never
		// PLAYER_X, PLAYER_O = Highly offensive, wins more games, ties very little, opponent scores only a bit
		if (priority == 0) {
			players[0] = PLAYER_O;
			players[1] = PLAYER_X;
		} else {
			players[0] = PLAYER_X;
			players[1] = PLAYER_O;
		}
		
		// Vertical Check both board[i][j-2] up to board[i][j+2]
		for (char player : players) {
	    	for (int k = -2; k < 1; k++) {
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[i].length; j++) {
						try {
							// Player about to win right
							if (board[i][j-k] == EMPTY && board[i][j-k+1] == player && board[i][j-k+2] == player) {
								board[i][j-k] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
						try {
							// Player about to win left
							if (board[i][j-k] == player && board[i][j-k+1] == player && board[i][j-k+2] == EMPTY) {
								board[i][j-k+2] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
						try { // Player about to win middle
							if (board[i][j-k] == player && board[i][j-k+1] == EMPTY && board[i][j-k+2] == player) {
								board[i][j-k+1] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
					}
				}
			}
	
			// Horizontal check both board[i-2][j] up to board[i+2][j]
			for (int k = -2; k < 1; k++) {
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[i].length; j++) {
						try {
							// Player about to win right
							if (board[i-k][j] == player && board[i-k+1][j] == player && board[i-k+2][j] == EMPTY) {
								board[i-k+2][j] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
						try {
							// Player about to win left
							if (board[i-k][j] == EMPTY && board[i-k+1][j] == player && board[i-k+2][j] == player) {
								board[i-k][j] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
	
						try {
							// Player about to win middle
							if (board[i-k][j] == player && board[i-k+1][j] == EMPTY && board[i-k+2][j] == player) {
								board[i-k+1][j] = PLAYER_X;
								return board;
							}
						} catch (Exception e) {}
					}
				}
			}
	
			// Diagonal check both board[i-2][j] up to board[i+2][j]
			// bottom right to up left
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					// Player about to win top left
					try { // Check for pattern
						if (board[i][j] == player && board[i+1][j+1] == player && board[i+2][j+2] == EMPTY) {
							board[i+2][j+2] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
					// Player about to win bottom right
					try { // Check for pattern
						if (board[i][j] == EMPTY && board[i+1][j+1] == player && board[i+2][j+2] == player) {
							board[i][j] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
	
					// Player about to win middle
					try { // Check for pattern
						if (board[i][j] == player && board[i+1][j+1] == EMPTY && board[i+2][j+2] == player) {
							board[i+1][j+1] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
				}
			}
			
			
			//Player about to win bottom left
			// top left to bottom right pattern
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					try { // Check for pattern
						if (board[i][j] == EMPTY && board[i+1][j-1] == player && board[i+2][j-2] == player) {
							// Bottom left about to win
							board[i][j] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
	
					try { // Check for pattern
						if (board[i][j] == player && board[i+1][j-1] == EMPTY && board[i+2][j-2] == player) {
							// Middle about to win
							board[i+1][j-1] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
	
					try { // Top left about to win
						if (board[i][j] == player && board[i+1][j-1] == player && board[i+2][j-2] == EMPTY) {
							// Middle about to win
							board[i+2][j-2] = PLAYER_X;
							return board;
						}
					} catch (Exception e) {
					}
				}
			}
		



		/*PATTERN: |
		1,1        |
		1,2        |
		2,1 	   V
		2,2*/
		// If no attack or defense needed, attack middle
		for (int i = 1; i < 3; i++) {
			for (int j = 1; j < 3; j++) {
				if (board[i][j] == EMPTY) {
					board[i][j] = PLAYER_X;
					return board;
				}
			}
		}
		}
		
		
		// If player isn't about to win, and middle taken, corners taken, place randomly
		while (true) {
		    Random generator = new Random();
			int randRow = generator.nextInt(4);
			int randCol = generator.nextInt(4);
			if (board[randCol][randRow] == EMPTY) {
				board[randCol][randRow] = PLAYER_X;
				break;
			}
		}

		return board;
	}
   
    private static void cpuMoveO() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
        } while (board[row][col] != EMPTY);
        board[row][col] = PLAYER_O;
    }

    private static boolean checkWin(char player) {
        for (int i = 0; i < SIZE; i++) {
            if (checkLine(player, board[i][0], board[i][1], board[i][2], board[i][3])) {
                return true;
            }
            if (checkLine(player, board[0][i], board[1][i], board[2][i], board[3][i])) {
                return true;
            }
        }
       
        if (checkLine(player, board[0][0], board[1][1], board[2][2], board[3][3])) {
            return true;
        }
        if (checkLine(player, board[0][3], board[1][2], board[2][1], board[3][0])) {
            return true;
        }
        return false;
    }

    private static boolean checkLine(char player, char a, char b, char c, char d) {
        return a == player && b == player && c == player && d == player;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
   
    private static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}