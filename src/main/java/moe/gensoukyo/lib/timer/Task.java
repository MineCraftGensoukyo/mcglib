package moe.gensoukyo.lib.timer;

/**
 * Task impl based on game ticks / manual update.
 * @author ChloePrime
 */
public
class Task extends AbstractTask {
    /**
     * autoUpdate = true
     */
    public Task() {
        super();
    }

    public Task(boolean autoUpdate) {
        super(autoUpdate);
    }

    private long ticks = 0L;

    @Override
    public void update() {
        ++ticks;
        super.update();
    }

    @Override
    protected long getTickCount() {
        return ticks;
    }
}
