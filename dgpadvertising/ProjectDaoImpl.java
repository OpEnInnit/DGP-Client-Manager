import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl implements ProjectDao {

    @Override
    public void addProject(Project project) {
        String sql = """
            INSERT INTO projects
            (client_id, project_name, location,
             date_started, date_completed, status, total_cost, notes,
             po_number, sales_invoice, dr_number)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, project.getClientId());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getLocation());

            if (project.getDateStarted() != null)
                stmt.setString(4, project.getDateStarted().toString());
            else
                stmt.setNull(4, Types.VARCHAR);

            if (project.getDateCompleted() != null)
                stmt.setString(5, project.getDateCompleted().toString());
            else
                stmt.setNull(5, Types.VARCHAR);

            stmt.setString(6, project.getStatus());
            stmt.setDouble(7, project.getTotalCost());
            stmt.setString(8, project.getNotes());

            if (project.getPoNumber() != null)
                stmt.setInt(9, project.getPoNumber());
            else
                stmt.setNull(9, Types.INTEGER);

            if (project.getSalesInvoice() != null)
                stmt.setInt(10, project.getSalesInvoice());
            else
                stmt.setNull(10, Types.INTEGER);

            if (project.getDrNumber() != null)
                stmt.setInt(11, project.getDrNumber());
            else
                stmt.setNull(11, Types.INTEGER);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                project.setProjectId(rs.getInt(1));
            }

            System.out.println("✅ Project added: " + project.getProjectName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProject(Project project) {
        String sql = """
            UPDATE projects SET
            client_id=?,
            project_name=?,
            location=?,
            date_started=?,
            date_completed=?,
            status=?,
            total_cost=?,
            notes=?,
            po_number=?,
            sales_invoice=?,
            dr_number=?
            WHERE project_id=?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, project.getClientId());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getLocation());

            if (project.getDateStarted() != null)
                stmt.setString(4, project.getDateStarted().toString());
            else
                stmt.setNull(4, Types.VARCHAR);

            if (project.getDateCompleted() != null)
                stmt.setString(5, project.getDateCompleted().toString());
            else
                stmt.setNull(5, Types.VARCHAR);

            stmt.setString(6, project.getStatus());
            stmt.setDouble(7, project.getTotalCost());
            stmt.setString(8, project.getNotes());

            if (project.getPoNumber() != null)
                stmt.setInt(9, project.getPoNumber());
            else
                stmt.setNull(9, Types.INTEGER);

            if (project.getSalesInvoice() != null)
                stmt.setInt(10, project.getSalesInvoice());
            else
                stmt.setNull(10, Types.INTEGER);

            if (project.getDrNumber() != null)
                stmt.setInt(11, project.getDrNumber());
            else
                stmt.setNull(11, Types.INTEGER);

            stmt.setInt(12, project.getProjectId());

            stmt.executeUpdate();
            System.out.println("✅ Project updated: " + project.getProjectName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(int projectId) {
        String sql = "DELETE FROM projects WHERE project_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Project getProjectById(int projectId) {
        String sql = "SELECT * FROM projects WHERE project_id=?";
        Project project = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                project = mapRowToProject(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    @Override
    public List<Project> getProjectsByClient(int clientId) {
        String sql = "SELECT * FROM projects WHERE client_id=? ORDER BY project_id";
        List<Project> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRowToProject(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    @Override
public List<Project> getProjectsByClientName(String clientName) {
    String sql = """
        SELECT p.*
        FROM projects p
        JOIN clients c ON p.client_id = c.client_id
        WHERE LOWER(c.name) LIKE LOWER(?)
        ORDER BY p.project_id
    """;

    List<Project> list = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, "%" + clientName.trim() + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(mapRowToProject(rs));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


    private Project mapRowToProject(ResultSet rs) throws SQLException {
        Project p = new Project();

        p.setProjectId(rs.getInt("project_id"));
        p.setClientId(rs.getInt("client_id"));
        p.setProjectName(rs.getString("project_name"));
        p.setLocation(rs.getString("location"));

        String start = rs.getString("date_started");
        if (start != null && !start.isBlank())
            p.setDateStarted(LocalDate.parse(start));

        String end = rs.getString("date_completed");
        if (end != null && !end.isBlank())
            p.setDateCompleted(LocalDate.parse(end));

        p.setStatus(rs.getString("status"));
        p.setTotalCost(rs.getDouble("total_cost"));
        p.setNotes(rs.getString("notes"));
        p.setPoNumber((Integer) rs.getObject("po_number"));
        p.setSalesInvoice((Integer) rs.getObject("sales_invoice"));
        p.setDrNumber((Integer) rs.getObject("dr_number"));

        return p;
    }
}
