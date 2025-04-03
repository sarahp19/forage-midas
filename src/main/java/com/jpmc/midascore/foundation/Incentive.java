package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incentive {
//    private long senderId;
//    private long recipientId;
    private float amount;

    public Incentive() {
    }

    public Incentive(float amount) {
//        this.senderId = senderId;
//        this.recipientId = recipientId;
        this.amount = amount;
    }


    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Incentive {amount=" + amount + "}";
    }
}
