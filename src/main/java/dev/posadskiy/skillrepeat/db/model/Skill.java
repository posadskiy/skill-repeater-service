package dev.posadskiy.skillrepeat.db.model;

import java.util.Date;

public class Skill {
    private String name;
    private Boolean isNeedRepeat;
    private Date lastRepeat;
    private Short level;

    public Skill(String name, Boolean isNeedRepeat, Date lastRepeat, Short level) {
        this.name = name;
        this.isNeedRepeat = isNeedRepeat;
        this.lastRepeat = lastRepeat;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNeedRepeat() {
        return isNeedRepeat;
    }

    public void setNeedRepeat(Boolean needRepeat) {
        isNeedRepeat = needRepeat;
    }

    public Date getLastRepeat() {
        return lastRepeat;
    }

    public void setLastRepeat(Date lastRepeat) {
        this.lastRepeat = lastRepeat;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }
}
