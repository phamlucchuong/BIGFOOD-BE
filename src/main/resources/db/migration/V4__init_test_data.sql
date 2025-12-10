insert into users (id, name, password, email, phone)
values (
        '39c1aa95-a8b0-436c-9fef-128ab52deccc',
        'admin',
        '$2y$10$rDcdwXRIWUMUGHI/YnE.m.mifyw.i/cF3OdeiM5h5JQqvVCIECFeO',
        'admin@gmail.com',
        '0377948504'
    ),
    (
        'cbd3d4f7-f91c-4a8a-8807-f2e65fa94db8',
        'user1',
        '$2y$10$j0yO4nS4A8atRQXjOW6HAOhqIR9p/t2PWmNMAWUZ/lqIMkNmZbEfm',
        'user1@gmail.com',
        '0377948504'
    ),
    (
        'f8db04aa-7760-4206-958f-6a42f2120bb5',
        'user2',
        '$2y$10$I9R.I/MPzvtYz5fHSwNKnexB5Ntd8rh.f1yMUssuTXEEnNb21ffNC',
        'user2@gmail.com',
        '0377948504'
    ),
    (
        'd1cc08be-5e00-4a85-a108-3adabfaa8d49',
        'restaurant1',
        '$2y$10$ByQGqsRq4/yhtlA/WeD9w.NqbJ.7T0YKKKiTuZeC..W8BS2UXyZRS',
        'restaurant1@gmail.com',
        '0377948504'
    ),
    (
        '299e093c-273a-4c6e-b005-aca5ce5d80dd',
        'restaurant2',
        '$2y$10$vaB4vc/ma47T5KcWCRJjneaeypCF8qNY6pxyhFtImGs78HfiiwbZe',
        'restaurant2@gmail.com',
        '0377948504'
    );

    
insert into user_roles (user_id, role_name)
values ('39c1aa95-a8b0-436c-9fef-128ab52deccc', 'ADMIN'),
    ('cbd3d4f7-f91c-4a8a-8807-f2e65fa94db8', 'USER'),
    ('f8db04aa-7760-4206-958f-6a42f2120bb5', 'USER'),
    ('d1cc08be-5e00-4a85-a108-3adabfaa8d49', 'RESTAURANT'),
    ('299e093c-273a-4c6e-b005-aca5ce5d80dd', 'RESTAURANT');

insert into restaurants (user_id, restaurant_name, address, latitude, longitude, banner_id, license_id, is_approved)
values ('d1cc08be-5e00-4a85-a108-3adabfaa8d49','Anh Thư Cafe and Tea', '450-451 Lê Văn Việt, phường Tăng Nhơn Phú, TP.HCM.', 10.845696, 106.794172, 'banner1.jpg', 'LICENSE123', true),
('299e093c-273a-4c6e-b005-aca5ce5d80dd', 'Lẩu gà Yến Trà', 'Kenbar Coffee And Tea', 10.84025325510041, 106.78136712955003, 'banner2.jpg', 'LICENSE456', true);

insert into food_categories (id, name, icon_index, is_deleted, restaurant_id)
values ('6e7574d2-8cb3-491f-a165-5e8af23ca420', 'Cà phê', 1, false, 'd1cc08be-5e00-4a85-a108-3adabfaa8d49'),
    ('b1f4c8e3-3c4d-4f5e-9f6a-7b8c9d0e1f2a', 'Trà', 2, false, 'd1cc08be-5e00-4a85-a108-3adabfaa8d49'),
    ('9f819529-6ab2-4004-849a-f4a72541c22e', 'Lẩu gà', 2, false, '299e093c-273a-4c6e-b005-aca5ce5d80dd'),
    ('c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f', 'Gỏi gà', 3, false, '299e093c-273a-4c6e-b005-aca5ce5d80dd');

insert into foods (id, name, description, image_id, price, count, is_deleted, is_available, food_category_id)
values ('f1e2d3c4-b5a6-7890-1234-56789abcdef0', 'Cà phê đen đá', 'Cà phê đen nguyên chất và đá', 'ca_phe_sua_da.jpg', 15000.00, 50, false, true, '6e7574d2-8cb3-491f-a165-5e8af23ca420'),
    ('775b1e86-a750-4a71-ab3c-73d1962fd8ea', 'Cà phê sữa đá', 'Cà phê pha với sữa đặc và đá', 'ca_phe_sua_da.jpg', 20000.00, 50, false, true, '6e7574d2-8cb3-491f-a165-5e8af23ca420'),
    ('a1b2c3d4-e5f6-7890-1234-56789abcdef1', 'Trà đào cam sả', 'Trà đào kết hợp với cam và sả tươi', 'tra_dao_cam_sa.jpg', 30000.00, 40, false, true, 'b1f4c8e3-3c4d-4f5e-9f6a-7b8c9d0e1f2a'),
    ('b2c3d4e5-f6a7-8901-2345-6789abcdef02', 'Lẩu gà thập cẩm', 'Lẩu gà với nhiều loại rau và nấm', 'lau_ga_thap_cam.jpg', 150000.00, 20, false, true, '9f819529-6ab2-4004-849a-f4a72541c22e'),
    ('d3b7ed44-fc7c-4df5-bb49-77e675f8d40a', 'Lẩu gà lá giang', 'Lẩu gà lá giang và nấm', 'lau_ga_thap_cam.jpg', 150000.00, 20, false, true, '9f819529-6ab2-4004-849a-f4a72541c22e'),
    ('ac382ece-f07a-4527-badb-48586a27dbba', 'Lẩu gà lá é', 'Lẩu gà với lá é và nấm', 'lau_ga_thap_cam.jpg', 150000.00, 20, false, true, '9f819529-6ab2-4004-849a-f4a72541c22e'),
    ('c3d4e5f6-a7b8-9012-3456-789abcdef023', 'Gỏi gà xé phay', 'Gỏi gà trộn với rau sống và nước mắm chua ngọt', 'goi_ga_xe_phay.jpg', 60000.00, 30, false, true, 'c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f'),
    ('47a5b3e6-efbc-4e70-9348-234b70227bcd', 'Gỏi gà măng cụt', 'Gỏi gà trộn với rau thơm và măng cụt kèm nước mắm chua ngọt', 'goi_ga_xe_phay.jpg', 60000.00, 30, false, true, 'c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f'),
    ('eb52b47b-f06a-473c-8cd8-81b5730032c3', 'Gỏi gà sốt cay', 'Gỏi gà trộn với rau sống và sốt cay độc quyền', 'goi_ga_xe_phay.jpg', 60000.00, 30, false, true, 'c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f');