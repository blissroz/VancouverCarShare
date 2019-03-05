import ca.blissroz.carshare.services.CarShareCost;

public class Main {
    public static void main(String[] args) {
        CarShareCost cost = new CarShareCost(true);
        cost.getCheapestCarShare(2500,
                4,
                "Days",
                2);
        System.out.println("Cheapest option - " + cost.getCheapestCompany() + " : " + cost.getCheapestPrice());
	// write your code here
    }
}
