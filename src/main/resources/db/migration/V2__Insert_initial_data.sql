-- Thêm dữ liệu vào bảng permissions
INSERT INTO permissions (name, description) VALUES
('CREATE_DATA', 'Permission to create data'),
('UPDATE_DATA', 'Permission to update data'),
('GET_DATA', 'Permission to get data'),
('DELETE_DATA', 'Permission to delete data');

-- Thêm dữ liệu vào bảng roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Admin role'),
('USER', 'User role'),
('RESTAURANT', 'Restaurant role');

-- Gán permission cho role USER (CREATE_DATA, UPDATE_DATA)
INSERT INTO role_permissions (role_name, permission_name) VALUES
('ADMIN', 'CREATE_DATA'),
('ADMIN', 'UPDATE_DATA'),
('ADMIN', 'GET_DATA'),
('ADMIN', 'DELETE_DATA'),

('USER', 'CREATE_DATA'),
('USER', 'GET_DATA'),
('USER', 'UPDATE_DATA'),

('RESTAURANT', 'CREATE_DATA'),
('RESTAURANT', 'GET_DATA'),
('RESTAURANT', 'UPDATE_DATA');


INSERT INTO restaurant_categories ( id ,name) VALUES
(UUID(), 'Đồ Uống'),
(UUID(), 'Thức Ăn Nhanh'),
(UUID(), 'Món Á-Âu'),
(UUID(), 'Cơm');