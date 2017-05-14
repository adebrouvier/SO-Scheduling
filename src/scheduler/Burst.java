package scheduler;

/**
 * TODO checkear burst limit (12)
 */
public class Burst {

    private Integer time;
    private String type;

    public Burst(Integer time, String type) {
        this.time = time;
        this.type = type;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return true if burst is finished
     */
    public boolean decreaseTime() {
        if (time > 0) {
            time--;
            return false;
        }
        return true;
    }
}
