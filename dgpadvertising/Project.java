import java.time.LocalDate;

public class Project {
    private int projectId;
    private int clientId;
    private String projectName;
    private String location;
    private String workType;       // NEW, REPAIR, ADD_ON
    private String description;
    private LocalDate dateStarted;
    private LocalDate dateCompleted;
    private String status;         // PENDING, ONGOING, COMPLETED, CANCELLED
    private double totalCost;
    private String notes;
    private Integer poNumber;
    private Integer salesInvoice;
    private Integer drNumber;
    private String specs;


    public Project() {}

    public Project(int projectId, int clientId, String projectName, String location,
                   String workType, String description, LocalDate dateStarted,
                   LocalDate dateCompleted, String status, double totalCost,
                   String notes) {

        this.projectId = projectId;
        this.clientId = clientId;
        this.projectName = projectName;
        this.location = location;
        this.workType = workType;
        this.description = description;
        this.dateStarted = dateStarted;
        this.dateCompleted = dateCompleted;
        this.status = status;
        this.totalCost = totalCost;
        this.notes = notes;
    }

    public Project(int clientId, String projectName, String location,
                   String workType, String description, LocalDate dateStarted,
                   LocalDate dateCompleted, String status, double totalCost,
                   String notes) {

        this(0, clientId, projectName, location, workType, description,
             dateStarted, dateCompleted, status, totalCost, notes);
    }

    // Getters and Setters
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateStarted() { return dateStarted; }
    public void setDateStarted(LocalDate dateStarted) { this.dateStarted = dateStarted; }

    public LocalDate getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(LocalDate dateCompleted) { this.dateCompleted = dateCompleted; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSpecs() {
    return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
    
    public Integer getPoNumber() {
    return poNumber;
    }

    public void setPoNumber(Integer poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getSalesInvoice() {
        return salesInvoice;
    }

    public void setSalesInvoice(Integer salesInvoice) {
        this.salesInvoice = salesInvoice;
    }

    public Integer getDrNumber() {
        return drNumber;
    }

    public void setDrNumber(Integer drNumber) {
        this.drNumber = drNumber;
    }


    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", clientId=" + clientId +
                ", projectName='" + projectName + '\'' +
                ", location='" + location + '\'' +
                ", workType='" + workType + '\'' +
                ", description='" + description + '\'' +
                ", dateStarted=" + dateStarted +
                ", dateCompleted=" + dateCompleted +
                ", status='" + status + '\'' +
                ", totalCost=" + totalCost +
                ", notes='" + notes + '\'' +
                '}';
    }
}
