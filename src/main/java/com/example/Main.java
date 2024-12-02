package main.java.com.example;

import main.java.com.example.exceptions.AccountIsLockedException;
import main.java.com.example.exceptions.InvalidAmountException;
import main.java.com.example.exceptions.NotEnoughMoneyException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/*Ответы на вопросы из задачи 3
1. Этот код написан верно.
2. Перехватываются все исключения, т.к. являются экземплярами класса Exception или его подклассами.
3.В се ArithmeticException уже будут обработаны в первом блоке catch (Exception e) т.к. являются подклассом Exception.
4. a-1 (Массив A объявлен, но не инициализирован, что приводит к исключению NullPointerException, если попытаться обратиться к его элементу.)
   b-3 (Если JVM не может найти классы, программа не запустится, так как отсутствуют необходимые файлы, такие как rt.jar или другие библиотеки стандартной Java.)
   c-4 (Достижение конца потока не вызывает исключения; метод чтения (read или readLine) просто возвращает -1 или null.)
   d-2 (Если поток закрыт, попытка чтения вызовет исключение IOException, которое является проверяемым.)*/

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean runProgram = true;


        while (runProgram) {

            System.out.println("\nВыберите номер программы: ");
            System.out.println("Введите 1, что бы выбрать программу Terminal.");
            System.out.println("Введите 2, что бы выбрать программу ParserURL.");
            System.out.println("Введите 0, для выхода.");

            String input = scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("Ошибка: введите только одну цифру.");
                continue; // Переход к следующей итерации цикла
            }

            for (char c : input.toCharArray()) {
                if (c == '1') {
                    // Task №1 terminal
                    terminalStart();
                } else if (c == '2') {
                    // Task №2 parse url
                    startParseURL();
                } else if (c == '0') {
                    runProgram = false;
                }
            }
        }

        scanner.close();
    }

    public static void terminalStart() {

        TerminalServer server = new TerminalServer();
        PinValidator pinValidator = new PinValidator();
        TerminalImpl terminal = new TerminalImpl(server, pinValidator);

        boolean running = true;

        System.out.println("Добро пожаловать в терминал!");

        // Авторизация
        while (!terminal.isAuthenticated()) {
            System.out.print("Введите пин-код (по одной цифре): ");
            String input = scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("Ошибка: введите только одну цифру.");
                continue; // Переход к следующей итерации цикла
            }

            for (char c : input.toCharArray()) {
                terminal.enterPin(c);
            }
        }

        // Основной цикл программы
        while (running) {
            System.out.println("\nДоступные команды:");
            System.out.println("1. check - Проверить баланс");
            System.out.println("2. withdraw <сумма> - Снять деньги");
            System.out.println("3. deposit <сумма> - Положить деньги");
            System.out.println("4. exit - Выход");

            System.out.print("Введите команду: ");
            String command = scanner.nextLine();
            String[] parts = command.split(" ");
            String action = parts[0];

            try {
                switch (action) {
                    case "check":
                        terminal.checkBalance();
                        break;
                    case "withdraw":
                        if (parts.length < 2) {
                            System.out.println("Ошибка: укажите сумму.");
                        } else {
                            int amount = Integer.parseInt(parts[1]);
                            terminal.withdrawMoney(amount);
                        }
                        break;
                    case "deposit":
                        if (parts.length < 2) {
                            System.out.println("Ошибка: укажите сумму.");
                        } else {
                            int amount = Integer.parseInt(parts[1]);
                            terminal.depositMoney(amount);
                        }
                        break;
                    case "exit":
                        System.out.println("Выход из системы...");
                        running = false;
                        break;
                    default:
                        System.out.println("Неизвестная команда.");
                }
            } catch (AccountIsLockedException | NotEnoughMoneyException | InvalidAmountException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: сумма должна быть числом.");
            }
        }

        System.out.println("Спасибо за использование терминала!");
    }

    public static void startParseURL() {

        boolean validInput = false;

        while (!validInput) {
            System.out.print("Введите URL ресурса: ");
            String inputUrl = scanner.nextLine();

            try {
                // Попытка прочитать содержимое сайта
                readContent(inputUrl);
                validInput = true; // Завершить цикл при успешном выполнении
            } catch (MalformedURLException e) {
                System.out.println("Ошибка: Неверный формат URL. Попробуйте снова.");
            } catch (Exception e) {
                System.out.println("Ошибка: Не удалось получить доступ к ресурсу. Попробуйте снова.");
            }
        }

    }

    public static void readContent(String urlString) throws Exception {
        // Проверяем формат URL
        URL url = new URL(urlString);

        // Устанавливаем соединение
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Проверяем код ответа
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Сервер вернул код ответа: " + responseCode);
        }

        // Чтение содержимого сайта
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            System.out.println("Содержимое ресурса:");
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            connection.disconnect();
        }
    }
}