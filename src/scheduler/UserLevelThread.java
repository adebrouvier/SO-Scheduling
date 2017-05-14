package scheduler;

/**
 * Created by Tobias on 13/5/2017.
 */
public class UserLevelThread extends Thread {
    public UserLevelThread(Burst[] processingTime) {
        super(processingTime);
    }
}
