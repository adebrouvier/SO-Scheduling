package main.model;

/**
 * TODO checkear burst limit (12)
 */
public class Burst {

    private Integer remainingTime;
    private final String type;

    public Burst(Integer remainingTime, String type) {
        this.remainingTime = remainingTime;
        this.type = type;
    }

    /**
     * Executes a unit of time
     * @return true if burst is finished
     */
    public boolean execute() {
        if (remainingTime > 0) {
            remainingTime--;
        }

        return remainingTime == 0;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public String getType() {
        return type;
    }
}
