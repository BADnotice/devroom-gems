package io.github.badnotice.devroomgems.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {

    private final UUID uniqueId;
    private final String name;

    private double balance;

    @Setter
    private boolean dirty;

    public User(UUID uniqueId, String name, double balance) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = balance;
    }

    public void withdraw(double amount) {
        balance -= amount;
        if (balance < 0) balance = 0;

        dirty = true;
    }

    public void deposit(double amount) {
        balance += amount;
        dirty = true;
    }

    public void set(double amount) {
        balance = amount;
        dirty = true;
    }

}
