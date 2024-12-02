package main.java.com.example;

import main.java.com.example.exceptions.AccountIsLockedException;
import main.java.com.example.exceptions.InvalidAmountException;
import main.java.com.example.exceptions.NotEnoughMoneyException;

public class TerminalImpl implements Terminal {
    private final TerminalServer server;
    private final PinValidator pinValidator;

    private StringBuilder enteredPin = new StringBuilder();
    private boolean authenticated = false;

    public TerminalImpl(TerminalServer server, PinValidator pinValidator) {
        this.server = server;
        this.pinValidator = pinValidator;
    }

    @Override
    public void enterPin(char input) {
        if (!Character.isDigit(input)) {
            System.out.println("Введен некорректный символ. Введите только цифры.");
            return;
        }

        enteredPin.append(input);
        if (enteredPin.length() == 4) {
            try {
                authenticated = pinValidator.validatePin(enteredPin.toString());
                if (authenticated) {
                    System.out.println("Пин-код успешно принят!");
                } else {
                    System.out.println("Неправильный пин-код.");
                }
            } catch (AccountIsLockedException e) {
                System.out.println(e.getMessage());
            } finally {
                enteredPin.setLength(0); // Очистка ввода
            }
        }
    }

    @Override
    public void checkBalance() throws AccountIsLockedException {
        if (!authenticated) {
            throw new AccountIsLockedException("Вы не авторизованы. Пожалуйста, введите пин-код.");
        }
        System.out.println("Текущий баланс: " + server.getBalance() + " руб.");
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void withdrawMoney(int amount) throws AccountIsLockedException, NotEnoughMoneyException, InvalidAmountException {
        if (!authenticated) {
            throw new AccountIsLockedException("Вы не авторизованы. Пожалуйста, введите пин-код.");
        }
        if (amount % 100 != 0) {
            throw new InvalidAmountException("Сумма должна быть кратна 100.");
        }
        server.withdraw(amount);
        System.out.println("Снято " + amount + " руб. Текущий баланс: " + server.getBalance() + " руб.");
    }

    @Override
    public void depositMoney(int amount) throws AccountIsLockedException, InvalidAmountException {
        if (!authenticated) {
            throw new AccountIsLockedException("Вы не авторизованы. Пожалуйста, введите пин-код.");
        }
        if (amount % 100 != 0) {
            throw new InvalidAmountException("Сумма должна быть кратна 100.");
        }
        server.deposit(amount);
        System.out.println("Положено " + amount + " руб. Текущий баланс: " + server.getBalance() + " руб.");
    }
}