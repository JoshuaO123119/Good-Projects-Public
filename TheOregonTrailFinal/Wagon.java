import java.util.*;

class Wagon {
	// Return inventory
	// Get each item from wagon
	// Allow player to move with wagon

	ArrayList<InventoryItem> items = new ArrayList<>();
	Scanner input = new Scanner(System.in);
	Random generator = new Random();
	// 3 = Grueling, 2 = Strenuous, 1 = Steady
	private int pace = 1;
	// The more grueling the higher of a bad event happening
	private int severityPace = 0;
	public boolean hasSwam = false;
	int distance = 0;

	private double oxenCost;
	private double foodCost;
	private double clothingCost;
	private double ammoCost;
	private double wheelsCost;
	private double axlesCost;
	private double tonguesCost;

    public Wagon() {
        
    }

	public void move(Game game, TrailEvent event, PlayerParty party, Location location, Food food) {
		if (getOxen().total() != 0) {
			location.removeStore();
			if (pace == 3) { // Grueling
				int moveDistance = generator.nextInt(15) + 30;
				this.distance += moveDistance;
				System.out.println("You moved " + moveDistance + " miles!");
				severityPace = pace;
			} else if (pace == 2) { // Strenuous
				int moveDistance = generator.nextInt(15) + 20;
				this.distance += moveDistance;
				System.out.println("You moved " + moveDistance + " miles!");
				severityPace = pace;
			} else { // Steady
				int moveDistance = generator.nextInt(15) + 10;
				this.distance += moveDistance;
				System.out.println("You moved " + moveDistance + " miles!");
				severityPace = pace;
			}

			party.eat(game, food, false, this, location);

			// Check if player moved past checkpoint
			this.distance = location.passCheckpointDistance(this.distance);

			// Chance an event based on severity from both pace and rations
			event.chanceEvent(game, this, party, location, food, party.severityRation, this.severityPace);
		} else {
			System.out.println("You don't have any oxen to move!");
			return;
		}
	}
	// When player comes against a river
	public void swimAction(Game game, PlayerParty party, Location location, int farWater, int deepWater) {
		while (!this.hasSwam) {
			int casualtyLikely = 10; // x / 10 someone would die and lose item
			if (farWater == -1) farWater = generator.nextInt(3); // 0 = no extra danger, 1 = small danger, 2 = large danger
			if (deepWater == -1) deepWater = generator.nextInt(3); // 0 = no extra danger, 1 = small danger, 2 = large danger
			boolean causeCasualty = false;
			boolean increaseSevere = false;

			// Clear console
			System.out.print("\033[H\033[2J");
			System.out.flush();
			// Display options

			String[] farWaterInfo = {"60 feet", "100 feet", "300 feet"};
			String[] deepWaterInfo = {"2.4 feet", "4.8 feet", "7.2 feet"};
			System.out.println("River width: " + farWaterInfo[farWater] + "\nRiver Depth: " + deepWaterInfo[deepWater]);

			System.out.println("You may:\n\t1. Attempt to ford the river\n\t2. Caulk the wagon and float it across\n\t3. Take a ferry across ($25)\n\t4. Wait to see if conditions improve\n\t5. Get more info\n");
			// Ask user for options
			System.out.print("What is your choice (1-5): ");
			String choice = input.nextLine().trim();
			if (choice.equals("1")) { // Attempt to ford the river
				// Increase risk for walking through
				increaseSevere = true;
			} else if (choice.equals("2")) { // Caulk wagon and float
				// Increase risk for floating through
				increaseSevere = true;
			} else if (choice.equals("3")) { // Take a ferry across
				if (party.money >= 25) { // If player can pay for ferry
					System.out.println("Spent $25 to use ferry!");
					party.money -= 25;
					location.needSwim = false;
					this.hasSwam = true;
					this.distance+=1;
					return;
				} else { // Player couldn't pay for ferry
					System.out.println("You don't have enough money!");
					continue;
				}
			} else if (choice.equals("4")) { // Wait to see if conditions improve
				farWater = generator.nextInt(3);
				deepWater = generator.nextInt(3);
				party.eat(game, (Food) getFood(), false, this, location);
				System.out.println("Press enter to continue: ");
				input.nextLine();
				continue;
			} else if (choice.equals("5")) { // Get more info
				System.out.println("Under construction!");
				continue;
			} else {
				System.out.println("That isn't a valid option!");
				System.out.print("Press enter to continue: ");
				input.nextLine();
				continue;
			}
			// Add severity of both how deep and far, return true if casualty should happen
			for (int i = farWater + deepWater; i > 0; i--) {
				if (increaseSevere) { // Allow one more loop (incrased risk of casualty)
					i+=20;
					increaseSevere = false;
				}
				if (generator.nextInt(casualtyLikely) == 0) {
					causeCasualty = true;
					break;
				}
			}

			if (causeCasualty) {
				// Kill 0-1 people
				int killAmount = generator.nextInt(2);
				for (int i = killAmount; i > 0; i--) {
					party.drownRandomTraveler();
				}
				InventoryItem food = getFood();
				int foodLost = (int) ((double) food.total() * .85); // Lose 85% of food
				food.remove(foodLost);
				System.out.print("Failed to safely cross river. ");
				System.out.println("You lost " + foodLost + " lbs of food!");
				causeCasualty = false;
			}
			location.needSwim = false;
			this.hasSwam = true;
			this.distance+=1;
			return;
		}
	}

	public int getDistance() {
		return this.distance;
	}

	// Get a list of all items
	public void addItem(InventoryItem i) {
		if (i instanceof Oxen) oxenCost = i.getCost();
		if (i instanceof Food) foodCost = i.getCost();
		if (i instanceof Clothing) clothingCost = i.getCost();
		if (i instanceof Ammo) ammoCost = (i.getCost() * 20);
		if (i instanceof Wheels) wheelsCost = i.getCost();
		if (i instanceof Axles) axlesCost = i.getCost();
		if (i instanceof Tongues) tonguesCost = i.getCost();

		items.add(i);
	}
	// Returns oxen item class (used for checking if player can still move)
	public InventoryItem getOxen() {
		for (InventoryItem i : items) {
			if (i instanceof Oxen) {
				return i;
			}
		}
		return null;
	}

	public InventoryItem getClothing() {
		for (InventoryItem i : items) {
			if (i instanceof Clothing) {
				return i;
			}
		}
		return null;
	}

	public InventoryItem getRandomItem() {
		return items.get(generator.nextInt(items.size()));
	}

	// Return items list
	public ArrayList<InventoryItem> getItems() {
		return items;
	}

	public void showInventory() {
		System.out.println("\nInventory:");
		for (InventoryItem i : items) {
			System.out.println("\t" + i + ": " + (int) i.total());
		}
		System.out.println();
	}



	// display store and buy
	public void buyItems(PlayerParty party, Location location) {
		if (location.canAccessStore()) {
			int oxenAmount = 0;
			int foodAmount = 0;
			int clothingAmount = 0;
			int ammoAmount = 0;
			int wheelsAmount = 0;
			int axlesAmount = 0;
			int tonguesAmount = 0;
			while (true) {
				displayShopItems(party.money);
				System.out.print("Which item do you want to buy? (1-8): ");
				String choice = input.nextLine().toLowerCase().trim();
				// Oxen
				if (choice.equals("1")) {
					int a = amountBuy(party, oxenCost, false);
					if (a != -1) {
						oxenAmount = a;
					}
				}
				// Food
				else if (choice.equals("2")) {
					int a = amountBuy(party, foodCost, false);
					if (a != -1) {
						foodAmount = a;
					}
				}
				// Clothing
				else if (choice.equals("3")) {
					int a = amountBuy(party, clothingCost, false);
					if (a != -1) {
						clothingAmount = a;
					}
				}
				// Ammo
				else if (choice.equals("4")) {
					int a = amountBuy(party, ammoCost, true);
					if (a != -1) {
						ammoAmount = a;
					}
				}
				// Wheels
				else if (choice.equals("5")) {
					int a = amountBuy(party, wheelsCost, false);
					if (a != -1) {
						wheelsAmount = a;
					}
				// Axles
				} else if (choice.equals("6")) {
				    int a = amountBuy(party, axlesCost, false);
				    if (a != -1) {
				        axlesAmount = a;
				    }
				// Tongues
				} else if (choice.equals("7")) {
				    int a = amountBuy(party, tonguesCost, false);
				    if (a != -1) {
				        tonguesAmount = a;
				    }
				}
				
				// Exit
				else if (choice.equals("8")) {
					return;
				} else {
					System.out.println("That isn't a valid option!");
					System.out.println("Press enter to continue: ");
					input.nextLine();
					continue;
				}

				for (InventoryItem i : items) {
					if (i instanceof Oxen) i.add(oxenAmount);
					if (i instanceof Food) i.add(foodAmount);
					if (i instanceof Clothing) i.add(clothingAmount);
					if (i instanceof Ammo) i.add(ammoAmount*20) ;
					if (i instanceof Wheels) i.add(wheelsAmount);
					if (i instanceof Axles) i.add(axlesAmount);
					if (i instanceof Tongues) i.add(tonguesAmount);
				}
				// Reset counts
				oxenAmount = 0;
				foodAmount = 0;
				clothingAmount = 0;
				ammoAmount = 0;
				wheelsAmount = 0;
				axlesAmount = 0;
				tonguesAmount = 0;
				input.nextLine();

			}
		} else {
			System.out.println("You can't access the store right now!");
			System.out.println("Press enter to continue: ");
			input.nextLine();
		}
	}
	// Returns amount that is bought, if failed returns -1
	private int amountBuy(PlayerParty party, double cost, boolean bulletBuy) {
		InventoryItem food = getFood();
		InventoryItem ammo = getAmmo();
		double allowedBuy = Double.valueOf((int) (party.money / cost));
		// Can only have 2000 max food (if buying food)
		if (cost == food.getCost()) {
			if (food.total() + allowedBuy > 2000) {
				allowedBuy = 2000 - food.total();
			}
		}
		// Can only have 500 max bullets (20 each)  (if buy ammo)
		if (bulletBuy) {
		    if (ammo.total() + (allowedBuy * 20) > 500) {
		        allowedBuy = (500 - ammo.total()) / 20;
		    }
		}

		System.out.print("How many do you want to buy? (0-" + (int) allowedBuy + "): ");

		int amount = input.nextInt();
		if (amount > allowedBuy || amount < 0) {
			System.out.println("You can't buy that much!");
			return -1;
		} else {
			party.money -= (amount * cost);
			return amount;
		}
	}

	public InventoryItem getFood() {
		for (InventoryItem i : items) {
			if (i instanceof Food) {
				return i;
			}
		}
		return null;
	}

	public InventoryItem getWheels() {
	    for (InventoryItem i : items) {
	        if (i instanceof Wheels) {
	            return i;
	        }
	    }
	    return null;
	}
	
	public InventoryItem getAxles() {
	    for (InventoryItem i : items) {
	        if (i instanceof Axles) {
	            return i;
	        }
	    }
	    return null;
	}
	
	public InventoryItem getTongues() {
	    for (InventoryItem i : items) {
	        if (i instanceof Tongues) {
	            return i;
	        }
	    }
	    return null;
	}
	
	public InventoryItem getAmmo() {
	    for (InventoryItem i : items) {
	        if (i instanceof Ammo) {
	            return i;
	        }
	    }
	    return null;
	}

	private void displayShopItems(double money) {
		// Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
		// Display store
		System.out.println("Money: " + money);
		System.out.println("Store:");
		for (int i = 0; i < items.size(); i++) {
			InventoryItem item = items.get(i);
			String itemName = item.getName();
			double itemCost = item.getCost();

			if (item instanceof Ammo) {
				itemName = "20x Ammo";
				itemCost*= 20;
			}

			int spacing = 15;
			spacing -= itemName.length();
			System.out.print("\t" + (i+1) + ". " + itemName);
			space(spacing);
			System.out.print("Cost: " + String.format("%.2f", itemCost));
			spacing = 10;
			spacing -= String.format("%.2f", itemCost).length();
			space(spacing);
			System.out.println("You have: " + (int) item.total());
		}
		System.out.println("\t" + (items.size() + 1) + ". Exit\n");
	}
	// Add spaces by amount
	private void space(int count) {
		for (int i = 0; i < count; i++) {
			System.out.print(" ");
		}
	}

	// Return pace
	private String displayPace() {
		if (this.pace == 3) {
			return "Grueling";
		} else if (this.pace == 2) {
			return "Strenuous";
		} else if (this.pace == 1) {
			return "Steady";
		}
		return "N/A";
	}
	// 3 = Grueling, 2 = Strenuous, 1 = Steady (pace at which you can travel)
	public void changePace() {
		// Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
		// Display options
		System.out.println("Current Pace: " + displayPace());
		System.out.println("Options: \n\t1. Steady\n\t2. Strenuous\n\t3. Grueling\n\t4. Find out what this means\n");
		System.out.print("New Pace (1-4): ");
		String choice = input.nextLine().trim();
		if (choice.equals("1")) {
			this.pace = 1;
			return;
		} else if (choice.equals("2")) {
			this.pace = 2;
			return;
		} else if (choice.equals("3")) {
			this.pace = 3;
			return;
		} else if (choice.equals("4")) {
			// Clear console
			System.out.print("\033[H\033[2J");
			System.out.flush();
			System.out.println("Steady - You travel about 8 hours a day, taking frequent rests. You take care not to get too tired.");
			System.out.println("\nStrenuous - You travel about 12 hours a day, starting just after sunrise and stopping shortly before\n sunset. You stop to rest only when necessary. You finish each day feeling very tired.");
			System.out.println("\nGrueling - You travel about 16 hours a day, starting before sunrise and continuing until dark. You\n almost never stop to rest. You do not get enough sleep at night. You finish each day feeling\n absolutely exhausted, and your health suffers.");
			System.out.print("\n\nPress enter to continue: ");
			input.nextLine();
			changePace();
		} else if (choice.equals("")) { // Allow exit without changing anything
			return;
		}
		else { // invalid option
			changePace();
		}
		return;
	}
}