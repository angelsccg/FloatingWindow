package com.angels.world.mode;

import java.io.Serializable;

public class StepEntity implements Serializable{
    /**time yyyy-MM-dd*/
    private String time;
    /**内容*/
    private long step;

    public StepEntity() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }
}
