package cc.davyy.slime.services.gameplay;

public interface BrandService {

    /**
     * Starts the brand name animation task.
     * <p>
     * Schedules a repeating task that updates the brand name at the interval specified in the configuration.
     */
    void startAnimation();

}