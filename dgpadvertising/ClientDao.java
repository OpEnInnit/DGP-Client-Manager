import java.util.List;

public interface ClientDao {
    void addClient(Client client);
    void updateClient(Client client);
    void deleteClient(int clientId);
    Client getClientById(int clientId);
    List<Client> getAllClients();
    List<Client> searchClientsByName(String name);
}
