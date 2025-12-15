CREATE TABLE clients (

	client_id 		INT PRIMARY KEY auto_increment,
    name      		VARCHAR(200) NOT NULL,
    contact_person	VARCHAR(150),
    phone			VARCHAR(50),
    email			VARCHAR(150),
    address			VARCHAR(255),
    notes			TEXT
) ENGINE=InnoDB;

CREATE TABLE projects (
    project_id     INT PRIMARY KEY AUTO_INCREMENT,
    client_id      INT NOT NULL,
    project_name   VARCHAR(200),
    location       VARCHAR(255),
    work_type      VARCHAR(50),     -- e.g. 'NEW', 'REPAIR', 'ADD_ON'
    description    TEXT,
    date_started   DATE,
    date_completed DATE,
    status         VARCHAR(30),     -- 'PENDING', 'ONGOING', 'COMPLETED', 'CANCELLED'
    total_cost     DECIMAL(10,2),
    notes          TEXT,
    
    CONSTRAINT fk_projects_clients
        FOREIGN KEY (client_id)
        REFERENCES clients(client_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO clients (name, contact_person, phone, email, address, notes)
VALUES
('Jollibee San Pedro', 'Anna Reyes', '0917-123-4567', 'anna.reyes@jollibee.com', 'San Pedro, Laguna', 'Prefers email, 30 days payment terms'),
('McDonalds Main Square', 'Mark Cruz', '0918-987-6543', 'mark.cruz@mcd.com', 'Main Square, Bacoor', 'Rush projects sometimes');

INSERT INTO projects (client_id, project_name, location, work_type, description, date_started, date_completed, status, total_cost, notes)
VALUES
(1, 'Main Pylon Sign', 'San Pedro Highway Frontage', 'NEW', 'Fabricated and installed 4x10m pylon signage with LED backlight', '2025-01-10', '2025-01-20', 'COMPLETED', 150000.00, 'Installed on time, client happy'),
(1, 'Menu Lightbox Repair', 'Drive-thru Area', 'REPAIR', 'Replaced LED modules and power supply', '2025-02-05', NULL, 'ONGOING', 25000.00, 'Waiting for imported LED modules'),
(2, 'Front Facade Signage', 'Main Square Entrance', 'NEW', 'Acrylic cut-out with backlight', '2025-01-15', '2025-01-25', 'COMPLETED', 80000.00, 'Minor alignment adjustment after install');