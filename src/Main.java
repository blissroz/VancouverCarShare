import ca.blissroz.carshare.CarShareCost;

public class Main {

    public static void main(String[] args) {
        CarShareCost cost = new CarShareCost(true);
        cost.getCheapestCarShare(600,
                30,
                "Hours",
                2);
        System.out.println("Cheapest option - " + cost.getCheapestCompany() + " : " + cost.getCheapestPrice());
	// write your code here
    }
}
