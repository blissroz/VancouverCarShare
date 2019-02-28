import ca.blissroz.carshare.CarShareCost;

public class Main {

    public static void main(String[] args) {
        CarShareCost cost = new CarShareCost(true);
        cost.getCheapestCarShare(5,
                2,
                "Hours",
                2);
        System.out.println(cost.getCheapestCompany() + " : " + cost.getCheapestPrice());
	// write your code here
    }
}
