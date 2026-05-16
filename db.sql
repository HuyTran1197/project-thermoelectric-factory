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


INSERT INTO parameter_definitions (name, unit, type_id) VALUES

-- ================= BƠM (1) =================
('Lưu lượng', 'm3/h', 1),
('Cột áp', 'm', 1),
('Công suất', 'kW', 1),
('Tốc độ quay', 'vòng/phút', 1),
('Hiệu suất', '%', 1),
('Đường kính ống hút', 'mm', 1),
('Đường kính ống xả', 'mm', 1),
('Hãng sản xuất', NULL, 1),
('Model', NULL, 1),
('Năm sản xuất', NULL, 1),

-- ================= QUẠT (2) =================
('Lưu lượng gió', 'm3/h', 2),
('Áp suất', 'Pa', 2),
('Công suất', 'kW', 2),
('Tốc độ quay', 'vòng/phút', 2),
('Hiệu suất', '%', 2),
('Đường kính cánh', 'mm', 2),
('Hãng sản xuất', NULL, 2),
('Model', NULL, 2),

-- ================= MÁY NÉN (3) =================
('Lưu lượng khí', 'm3/h', 3),
('Áp suất đầu ra', 'bar', 3),
('Công suất', 'kW', 3),
('Tốc độ quay', 'vòng/phút', 3),
('Nhiệt độ làm việc', '°C', 3),
('Hãng sản xuất', NULL, 3),
('Model', NULL, 3),

-- ================= VAN (4) =================
('Đường kính danh nghĩa', 'mm', 4),
('Áp suất làm việc', 'bar', 4),
('Nhiệt độ làm việc', '°C', 4),
('Loại van', NULL, 4),
('Vật liệu', NULL, 4),
('Kiểu kết nối', NULL, 4),
('Hãng sản xuất', NULL, 4),

-- ================= ĐỘNG CƠ (6) =================
('Công suất', 'kW', 6),
('Điện áp', 'V', 6),
('Dòng điện', 'A', 6),
('Tần số', 'Hz', 6),
('Tốc độ quay', 'vòng/phút', 6),
('Hiệu suất', '%', 6),
('Hệ số công suất', 'cosφ', 6),
('Cấp cách điện', NULL, 6),
('Hãng sản xuất', NULL, 6),
('Model', NULL, 6),

-- ================= MÁY BIẾN ÁP (7) =================
('Công suất định mức', 'kVA', 7),
('Điện áp sơ cấp', 'kV', 7),
('Điện áp thứ cấp', 'kV', 7),
('Dòng điện', 'A', 7),
('Tần số', 'Hz', 7),
('Tổ đấu dây', NULL, 7),
('Hãng sản xuất', NULL, 7),
('Năm sản xuất', NULL, 7),

-- ================= THIẾT BỊ ĐO (5) =================
('Loại tín hiệu', NULL, 5),
('Dải đo', NULL, 5),
('Độ chính xác', '%', 5),
('Nguồn cấp', 'V', 5),
('Tín hiệu đầu ra', NULL, 5),
('Hãng sản xuất', NULL, 5),
('Model', NULL,5),

-- ================= THIẾT BỊ ĐIỀU KHIỂN (8) =================
('Điện áp hoạt động', 'V', 8),
('Dòng điện', 'A', 8),
('Giao thức truyền thông', NULL, 8),
('Số kênh I/O', NULL, 8),
('Nhiệt độ làm việc', '°C', 8),
('Hãng sản xuất', NULL, 8),
('Model', NULL, 8);

select et.name as type,e.name as name,e.code as kks,pd.name as parameters,ep.value as value 
from equipment_types et
join equipments e on e.type_id = et.id
join parameter_definitions pd on pd.type_id = et.id
left join equipment_parameters ep on ep.parameter_id = pd.id
and ep.equipment_id = e.id
where et.name = 'Động cơ'
and e.id = 3;

select * from equipments
where type_id = 5;

INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES

-- =====================================================
-- 1. BƠM CẤP NƯỚC THÔ (equipment_id = 1)
-- type_id = 1 (Bơm)
-- =====================================================
(1, 1, '850'),
(1, 2, '180'),
(1, 3, '3200'),
(1, 4, '2980'),
(1, 5, '89'),
(1, 6, '250'),
(1, 7, '200'),
(1, 8, 'KSB'),
(1, 9, 'HGC-450'),
(1, 10, '2020'),

-- =====================================================
-- 2. ĐỘNG CƠ BƠM NƯỚC THÔ (equipment_id = 3)
-- type_id = 6 (Động cơ)
-- parameter_id = 33 -> 42
-- =====================================================
(3, 33, '315'),
(3, 34, '6000'),
(3, 35, '38'),
(3, 36, '50'),
(3, 37, '2980'),
(3, 38, '95'),
(3, 39, '0.89'),
(3, 40, 'F'),
(3, 41, 'ABB'),
(3, 42, 'MOTOR-315'),

-- =====================================================
-- 3. ĐỒNG HỒ ĐO ÁP SUẤT DẦU (equipment_id = 5)
-- type_id = 5 (Thiết bị đo)
-- parameter_id = 51 -> 57
-- =====================================================
(5, 51, '4-20mA'),
(5, 52, '0-25 bar'),
(5, 53, '0.5'),
(5, 54, '24'),
(5, 55, 'Analog'),
(5, 56, 'Yokogawa'),
(5, 57, 'EJA530A'),

-- =====================================================
-- 4. THIẾT BỊ CẢM BIẾN HƠI NÓNG (equipment_id = 6)
-- type_id = 5 (Thiết bị đo)
-- =====================================================
(6, 51, '4-20mA'),
(6, 52, '0-600 °C'),
(6, 53, '0.2'),
(6, 54, '24'),
(6, 55, 'Analog'),
(6, 56, 'Siemens'),
(6, 57, 'SITRANS TS');

select e.name as equipment,pd.name as parameter, ep.value as value
 from equipment_parameters ep
join equipments e on e.id = ep.equipment_id
join parameter_definitions pd on pd.id = ep.parameter_id;

-- EquipmentRequestDto
select e.id as equipmentId,pd.id as paramId,e.name as name,
e.code as code, e.system_id as systemId, e.type_id as typeId,
pd.name as paramName,pd.unit as unit,ep.value as value,
e.status as status 
from equipments e
join equipment_types et on et.id = e.type_id
join parameter_definitions pd on pd.type_id = et.id
join equipment_parameters ep on ep.parameter_id = pd.id;



