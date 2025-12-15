import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class DgpMainFrame extends JFrame {

    private ClientDao clientDao = new ClientDaoImpl();
    private ProjectDao projectDao = new ProjectDaoImpl();

    private DefaultTableModel clientTableModel;
    private JTable clientTable;

    private DefaultTableModel projectTableModel;
    private JTable projectTable;
    private JTextField clientNameSearchField;

    public DgpMainFrame() {
        setTitle("DGP Advertising - Client & Project Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clients", createClientsPanel());
        tabs.addTab("Projects", createProjectsPanel());
        add(tabs, BorderLayout.CENTER);
    }

    // ---------- HEADER ----------
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel title = new JLabel("DGP Advertising - Client & Project Manager");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.CENTER);
        return panel;
    }

    private Client showClientForm(Client existing) {

    JTextField name = new JTextField();
    JTextField contact = new JTextField();
    JTextField phone = new JTextField();
    JTextField email = new JTextField();
    JTextField address = new JTextField();
    JTextArea notes = new JTextArea(3, 20);
    JComboBox<String> statusBox = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});


    if (existing != null) {
        name.setText(existing.getName());
        contact.setText(existing.getContactPerson());
        phone.setText(existing.getPhone());
        email.setText(existing.getEmail());
        address.setText(existing.getAddress());
        notes.setText(existing.getNotes());
        statusBox.setSelectedItem(existing.getStatus());
    }

    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 6));
    panel.add(new JLabel("Name"));
    panel.add(name);
    panel.add(new JLabel("Contact"));
    panel.add(contact);
    panel.add(new JLabel("Phone"));
    panel.add(phone);
    panel.add(new JLabel("Email"));
    panel.add(email);
    panel.add(new JLabel("Address"));
    panel.add(address);
    panel.add(new JLabel("Notes"));
    panel.add(new JScrollPane(notes));
    panel.add(new JLabel("Status"));
    panel.add(statusBox);


    int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            existing == null ? "Add Client" : "Edit Client",
            JOptionPane.OK_CANCEL_OPTION
    );

    if (result != JOptionPane.OK_OPTION) return null;

    Client c = new Client();
    c.setName(name.getText().trim());
    c.setContactPerson(contact.getText().trim());
    c.setPhone(phone.getText().trim());
    c.setEmail(email.getText().trim());
    c.setAddress(address.getText().trim());
    c.setNotes(notes.getText().trim());
    c.setStatus((String) statusBox.getSelectedItem());

    return c;
}


    // ---------- CLIENTS TAB ----------
    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        clientTableModel = new DefaultTableModel(
                new Object[]{"Client ID","Name","Contact","Phone","Email", "Address", "Status"},0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        clientTable = new JTable(clientTableModel);
        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add Client");
        JButton edit = new JButton("Edit Client");
        //deleted  JButton del = new JButton("Delete Client"); for security purposes.
        JButton export = new JButton("Export CSV");
        JButton viewNotes = new JButton("View Notes");
        viewNotes.addActionListener(this::onViewClientNotes);
        buttons.add(viewNotes);

        refresh.addActionListener(e -> loadClients());
        add.addActionListener(this::onAddClient);
        edit.addActionListener(this::onEditClient);
        //removed del.addActionListener(this::onDeleteClient); for security purposes.
        export.addActionListener(this::onExportCSV);

        buttons.add(refresh);
        buttons.add(add);
        buttons.add(edit);
        //removed buttons.add(del); for security purposes
        buttons.add(export);

        panel.add(buttons, BorderLayout.SOUTH);
        loadClients();
        return panel;
    }

    private void loadClients() {
        clientTableModel.setRowCount(0);
        for (Client c : clientDao.getAllClients()) {
            clientTableModel.addRow(new Object[]{
                c.getClientId(), c.getName(),
                c.getContactPerson(), c.getPhone(), c.getEmail(), c.getAddress(), c.getStatus()
            });
        }
    }

    // ---------- PROJECTS TAB ----------
    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Client Name:"));
        clientNameSearchField = new JTextField(20);
        top.add(clientNameSearchField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(this::onSearchByClientName);
        top.add(searchBtn);

        panel.add(top, BorderLayout.NORTH);

        projectTableModel = new DefaultTableModel(new Object[]{
            "Project ID","Client Name","Project Name","Location",
            "Start Date","End Date",
            "PO #","Invoice #","DR #",
            "Status","Cost (₱)"
        },0){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        projectTable = new JTable(projectTableModel);
        panel.add(new JScrollPane(projectTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton add = new JButton("Add Project");
        JButton edit = new JButton("Edit Project");
        JButton del = new JButton("Delete Project");
        JButton notes = new JButton("Show Notes");
        JButton specsBtn = new JButton("Show Specs");
        specsBtn.addActionListener(this::onShowSpecs);
        buttons.add(specsBtn);

        add.addActionListener(this::onAddProject);
        edit.addActionListener(this::onEditProject);
        del.addActionListener(this::onDeleteProject);
        notes.addActionListener(this::onShowNotes);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(del);
        buttons.add(notes);

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- PROJECT SEARCH ----------
    private void onSearchByClientName(ActionEvent e) {
        String name = clientNameSearchField.getText().trim().toLowerCase();
        projectTableModel.setRowCount(0);

        for (Client c : clientDao.getAllClients()) {
            if (!c.getName().toLowerCase().contains(name)) continue;

            for (Project p : projectDao.getProjectsByClient(c.getClientId())) {
                projectTableModel.addRow(new Object[]{
                    p.getProjectId(),
                    c.getName(),
                    p.getProjectName(),
                    p.getLocation(),
                    p.getDateStarted(),
                    p.getDateCompleted(),
                    p.getPoNumber(),
                    p.getSalesInvoice(),
                    p.getDrNumber(),
                    p.getStatus(),
                    p.getTotalCost()
                });
            }
        }
    }

    // ---------- ADD / EDIT / DELETE CLIENT ----------

private void onAddClient(ActionEvent e) {
    Client c = showClientForm(null);
    if (c != null) {
        clientDao.addClient(c);
        loadClients();
    }
}

private void onEditClient(ActionEvent e) {
    int row = clientTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a client first.");
        return;
    }

    int clientId = (int) clientTableModel.getValueAt(row, 0);
    Client existing = clientDao.getClientById(clientId);

    Client updated = showClientForm(existing);
    if (updated != null) {
        updated.setClientId(clientId);
        clientDao.updateClient(updated);
        loadClients();
    }
}

    private LocalDate parseDateSafe(String text) {
        try {
            return LocalDate.parse(text.trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Invalid date format.\nPlease use YYYY-MM-DD (e.g. 2025-12-01)"
            );
            return null;
        }
    }

/* commented out for security purposes.
    private void onDeleteClient(ActionEvent e) {
        int row = clientTable.getSelectedRow();
        if (row == -1) return;

        int clientId = (int) clientTableModel.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete this client and all projects?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            clientDao.deleteClient(clientId);
            loadClients();
        }
    }
 */

    private void onViewClientNotes(ActionEvent e) {
        int row = clientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a client first.");
            return;
        }

        int clientId = (int) clientTableModel.getValueAt(row, 0);
        Client c = clientDao.getClientById(clientId);

        JTextArea area = new JTextArea(c.getNotes());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Client Notes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }



    // ---------- ADD / EDIT / DELETE PROJECT ----------
   private void onAddProject(ActionEvent e) {
    String name = clientNameSearchField.getText().trim().toLowerCase();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter a client name first.");
        return;
    }

    List<Client> matches = new java.util.ArrayList<>();

    for (Client c : clientDao.getAllClients()) {
        if (c.getName().toLowerCase().contains(name)) {
            matches.add(c);
        }
    }

    if (matches.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No matching client found.");
        return;
    }

    if (matches.size() > 1) {
        JOptionPane.showMessageDialog(
            this,
            "Multiple clients match this name.\nPlease be more specific."
        );
        return;
    }

    Client selectedClient = matches.get(0);

    Project p = showProjectForm(null);
    if (p != null) {
        p.setClientId(selectedClient.getClientId());
        projectDao.addProject(p);
        onSearchByClientName(null);
    }
}

    private void onEditProject(ActionEvent e) {
    int row = projectTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a project first.");
        return;
    }

    int projectId = (int) projectTableModel.getValueAt(row, 0);
    Project existing = projectDao.getProjectById(projectId);

    if (existing == null) {
        JOptionPane.showMessageDialog(this, "Project not found.");
        return;
    }

    Project updated = showProjectForm(existing);
    if (updated != null) {
        updated.setProjectId(projectId);

        updated.setClientId(existing.getClientId());

        projectDao.updateProject(updated);
        onSearchByClientName(null);
    }
}

    private void onDeleteProject(ActionEvent e) {
        int row = projectTable.getSelectedRow();
        if (row == -1) return;

        int projectId = (int) projectTableModel.getValueAt(row,0);
        if (JOptionPane.showConfirmDialog(
            this,"Delete this project?","Confirm",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            projectDao.deleteProject(projectId);
            onSearchByClientName(null);
        }
    }
    private void onExportCSV(ActionEvent e) {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Export Clients and Projects");

    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

    java.io.File file = chooser.getSelectedFile();
    if (!file.getName().toLowerCase().endsWith(".csv")) {
        file = new java.io.File(file.getAbsolutePath() + ".csv");
    }

    try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {

        pw.println(
            "Client ID,Client Name,Project ID,Project Name,Location," +
            "Start Date,End Date,PO,Invoice,DR,Status,Cost,Notes,Specs"
        );


        for (Client c : clientDao.getAllClients()) {
            List<Project> projects = projectDao.getProjectsByClient(c.getClientId());

            if (projects.isEmpty()) {
                pw.printf("%d,\"%s\",,,,,,,,,\n",
                        c.getClientId(), c.getName());
            } else {
                for (Project p : projects) {
                    pw.printf(
                        "%d,\"%s\",%d,\"%s\",\"%s\",%s,%s,%s,%s,%s,\"%s\",%.2f,\"%s\",\"%s\"\n",
                        c.getClientId(),
                        c.getName(),
                        p.getProjectId(),
                        p.getProjectName(),
                        p.getLocation(),
                        p.getDateStarted(),
                        p.getDateCompleted(),
                        p.getPoNumber(),
                        p.getSalesInvoice(),
                        p.getDrNumber(),
                        p.getStatus(),
                        p.getTotalCost(),
                        escapeCsv(p.getNotes()),
                        escapeCsv(p.getSpecs())
                    );
                }
            }
        }

        JOptionPane.showMessageDialog(this,
                "Export successful:\n" + file.getAbsolutePath());

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Export failed: " + ex.getMessage());
    }
}


    // ---------- PROJECT FORM ----------
    private Project showProjectForm(Project existing) {

    JTextField nameField = new JTextField();
    JTextField locationField = new JTextField();
    JTextField startField = new JTextField();
    JTextField endField = new JTextField();
    JTextField poField = new JTextField();
    JTextField invoiceField = new JTextField();
    JTextField drField = new JTextField();
    JComboBox<String> statusBox = new JComboBox<>(
    new String[]{"----","ONGOING", "COMPLETED", "CANCELLED"}
    );
    JTextField costField = new JTextField();
    JTextArea notesArea = new JTextArea(4, 20);
    JTextArea specsArea = new JTextArea(4,20);

    if (existing != null) {
        nameField.setText(existing.getProjectName());
        locationField.setText(existing.getLocation());

        if (existing.getDateStarted() != null)
            startField.setText(existing.getDateStarted().toString());

        if (existing.getDateCompleted() != null)
            endField.setText(existing.getDateCompleted().toString());

        if (existing.getPoNumber() != null)
            poField.setText(existing.getPoNumber().toString());

        if (existing.getSalesInvoice() != null)
            invoiceField.setText(existing.getSalesInvoice().toString());

        if (existing.getDrNumber() != null)
            drField.setText(existing.getDrNumber().toString());

        statusBox.setSelectedItem(existing.getStatus());
        costField.setText(String.valueOf(existing.getTotalCost()));
        notesArea.setText(existing.getNotes());
        specsArea.setText(existing.getSpecs());
    }

    JPanel panel = new JPanel(new GridLayout(0,2,10,6));
    panel.add(new JLabel("Project Name"));
    panel.add(nameField);
    panel.add(new JLabel("Location"));
    panel.add(locationField);
    panel.add(new JLabel("Start Date (YYYY-MM-DD)"));
    panel.add(startField);
    panel.add(new JLabel("End Date (YYYY-MM-DD)"));
    panel.add(endField);
    panel.add(new JLabel("PO Number"));
    panel.add(poField);
    panel.add(new JLabel("Sales Invoice"));
    panel.add(invoiceField);
    panel.add(new JLabel("DR Number"));
    panel.add(drField);
    panel.add(new JLabel("Status"));
    panel.add(statusBox);
    panel.add(new JLabel("Total Cost"));
    panel.add(costField);
    panel.add(new JLabel("Notes"));
    panel.add(new JScrollPane(notesArea));
    panel.add(new JLabel("Specs"));
    panel.add(new JScrollPane(specsArea));

    int result = JOptionPane.showConfirmDialog(
        this, panel,
        existing == null ? "Add Project" : "Edit Project",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (result != JOptionPane.OK_OPTION) return null;

    Project p = new Project();

    // 🔑 CRITICAL: KEEP CLIENT ID ON EDIT
    if (existing != null) {
        p.setClientId(existing.getClientId());
    }

    p.setProjectName(nameField.getText().trim());
    p.setLocation(locationField.getText().trim());
    p.setStatus((String) statusBox.getSelectedItem());
    p.setNotes(notesArea.getText().trim());
    p.setSpecs(specsArea.getText().trim());

    if (!startField.getText().isBlank()) {
    LocalDate d = parseDateSafe(startField.getText());
    if (d == null) return null;   // stop saving if invalid
    p.setDateStarted(d);
    }

    if (!endField.getText().isBlank()) {
        LocalDate d = parseDateSafe(endField.getText());
        if (d == null) return null;
        p.setDateCompleted(d);
    }

    if (!poField.getText().isBlank())
        p.setPoNumber(Integer.parseInt(poField.getText().trim()));

    if (!invoiceField.getText().isBlank())
        p.setSalesInvoice(Integer.parseInt(invoiceField.getText().trim()));

    if (!drField.getText().isBlank())
        p.setDrNumber(Integer.parseInt(drField.getText().trim()));

    try {
        p.setTotalCost(Double.parseDouble(costField.getText().trim()));
    } catch (Exception ex) {
        p.setTotalCost(0);
    }

    return p;
}

    //helper
    private void onShowSpecs(ActionEvent e) {
        int row = projectTable.getSelectedRow();
        if (row == -1) return;

        int projectId = (int) projectTableModel.getValueAt(row, 0);
        Project p = projectDao.getProjectById(projectId);

        JTextArea area = new JTextArea(p.getSpecs());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(area),
            "Project Specs",
            JOptionPane.INFORMATION_MESSAGE
        );
    }



    // ---------- NOTES ----------
    private void onShowNotes(ActionEvent e) {
        int row = projectTable.getSelectedRow();
        if (row == -1) return;

        int projectId = (int) projectTableModel.getValueAt(row,0);
        Project p = projectDao.getProjectById(projectId);

        JTextArea area = new JTextArea(p.getNotes());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JOptionPane.showMessageDialog(this,new JScrollPane(area),
            "Project Notes",JOptionPane.INFORMATION_MESSAGE);
    }

    //helper

        private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }


    // ---------- MAIN ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DgpMainFrame().setVisible(true));
    }
}
