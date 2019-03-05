package ca.blissroz.carshare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarShareCost {
    private final List<AbstractCarShareService> carShareServices;
    private double cheapestPrice;
    private String cheapestCompany;

    public CarShareCost(final boolean hasModoPlus) {
        Car2GoCarShare car2GoCarShare = new Car2GoCarShare();
        EvoCarShare evoCarShare = new EvoCarShare();
        ModoCarShare modoCarShare = new ModoCarShare(hasModoPlus);
        carShareServices = new ArrayList<>(Arrays.asList(car2GoCarShare, evoCarShare, modoCarShare));
    }

    public void getCheapestCarShare(final double distance,
                                    final double time,
                                    final String timeUnit,
                                    final int numPeople) {
        this.cheapestPrice = Integer.MAX_VALUE;
        for (AbstractCarShareService service : carShareServices) {
            AbstractCarShareService.Result newCost = service.getCost(distance, time, timeUnit, numPeople);
            System.out.println(newCost.description + " : " + newCost.cost);
            if (newCost.cost < this.cheapestPrice) {
                this.cheapestPrice = newCost.cost;
                this.cheapestCompany = service.getName();
            }
        }
    }

    public double getCheapestPrice() {
        return this.cheapestPrice;
    }

    public String getCheapestCompany() {
        return this.cheapestCompany;
    }
}
