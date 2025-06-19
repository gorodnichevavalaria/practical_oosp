import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VotingSystem system = new VotingSystem();
        system.loadData();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nДобро пожаловать в систему электронного голосования!");
            System.out.println("1. Вход");
            System.out.println("2. Регистрация");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите логин: ");
                    String username = scanner.nextLine();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();

                    User user = system.authenticate(username, password);
                    if (user != null) {
                        system.showUserMenu(user);
                    } else {
                        System.out.println("Неверный логин или пароль!");
                    }
                    break;
                case 2:
                    system.registerUser();
                    break;
                case 3:
                    system.saveData();
                    System.out.println("До свидания!");
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}