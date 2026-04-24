drop database cmms_db;
CREATE DATABASE IF NOT EXISTS cmms_db;
USE cmms_db;

-- ======================
-- 1. DEPARTMENTS (PHÒNG BAN)
-- -> Ví dụ : Phòng Nhân sự, Phân xưởng Vận hành, PX sửa chữa, Phòng kế hoạch vật tư
-- ======================
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(100)
) ENGINE=InnoDB;

-- ======================
-- 2. ROLES (quyền hệ thống) => sử dụng hệ thống scms
-- ví dụ: Admin, Nhân sự, Thủ Kho, Quản đốc vận hành, Trưởng Ca, Trưởng Kíp,  Tổ trưởng sửa chữa, Quản đốc sửa chữa
-- ======================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE
) ENGINE=InnoDB;

-- ======================
-- 3. POSITIONS (chức vụ => Giám đốc, Trưởng phòng, Nhân viên)
-- ======================
CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
) ENGINE=InnoDB;

-- ======================
-- 4. EMPLOYEES (nhân viên)
-- ======================
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    department_id BIGINT,
    position_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (position_id) REFERENCES positions(id)
) ENGINE=InnoDB;

-- ======================
-- 5. USERS (tài khoản)
-- ======================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    employee_id BIGINT UNIQUE, -- 1-1 với employee
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

-- ======================
-- 6. USER_ROLES (một user có thể có nhiều role, một role có thể nhiều user dùng)
-- ======================
CREATE TABLE user_roles (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT unique,
    role_id BIGINT unique,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ======================
-- 7. WORK POSITIONS ( ví trí làm việc trực tiếp # với chức vụ)  => bảng này ý nghĩa để tạo vị trí làm việc khi  tạo work_order_request
--  Ví dụ: Chức vụ : + Quản đốc PXVH có thể làm việc ở vị trí ( Trưởng Ca, Trưởng Kíp, VHV...)
				--   + Tổ Trưởng Tổ Tuabin có thể làm việc vị trí ( Lãnh đạo công việc, chỉ huy trực tiếp, Giám sát an toàn...)
-- ======================
CREATE TABLE work_positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
) ENGINE=InnoDB;

-- ======================
-- 8. EMPLOYEE_WORK_POSITIONS ( lưu các vị trí mà một nhân viên có thể được phép làm việc)
-- ======================
CREATE TABLE employee_work_positions (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT unique,
    work_position_id BIGINT unique,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (work_position_id) REFERENCES work_positions(id)
) ENGINE=InnoDB;

-- ======================
-- 9. SYSTEMS
-- ======================
CREATE TABLE systems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 10. DOMAINS (Lĩnh vực của thiết bị ( Cơ nhiệt, Điện, CI)
-- ======================
CREATE TABLE domains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
) ENGINE=InnoDB;

-- ======================
-- 11. EQUIPMENT TYPES (Bơm, Quạt, Máy nén, Van, Động cơ, Biến áp, Máy cắt, Thiết bị đo nhiệt độ, Role...
-- ======================
CREATE TABLE equipment_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    domain_id BIGINT,
    FOREIGN KEY (domain_id) REFERENCES domains(id)
) ENGINE=InnoDB;

-- ======================
-- 12. EQUIPMENTS ( lưu thông tin chung của một thiết bị
-- ======================
CREATE TABLE equipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(100),
    system_id BIGINT,
    type_id BIGINT,
    status VARCHAR(50),
    FOREIGN KEY (system_id) REFERENCES systems(id),
    FOREIGN KEY (type_id) REFERENCES equipment_types(id)
) ENGINE=InnoDB;

-- ======================
-- 13. PARAMETER DEFINITIONS (Thêm các thông số của từng loại thiết bị cho linh hoạt)
-- Ví dụ: Công suất, Điệp áp, Đường kính ống, dung tích...., lưu lượng bơm...
-- ======================
CREATE TABLE parameter_definitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    unit VARCHAR(50),
    type_id BIGINT,
    FOREIGN KEY (type_id) REFERENCES equipment_types(id)
) ENGINE=InnoDB;

-- ======================
-- 14. EQUIPMENT PARAMETERS ( lưu thông số của một thiết bị cụ thể)
-- > mỗi một thiết bị có n thống số thì trong bảng này có dòng.)
-- ======================
CREATE TABLE equipment_parameters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_id BIGINT,
    parameter_id BIGINT,
    value TEXT,
    FOREIGN KEY (equipment_id) REFERENCES equipments(id) ON DELETE CASCADE,
    FOREIGN KEY (parameter_id) REFERENCES parameter_definitions(id)
) ENGINE=InnoDB;

-- ======================
-- 15. REQUESTS ( Khi có một hoặc nhiều thiết bị bị hỏng thì Trưởng CA/ Trưởng Kíp => tạo một yêu cầu sửa chữa
-- ======================
CREATE TABLE requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    created_by BIGINT,
    status VARCHAR(50) DEFAULT 'NEW',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 16. REQUEST_EQUIPMENTS ( 1 Yêu cầu  sẽ có nhiều thiết bị => tạo bảng detail 
-- ======================
CREATE TABLE request_equipments (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT unique,
    equipment_id BIGINT unique,
    FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipments(id)
) ENGINE=InnoDB;

-- ======================
-- 17. WORK ORDERS ( Sau khi có Request từ PXVH => bên sửa chữa sẽ nhận (Tổ trường/ quản đốc phân xưởng sửa chữa 
-- => sẽ xem  và tạo một lịch để sửa chữa
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
-- 18. WORK ORDER ASSIGNMENTS ( lịch sửa chữa sẽ có nhiều nhân viên nên tạo bảng detail) => phân công nhân sự làm
-- ======================
CREATE TABLE work_order_assignments (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT unique,
    employee_id BIGINT unique,
    role_in_work VARCHAR(100),
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

-- ======================
-- 19. TECHNICAL REPORTS ( trong quá trình công tác các thiết bị có thể bị hỏng nên cần làm biên bản đánh giá kỹ thuật.
-- ======================
CREATE TABLE technical_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT,
    content TEXT,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ======================
-- 20. MAINTENANCE LOGS ( nghi lại lịch sử sửa chữa của thiết bị sau khi sửa)
-- ======================
CREATE TABLE maintenance_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_id BIGINT,
    work_order_id BIGINT,
    description TEXT,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipments(id),
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
) ENGINE=InnoDB;

-- ======================
-- 21. VẬT TƯ TIÊU HAO
CREATE TABLE consumable_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(100),
    unit VARCHAR(50),
    location VARCHAR(255),
    description TEXT
)  ENGINE=InnoDB;
-- =======================================
-- 22. XUẤT/NHẬP VẬT TƯ TIÊU HAO
CREATE TABLE consumable_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT,
    type VARCHAR(50), -- IMPORT / EXPORT
    quantity INT,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES consumable_materials(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;
-- ================================
-- 23 . VẬT TƯ THAY THẾ
CREATE TABLE replacement_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(100),
    unit VARCHAR(50),
    location VARCHAR(255),
    description TEXT
) ENGINE=InnoDB;

-- =======================================
-- 24. XUẤT/ NHẬP VẬT TƯ THAY THẾ
-- ======================================
CREATE TABLE replacement_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT,
    type VARCHAR(50), -- IMPORT / INSTALL / REMOVE
    quantity INT,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES replacement_materials(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- =================================================
-- 25. BẢNG CẤP VẬT TƯ TIÊU HAO CHO TỪNG PHIẾU SỬA CHỮA
-- =================================================
CREATE TABLE work_order_consumables (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT,
    material_id BIGINT,
    quantity INT,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES consumable_materials(id)
) ENGINE=InnoDB;
-- ======================================
-- 26.-- BẢNG CẤP VẬT TƯ THAY THẾ CHO TỪNG PHIẾU SỬA CHỮA
CREATE TABLE work_order_replacements (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT,
    material_id BIGINT,
    quantity INT,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES replacement_materials(id)
) ENGINE=InnoDB;

-- 26. TOOLS (Thông tin công cụ dụng cụ trong kho)
-- ======================
CREATE TABLE tools (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(100),
    type VARCHAR(100),
    total_quantity INT DEFAULT 0, -- tổng số CCDC của kho
    available_quantity INT DEFAULT 0, -- số lượng còn trong kho hiện tại để cho mượn
    location VARCHAR(255),  -- Vị trí trong kho
    description TEXT
) ENGINE=InnoDB;

-- ======================
-- 27. TOOL BORROWINGS (cho mượn và trả công cụ dụng cụ)
-- ======================
CREATE TABLE tool_borrowings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tool_id BIGINT,
    employee_id BIGINT, -- người mượn/trả
    quantity INT,
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- ngày mượn
    due_date DATETIME, -- hạn phải trả
    return_date DATETIME, -- ngày trả
    status VARCHAR(50),
    note TEXT,
    FOREIGN KEY (tool_id) REFERENCES tools(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;