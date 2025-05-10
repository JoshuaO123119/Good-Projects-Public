class InventoryItem {
    private double cost = -1.0;
    private double total = 0;
    private String name;
    // Set default price
    public InventoryItem(double cost, Wagon wagon, String name) {
        this.cost = cost;
        this.name = name;
        wagon.addItem(this);
        
    }
    // Returns cost of item
    public double getCost() {
        return this.cost;
    }
    // Returns total amount
    public double total() {
        return this.total;
    }
    // Add to total by amount
    public void add(double amount) {
        this.total+= amount;
    }
    // Subtract total by amount
    public void remove(double amount) {
        if (amount >= this.total) this.total = 0;
        else this.total -= amount;
    }
    // Returns name of item when printed (for displaying inventory later)
    public String toString() {
        return this.name;
    }
    
    public String getName() {
        return this.name;
    }
}

class Food extends InventoryItem{
    // Default price 0.2 per lb
    public Food(Wagon wagon) {
        super(0.20, wagon, "Food");
    }
}
class Ammo extends InventoryItem {
    // Default price 0.1 per bullet (buy in 20)
    public Ammo(Wagon wagon) {
        super(0.10, wagon, "Ammo");
    }
}

class Wheels extends InventoryItem {
    // Default price $10 each
    public Wheels(Wagon wagon) {
        super(10.0, wagon, "Wagon Wheels");
    }
}

class Axles extends InventoryItem {
    // Default price $10 each
    public Axles(Wagon wagon) {
        super(10.0, wagon, "Wagon Axles");
    }
}

class Tongues extends InventoryItem {
    // Default price $10 each
    public Tongues(Wagon wagon) {
        super(10.0, wagon, "Wagon Tongues");
    }
}

class Oxen extends InventoryItem {
    // Default price $20 per oxen (buy in 2)
    public Oxen(Wagon wagon) {
        super(20.0, wagon, "Oxen");
    }
}

class Clothing extends InventoryItem {
    // Default price $10 per set (for one person each)
    public Clothing(Wagon wagon) {
        super(10.0, wagon, "Clothing");
    }
}