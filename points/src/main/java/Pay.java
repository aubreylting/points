package main.java;

import java.util.Date;

public class Pay {
    private String payer;
    private long points;
    private Date timeStamp;

    public Pay(String payer, int points, Date timeStamp){
        this.payer = payer;
        this.points = points;
        this.timeStamp = timeStamp;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
