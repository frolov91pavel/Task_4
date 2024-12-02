package main.java.com.example;

import main.java.com.example.exceptions.AccountIsLockedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PinValidator {
    private static final String CORRECT_PIN = "1234";
    private int failedAttempts = 0;
    private LocalDateTime lockTime;

    public boolean validatePin(String enteredPin) throws AccountIsLockedException {
        if (isLocked()) {
            long secondsLeft = lockTime.plusSeconds(10).toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            throw new AccountIsLockedException("Аккаунт заблокирован. Осталось " + secondsLeft + " секунд.");
        }

        if (CORRECT_PIN.equals(enteredPin)) {
            failedAttempts = 0;
            return true;
        } else {
            failedAttempts++;
            if (failedAttempts >= 3) {
                lockTime = LocalDateTime.now();
            }
            return false;
        }
    }

    private boolean isLocked() {
        if (lockTime == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(lockTime.plusSeconds(10));
    }
}