import java.util.*;
class Traveler {
	Random generator = new Random();
	Scanner input = new Scanner(System.in);
	String name;
	boolean dead = false;
	int health = 2; // 2 = Good Health, 1 = Poor Health, 0 = Dead;

	// Possible ways to die and get injured
	String[] injuries = {"broke their arm", "has dysentary", "sneezed too hard", "bit their tongue too hard", "broke their leg", "has food poisoning", "has typhoid", "slept too hard"};

    public Traveler(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}
	
	public String displayHealth() {
	    if (getHealth() >= 2) {
	        return "Good";
	    }
	    else if (getHealth() == 1) {
	        return "Poor";
	    } else if (getHealth() <= 0) {
	        return "Dead";
	    }
	    return "N/A";
	}

	// Injure player
	public void injure() {
		this.health -= 1;
		// If now poor health
		if (this.health == 1) {
			String harm = injuries[generator.nextInt(injuries.length)]; // Pick a random injury line from the array of given size
			System.out.println(this.name + " " + harm + "! Now has poor health (rest to heal)");
		}
		// If now dead
		if (this.health == 0) {
			System.out.println(this.name + " has died!"); //
			this.dead = true;
		}
	}
	
	public boolean isAlive() {
	    return !this.dead;
	}

	public void heal() {
		if (health == 1) { // Don't heal if already at max health (2) and don't recover from being dead (0)
			this.health = 2;
			System.out.println(this.name + " healed from their injuries!");
		}
	}

	public int getHealth() {
        return this.health;
	}
}