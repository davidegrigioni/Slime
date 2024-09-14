package cc.davyy.slime.interfaces;

public interface IBrand {

    /**
     * Starts the brand name animation task.
     * <p>
     * Schedules a repeating task that updates the brand name at the interval specified in the configuration.
     */
    void startAnimation();

}