import java.time.LocalDate;
import java.util.List;

public class DgpAdvertisingClientManager {
    public static void main(String[] args) {

        int clientId = 3; // use an existing client ID you know

        ProjectDao projectDao = new ProjectDaoImpl();

        // 1) Add a test project
        Project proj = new Project(
                clientId,
                "Test Front Signage",
                "San Pedro Highway",
                "NEW",
                "Test project for DGP system",
                LocalDate.of(2025, 1, 10),
                null,
                "ONGOING",
                50000.00,
                "Sample project entry"
        );

        projectDao.addProject(proj);
        System.out.println("Inserted project ID: " + proj.getProjectId());

        // 2) List all projects for that client
        List<Project> projects = projectDao.getProjectsByClient(clientId);
        System.out.println("Projects for client " + clientId + ":");
        for (Project p : projects) {
            System.out.println(p);
        }
    }
}
