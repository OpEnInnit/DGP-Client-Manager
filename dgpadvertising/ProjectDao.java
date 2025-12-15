import java.util.List;

public interface ProjectDao {

    void addProject(Project project);

    void updateProject(Project project);

    void deleteProject(int projectId);

    Project getProjectById(int projectId);

    // Existing: still useful internally
    List<Project> getProjectsByClient(int clientId);

    // NEW: search projects using client name (case-insensitive)
    List<Project> getProjectsByClientName(String clientName);
}
