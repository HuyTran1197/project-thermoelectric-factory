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
-- (password mã hoá BCrypt của "123456")
-- ======================
INSERT INTO users (username, password, employee_id) VALUES
('admin', '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 1),
('nam',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 2),
('huy',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 3),
('thai',  '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 4),
('to',    '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 5),
('thien', '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 6),
('lan',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 7),
('ca',    '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 8),
('kip',   '$2a$10$ZLGZXd9htze4RUReEWy0JOtJV0BFA69Csn1LCtxQT3f4H/Ktqv9Qu', 9);

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
('Bơm cấp nước thô', 'KKS-0001', 1, 1, 'DANG_VAN_HANH'),
('Quạt gió chính', 'KKS-0002', 5, 2, 'DANG_VAN_HANH'),
('Động cơ bơm nước thô', 'KKS-0003', 1, 3, 'DANG_VAN_HANH'),
('Van xả đáy lò hơi', 'KKS-0004', 5, 4, 'DANG_VAN_HANH'),
('Đồng hồ đo áp suất dầu', 'KKS-0005', 2, 5, 'DANG_VAN_HANH');

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
('Vòng Bi SKF', 'REP-0001', 'Cái', 'Kho B1', 'Vòng bi cho động cơ bơm'),
('Dây Curoa', 'REP-0002', 'Cái', 'Kho B1', 'Dây curoa truyền động'),
('Phớt Làm Kín', 'REP-0003', 'Cái', 'Kho B2', 'Phớt làm kín trục bơm'),
('Bulong M16', 'REP-0004', 'Cái', 'Kho B2', 'Bulong lắp ghép thiết bị'),
('Van Một Chiều', 'REP-0005', 'Cái', 'Kho B3', 'Van một chiều thay thế');

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
(2, 'DANG_MUON', 'Mượn sửa bơm cấp nước', '2026-06-20 08:00:00', '2026-06-25 17:00:00', NULL, 1, 2),
(1, 'DA_TRA', 'Đã trả đúng hạn', '2026-06-15 08:00:00', '2026-06-18 17:00:00', '2026-06-18 16:00:00', 2, 3),
(1, 'DANG_MUON', 'Mượn khoan lắp đặt thiết bị', '2026-06-22 09:00:00', '2026-06-24 17:00:00', NULL, 3, 4),
(1, 'DA_TRA', 'Kiểm tra thông số điện', '2026-06-10 08:00:00', '2026-06-12 17:00:00', '2026-06-12 15:30:00', 4, 5),
(1, 'DANG_MUON', 'Nâng thiết bị bảo trì', '2026-06-23 08:00:00', '2026-06-26 17:00:00', NULL, 5, 2);





-- KHÔNG DÙNG LỆNH SQL NÀY ĐỂ INSERT -> DỮ LIỆU CŨ
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


-- 1. Xoá constraint cũ đang trỏ sai bảng
ALTER TABLE work_orders DROP FOREIGN KEY work_orders_ibfk_1;

-- 2. Thêm lại constraint trỏ đúng về bảng repair_order
ALTER TABLE work_orders
ADD CONSTRAINT work_orders_ibfk_1
FOREIGN KEY (request_id) REFERENCES repair_order(id);
SHOW CREATE TABLE work_orders;

-- 1. Xoá foreign key constraint trên cột này trước
ALTER TABLE work_orders DROP FOREIGN KEY FK734xdi12pbbon5yox0svek4xo;

-- 2. Xoá luôn cột (vì cũng không dùng đến)
ALTER TABLE work_orders DROP COLUMN repair_order_id;

-- tắt safe mode để update status
SET SQL_SAFE_UPDATES = 0;

UPDATE equipments SET status = 'DANG_VAN_HANH' WHERE status = 'Đang vận hành';
UPDATE equipments SET status = 'DANG_SUA_CHUA' WHERE status = 'Đang sửa chửa';
UPDATE equipments SET status = 'DANG_DONG' WHERE status = 'Đang đóng';

-- bật lại safe mode
SET SQL_SAFE_UPDATES = 1;

select tr.id as id,wo.code as workOrderCode, e.code as equipmentCode, e.name as equipmentName,
tr.created_at as createdAt
from technical_reports tr
join work_orders wo on wo.id = tr.work_order_id
join repair_order ro on ro.id = wo.request_id
join equipments e on e.id = ro.equipment_id
where searchWorkOrderCode



