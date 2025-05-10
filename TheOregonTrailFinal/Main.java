import java.util.*;
public class Main {
	public static void main(String[] args) {
        // Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
		
		// Load game and start
		Game game = new Game();
		game.start();
	}
}