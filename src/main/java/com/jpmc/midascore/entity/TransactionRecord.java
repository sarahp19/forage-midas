package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private long id;

    @ManyToOne
    @JoinColumn(name = "senderid", nullable = false)
    private UserRecord nameSend;

    @ManyToOne
    @JoinColumn(name = "receiverid", nullable = false)
    private UserRecord nameRec;

    @Column(nullable = false)
    private float amount;

    protected TransactionRecord() {
    }

    public TransactionRecord(UserRecord nameSend, UserRecord nameRec, float amount) {
        this.nameSend = nameSend;
        this.nameRec = nameRec;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("Transaction[id=%d, nameSend=%s, nameRec=%s, amount=%.2f]", id, nameSend.getName(), nameRec.getName(), amount);
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSendName() {
        return nameSend;
    }
    public UserRecord getRecName() {
        return nameRec;
    }

    public float getAmt() {
        return amount;
    }

}
