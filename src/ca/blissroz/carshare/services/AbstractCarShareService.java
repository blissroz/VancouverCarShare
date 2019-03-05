package ca.blissroz.carshare.services;

import lombok.AllArgsConstructor;
import lombok.Data;

public abstract class AbstractCarShareService {
    public static final String HOURS_TIME = "Hours";
    public static final String MINUTES_TIME = "Minutes";
    public static final String DAYS_TIME = "Days";
    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;

    /**
     * Determines the cost of the car rental based on given parameters
     *
     * @param distance  how far the trip will be, in km
     * @param time      how long the trip will take, in minutes, hours, or days
     * @param timeUnit  which unit the previous parameter is (mins, hrs, days)
     * @param numPeople how many people will go on the trip, between 1 and 5, inclusive
     * @return the amount it'll cost
     */
    public abstract Result getCost(final double distance,
                                   final double time,
                                   final String timeUnit,
                                   final int numPeople);

    /**
     * @return the name of the carShareCompany
     */
    public abstract String getName();

    double getTimeInHours(final double time, final String timeUnit) {
        switch (timeUnit) {
            case MINUTES_TIME:
                return time * MINUTES_PER_HOUR;
            case HOURS_TIME:
                return time;
            case DAYS_TIME:
                return time * HOURS_PER_DAY;
            default:
                return 0; //throw new Exception("Unexpected time unit");
        }
    }

    public class Result {
        String description;
        Double cost;

        public Result(String description, Double cost) {
            this.description = description;
            this.cost = cost;
        }

        public Double getCost() {
            return cost;
        }

        public String getDescription() {
            return description;
        }
    }
}
