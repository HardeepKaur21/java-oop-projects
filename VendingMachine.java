import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

public class VendingMachine {
    public static void main(String args[]) {
        ArrayList<Item> items = new ArrayList<Item>();
        Scanner sc = new Scanner(System.in);

        readFile(items);
        showMenu(items);
        Item selectedItem;

        // Get user inputs
        System.out.println("\nEnter item code: ");
        int index = sc.nextInt();
        selectedItem = items.get(index);

        if (index >= items.size()) {
            System.out.println("Please enter a valid code!");
        }

        System.out.println("\nEnter cash amount: ");
        double cash = sc.nextDouble();

        if (cash < selectedItem.getPrice()) {
            System.out.println("Please enter more cash!");
        }

        // decrease stock Level
        selectedItem.decreaseStockLevel();

        // dealing with the change
        if (cash > selectedItem.getPrice()) {
            System.out.println("\nChange: " + (cash - selectedItem.getPrice()));
        }

        updateFile(items);

        sc.close();
        System.out.println("\n\nThanks for using the Vending Machine. Hope to see you again!");
    }

    static void readFile(ArrayList<Item> items) {
        File file = new File("Inventory.txt");
        Scanner sc;

        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                String item = line.split(",")[0];
                Double price = Double.parseDouble(line.split(",")[1]);
                int stockLevel = Integer.parseInt(line.split(",")[2]);

                items.add(new Item(item, price, stockLevel));
            }

            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!!");
        }
    }

    static void showMenu(ArrayList<Item> items) {
        System.out.println("The menu is as follows: \n");
        System.out.println("Num: Item Name, Price, Stock Level");

        for (int i = 0; i < items.size(); i++) {
            System.out.println(i + ": " + items.get(i));
        }
    }

    static void updateFile(ArrayList<Item> items) {
        try {
            Formatter f = new Formatter("Inventory.txt");

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                f.format("%s,%.2f,%d\n", item.getItemName(), item.getPrice(), item.getStockLevel());
            }
            f.close();

        } catch (Exception e) {
            System.out.println("Error: File not found !!!!!");
        }
    }
}

class Item {
    private String itemName;
    private double price;
    private int stockLevel;

    Item(String itemName, double price, int stockLevel) {
        this.itemName = itemName;
        this.price = price;
        this.stockLevel = stockLevel;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void decreaseStockLevel() {
        stockLevel -= 1;
    }

    public String toString() { // without this the compiler is gonna print the memory address instead of the
                               // thing you are returning
        return String.format("%s, %.2f, %d", itemName, price, stockLevel);
    }
}
