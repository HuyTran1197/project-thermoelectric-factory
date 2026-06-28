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
-- !!. REPAIR_ORDER
-- ======================
CREATE TABLE repair_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title varchar(255) not null,
    description TEXT,
    status VARCHAR(50) not null,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT not null,
    equipment_id BIGINT NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (equipment_id) REFERENCES equipments(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ======================
-- 17. WORK_ORDERS
-- ======================
CREATE TABLE work_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    request_id BIGINT,
    created_by BIGINT,
    status VARCHAR(50) DEFAULT 'ASSIGNED',
    material_status VARCHAR(50) DEFAULT 'ISSUANCE_NOT_YET_REQUESTED',
    start_date DATETIME,
    end_date DATETIME,
    FOREIGN KEY (request_id) REFERENCES repair_order(id),
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
    released BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(work_order_id, material_id),
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
    released BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(work_order_id, material_id),
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

-- ======================
-- 1. DEPARTMENTS
-- ======================
INSERT INTO departments (name, type) VALUES
('Phòng Nhân sự', 'NHAN_SU'),
('Phân xưởng vận hành', 'VAN_HANH'),
('Phân xưởng sửa chữa', 'SUA_CHUA'),
('Phòng kế toán', 'KE_TOAN'),
('Phòng kế hoạch vật tư', 'VAT_TU'),
('Phòng kế hoạch vật tư', 'CCDC');

-- ======================
-- 2. ROLES (đủ 9 theo bảng kế hoạch / role.js)
-- ======================
INSERT INTO roles (name) VALUES
('Admin'),
('Nhân sự'),
('Quản đốc vận hành'),
('Quản đốc sửa chữa'),
('Tổ trưởng'),
('Thủ kho vật tư'),
('Thủ kho CCDC'),
('Trưởng ca'),
('Trưởng kíp');

-- ======================
-- 3. POSITIONS
-- ======================
INSERT INTO positions (name) VALUES
('Quản trị hệ thống'),
('Nhân viên nhân sự'),
('Quản đốc'),
('Tổ trưởng'),
('Thủ kho'),
('Trưởng ca'),
('Trưởng kíp');

-- ======================
-- 4. EMPLOYEES (9 nhân viên, ứng với 9 role)
-- ======================
INSERT INTO employees (full_name, department_id, position_id) VALUES
('Quản Trị Viên',     1, 1),  -- 1 Admin
('Nguyễn Văn Nam',    1, 2),  -- 2 Nhân sự
('Trần Minh Huy',     2, 3),  -- 3 Quản đốc vận hành
('Phạm Văn Thái',     3, 3),  -- 4 Quản đốc sửa chữa
('Đỗ Văn Tổ',         3, 4),  -- 5 Tổ trưởng
('Lê Văn Thiện',      4, 5),  -- 6 Thủ kho vật tư
('Hoàng Thị Lan',     5, 5),  -- 7 Thủ kho CCDC
('Vũ Văn Ca',         2, 6),  -- 8 Trưởng ca
('Bùi Văn Kíp',       2, 7);  -- 9 Trưởng kíp

-- ======================
-- 5. USERS
-- (password mã hoá BCrypt của "123456") !!!!
-- ======================
INSERT INTO users (username, password, employee_id) VALUES
('admin', '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 1),
('namnv',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 2),
('huytm',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 3),
('thaipv',  '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 4),
('todv',    '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 5),
('thienlv', '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 6),
('lanht',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 7),
('cavv',    '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 8),
('kipbv',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 9);

-- ======================
-- 6. USER_ROLES (mỗi user 1 role tương ứng)
-- ======================
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin   - Admin
(2, 2), -- nam     - Nhân sự
(3, 3), -- huy     - Quản đốc vận hành
(4, 4), -- thai    - Quản đốc sửa chữa
(5, 5), -- to      - Tổ trưởng
(6, 6), -- thien   - Thủ kho vật tư
(7, 7), -- lan     - Thủ kho CCDC
(8, 8), -- ca      - Trưởng ca
(9, 9); -- kip     - Trưởng kíp

-- ======================
-- 7. SYSTEMS
-- ======================
INSERT INTO systems (name, description) VALUES
('Hệ thống cấp nước', 'Hệ thống cấp nước tuần hoàn cho lò hơi'),
('Hệ thống khí nén', 'Hệ thống cung cấp khí nén cho thiết bị điều khiển'),
('Hệ thống điện', 'Hệ thống cung cấp và phân phối điện'),
('Hệ thống đo lường', 'Hệ thống các thiết bị đo lường thông số vận hành'),
('Hệ thống lò hơi', 'Hệ thống sinh hơi chính của nhà máy');

-- ======================
-- 8. DOMAINS
-- ======================
INSERT INTO domains (name) VALUES
('Cơ khí'),
('Điện'),
('CI'),
('Đo lường điều khiển'),
('Van đường ống'),
('Tự động hoá');

-- ======================
-- 9. EQUIPMENT_TYPES
-- (domain_id tham chiếu domains vừa tạo)
-- ======================
INSERT INTO equipment_types (name, domain_id) VALUES
('Bơm', 1),
('Quạt', 1),
('Động cơ', 2),
('Van', 4),
('Thiết bị đo', 3);

-- ======================
-- 10. EQUIPMENTS
-- ======================
INSERT INTO equipments (name, code, system_id, type_id, status) VALUES
('Bơm cấp nước thô', 'KKS-0001', 1, 1, 'ACTIVE'),
('Quạt gió chính', 'KKS-0002', 5, 2, 'ACTIVE'),
('Động cơ bơm nước thô', 'KKS-0003', 1, 3, 'ACTIVE'),
('Van xả đáy lò hơi', 'KKS-0004', 5, 4, 'ACTIVE'),
('Đồng hồ đo áp suất dầu', 'KKS-0005', 2, 5, 'CLOSING');

-- ======================
-- PARAMETER_DEFINITIONS (đầy đủ cho 5 types)
-- ======================

-- type_id = 1 (Bơm) -> id 1-10
INSERT INTO parameter_definitions (name, unit, type_id) VALUES
('Lưu lượng', 'm3/h', 1),
('Cột áp', 'm', 1),
('Công suất', 'kW', 1),
('Tốc độ quay', 'vòng/phút', 1),
('Hiệu suất', '%', 1),
('Đường kính ống hút', 'mm', 1),
('Đường kính ống xả', 'mm', 1),
('Hãng sản xuất', NULL, 1),
('Model', NULL, 1),
('Năm sản xuất', NULL, 1);

-- type_id = 2 (Quạt) -> id 11-18
INSERT INTO parameter_definitions (name, unit, type_id) VALUES
('Lưu lượng gió', 'm3/h', 2),
('Áp suất', 'Pa', 2),
('Công suất', 'kW', 2),
('Tốc độ quay', 'vòng/phút', 2),
('Hiệu suất', '%', 2),
('Đường kính cánh', 'mm', 2),
('Hãng sản xuất', NULL, 2),
('Model', NULL, 2);

-- type_id = 3 (Động cơ) -> id 19-28
INSERT INTO parameter_definitions (name, unit, type_id) VALUES
('Công suất', 'kW', 3),
('Điện áp', 'V', 3),
('Dòng điện', 'A', 3),
('Tần số', 'Hz', 3),
('Tốc độ quay', 'vòng/phút', 3),
('Hiệu suất', '%', 3),
('Hệ số công suất', 'cosφ', 3),
('Cấp cách điện', NULL, 3),
('Hãng sản xuất', NULL, 3),
('Model', NULL, 3);

-- type_id = 4 (Van) -> id 29-35
INSERT INTO parameter_definitions (name, unit, type_id) VALUES
('Đường kính danh nghĩa', 'mm', 4),
('Áp suất làm việc', 'bar', 4),
('Nhiệt độ làm việc', '°C', 4),
('Loại van', NULL, 4),
('Vật liệu', NULL, 4),
('Kiểu kết nối', NULL, 4),
('Hãng sản xuất', NULL, 4);

-- type_id = 5 (Thiết bị đo) -> id 36-42
INSERT INTO parameter_definitions (name, unit, type_id) VALUES
('Loại tín hiệu', NULL, 5),
('Dải đo', NULL, 5),
('Độ chính xác', '%', 5),
('Nguồn cấp', 'V', 5),
('Tín hiệu đầu ra', NULL, 5),
('Hãng sản xuất', NULL, 5),
('Model', NULL, 5);


-- ======================
-- EQUIPMENT_PARAMETERS (giá trị cho cả 5 equipments, khớp đúng type)
-- ======================

-- equipment_id = 1 (Bơm cấp nước thô, type 1) -> parameter_id 1-10
INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES
(1, 1, '850'),
(1, 2, '180'),
(1, 3, '3200'),
(1, 4, '2980'),
(1, 5, '89'),
(1, 6, '250'),
(1, 7, '200'),
(1, 8, 'KSB'),
(1, 9, 'HGC-450'),
(1, 10, '2020');

-- equipment_id = 2 (Quạt gió chính, type 2) -> parameter_id 11-18
INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES
(2, 11, '120000'),
(2, 12, '3500'),
(2, 13, '450'),
(2, 14, '1480'),
(2, 15, '91'),
(2, 16, '2200'),
(2, 17, 'Howden'),
(2, 18, 'FD-4500');

-- equipment_id = 3 (Động cơ bơm nước thô, type 3) -> parameter_id 19-28
INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES
(3, 19, '315'),
(3, 20, '6000'),
(3, 21, '38'),
(3, 22, '50'),
(3, 23, '2980'),
(3, 24, '95'),
(3, 25, '0.89'),
(3, 26, 'F'),
(3, 27, 'ABB'),
(3, 28, 'MOTOR-315');

-- equipment_id = 4 (Van xả đáy lò hơi, type 4) -> parameter_id 29-35
INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES
(4, 29, '150'),
(4, 30, '160'),
(4, 31, '450'),
(4, 32, 'Van cầu'),
(4, 33, 'Thép hợp kim'),
(4, 34, 'Mặt bích'),
(4, 35, 'Velan');

-- equipment_id = 5 (Đồng hồ đo áp suất dầu, type 5) -> parameter_id 36-42
INSERT INTO equipment_parameters (equipment_id, parameter_id, value) VALUES
(5, 36, '4-20mA'),
(5, 37, '0-25 bar'),
(5, 38, '0.5'),
(5, 39, '24'),
(5, 40, 'Analog'),
(5, 41, 'Yokogawa'),
(5, 42, 'EJA530A');

-- ======================
-- 13. CONSUMABLE_MATERIALS
-- ======================
INSERT INTO consumable_materials (name, code, unit, location, description) VALUES
('Dầu Bôi Trơn', 'CON-0001', 'Lít', 'Kho A1', 'Dầu bôi trơn dùng cho ổ bi'),
('Khăn Lau', 'CON-0002', 'Cái', 'Kho A1', 'Khăn lau công nghiệp'),
('Mỡ Bôi Trơn', 'CON-0003', 'Kg', 'Kho A2', 'Mỡ bôi trơn chịu nhiệt'),
('Găng Tay', 'CON-0004', 'Cặp', 'Kho A2', 'Găng tay bảo hộ lao động'),
('Keo Dán', 'CON-0005', 'Tuýp', 'Kho A3', 'Keo dán công nghiệp');

-- ======================
-- 14. REPLACEMENT_MATERIALS
-- ======================
INSERT INTO replacement_materials (name, code, unit, location, description) VALUES
('Vòng Bi SKF', 'REP-0001', 'Cái', NULL, 'Vòng bi cho động cơ bơm'),
('Dây Curoa', 'REP-0002', 'Cái', NULL, 'Dây curoa truyền động'),
('Phớt Làm Kín', 'REP-0003', 'Cái', NULL, 'Phớt làm kín trục bơm'),
('Bulong M16', 'REP-0004', 'Cái', NULL, 'Bulong lắp ghép thiết bị'),
('Van Một Chiều', 'REP-0005', 'Cái', NULL, 'Van một chiều thay thế');

-- ======================
-- 15. TOOLS
-- ======================
INSERT INTO tools (name, code, type, total_quantity, available_quantity, location, description) VALUES
('Cờ Lê', 'TOOL-0001', 'Dụng cụ cơ khí', 10, 8, 'Kho CCDC 1', 'Cờ lê đa năng'),
('Tua Vít', 'TOOL-0002', 'Dụng cụ cơ khí', 15, 12, 'Kho CCDC 1', 'Bộ tua vít các loại'),
('Máy Khoan', 'TOOL-0003', 'Thiết bị điện', 5, 4, 'Kho CCDC 2', 'Máy khoan cầm tay'),
('Đồng Hồ Vạn Năng', 'TOOL-0004', 'Thiết bị đo', 6, 5, 'Kho CCDC 2', 'Đồng hồ đo điện đa năng'),
('Kích Thuỷ Lực', 'TOOL-0005', 'Dụng cụ nâng hạ', 3, 3, 'Kho CCDC 3', 'Kích thuỷ lực nâng thiết bị');

-- ======================
-- 16. TOOL_BORROWINGS
-- ======================
INSERT INTO tool_borrowings (quantity, status, note, borrow_date, due_date, return_date, tool_id, employee_id) VALUES
(2, 'BORROWED', 'Mượn sửa bơm cấp nước', '2026-06-20 08:00:00', '2026-06-25 17:00:00', NULL, 1, 2),
(1, 'RETURNED', 'Đã trả đúng hạn', '2026-06-15 08:00:00', '2026-06-18 17:00:00', '2026-06-18 16:00:00', 2, 3),
(1, 'BORROWED', 'Mượn khoan lắp đặt thiết bị', '2026-06-22 09:00:00', '2026-06-24 17:00:00', NULL, 3, 4),
(1, 'RETURNED', 'Kiểm tra thông số điện', '2026-06-10 08:00:00', '2026-06-12 17:00:00', '2026-06-12 15:30:00', 4, 5),
(1, 'BORROWED', 'Nâng thiết bị bảo trì', '2026-06-23 08:00:00', '2026-06-26 17:00:00', NULL, 5, 2);


-- ======================
-- REPAIR_ORDER (5 yêu cầu sửa chữa)
-- created_by: users (4=thaipv, 5=todv -> Trưởng ca/kíp tạo yêu cầu)
-- ======================
INSERT INTO repair_order (title, description, status, created_at, created_by, equipment_id) VALUES
('Máy bơm rung bất thường', 'Máy bơm rung mạnh khi vận hành tải cao. Yêu cầu kiểm tra vòng bi và căn chỉnh trục.', 'COMPLETED', '2026-06-15 08:00:00', 4, 1),
('Quạt gió phát tiếng ồn lớn', 'Quạt gió chính phát tiếng ồn bất thường khi chạy tốc độ cao.', 'COMPLETED', '2026-06-18 09:00:00', 5, 2),
('Động cơ bơm quá nhiệt', 'Động cơ bơm nước thô bị nóng bất thường sau 2 giờ vận hành.', 'IN_PROGRESS', '2026-06-22 10:00:00', 4, 3),
('Van xả đáy bị rò rỉ', 'Van xả đáy lò hơi có hiện tượng rò rỉ nhẹ tại mặt bích.', 'IN_PROGRESS', '2026-06-24 14:00:00', 5, 4),
('Đồng hồ đo áp suất sai số', 'Đồng hồ đo áp suất dầu hiển thị sai số lớn so với thực tế.', 'PENDING', '2026-06-26 11:00:00', 4, 5);

-- ======================
-- WORK_ORDERS (4 phiếu công tác - ứng với 4 repair_order đã xử lý)
-- created_by: users (3=huytm Quản đốc sửa chữa)
-- ======================
INSERT INTO work_orders (code, request_id, created_by, status, material_status, start_date, end_date) VALUES
('0001/06/2026', 1, 3, 'COMPLETED', 'ISSUED', '2026-06-15 08:30:00', '2026-06-15 17:00:00'),
('0002/06/2026', 2, 3, 'COMPLETED', 'ISSUED', '2026-06-18 09:30:00', '2026-06-19 16:00:00'),
('0003/06/2026', 3, 3, 'IN_PROGRESS', 'ISSUED', '2026-06-22 10:30:00', NULL),
('0004/06/2026', 4, 3, 'WAITING_FOR_MATERIALS', 'PENDING_ISSUANCE', '2026-06-24 14:30:00', NULL);

-- ======================
-- WORK_ORDER_ASSIGNMENTS (phân công nhân sự cho 4 work_order trên)
-- employee_id: 3=Huy,5=Tổ,6=Thiện,8=Ca,9=Kíp (dùng làm nhân sự thi công)
-- ======================
INSERT INTO work_order_assignments (work_order_id, employee_id, role_in_work) VALUES
(1, 3, 'LANH_DAO_CONG_VIEC'),
(1, 8, 'NHAN_VIEN_LAM_VIEC'),
(2, 5, 'CHI_HUY_TRUC_TIEP'),
(2, 9, 'NHAN_VIEN_LAM_VIEC'),
(3, 3, 'LANH_DAO_CONG_VIEC');

-- ======================
-- TECHNICAL_REPORTS (biên bản đánh giá kỹ thuật)
-- created_by: 3=huytm
-- ======================
INSERT INTO technical_reports (work_order_id, content, created_by, created_at) VALUES
(1, '{"conclusion":"Đã thay vòng bi và căn chỉnh trục, thiết bị vận hành ổn định.","equipmentReports":[{"equipmentId":1,"equipmentCode":"KKS-0001","equipmentName":"Bơm cấp nước thô","damageDescription":"Vòng bi mòn, trục lệch tâm","assessment":"Vòng bi hư hỏng nặng cần thay mới","proposedSolution":"Thay vòng bi SKF và căn chỉnh lại trục"}]}', 3, '2026-06-15 16:00:00'),
(2, '{"conclusion":"Đã thay dây curoa, quạt vận hành êm trở lại.","equipmentReports":[{"equipmentId":2,"equipmentCode":"KKS-0002","equipmentName":"Quạt gió chính","damageDescription":"Dây curoa bị mòn, gây tiếng ồn","assessment":"Dây curoa đến hạn thay thế","proposedSolution":"Thay dây curoa mới"}]}', 3, '2026-06-19 15:00:00'),
(3, '{"conclusion":"Đang tiếp tục kiểm tra hệ thống làm mát động cơ.","equipmentReports":[{"equipmentId":3,"equipmentCode":"KKS-0003","equipmentName":"Động cơ bơm nước thô","damageDescription":"Nhiệt độ tăng cao bất thường","assessment":"Nghi ngờ quạt làm mát hoạt động kém","proposedSolution":"Kiểm tra và vệ sinh quạt làm mát"}]}', 3, '2026-06-22 15:00:00'),
(4, '{"conclusion":"Chờ vật tư để hoàn tất sửa chữa van.","equipmentReports":[{"equipmentId":4,"equipmentCode":"KKS-0004","equipmentName":"Van xả đáy lò hơi","damageDescription":"Rò rỉ tại mặt bích","assessment":"Phớt làm kín bị lão hoá","proposedSolution":"Thay phớt làm kín mới"}]}', 3, '2026-06-24 16:00:00'),
(2, '{"conclusion":"Kiểm tra bổ sung sau 1 tuần vận hành, quạt hoạt động bình thường.","equipmentReports":[{"equipmentId":2,"equipmentCode":"KKS-0002","equipmentName":"Quạt gió chính","damageDescription":"Không phát hiện bất thường","assessment":"Đạt yêu cầu kỹ thuật","proposedSolution":"Không cần xử lý thêm"}]}', 3, '2026-06-26 10:00:00');

-- ======================
-- MAINTENANCE_LOGS (lịch sử sửa chữa thiết bị)
-- ======================
INSERT INTO maintenance_logs (equipment_id, work_order_id, description, date) VALUES
(1, 1, 'Thay vòng bi SKF và căn chỉnh trục bơm cấp nước thô', '2026-06-15 17:00:00'),
(2, 2, 'Thay dây curoa truyền động quạt gió chính', '2026-06-19 16:00:00'),
(3, 3, 'Kiểm tra hệ thống làm mát động cơ bơm nước thô', '2026-06-22 16:00:00'),
(4, 4, 'Phát hiện rò rỉ tại mặt bích van xả đáy, chờ vật tư thay thế', '2026-06-24 17:00:00'),
(5, NULL, 'Bảo trì định kỳ đồng hồ đo áp suất dầu', '2026-06-10 09:00:00');

-- ======================
-- CONSUMABLE_TRANSACTIONS (nhập/xuất vật tư tiêu hao)
-- created_by: 6=thienlv (Thủ kho vật tư)
-- ======================
INSERT INTO consumable_transactions (material_id, type, quantity, created_by, created_at) VALUES
(1, 'IMPORT', 20, 6, '2026-06-01 08:00:00'),
(2, 'IMPORT', 50, 6, '2026-06-01 08:10:00'),
(1, 'EXPORT', 5, 6, '2026-06-15 16:30:00'),
(3, 'IMPORT', 15, 6, '2026-06-10 09:00:00'),
(2, 'EXPORT', 10, 6, '2026-06-19 15:30:00');

-- ======================
-- REPLACEMENT_TRANSACTIONS (nhập/xuất phụ tùng thay thế)
-- ======================
INSERT INTO replacement_transactions (material_id, type, quantity, created_by, created_at) VALUES
(1, 'IMPORT', 10, 6, '2026-06-01 08:30:00'),
(2, 'IMPORT', 8, 6, '2026-06-01 08:40:00'),
(1, 'EXPORT', 2, 6, '2026-06-15 16:35:00'),
(2, 'EXPORT', 1, 6, '2026-06-19 15:35:00'),
(3, 'IMPORT', 12, 6, '2026-06-10 09:30:00');

-- ======================
-- WORK_ORDER_CONSUMABLES
-- ======================
INSERT INTO work_order_consumables (work_order_id, material_id, quantity, released) VALUES
(1, 1, 5, TRUE),
(1, 2, 10, TRUE),
(2, 2, 10, TRUE),
(3, 3, 3, TRUE),
(4, 4, 4, FALSE);

-- ======================
-- WORK_ORDER_REPLACEMENTS
-- ======================
INSERT INTO work_order_replacements (work_order_id, material_id, quantity, released) VALUES
(1, 1, 2, TRUE),
(2, 2, 1, TRUE),
(3, 3, 1, TRUE),
(4, 3, 1, FALSE),
(4, 4, 2, FALSE);