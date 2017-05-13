package scheduler;

/**
 * Created by Tobias on 13/5/2017.
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
}
