package scheduler;

/**
 *
 */
public enum ThreadType {

    KLT("K"),
    ULT("U");

    public final String threadType;

    ThreadType (String threadType){
        this.threadType=threadType;
    }
}
