package net.mineground.lobby.Utils;

import org.bukkit.scheduler.BukkitTask;

public class Delay {
    private BukkitTask task;

    private int i;

    private int startX;

    private int startY;

    private int startZ;

    public Delay(BukkitTask task, int startX, int startY, int startZ) {
        this.task = task;
        this.i = 0;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
    }

    public Delay setI(int i) {
        this.i = i;
        return this;
    }

    public BukkitTask getTask() {
        return this.task;
    }

    public int getI() {
        return this.i;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getStartY() {
        return this.startY;
    }

    public int getStartZ() {
        return this.startZ;
    }
}
