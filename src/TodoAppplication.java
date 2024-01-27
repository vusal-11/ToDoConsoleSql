import entity.Todo;
import services.DbContext;
import services.TodoService;

import java.util.Scanner;

public class TodoAppplication {

    public static void main(String[] args) {
        System.out.println("Welcome to Todo Application!");

        DbContext dbContext = new DbContext();
        var connection = dbContext.connect("jdbc:postgresql://localhost:5432/postgres", "postgres", "Post7027472");
        TodoService todoService = new TodoService();

        Scanner scanner = new Scanner(System.in);

        boolean isCreated = false;

        while (true) {
            System.out.println("\n1. Add Todo");
            System.out.println("2. Update Todo");
            System.out.println("3. Delete Todo");
            System.out.println("4. View All Todos");
            System.out.println("5. Find Todo by ID");
            System.out.println("6. Create Todo Table");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Enter Todo title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Todo description: ");
                        String description = scanner.nextLine();
                        todoService.insert(connection, title, description);
                        break;
                    case 2:
                        System.out.print("Enter Todo ID to update: ");
                        int idToUpdate = scanner.nextInt();
                        scanner.nextLine();
                        Todo todoToUpdate = todoService.findById(connection, idToUpdate);
                        if (todoToUpdate != null) {
                            System.out.print("Enter new title: ");
                            todoToUpdate.setTitle(scanner.nextLine());
                            System.out.print("Enter new description: ");
                            todoToUpdate.setDescription(scanner.nextLine());
                            System.out.print("Is it done? (true/false): ");
                            todoToUpdate.setDone(scanner.nextBoolean());
                            todoService.update(connection, todoToUpdate);
                        } else {
                            System.out.println("No Todo found with ID " + idToUpdate);
                        }
                        break;
                    case 3:
                        System.out.print("Enter Todo ID to delete: ");
                        int idToDelete = scanner.nextInt();
                        todoService.delete(connection, idToDelete);
                        break;
                    case 4:
                        System.out.println("All Todos:");
                        var todos = todoService.findAll(connection);
                        if (!todos.isEmpty()) {
                            todos.forEach(System.out::println);
                        } else {
                            System.out.println("No Todos found.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter Todo ID to find: ");
                        int idToFind = scanner.nextInt();
                        Todo foundTodo = todoService.findById(connection, idToFind);
                        if (foundTodo != null) {
                            System.out.println("Found Todo:");
                            System.out.println(foundTodo);
                        } else {
                            System.out.println("No Todo found with ID " + idToFind);
                        }
                        break;
                    case 6:
                        todoService.createTable(connection);
                        if(!isCreated){
                            System.out.println("Todo Table created successfully!");
                        }
                        else {
                            System.out.println("Todo Table is already exists!!!");
                        }
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

}
