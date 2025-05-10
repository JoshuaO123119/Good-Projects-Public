import java.util.*;
public class Game
{
	public void start() {
		// Allow input from user
		Scanner input = new Scanner(System.in);
		// Easy - 1600, Medium = 800, Hard = 400
		int startMoney = 1600;

		// Ask user for difficulty
		while (true) {
			System.out.print("\033[H\033[2J");
			System.out.flush();
			System.out.println("Difficulty Options:\n\t1. Banker (Easy)\n\t2. Carpenter (Medium)\n\t3. Farmer (Hard)\n\t4. Find out what these mean\n");
			System.out.print("Choice (1-4): ");
			String choice = input.nextLine().trim();
			if (choice.equals("1")) {// Easy
				startMoney = 1600;
			} else if (choice.equals("2")) { // Medium
				startMoney = 800;
			} else if (choice.equals("3")) { // Hard
				startMoney = 400;
			} else if (choice.equals("4")) {
				System.out.print("\033[H\033[2J");
				System.out.flush();

				System.out.println("Banker    - Starts with 1600 cash");
				System.out.println("Carpenter - Starts with 800 cash");
				System.out.println("Farmer    - Starts with 400 cash");
				System.out.print("\nPress enter to continue: ");
				input.nextLine();
				continue;
			}
			else {
				System.out.println("That isn't a valid option!");
				System.out.print("Press enter to continue: ");
				input.nextLine();
				continue;
			}
			// Clear console
			System.out.print("\033[H\033[2J");
			System.out.flush();
			break;
		}

		// Initialize all classes
		TrailEvent event = new TrailEvent();
		Location location = new Location();
		Wagon wagon = new Wagon();
		PlayerParty party = new PlayerParty(startMoney);
		// Initialize items and assign wagon to it (for inventory use later)
		Oxen oxen = new Oxen(wagon);
		Food food = new Food(wagon);
		Clothing clothing = new Clothing(wagon);
		Ammo ammo = new Ammo(wagon);
		Wheels wheels = new Wheels(wagon);
		Axles axles = new Axles(wagon);
		Tongues tongues = new Tongues(wagon);

		int totalPlayers = 5;

		// Create Players
		party.createPlayers(totalPlayers);

		// Display Party to player
		System.out.println(party);
		System.out.print("Press enter to continue: ");
		input.nextLine();

		// Main Game loop
		while (true) {
			if (party.amountAlive() == 0) {
				System.out.println("Your entire party died! You lose!");
				displayScoring(wagon, party, location);
			}
			if (!location.needSwim) {
				wagon.hasSwam = false;
				displayOptions(wagon, food, location);

				System.out.print("Choice (1-10): ");
				String choice = input.nextLine().toLowerCase().trim();
				// Continue on trail
				if (choice.equals("1")) {
					wagon.move(this, event, party, location, food);
				} else if (choice.equals("2")) { // Check supplies
					wagon.showInventory();
				} else if (choice.equals("3")) { // Party Health
					System.out.println(party);
				} else if (choice.equals("4")) { // Change pace
					wagon.changePace();
					continue;
				} else if (choice.equals("5")) { // Change rations
					party.changeRation();
					continue;
				} else if (choice.equals("6")) { // Rest
					party.rest(this, food, wagon, location);
				} else if (choice.equals("7")) { // Attempt to trade
					if (party.attemptTrade(wagon, false)) continue;
				} else  if (choice.equals("8")) { // Buy supplies
					wagon.buyItems(party, location);
					continue;
				} else if (choice.equals("9")) { // Hunt
					party.hunt(event, food, ammo);
				} else if (choice.equals("10")) { // Look at map
					location.displayMap(wagon.distance);
				}
				else {
					System.out.println("That isn't a valid option!");
					System.out.print("Press enter to continue: ");
					input.nextLine();
					continue;
				}
				// Did player win yet
				if (location.winCheck()) {
					System.out.println("You arrived at Oregon City!");
					System.out.println("You win!");
					displayScoring(wagon, party, location);
				}
				System.out.print("Press enter to continue: ");
				input.nextLine();
			} else {
				wagon.swimAction(this, party, location, -1, -1);
				System.out.println("You continue down the trail.");
				System.out.print("Press enter to continue: ");
				input.nextLine();
			}
		}
	}
	// Display all options player can make
	public void displayOptions(Wagon wagon, Food food, Location location) {
		location.updateDistanceToGo(wagon.distance);
		// Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
		// Display available options
		System.out.println("Food: " + food.total());
		System.out.println("Miles Traveled: " + wagon.getDistance());
		System.out.println("Next Location: " + location.nextDestination() + " " + location.getDistanceToGo() + " miles left");
		System.out.println("\nAvailable Actions:\n\t1. Continue on trail\n\t2. Check Supplies\n\t3. Party Health\n\t4. Change Pace\n\t5. Change Rations\n\t6. Rest\n\t7. Attempt to trade\n\t8. Buy Supplies\n\t9. Hunt in nearby forest\n\t10. Look at map\n");
	}

	public void displayScoring(Wagon wagon, PlayerParty party, Location location) {
		int spaces = 30;
		int endSpaces = 24;
		double score = 0;
		String amountAliveStr = "| Travelers Alive: ";
		int amountAlive = party.amountAlive();
		String amountInjuredStr = "| Travelers Injured: ";
		int amountInjured = party.amountInjured();
		String amountDeadStr = "| Travelers Dead: ";
		int amountDead = party.amountDead();
		String distanceTraveledStr = "| Distance Traveled: ";
		String finalScoreStr = "FINAL SCORE: ";
		String moneyTotalStr = "| Total Money: ";
		String winBonusStr = "Win Bonus: ";
		
		double winBonus = 0;
		if (location.winCheck()) { // If won double score
		    winBonus = 1.5;
		}
		
		String winBonusMultiStr = " x" + String.valueOf(winBonus) + " = ";

		int lineSize = (amountAliveStr + String.valueOf(amountAlive) + spacing(amountAliveStr, amountAlive, spaces) + " x100 = " + String.valueOf((amountAlive * 100)) + "  |").length();

		score+= (amountAlive * 100);
		score+= (amountInjured * 50);
		score+= (amountDead * 20);
		score+= wagon.distance;
		score+= (int)party.money;
		System.out.println(returnFiller(lineSize/2+1, false, false) + " SCORING " + returnFiller(lineSize/2, true, false) + "|");
		System.out.println(returnFiller(lineSize+10, false, true) + "|");
		System.out.println(moneyTotalStr + party.money + spacing(moneyTotalStr, (int) party.money, spaces-2) + " x1 = " + ((int) party.money) + spacing("x1 = ", ((int) party.money), endSpaces) + "|");
		System.out.println(returnFiller(lineSize+10, false, true) + "|");
		System.out.println(amountAliveStr + amountAlive + spacing(amountAliveStr, amountAlive, spaces) + " x100 = " + (amountAlive * 100) + spacing("x100 = ", (amountAlive *100), endSpaces) + "|");
		System.out.println(amountInjuredStr + amountInjured + spacing(amountInjuredStr, amountInjured, spaces) + " x50 = " + (amountInjured * 20) + spacing("x50 = ",(amountInjured * 50), endSpaces) + "|");
		System.out.println(amountDeadStr + amountDead + spacing(amountDeadStr, amountDead, spaces) + " x20 = " + (amountDead * 20) + spacing("x20 = ", (amountDead * 20), endSpaces) + "|");
		System.out.println(returnFiller(lineSize+10, false, true) + "|");
		System.out.println(distanceTraveledStr + wagon.distance + spacing(distanceTraveledStr, wagon.distance, spaces) + " x1 = " + (wagon.distance) + spacing("x1 = ", (wagon.distance), endSpaces) + "|");
		System.out.println(returnFiller(lineSize+10, false, true) + "|");

		// Print all items
		for (InventoryItem i : wagon.getItems()) {
			if (i instanceof Oxen) {
			    System.out.println("| "+ i.getName() + ": " + (int) i.total() + spacing(i.getName() + "    ", (int) i.total(), spaces) + " x20 = " + ((int) i.total() * 20) + spacing("x20 = ", ((int) i.total() * 20), endSpaces) + "|");
			score+= ((int) i.total() * 20);
			continue;
			}
			if (i instanceof Ammo) {
				System.out.println("| "+ i.getName() + ": " + (int) i.total() + spacing(i.getName() + "    ", (int) i.total(), spaces) + " x0.5 = " + ((int) (i.total() * 0.5)) + spacing("x0.5 = ", ((int)(i.total() * 0.5)), endSpaces) + "|");
			    score+= ((int) (i.total() * 0.5));
			    continue;
			}
			if (i instanceof Food) {
			    System.out.println("| "+ i.getName() + ": " + (int) i.total() + spacing(i.getName() + "    ", (int) i.total(), spaces) + " x0.5 = " + ((int) (i.total() * 0.5)) + spacing("x0.5 = ", ((int)(i.total() * 0.5)), endSpaces) + "|");
			    score+= ((int) (i.total() * 0.5));
			    continue;
			}
			System.out.println("| "+ i.getName() + ": " + (int) i.total() + spacing(i.getName() + "    ", (int) i.total(), spaces) + " x10 = " + ((int) i.total() * 10) + spacing("x10 = ", ((int) i.total() * 10), endSpaces) + "|");
			score+= ((int) i.total() * 10);
		}
		System.out.println(returnFiller(lineSize+10, false, true) + "|");
        
        if (location.winCheck()) {
            score*= winBonus;
        }
        String filler = returnFiller(lineSize/3, true, true);
		System.out.println(returnFiller(lineSize+10, false, true) + "|");
        System.out.println("|" + filler + "  " + winBonusStr + winBonus + "x" + spacing(filler + winBonusStr + String.valueOf(winBonus) + "x", -4, endSpaces + spaces)+ "|");
		System.out.println("|" + filler + " " + finalScoreStr + (int) score + spacing(filler + finalScoreStr + String.valueOf((int) score), -1, endSpaces + spaces - 1)+ "|");
		System.out.println(returnFiller(lineSize + 10, false, false) + "|");

		System.exit(0);
	}

	// Used for auto indenting too make lists look cleaner
	public String spacing(String name, int num, int spaces) {
		int spaceing = 0;
		if (num!= -1) spaceing = spaces - (name + String.valueOf(num)).length();
		else spaceing = spaces - name.length();
		String returnString = "";
		for (int i = 0; i < spaceing; i++) {
			returnString += " ";
		}
		return returnString;
	}
	public String returnFiller(int amount, boolean noStart, boolean onlySpaces) {
		String returnStr = "";
		if (!noStart) returnStr = "|";
		for (int i = amount; i > 0; i--) {
			if (onlySpaces) returnStr += " ";
			else returnStr += "~";
		}
		return returnStr;

	}
}