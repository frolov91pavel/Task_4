package main.java.com.example;

import main.java.com.example.exceptions.AccountIsLockedException;
import main.java.com.example.exceptions.InvalidAmountException;
import main.java.com.example.exceptions.NotEnoughMoneyException;

public interface Terminal {
    void enterPin(char input);
    void checkBalance() throws AccountIsLockedException;
    void withdrawMoney(int amount) throws AccountIsLockedException, NotEnoughMoneyException, InvalidAmountException;
    void depositMoney(int amount) throws AccountIsLockedException, InvalidAmountException;
}
