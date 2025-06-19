import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VotingSystem {
    private List<User> users;
    private List<ElectionCommission> commissions;
    private List<Candidate> candidates;
    private List<Voting> votings;

    public VotingSystem() {
        users = new ArrayList<>();
        commissions = new ArrayList<>();
        candidates = new ArrayList<>();
        votings = new ArrayList<>();
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nРегистрация нового пользователя");
        System.out.print("Введите ФИО: ");
        String fullName = scanner.nextLine();

        System.out.print("Введите дату рождения (дд.мм.гггг): ");
        String birthDate = scanner.nextLine();

        System.out.print("Введите СНИЛС (если есть): ");
        String snils = scanner.nextLine();

        System.out.print("Придумайте логин: ");
        String username = scanner.nextLine();

        System.out.print("Придумайте пароль: ");
        String password = scanner.nextLine();

        if (getUserByUsername(username) != null) {
            System.out.println("Пользователь с таким логином уже существует!");
            return;
        }

        if (!snils.isEmpty() && getUserBySnils(snils) != null) {
            System.out.println("Пользователь с таким СНИЛС уже зарегистрирован!");
            return;
        }

        User newUser = new User(fullName, birthDate, snils, username, password, "user");
        users.add(newUser);
        System.out.println("Регистрация прошла успешно!");
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private User getUserBySnils(String snils) {
        for (User user : users) {
            if (user.getSnils() != null && user.getSnils().equals(snils)) {
                return user;
            }
        }
        return null;
    }

    public void showUserMenu(User user) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nГлавное меню (" + user.getRole() + ")");

            switch (user.getRole().toLowerCase()) {
                case "admin":
                    showAdminMenu(scanner);
                    break;
                case "commission":
                    showCommissionMenu(scanner, user);
                    break;
                case "candidate":
                    showCandidateMenu(scanner, user);
                    break;
                case "user":
                    showRegularUserMenu(scanner, user);
                    break;
                default:
                    System.out.println("Неизвестная роль пользователя!");
                    return;
            }

            System.out.print("Хотите выйти из системы? (да/нет): ");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("да")) {
                return;
            }
        }
    }

    private void showAdminMenu(Scanner scanner) {
        System.out.println("1. Просмотр списка пользователей");
        System.out.println("2. Удаление пользователя");
        System.out.println("3. Просмотр списка ЦИК");
        System.out.println("4. Удаление ЦИК");
        System.out.println("5. Создание ЦИК");
        System.out.println("6. Просмотр списка кандидатов");
        System.out.println("7. Удаление кандидата");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewUsers();
                break;
            case 2:
                deleteUser(scanner);
                break;
            case 3:
                viewCommissions();
                break;
            case 4:
                deleteCommission(scanner);
                break;
            case 5:
                createCommission(scanner);
                break;
            case 6:
                viewCandidates();
                break;
            case 7:
                deleteCandidate(scanner);
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void showCommissionMenu(Scanner scanner, User user) {
        System.out.println("1. Создать голосование");
        System.out.println("2. Добавить кандидата");
        System.out.println("3. Печать результатов (PDF)");
        System.out.println("4. Выбор группировки результатов");
        System.out.println("5. Сортировка результатов");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                createVoting(scanner);
                break;
            case 2:
                addCandidate(scanner);
                break;
            case 3:
                printResultsToPdf(scanner);
                break;
            case 4:
                groupResults(scanner);
                break;
            case 5:
                sortResults(scanner);
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void showCandidateMenu(Scanner scanner, User user) {
        System.out.println("1. Заполнить данные о себе");
        System.out.println("2. Просмотреть результаты предыдущего голосования");
        System.out.println("3. Просмотреть все голосования, в которых участвовал");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                fillCandidateData(scanner, user);
                break;
            case 2:
                viewPreviousResults(user);
                break;
            case 3:
                viewAllParticipatedVotings(user);
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void showRegularUserMenu(Scanner scanner, User user) {
        System.out.println("1. Проголосовать");
        System.out.println("2. Просмотреть список кандидатов");
        System.out.println("3. Просмотреть все голосования");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                vote(scanner, user);
                break;
            case 2:
                viewCandidatesForUser();
                break;
            case 3:
                viewAllVotings(user);
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void viewUsers() {
        System.out.println("\nСписок пользователей:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void deleteUser(Scanner scanner) {
        viewUsers();
        System.out.print("Введите логин пользователя для удаления: ");
        String username = scanner.nextLine();

        User userToDelete = getUserByUsername(username);
        if (userToDelete != null) {
            users.remove(userToDelete);
            System.out.println("Пользователь удален успешно!");
        } else {
            System.out.println("Пользователь с таким логином не найден!");
        }
    }

    private void viewCommissions() {
        System.out.println("\nСписок ЦИК:");
        for (ElectionCommission commission : commissions) {
            System.out.println(commission);
        }
    }

    private void deleteCommission(Scanner scanner) {
        viewCommissions();
        System.out.print("Введите логин ЦИК для удаления: ");
        String username = scanner.nextLine();

        ElectionCommission commissionToDelete = getCommissionByUsername(username);
        if (commissionToDelete != null) {
            commissions.remove(commissionToDelete);
            System.out.println("ЦИК удален успешно!");
        } else {
            System.out.println("ЦИК с таким логином не найден!");
        }
    }

    private void createCommission(Scanner scanner) {
        System.out.println("\nСоздание новой ЦИК");
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (getUserByUsername(username) != null) {
            System.out.println("Пользователь с таким логином уже существует!");
            return;
        }

        ElectionCommission newCommission = new ElectionCommission(username, password);
        commissions.add(newCommission);

        User newUser = new User("ЦИК " + username, "", "", username, password, "commission");
        users.add(newUser);

        System.out.println("ЦИК создан успешно!");
    }

    private ElectionCommission getCommissionByUsername(String username) {
        for (ElectionCommission commission : commissions) {
            if (commission.getUsername().equals(username)) {
                return commission;
            }
        }
        return null;
    }

    private void viewCandidates() {
        System.out.println("\nСписок кандидатов:");
        for (Candidate candidate : candidates) {
            System.out.println(candidate);
        }
    }

    private void deleteCandidate(Scanner scanner) {
        viewCandidates();
        System.out.print("Введите ID кандидата для удаления: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Candidate candidateToDelete = getCandidateById(id);
        if (candidateToDelete != null) {
            candidates.remove(candidateToDelete);
            System.out.println("Кандидат удален успешно!");
        } else {
            System.out.println("Кандидат с таким ID не найден!");
        }
    }

    private void createVoting(Scanner scanner) {
        System.out.println("\nСоздание нового голосования");
        System.out.print("Введите название голосования: ");
        String title = scanner.nextLine();

        System.out.print("Введите описание: ");
        String description = scanner.nextLine();

        System.out.print("Введите дату окончания (дд.мм.гггг): ");
        String endDateStr = scanner.nextLine();

        try {
            LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            Voting newVoting = new Voting(title, description, LocalDate.now(), endDate);
            votings.add(newVoting);
            System.out.println("Голосование создано успешно! ID: " + newVoting.getId());
        } catch (Exception e) {
            System.out.println("Неверный формат даты! Используйте дд.мм.гггг");
        }
    }

    private void addCandidate(Scanner scanner) {
        System.out.println("\nДобавление кандидата");
        System.out.print("Введите ID голосования: ");
        int votingId = scanner.nextInt();
        scanner.nextLine();

        Voting voting = getVotingById(votingId);
        if (voting == null) {
            System.out.println("Голосование с таким ID не найдено!");
            return;
        }

        System.out.print("Введите логин пользователя: ");
        String username = scanner.nextLine();

        User user = getUserByUsername(username);
        if (user == null) {
            System.out.println("Пользователь с таким логином не найден!");
            return;
        }

        System.out.print("Введите краткую информацию о кандидате: ");
        String info = scanner.nextLine();

        Candidate newCandidate = new Candidate(user, info);
        candidates.add(newCandidate);
        voting.addCandidate(newCandidate);

        user.setRole("candidate");

        System.out.println("Кандидат добавлен успешно! ID: " + newCandidate.getId());
    }

    private void printResultsToPdf(Scanner scanner) {
        System.out.println("\nПечать результатов в PDF");
        System.out.print("Введите ID голосования (или 0 для всех): ");
        int votingId = scanner.nextInt();
        scanner.nextLine();

        if (votingId == 0) {
            System.out.print("Выгрузить все в один файл? (да/нет): ");
            String answer = scanner.nextLine();

            boolean singleFile = answer.equalsIgnoreCase("да");

            System.out.print("Введите путь для сохранения (оставьте пустым для папки программы): ");
            String path = scanner.nextLine();

            if (path.isEmpty()) {
                path = System.getProperty("user.dir");
            }

            if (singleFile) {
                String filename = path + "/results_all_" + System.currentTimeMillis() + ".pdf";
                System.out.println("Результаты сохранены в файл: " + filename);
            } else {
                for (Voting voting : votings) {
                    String filename = path + "/results_" + voting.getId() + "_" + System.currentTimeMillis() + ".pdf";
                    System.out.println("Результаты голосования " + voting.getId() + " сохранены в файл: " + filename);
                }
            }
        } else {
            Voting voting = getVotingById(votingId);
            if (voting == null) {
                System.out.println("Голосование с таким ID не найдено!");
                return;
            }

            System.out.print("Введите путь для сохранения (оставьте пустым для папки программы): ");
            String path = scanner.nextLine();

            if (path.isEmpty()) {
                path = System.getProperty("user.dir");
            }

            String filename = path + "/results_" + voting.getId() + "_" + System.currentTimeMillis() + ".pdf";
            System.out.println("Результаты сохранены в файл: " + filename);
        }
    }

    private void groupResults(Scanner scanner) {
        System.out.println("\nГруппировка результатов");
        System.out.print("Введите ID голосования: ");
        int votingId = scanner.nextInt();
        scanner.nextLine();

        Voting voting = getVotingById(votingId);
        if (voting == null) {
            System.out.println("Голосование с таким ID не найдено!");
            return;
        }

        System.out.println("1. По кандидатам");
        System.out.println("2. По регионам");
        System.out.print("Выберите тип группировки: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Результаты по кандидатам:");
                break;
            case 2:
                System.out.println("Результаты по регионам:");
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void sortResults(Scanner scanner) {
        System.out.println("\nСортировка результатов");
        System.out.print("Введите ID голосования: ");
        int votingId = scanner.nextInt();
        scanner.nextLine();

        Voting voting = getVotingById(votingId);
        if (voting == null) {
            System.out.println("Голосование с таким ID не найдено!");
            return;
        }

        System.out.println("1. По количеству голосов (по убыванию)");
        System.out.println("2. По количеству голосов (по возрастанию)");
        System.out.println("3. По имени кандидата");
        System.out.print("Выберите тип сортировки: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Результаты отсортированы по убыванию голосов:");
                break;
            case 2:
                System.out.println("Результаты отсортированы по возрастанию голосов:");
                break;
            case 3:
                System.out.println("Результаты отсортированы по имени кандидата:");
                break;
            default:
                System.out.println("Неверный выбор!");
        }
    }

    private void fillCandidateData(Scanner scanner, User user) {
        Candidate candidate = getCandidateByUser(user);
        if (candidate == null) {
            System.out.println("Вы не являетесь кандидатом!");
            return;
        }

        System.out.println("\nЗаполнение данных о кандидате");
        System.out.print("Введите краткую биографию: ");
        String bio = scanner.nextLine();

        System.out.print("Введите предвыборную программу: ");
        String program = scanner.nextLine();

        System.out.print("Введите контактную информацию: ");
        String contacts = scanner.nextLine();

        candidate.setBio(bio);
        candidate.setProgram(program);
        candidate.setContacts(contacts);

        System.out.println("Данные успешно сохранены!");
    }

    private void viewPreviousResults(User user) {
        Candidate candidate = getCandidateByUser(user);
        if (candidate == null) {
            System.out.println("Вы не являетесь кандидатом!");
            return;
        }

        System.out.println("\nРезультаты предыдущего голосования:");
    }

    private void viewAllParticipatedVotings(User user) {
        Candidate candidate = getCandidateByUser(user);
        if (candidate == null) {
            System.out.println("Вы не являетесь кандидатом!");
            return;
        }

        System.out.println("\nВсе голосования, в которых вы участвовали:");
        for (Voting voting : votings) {
            if (voting.hasCandidate(candidate)) {
                System.out.println(voting);
            }
        }
    }

    private Candidate getCandidateByUser(User user) {
        for (Candidate candidate : candidates) {
            if (candidate.getUser().equals(user)) {
                return candidate;
            }
        }
        return null;
    }

    private void vote(Scanner scanner, User user) {
        System.out.println("\nГолосование");
        System.out.println("Активные голосования:");

        List<Voting> activeVotings = new ArrayList<>();
        for (Voting voting : votings) {
            if (voting.isActive() && !voting.hasUserVoted(user)) {
                activeVotings.add(voting);
                System.out.println(voting.getId() + ". " + voting.getTitle() + " (до " +
                        voting.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")");
            }
        }

        if (activeVotings.isEmpty()) {
            System.out.println("Нет активных голосований или вы уже проголосовали во всех!");
            return;
        }

        System.out.print("Выберите голосование (ID): ");
        int votingId = scanner.nextInt();
        scanner.nextLine();

        Voting voting = getVotingById(votingId);
        if (voting == null || !voting.isActive() || voting.hasUserVoted(user)) {
            System.out.println("Неверный выбор голосования!");
            return;
        }

        System.out.println("Кандидаты:");
        List<Candidate> votingCandidates = voting.getCandidates();
        for (int i = 0; i < votingCandidates.size(); i++) {
            System.out.println((i+1) + ". " + votingCandidates.get(i).getUser().getFullName());
        }

        System.out.print("Выберите кандидата (номер): ");
        int candidateNum = scanner.nextInt();
        scanner.nextLine();

        if (candidateNum < 1 || candidateNum > votingCandidates.size()) {
            System.out.println("Неверный выбор кандидата!");
            return;
        }

        Candidate selectedCandidate = votingCandidates.get(candidateNum - 1);
        voting.addVote(user, selectedCandidate);

        System.out.println("Ваш голос за " + selectedCandidate.getUser().getFullName() + " учтен!");
    }

    private void viewCandidatesForUser() {
        System.out.println("\nСписок кандидатов:");
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getUser().getFullName() + " - " + candidate.getInfo());
        }
    }

    private void viewAllVotings(User user) {
        System.out.println("\nВсе голосования:");
        for (Voting voting : votings) {
            System.out.print(voting.getId() + ". " + voting.getTitle());
            if (voting.hasUserVoted(user)) {
                System.out.println(" - Вы проголосовали");
            } else if (voting.isActive()) {
                System.out.println(" - Активно (вы еще не голосовали)");
            } else {
                System.out.println(" - Завершено (вы не голосовали)");
            }
        }
    }

    private Voting getVotingById(int id) {
        for (Voting voting : votings) {
            if (voting.getId() == id) {
                return voting;
            }
        }
        return null;
    }

    private Candidate getCandidateById(int id) {
        for (Candidate candidate : candidates) {
            if (candidate.getId() == id) {
                return candidate;
            }
        }
        return null;
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("voting_system.dat"))) {
            oos.writeObject(users);
            oos.writeObject(commissions);
            oos.writeObject(candidates);
            oos.writeObject(votings);
            System.out.println("Данные сохранены успешно!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("voting_system.dat"))) {
            users = (List<User>) ois.readObject();
            commissions = (List<ElectionCommission>) ois.readObject();
            candidates = (List<Candidate>) ois.readObject();
            votings = (List<Voting>) ois.readObject();
            System.out.println("Данные загружены успешно!");
        } catch (FileNotFoundException e) {
            System.out.println("Файл данных не найден. Будет создан новый при сохранении.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }
}
