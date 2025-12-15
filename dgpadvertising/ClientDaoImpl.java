import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl implements ClientDao {

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO clients (name, contact_person, phone, email, address, notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getContactPerson());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getAddress());
            stmt.setString(6, client.getNotes());

            stmt.executeUpdate();

            // Set generated ID back to object
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                client.setClientId(rs.getInt(1));
            }

            System.out.println("Client added: " + client.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateClient(Client client) {
        String sql = "UPDATE clients SET name=?, contact_person=?, phone=?, email=?, address=?, notes=? WHERE client_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getContactPerson());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getAddress());
            stmt.setString(6, client.getNotes());
            stmt.setInt(7, client.getClientId());

            stmt.executeUpdate();

            System.out.println("Client updated: " + client.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteClient(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            stmt.executeUpdate();

            System.out.println("Client deleted: " + clientId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM clients WHERE client_id=?";
        Client client = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = mapRowToClient(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients ORDER BY client_id";
        List<Client> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToClient(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        String sql = "SELECT * FROM clients WHERE name LIKE ? ORDER BY name";
        List<Client> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRowToClient(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Helper method: Convert ResultSet row to Client object
    private Client mapRowToClient(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setClientId(rs.getInt("client_id"));
        c.setName(rs.getString("name"));
        c.setContactPerson(rs.getString("contact_person"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setNotes(rs.getString("notes"));
        return c;
    }
}
