import java.util.*;
import java.time.LocalDate;

public class App {

    private static ClientDao clientDao = new ClientDaoImpl();
    private static ProjectDao projectDao = new ProjectDaoImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> addClient();
                case 2 -> listClients();
                case 3 -> addProjectForClient();
                case 4 -> viewClientProjects();
                case 5 -> editClient();
                case 6 -> deleteClient();
                case 7 -> editProject();
                case 8 -> deleteProject();
                case 0 -> {
                    System.out.println("Exiting program...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }

    }

     private static void showMenu() {
        System.out.println("\n==== DGP Advertising Management ====");
        System.out.println("1. Add Client");
        System.out.println("2. List Clients");
        System.out.println("3. Add Project for Client");
        System.out.println("4. View Projects for Client");
        System.out.println("5. Edit Client");
        System.out.println("6. Delete Client");
        System.out.println("7. Edit Project");
        System.out.println("8. Delete Project");
        System.out.println("0. Exit");
    }


    // ---------------- Client Options ----------------

    private static void addClient() {
        System.out.println("\n-- Add Client --");

        String name = read("Client name: ");
        String contact = read("Contact person: ");
        String phone = read("Phone: ");
        String email = read("Email: ");
        String address = read("Address: ");
        String notes = read("Notes: ");

        Client c = new Client(name, contact, phone, email, address, notes);
        clientDao.addClient(c);

        System.out.println("Client added with ID: " + c.getClientId());
    }

    private static void listClients() {
        System.out.println("\n-- All Clients --");
        List<Client> clients = clientDao.getAllClients();

        for (Client c : clients) {
            System.out.println(c.getClientId() + " - " + c.getName());
        }

        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        }
    }

        private static void editClient() {
        System.out.println("\n-- Edit Client --");
        int clientId = readInt("Enter client ID to edit: ");

        Client c = clientDao.getClientById(clientId);
        if (c == null) {
            System.out.println("Client not found.");
            return;
        }

        System.out.println("Editing client: " + c.getName());

        String newName = readOrDefault("Client name", c.getName());
        String newContact = readOrDefault("Contact person", c.getContactPerson());
        String newPhone = readOrDefault("Phone", c.getPhone());
        String newEmail = readOrDefault("Email", c.getEmail());
        String newAddress = readOrDefault("Address", c.getAddress());
        String newNotes = readOrDefault("Notes", c.getNotes());

        c.setName(newName);
        c.setContactPerson(newContact);
        c.setPhone(newPhone);
        c.setEmail(newEmail);
        c.setAddress(newAddress);
        c.setNotes(newNotes);

        clientDao.updateClient(c);

        System.out.println("✔ Client updated.");
    }

        private static void deleteClient() {
        System.out.println("\n-- Delete Client --");
        int clientId = readInt("Enter client ID to delete: ");

        Client c = clientDao.getClientById(clientId);
        if (c == null) {
            System.out.println("Client not found.");
            return;
        }

        System.out.println("You are about to delete client: " + c.getName());
        System.out.println("This will also delete all its projects (because of ON DELETE CASCADE).");
        String confirm = read("Type YES to confirm: ");

        if (confirm.equalsIgnoreCase("YES")) {
            clientDao.deleteClient(clientId);
            System.out.println("Client deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

        private static void editProject() {
        System.out.println("\n-- Edit Project --");
        int projectId = readInt("Enter project ID to edit: ");

        Project p = projectDao.getProjectById(projectId);
        if (p == null) {
            System.out.println("Project not found.");
            return;
        }

        Client c = clientDao.getClientById(p.getClientId());
        System.out.println("Client: " + (c == null ? ("ID " + p.getClientId()) : c.getName()));

        String newName = readOrDefault("Project name", p.getProjectName());
        String newLocation = readOrDefault("Location", p.getLocation());
        String newWorkType = readOrDefault("Work type (NEW/REPAIR/ADD_ON)", p.getWorkType());
        String newDesc = readOrDefault("Description", p.getDescription());
        LocalDate newStart = readDateOrDefault("Date started YYYY-MM-DD", p.getDateStarted());
        LocalDate newEnd = readDateOrDefault("Date completed YYYY-MM-DD", p.getDateCompleted());
        String newStatus = readOrDefault("Status (PENDING/ONGOING/COMPLETED/CANCELLED)", p.getStatus());
        double newCost = readDoubleOrDefault("Total cost", p.getTotalCost());
        String newNotes = readOrDefault("Notes", p.getNotes());

        p.setProjectName(newName);
        p.setLocation(newLocation);
        p.setWorkType(newWorkType);
        p.setDescription(newDesc);
        p.setDateStarted(newStart);
        p.setDateCompleted(newEnd);
        p.setStatus(newStatus);
        p.setTotalCost(newCost);
        p.setNotes(newNotes);

        projectDao.updateProject(p);

        System.out.println("Project updated.");
    }

        private static void deleteProject() {
        System.out.println("\n-- Delete Project --");
        int projectId = readInt("Enter project ID to delete: ");

        Project p = projectDao.getProjectById(projectId);
        if (p == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.println("You are about to delete project: " + p.getProjectName());
        String confirm = read("Type YES to confirm: ");

        if (confirm.equalsIgnoreCase("YES")) {
            projectDao.deleteProject(projectId);
            System.out.println("Project deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }





    // ---------------- Project Options ----------------

    private static void addProjectForClient() {
        System.out.println("\n-- Add Project for Client --");

        int clientId = readInt("Enter client ID: ");
        Client c = clientDao.getClientById(clientId);

        if (c == null) {
            System.out.println("❌ Client not found.");
            return;
        }

        System.out.println("Adding project for: " + c.getName());

        String projectName = read("Project name: ");
        String location = read("Location: ");
        String workType = read("Work type (NEW/REPAIR/ADD_ON): ");
        String description = read("Description: ");

        String startStr = read("Date started (YYYY-MM-DD or leave blank): ");
        LocalDate startDate = startStr.isBlank() ? null : LocalDate.parse(startStr);

        String endStr = read("Date completed (YYYY-MM-DD or leave blank): ");
        LocalDate endDate = endStr.isBlank() ? null : LocalDate.parse(endStr);

        String status = read("Status (PENDING/ONGOING/COMPLETED/CANCELLED): ");
        double cost = readDouble("Total cost: ");
        String notes = read("Notes: ");

        Project p = new Project(clientId, projectName, location, workType, description,
                startDate, endDate, status, cost, notes);

        projectDao.addProject(p);

        System.out.println("✔ Project added with ID: " + p.getProjectId());
    }

    private static void viewClientProjects() {
        System.out.println("\n-- View Projects --");

        int clientId = readInt("Enter client ID: ");
        Client c = clientDao.getClientById(clientId);

        if (c == null) {
            System.out.println("❌ Client not found.");
            return;
        }

        System.out.println("Projects for: " + c.getName());

        List<Project> projects = projectDao.getProjectsByClient(clientId);

        if (projects.isEmpty()) {
            System.out.println("No projects.");
            return;
        }

        for (Project p : projects) {
            System.out.println("\n------------------------------");
            System.out.println("Project ID: " + p.getProjectId());
            System.out.println("Name: " + p.getProjectName());
            System.out.println("Location: " + p.getLocation());
            System.out.println("Work type: " + p.getWorkType());
            System.out.println("Description: " + p.getDescription());
            System.out.println("Start: " + p.getDateStarted());
            System.out.println("End: " + p.getDateCompleted());
            System.out.println("Status: " + p.getStatus());
            System.out.println("Cost: ₱" + p.getTotalCost());
            System.out.println("Notes: " + p.getNotes());
        }
    }

    // ---------------- Helper Methods ----------------

    private static String read(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid amount.");
            }
        }
    }
        // ---- Extra helpers for editing ----

    private static String readOrDefault(String label, String current) {
        System.out.print(label + " (" + current + "): ");
        String input = scanner.nextLine();
        return input.isBlank() ? current : input;
    }

    private static LocalDate readDateOrDefault(String label, LocalDate current) {
        String currentStr = current == null ? "none" : current.toString();
        System.out.print(label + " (" + currentStr + ", blank to keep): ");
        String input = scanner.nextLine();
        if (input.isBlank()) {
            return current;
        }
        if (input.equalsIgnoreCase("none")) {
            return null;
        }
        return LocalDate.parse(input);
    }

    private static double readDoubleOrDefault(String label, double current) {
        System.out.print(label + " (" + current + "): ");
        String input = scanner.nextLine();
        if (input.isBlank()) {
            return current;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount, keeping old value.");
            return current;
        }
    }

}
