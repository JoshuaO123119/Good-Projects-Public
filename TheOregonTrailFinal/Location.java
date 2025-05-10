class Location {
	
	// River points: kansasRiver, bigBlueRiverCrossing, right before oregonCity
	
	// Checkpoint distances
	private final int kansasRiver = 102;
	private final int bigBlueRiverCrossing = kansasRiver + 83;
	private final int fortKearney = bigBlueRiverCrossing + 119;
	private final int chimneyRock = fortKearney + 250;
	private final int fortLaramie = chimneyRock + 86;
	private final int independenceRock = fortLaramie + 190;
	private final int southPass = independenceRock + 102;
	private final int fortBoise  = southPass + 350;
	private final int oregonCity = fortBoise + 525;

	private boolean atDestination = false;
	private int distanceToGo = 0;
	private boolean accessStore = true;
	public boolean needSwim = false;

	// Check if checkpoint crossed (dont repeat later down the road)
	private boolean kansasCheck = true;
	private boolean blueRiverCheck = true;
	private boolean fortKearneyCheck = true;
	private boolean chimneyRockCheck = true;
	private boolean laramieCheck = true;
	private boolean rockCheck = true;
	private boolean southPassCheck = true;
	private boolean boiseCheck = true;
	private boolean cityCheck = true;
	
	public Location() {
	    
	}
	
	// returns distance if passed checkpoint, else returns original distance given
	public int passCheckpointDistance(int distance) {
		if ((distance >= kansasRiver)&& kansasCheck) {
			this.atDestination = true;
			announceArrival();
			kansasCheck = false;
			this.needSwim = true;
			return kansasRiver;
		}
		else if ((distance >= bigBlueRiverCrossing)&& blueRiverCheck) {
			this.atDestination = true;
			announceArrival();
			this.needSwim = true;
			blueRiverCheck = false;
			return bigBlueRiverCrossing;
		} else if ((distance >= fortKearney)&& fortKearneyCheck) {
			this.atDestination = true;
			announceArrival();
			System.out.println("You can now access the store!");
			this.accessStore = true;
			fortKearneyCheck = false;

			return fortKearney;
		} else if ((distance >= chimneyRock)&& chimneyRockCheck) {
			this.atDestination = true;
			announceArrival();
			chimneyRockCheck = false;

			return chimneyRock;
		} else if ((distance >= fortLaramie)&& laramieCheck) {
			this.atDestination = true;
			announceArrival();
			System.out.println("You can now access the store!");
			this.accessStore = true;
			laramieCheck = false;

			return fortLaramie;
		} else if ((distance >= independenceRock)&& rockCheck) {
			this.atDestination = true;
			announceArrival();
			System.out.println("You can now access the store!");
			this.accessStore = true;
			rockCheck = false;

			return independenceRock;
		} else if ((distance >= southPass)&& southPassCheck) {
			this.atDestination = true;
			announceArrival();
			southPassCheck = false;

			return southPass;
		} else if ((distance >= fortBoise)&& boiseCheck) {
			this.atDestination = true;
			announceArrival();
			boiseCheck = false;

			return fortBoise;
		} else if ((distance >= oregonCity)&& cityCheck) {
			this.atDestination = true;
			announceArrival();
			System.out.println("You can now access the store!");
			this.accessStore = true;
			cityCheck = false;

			return oregonCity;
		}
		return distance;
	}
	// If successfully at oregon city return true, else return false
	public boolean winCheck() {
		if (!this.cityCheck) {
			return true;
		}
		return false;
	}
	
	public void displayMap(int distance) {
        int distanceWord = 35;
        String kansasRiverL = "Kansas River: ";
        String blueRiverL = "Big Blue River Crossing: ";
        String kearneyL = "Fort Kearney: ";
        String chimneyRockL = "Chimney Rock: ";
        String laramieL = "Fort Laramie: ";
        String rockL = "Independence Rock: ";
        String passL = "South Pass: ";
        String boiseL = "Fort Boise: ";
        String cityL = "Oregon City: ";
        // Clear console
		System.out.print("\033[H\033[2J");
		System.out.flush();
        System.out.println("Currently : " + distance);
        System.out.println("\n\n" + kansasRiverL  + kansasRiver + " miles" + space(kansasRiverL + String.valueOf(kansasRiver),distanceWord) + "Passed: " + !kansasCheck);
        System.out.println(blueRiverL + bigBlueRiverCrossing + " miles" + space(blueRiverL + String.valueOf(bigBlueRiverCrossing),distanceWord) + "Passed: " + !blueRiverCheck);
        System.out.println(kearneyL + fortKearney + " miles" + space(kearneyL + String.valueOf(fortKearney),distanceWord) + "Passed: " + !fortKearneyCheck);
        System.out.println(chimneyRockL + chimneyRock + " miles" + space(chimneyRockL + String.valueOf(chimneyRock),distanceWord) + "Passed: " + !chimneyRockCheck);
        System.out.println(laramieL + fortLaramie + " miles" + space(laramieL + String.valueOf(fortLaramie),distanceWord) + "Passed: " + !laramieCheck);
        System.out.println(rockL + independenceRock + " miles" + space(rockL + String.valueOf(independenceRock),distanceWord) + "Passed: " + !rockCheck);
        System.out.println(passL + southPass + " miles" + space(passL + String.valueOf(southPass),distanceWord) + "Passed: " + !southPassCheck);
        System.out.println(boiseL + fortBoise + " miles" + space(boiseL + String.valueOf(fortBoise),distanceWord) +"Passed: " + !boiseCheck);
        System.out.println(cityL + oregonCity + " miles" + space(cityL + String.valueOf(oregonCity),distanceWord) + "Passed: " + !cityCheck + "\n\n");
	}
	
	// Add spaces by amount
	public String space(String name, int spaces) {
		int spaceing = spaces - name.length();
		String returnString = "  ";
		for (int i = 0; i < spaceing-3; i++) {
			
			returnString += "-";
		}
		returnString+= "> ";
		return returnString;
	}
	
	public void updateDistanceToGo(int distance) {
	    if (kansasCheck) {
	        this.distanceToGo = kansasRiver - distance;
	    } else if (blueRiverCheck) {
	        this.distanceToGo = bigBlueRiverCrossing - distance;
	    } else if (fortKearneyCheck) {
	        this.distanceToGo = fortKearney - distance;
	    } else if (chimneyRockCheck) {
	        this.distanceToGo = chimneyRock - distance;
	    } else if (laramieCheck) {
	        this.distanceToGo = fortLaramie - distance;
	    } else if (rockCheck) {
	        this.distanceToGo = independenceRock - distance;
	    } else if (southPassCheck) {
	        this.distanceToGo = southPass - distance;
	    } else if (boiseCheck) {
	        this.distanceToGo = fortBoise - distance;
	    } else if (cityCheck) {
	        this.distanceToGo = oregonCity - distance;
	    }
	}
	
	public int getDistanceToGo() {
	    return this.distanceToGo;
	}
	
	// returns the next destination in string
	public String nextDestination() {
		if (kansasCheck) {
			return "Kansas River";
		} else if (blueRiverCheck) {
			return "Big Blue River Crossing";
		} else if (fortKearneyCheck) {
			return "Fort Kearney";
		} else if (chimneyRockCheck) {
			return "Chimney Rock";
		} else if (laramieCheck) {
			return "Fort Laramie";
		} else if (rockCheck) {
			return "Independence Rock";
		} else if (southPassCheck) {
			return "South Pass";
		} else if (boiseCheck) {
			return "Fort Boise";
		} else if (winCheck()) {
		    return "N/A";
		} else {
			return "Oregon City" ;
		}
	}
    
    public void removeStore() {
        this.accessStore = false;
    }
    public boolean canAccessStore() {
        return this.accessStore;
    }
    
	private void announceArrival() {
		if (this.atDestination) {
			System.out.println("Arrived at " + nextDestination() + "!");
			this.atDestination = false;
		}
	}
}