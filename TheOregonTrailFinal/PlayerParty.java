import java.util.*;
class PlayerParty {
	double money = 1600;
	ArrayList<Traveler> travelers = new ArrayList<>();
	Random generator = new Random();
	Scanner input = new Scanner(System.in);

	// 3 = Bare Bones, 2 = Meager, 1 = Filling
	private int ration = 1;
	public int severityRation = 0;
	private int restCount = 0;
	// Dont let party die of starvation if they run out of food
	private boolean finalStand = false;

	public PlayerParty(int money) {
		this.money = money;
	}
	public PlayerParty() {

	}
	// Eat food based on the rations
	public void eat(Game game, Food food, boolean lostTrail, Wagon wagon, Location location) {
		// If out of control to user for eating (i.e. caused by event) dont put in final stand and die
		if (!lostTrail) {
			// Warning system
			if (food.total() <= 0) {
				System.out.println("You ate the remaining amount of your food!");
				System.out.println("You are about to starve!");
			} else {
				finalStand = false;
			}

			if (finalStand) {
				System.out.println("You ran out of food and died of starvation!");
				System.out.println("GAME OVER");
				game.displayScoring(wagon, this, location);
			}
			// Set status to final stand
			if (food.total() <= 0) {
				finalStand = true;
			}
		}

		int amountLoss = 0;
		if (ration == 3) { // Bare bones
			amountLoss = generator.nextInt(5) + 41;
			food.remove(amountLoss);
			severityRation = ration;
		} else if (ration == 2) { // Meager
			amountLoss = generator.nextInt(10) + 51;
			food.remove(amountLoss);
			severityRation = ration;
		} else { // Filling
			amountLoss = generator.nextInt(15) + 61;
			food.remove(amountLoss);
			severityRation = ration;
		}
		if (food.total() != 0) {
			System.out.println("You ate " + amountLoss + " lbs of food!");
		}

	}
	// Ammo --> food
	// Play rock paper scissors and every win add 15 lbs of food, -3 bullet every time played
	public void hunt(TrailEvent minigames, Food food, Ammo ammo) {
		int timesWon = 0;
		int foodWinAmount = 30;
		int ammoRemoveAmount = 5;
		boolean win = minigames.rockPaperScissors();
		while (win && (ammo.total() - ammoRemoveAmount >= 0)) {
			System.out.println("You won!");
			timesWon++;
			ammo.remove(ammoRemoveAmount);
			System.out.println("Press enter to continue: ");
			input.nextLine();
			win = minigames.rockPaperScissors();

		}
		ammo.remove(ammoRemoveAmount);
		food.add(foodWinAmount * timesWon);

		if (ammo.total() == 0) {
			System.out.println("You're out of bullets!");
		}
		if (!win) {
			System.out.println("You lost! +" + (timesWon * foodWinAmount) + " lbs of food   -" + (ammoRemoveAmount* (timesWon+1)) + " bullets");
		}
	}
	// Grabs a random traveler from the list of travelers
	public Traveler getRandomTraveler() {
		return travelers.get(generator.nextInt(travelers.size()));
	}

	// Add traveler to list
	public void addTraveler(Traveler t) {
		travelers.add(t);
	}
	// Returns entire traveler list
	public ArrayList<Traveler> getTravelers() {
		return travelers;
	}
	// Create party of x amount of players
	public void createPlayers(int totalPlayers) {
		// Create players
		for (int i = 0; i < (totalPlayers); i++) {
			// Create leader
			if (i == 0) {
				System.out.println("First name of the leader? ");
				// At to the player party list of new traveler with name inputted by user
				addTraveler(new Traveler(input.nextLine()));
			} else {
				System.out.print("(" + (i+1) + "/" + totalPlayers + ") Input a first name for your new traveler: ");
				addTraveler(new Traveler(input.nextLine()));
			}
		}
	}
	// Used for auto indenting too make lists look cleaner
	public String spacing(String name, int spaces) {
		int spaceing = spaces - name.length();
		String returnString = "";
		for (int i = 0; i < spaceing; i++) {
			returnString += " ";
		}
		return returnString;
	}

	public int amountAlive() {
		int counter = 0;
		for (Traveler t : travelers) {
			if (t.isAlive()) {
				counter++;
			}
		}
		return counter;
	}
	public int amountHealthy() {
		int counter = 0;
		for (Traveler t : travelers) {
			if (t.getHealth() == 2) {
				counter++;
			}
		}
		return counter;
	}
	public int amountInjured() {
		int counter = 0;
		for (Traveler t : travelers) {
			if (t.getHealth() == 1) {
				counter++;
			}
		}
		return counter;
	}
	public int amountDead() {
		int counter = 0;
		for (Traveler t : travelers) {
			if (t.getHealth() == 0) {
				counter++;
			}
		}
		return counter;
	}


	// Allow travelers to heal after total of 3 times (eats food every time)
	public void rest(Game game, Food food, Wagon wagon, Location location) {
		System.out.print("The party sleeps well after a long day ");
		if (restCount <= 1) {
			System.out.println("(keep resting to fully heal)");
			this.restCount++;
		} else {
			System.out.println();
			this.restCount = 0;
			for (Traveler t : travelers) {
				t.heal();
			}
		}
		eat(game, food, false, wagon, location);
	}


	// Returns entire player list and health
	public String toString() {
		String names = "Party:\n";
		int counter = 0;
		for (Traveler t : travelers) {
			if (counter == 0) {
				names += "\t" +  "Leader: " + t.getName();
				names += spacing("12345"+t.getName(), 20);
				names += "  Health: " + t.displayHealth() + "\n";
			}
			else {
				names += "\t" + counter + ". " + t.getName();
				names += spacing(t.getName(), 20);
				names += "  Health: " + t.displayHealth() + "\n";
			}
			counter+=1;
		}

		return names;
	}
	// Display rations
	private String displayRation() {
		if (this.ration == 3) {
			return "Bare Bones";
		} else if (this.ration == 2) {
			return "Meager";
		} else if (this.ration == 1) {
			return "Filling";
		}
		return "N/A";
	}
	// 3 = Bare Bones, 2 = Meager, 1 = Filling
	public void changeRation() {
		// Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
		// Dispaly options
		System.out.println("Current Ration: " + displayRation());
		System.out.println("Options: \n\t1. Filling\n\t2. Meager\n\t3. Bare Bones\n\t4. Find out what this means\n");
		System.out.println("New Ration (1-4): ");
		String choice = input.nextLine().trim();
		if (choice.equals("1")) {
			this.ration = 1;
			return;
		} else if (choice.equals("2")) {
			this.ration = 2;
			return;
		} else if (choice.equals("3")) {
			this.ration = 3;
			return;
		} else if (choice.equals("4")) {
			// Clear console
			System.out.print("\033[H\033[2J");
			System.out.flush();
			System.out.println("Filling - meals are large and generous.");
			System.out.println("\nMeager - meals are small, but adequate.");
			System.out.println("\nBare Bones - meals are very small;\neveryone stays hungry.");
			System.out.print("\n\nPress enter to continue: ");
			input.nextLine();
			changeRation();
		} else if (choice.equals("")) { // Allow exit without changing anything
			return;
		}
		else { // invalid option
			changeRation();
		}
		return;

	}

	public void drownRandomTraveler() {
		Traveler t = travelers.get(generator.nextInt(travelers.size()));
		t.dead = true;
		System.out.println(t.getName() + " drowned!");
	}

	public boolean attemptTrade(Wagon wagon, boolean chanceSell) {
		if (generator.nextInt(8) == 0) chanceSell = true; // chance to sell an existing item instad
		InventoryItem item1 = wagon.getRandomItem();
		InventoryItem item2 = wagon.getRandomItem();
		// reroll second item if you're trading the same item for the same item
		while (true) {
			if (item1.getName().equals(item2.getName())) {
				item2 = wagon.getRandomItem();
				continue;
			} else {
				break;
			}
		}

		double tradeGetAmount = 0;
		double tradeGiveAmount = 0;

		// Giveaway items
		if (item1 instanceof Oxen) tradeGiveAmount = generator.nextInt(2) + 1; // 1-2
		if (item1 instanceof Food) tradeGiveAmount = generator.nextInt(25) + 25; // 25-50
		if (item1 instanceof Clothing) tradeGiveAmount = generator.nextInt(3) + 1; // 1-3
		if (item1 instanceof Ammo) tradeGiveAmount = generator.nextInt(15) + 11; // 10-25
		if (item1 instanceof Wheels) tradeGiveAmount = generator.nextInt(2) + 1; //1-2
		if (item1 instanceof Axles) tradeGiveAmount = generator.nextInt(2) + 1; //1-2
		if (item1 instanceof Tongues) tradeGiveAmount = generator.nextInt(2) + 1; //1-2


		// Get items offer
		if (!chanceSell) {
			if (item2 instanceof Oxen) tradeGetAmount = generator.nextInt(2) + 1; // 1-2
			if (item2 instanceof Food) tradeGetAmount = generator.nextInt(25) + 25; // 25-50
			if (item2 instanceof Clothing) tradeGetAmount = generator.nextInt(3) + 1; // 1-3
			if (item2 instanceof Ammo) tradeGetAmount = generator.nextInt(15) + 11; // 10-25
			if (item2 instanceof Wheels) tradeGetAmount = generator.nextInt(2) + 1; //1-2
			if (item2 instanceof Axles) tradeGetAmount = generator.nextInt(2) + 1; //1-2
			if (item2 instanceof Tongues) tradeGetAmount = generator.nextInt(2) + 1; //1-2
		} else {
		    tradeGetAmount = item1.getCost() * tradeGiveAmount;
		}

		// Check to make sure player can make trade
		if (item1.total() >= tradeGiveAmount) {
			if (!chanceSell) System.out.println("Do you want to give " + (int) tradeGiveAmount + " " + item1.getName() + " for " + (int) tradeGetAmount + " " + item2.getName() + "?");
			else System.out.println("Do you want to give " + (int) tradeGiveAmount + " " + item1.getName() + " for $" + (int) tradeGetAmount + "?");
			System.out.print("Choice (y/n): ");
			String choice = input.nextLine();
			try {
				if (choice.toLowerCase().trim().charAt(0) == 'y') {
					item1.remove(tradeGiveAmount);
					item2.add(tradeGetAmount);
					return true;
				} else {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That isn't a valid option!");
				return false;

			}
		} else { // Can't give enough for trade (sell instead)
			// Make sure they aren't stuck in an infinite loop of searching for something (if player has nothing to trade at all)
			boolean hasSomething = false;
			for (InventoryItem i : wagon.getItems()) {
			    if (i.total() > 0) {
			        hasSomething = true;
			    }
			}
			if (hasSomething) attemptTrade(wagon, true);
			else System.out.println("You don't have anything to trade!");
		
			return false;
		}
	}
}