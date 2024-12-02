package main.java.com.example;

import main.java.com.example.exceptions.NotEnoughMoneyException;

public class TerminalServer {
    private int balance = 1000; // Начальный баланс

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) throws NotEnoughMoneyException {
        if (balance < amount) {
            throw new NotEnoughMoneyException("Недостаточно средств на счете.");
        }
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }
}