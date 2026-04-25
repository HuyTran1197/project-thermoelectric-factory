DROP DATABASE IF EXISTS cmms_db;
CREATE DATABASE cmms_db;
USE cmms_db;

-- ======================
-- 1. DEPARTMENTS
-- ======================
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100)
) ENGINE=InnoDB;

-- ======================
-- 2. ROLES
-- ======================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ======================
-- 3. POSITIONS
-- ======================
CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- ======================
-- 4. EMPLOYEES
-- ======================
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (position_id) REFERENCES positions(id)
) ENGINE=InnoDB;

-- ======================
-- 5. USERS
-- ======================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    employee_id BIGINT UNIQUE,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

-- ======================
-- 6. USER_ROLES (FIXED)
-- ======================
CREATE TABLE user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ======================
-- 7. WORK_POSITIONS
-- ======================
CREATE TABLE work_positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- ======================
-- 8. EMPLOYEE_WORK_POSITIONS (FIXED)
-- ======================
CREATE TABLE employee_work_positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    work_position_id BIGINT NOT NULL,
    UNIQUE(employee_id, work_position_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (work_position_id) REFERENCES work_positions(id)
) ENGINE=InnoDB;

-- ======================
-- 9. SYSTEMS
-- ======================
CREATE TABLE systems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 10. DOMAINS
-- ======================
CREATE TABLE domains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- ======================
-- 11. EQUIPMENT_TYPES
-- ======================
CREATE TABLE equipment_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    domain_id BIGINT NOT NULL,
    FOREIGN KEY (domain_id) REFERENCES domains(id)
) ENGINE=InnoDB;

-- ======================
-- 12. EQUIPMENTS
-- ======================
CREATE TABLE equipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE,
    system_id BIGINT NOT NULL,
    type_id BIGINT NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (system_id) REFERENCES systems(id),
    FOREIGN KEY (type_id) REFERENCES equipment_types(id)
) ENGINE=InnoDB;

-- ======================
-- 13. PARAMETER_DEFINITIONS
-- ======================
CREATE TABLE parameter_definitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50),
    type_id BIGINT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES equipment_types(id)
) ENGINE=InnoDB;

-- ======================
-- 14. EQUIPMENT_PARAMETERS (FIXED)
-- ======================
CREATE TABLE equipment_parameters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_id BIGINT NOT NULL,
    parameter_id BIGINT NOT NULL,
    value TEXT,
    UNIQUE(equipment_id, parameter_id),
    FOREIGN KEY (equipment_id) REFERENCES equipments(id) ON DELETE CASCADE,
    FOREIGN KEY (parameter_id) REFERENCES parameter_definitions(id)
) ENGINE=InnoDB;

-- ======================
-- 15. REQUESTS
-- ======================
CREATE TABLE requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_by BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'NEW',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 16. REQUEST_EQUIPMENTS (FIXED)
-- ======================
CREATE TABLE request_equipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    UNIQUE(request_id, equipment_id),
    FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipments(id)
) ENGINE=InnoDB;

-- ======================
-- 17. WORK_ORDERS
-- ======================
CREATE TABLE work_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT,
    created_by BIGINT,
    status VARCHAR(50) DEFAULT 'OPEN',
    start_date DATETIME,
    end_date DATETIME,
    FOREIGN KEY (request_id) REFERENCES requests(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 18. WORK_ORDER_ASSIGNMENTS (FIXED)
-- ======================
CREATE TABLE work_order_assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    role_in_work VARCHAR(100),
    UNIQUE(work_order_id, employee_id),
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

-- ======================
-- 19. TECHNICAL_REPORTS
-- ======================
CREATE TABLE technical_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    content TEXT,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 20. MAINTENANCE_LOGS
-- ======================
CREATE TABLE maintenance_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_id BIGINT NOT NULL,
    work_order_id BIGINT,
    description TEXT,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipments(id),
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
) ENGINE=InnoDB;

-- ======================
-- 21. CONSUMABLE_MATERIALS
-- ======================
CREATE TABLE consumable_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE,
    unit VARCHAR(50),
    location VARCHAR(255),
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 22. CONSUMABLE_TRANSACTIONS
-- ======================
CREATE TABLE consumable_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL,
    type VARCHAR(50),
    quantity INT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES consumable_materials(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 23. REPLACEMENT_MATERIALS
-- ======================
CREATE TABLE replacement_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE,
    unit VARCHAR(50),
    location VARCHAR(255),
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 24. REPLACEMENT_TRANSACTIONS
-- ======================
CREATE TABLE replacement_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL,
    type VARCHAR(50),
    quantity INT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES replacement_materials(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 25. WORK_ORDER_CONSUMABLES
-- ======================
CREATE TABLE work_order_consumables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES consumable_materials(id)
) ENGINE=InnoDB;

-- ======================
-- 26. WORK_ORDER_REPLACEMENTS
-- ======================
CREATE TABLE work_order_replacements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES replacement_materials(id)
) ENGINE=InnoDB;

-- ======================
-- 27. TOOLS
-- ======================
CREATE TABLE tools (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE,
    type VARCHAR(100),
    total_quantity INT DEFAULT 0,
    available_quantity INT DEFAULT 0,
    location VARCHAR(255),
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 28. TOOL_BORROWINGS
-- ======================
CREATE TABLE tool_borrowings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tool_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    due_date DATETIME,
    return_date DATETIME,
    status VARCHAR(50),
    note TEXT,
    FOREIGN KEY (tool_id) REFERENCES tools(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;