import java.util.Random;
import java.util.Scanner;
class TrailEvent {
	private static final Random generator = new Random();
	private static final Scanner input = new Scanner(System.in);
	
	public TrailEvent() {
	    
	}
	
	public void chanceEvent(Game game, Wagon wagon, PlayerParty party, Location location, Food food, int severityRation, int severityPace) {
		int chanceEvent = 13;
		int possibleChanceEvent = severityRation+severityPace;
		boolean causeEvent = false;
		// Cause more events to happen if steady pace and filling (too safe make game interesting)
		if (possibleChanceEvent == 2) {
		    possibleChanceEvent += 2;
		}
		
	    // If clothing is less than 1 per person (someone not having clothing) increase severity by 1
	    if (wagon.getClothing().total() < (party.amountAlive())) {
	        possibleChanceEvent+=1;
	    }
		
		// Run severity amount of times (if both max + low clothing : 7) out of 13 times
		// Chance of worst happening is 53%, chance of best happening is 30%
		for (int i = possibleChanceEvent; i > 0; i--) {
			if (generator.nextInt(chanceEvent) == 0) {
				causeEvent = true;
			}
		}
		// 1/4 chance to injure a random person, 2/4 chance to break item, 1/4 chance to lose trail (lose 2-3 days)
		if (causeEvent) {
			int randomEvent = generator.nextInt(4);
			if (randomEvent == 0) {

				// injure random player
				party.getRandomTraveler().injure();
			} else if (randomEvent == 1 || randomEvent == 2){
				// Break item
				InventoryItem i = wagon.getRandomItem();
				if (i instanceof Oxen) {
					i.remove(1);
					System.out.println("One of your oxen died!");
				}
				if (i instanceof Food)  {
					int amountLoss = generator.nextInt(10) + 15;
					i.remove(amountLoss);
					System.out.println("You lost " + amountLoss + " pounds of food!");
				}
				if (i instanceof Clothing)  {
					int amountLoss = generator.nextInt(2) + 1;
					i.remove(amountLoss);
					System.out.println("You lost " + amountLoss + " sets of clothing!");
				}
				if (i instanceof Ammo) {
					int amountLoss = generator.nextInt(10) + 5;
					i.remove(amountLoss);
					System.out.println("You lost " + amountLoss + " bullets!");
				}
				if (i instanceof Wheels) {
					if (wagon.getWheels().total() == 0) { // If can't lose anymore wagon wheels punish player
					    System.out.println("You don't have any replacement wagon wheels!");
					    System.out.println("Lose 4 days!");
					    for (int j = 4; j > 0; j--) {
					        party.eat(game, food, true, wagon, location);
					    } return;
					}
					int amountLoss = generator.nextInt(2) + 1; // 1-2
					i.remove(amountLoss);
					System.out.println("You lost " + amountLoss + " wheel(s)!");
				}
				if (i instanceof Axles) {
				    if (wagon.getAxles().total() == 0) { // If can't lose anymore wagon axles punish player
					    System.out.println("You don't have any replacement wagon axles!");
					    System.out.println("Lose 4 days!");
					    for (int j = 4; j > 0; j--) {
					        party.eat(game, food, true, wagon, location);
					    } return;
					}
				    
				    int amountLoss = generator.nextInt(2) + 1; // 1-2
					i.remove(amountLoss);
				    System.out.println("You lost " + amountLoss + " axles(s)!");
				}
				if (i instanceof Tongues) {
				    if (wagon.getTongues().total() == 0) { // If can't lose anymore wagon tongues punish player
					    System.out.println("You don't have any replacement wagon tongues!");
					    System.out.println("Lose 4 days!");
					    for (int j = 4; j > 0; j--) {
					        party.eat(game, food, true, wagon, location);
					    } return;
					}
				    int amountLoss = generator.nextInt(2) + 1; // 1-2
					i.remove(amountLoss);
				    System.out.println("You lost " + amountLoss + " tongue(s)!");
				}
			} else if (randomEvent == 3) {
			    int randLoss = generator.nextInt(2) + 2; // 2-3
			    String[] lostTrail = {"Wrong trail", "Ox wanders off", "Severe thunderstorm"};
			    System.out.println(lostTrail[generator.nextInt(2)] + ". Lose " + randLoss + " days!");
			    for (int i = randLoss; i > 0; i--) {
			        party.eat(game, food, true, wagon, location);
			    }
			}
		}
	}
	
	// Returns true if player won
	public boolean rockPaperScissors() {

		while (true) {
			// Clear console
			System.out.print("\033[H\033[2J");
			System.out.flush();

            // 0 = Rock, 1 = Paper, 2 = Scissors
		    int playerAction = -1;

            int computer = generator.nextInt(3); // 0-2
		    boolean tie = false;
		
		    for (int i = generator.nextInt(50); i > 0; i--) {
		        if (i == 1) {
		            computer = generator.nextInt(3);
		        }
		    }


			// Ask user for choice
			System.out.println("Your Actions:\n\t1. Rock\n\t2. Paper\n\t3. Scissors\n\t4. Exit (Forfeit)\n");
			System.out.print("Choice: ");
			String choice = input.nextLine().toLowerCase().trim();
			if (choice.equals("1") || choice.equals("rock")) {
				playerAction = 0; // Rock
			} else if (choice.equals("2") || choice.equals("paper")) {
				playerAction = 1; // Paper
			} else if (choice.equals("3") || choice.equals("scissors")) {
				playerAction = 2; // Scissors
			} else if (choice.equals("4") || choice.equals("exit") || choice.equals("quit") || choice.equals("stop")) {
			    return false;
			} else { // If not valid option restart
				System.out.println("That isn't a valid option!");
				System.out.print("Press enter to continue: ");
				input.nextLine();
				continue;
			}
    
            // Output human action
            System.out.println();
            if (playerAction == 0) System.out.println("You played rock!");
            if (playerAction == 1) System.out.println("You played paper!");
            if (playerAction == 2) System.out.println("You played scissors!");
    
			// Output computer action
			if (computer == 0) System.out.println("Computer played rock!");
			if (computer == 1) System.out.println("Computer played paper!");
			if (computer == 2) System.out.println("Computer played scissors!");
			
			
			
			// Verify win --- >  0 = Rock, 1 = Paper, 2 = Scissors
			if (playerAction == 0 && computer == 0) tie = true; // if tie, restart
			if (playerAction == 0 && computer == 1) return false; // player == rock, computer == paper (loss)
			if (playerAction == 0 && computer == 2) return true; // player == rock, computer == scissors (win)

			if (playerAction == 1 && computer == 0) return true; // player == paper, computer == rock (win)
			if (playerAction == 1 && computer == 1) tie = true; // if tie, restart
			if (playerAction == 1 && computer == 2) return false; // player == paper, compuer == scissors (loss)

			if (playerAction == 2 && computer == 0) return false; // player == scissors, computer == rock (loss)
			if (playerAction == 2 && computer == 1) return true; // player == scissors, computer == paper (win)
			if (playerAction == 2 && computer == 2) tie = true; // if tie, restart
			
			if (tie) {
			    System.out.println("You tied!");
			    System.out.print("Press enter to continue: ");
			    input.nextLine();
			    continue;
			}

	    }
	}
	
}