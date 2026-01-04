-- Auto-generated SQL INSERT statements for restaurants
-- Generated: 2026-01-04 11:50:42
-- ========================================

-- STEP 1: Tạo tài khoản Users cho nhà hàng
insert into users (id, name, password, email, phone)
values
    (
        '4d72d34b-aabd-4aa6-8027-a8a74e04ca31',
        'restaurant1',
        '$2y$10$rDcdwXRIWUMUGHI/YnE.m.mifyw.i/cF3OdeiM5h5JQqvVCIECFeO',
        'restaurant1@restaurants.com',
        '0377948500'
    ),
    (
        '8017074b-eac3-4f26-a7f2-68ff080164d9',
        'restaurant2',
        '$2y$10$rDcdwXRIWUMUGHI/YnE.m.mifyw.i/cF3OdeiM5h5JQqvVCIECFeO',
        'restaurant2@restaurants.com',
        '0377948501'
    );

-- STEP 2: Gán RESTAURANT role cho users
insert into user_roles (user_id, role_name)
values
    ('4d72d34b-aabd-4aa6-8027-a8a74e04ca31', 'RESTAURANT'),
    ('8017074b-eac3-4f26-a7f2-68ff080164d9', 'RESTAURANT');

-- STEP 3: Tạo nhà hàng
insert into restaurants (user_id, restaurant_name, address, latitude, longitude, location, banner_id, license_id, is_approved)
values
('4d72d34b-aabd-4aa6-8027-a8a74e04ca31',
'Cơm Gà Đệ Nhất - Đỗ Xuân Hợp',
'139 Đỗ Xuân Hợp, P. Phước Long B, Thành Phố Thủ Đức, TP. HCM',
10.580494, 106.846661,
ST_SRID(POINT(106.846661, 10.580494), 4326),
'bigfood/images/banners/yafncljungpcojx7zi8p', 'bigfood/images/lisense/y1ukrdeq0ohtxhlxpfts', true),
('8017074b-eac3-4f26-a7f2-68ff080164d9',
'Cơm Thố Bụi - 12 Nguyễn Sơn Hà',
'12 Nguyễn Sơn Hà, P. 5, Quận 3, TP. HCM',
10.925798, 106.819573,
ST_SRID(POINT(106.819573, 10.925798), 4326),
'bigfood/images/banners/qkiliibkh0t5hmiad2l6', 'bigfood/images/lisense/y1ukrdeq0ohtxhlxpfts', true);

-- STEP 4: Gán categories cho nhà hàng
insert into restaurant_has_categories (restaurant_id, restaurant_category_id)
values
('4d72d34b-aabd-4aa6-8027-a8a74e04ca31', '5550696f-8601-42b2-91b8-a11b601e32d3'),
('8017074b-eac3-4f26-a7f2-68ff080164d9', '5550696f-8601-42b2-91b8-a11b601e32d3');

-- STEP 5: Tạo food categories
insert into food_categories (id, name, icon_index, is_deleted, restaurant_id)
values
('093810e3-61a6-4071-9244-56f8147be8e8', 'COMBO CƠM GÀ XỐT TỰ CHỌN', 1, false, '4d72d34b-aabd-4aa6-8027-a8a74e04ca31'),
('1ed53792-1bb6-4e88-a0e3-9e222ea739ed', 'COMBO ĐỘC QUYỀN SHOPEEFOOD', 2, false, '4d72d34b-aabd-4aa6-8027-a8a74e04ca31'),
('bf7b4ae7-c801-4982-9b1a-9caf00c022d4', 'CƠM GÀ TIẾT KIỆM', 3, false, '4d72d34b-aabd-4aa6-8027-a8a74e04ca31'),
('37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977', 'CƠM TRỨNG', 4, false, '4d72d34b-aabd-4aa6-8027-a8a74e04ca31'),
('cb7bfcfc-3d39-4ebe-a467-271e8ca09e22', 'CƠM THỐ HEO GIÒN', 5, false, '8017074b-eac3-4f26-a7f2-68ff080164d9'),
('ab9a3971-c03d-4790-8381-8ba382a91452', 'CANH NGON', 6, false, '8017074b-eac3-4f26-a7f2-68ff080164d9'),
('c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf', 'MÓN THÊM', 7, false, '8017074b-eac3-4f26-a7f2-68ff080164d9');

-- STEP 6: Tạo foods
insert into foods (id, name, description, image_id, sold, is_deleted, is_available, food_category_id)
values
('5a907458-a7b7-43ad-a8b4-671712505eb2', 'Combo Cơm Gà Góc 4 Đặc Biệt', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/sy1gsigsepkrwxhhg2ay', 0, false, true, '093810e3-61a6-4071-9244-56f8147be8e8'),
('7d6ff5b5-ab40-4657-8ec7-814a05e7a252', 'Combo Đệ Nhất Khao 29%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kejtdc0dlquihfqzad46', 0, false, true, '093810e3-61a6-4071-9244-56f8147be8e8'),
('eab0745f-c104-497b-85c1-93fd3846a085', 'Combo Cơm Đùi gà GÓC 4 + 1 món tùy chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/lwcderph7piwu5paqary', 0, false, true, '093810e3-61a6-4071-9244-56f8147be8e8'),
('45d4970f-4bf1-4050-a58b-b9b6b05ee3c5', 'Combo Cơm Má Gà Chiên & 1 Món Tùy Chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/meohwja2iqnulzwzualw', 0, false, true, '093810e3-61a6-4071-9244-56f8147be8e8'),
('a68acf05-f142-4ee4-a8f4-fe8994cbb710', 'Combo Gà Vừa Khao 32%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kvzheuxr2vukpucmuahy', 0, false, true, '093810e3-61a6-4071-9244-56f8147be8e8'),
('ce9a96cf-1026-447b-aab9-902e94d2af8b', 'Combo Cơm Gà Góc 4 Đặc Biệt', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/sy1gsigsepkrwxhhg2ay', 0, false, true, '1ed53792-1bb6-4e88-a0e3-9e222ea739ed'),
('6cebe17f-10ff-4791-9b80-a376be1a5b3c', 'Combo Đệ Nhất Khao 29%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kejtdc0dlquihfqzad46', 0, false, true, '1ed53792-1bb6-4e88-a0e3-9e222ea739ed'),
('1dbe9ddd-af98-4f45-9c9b-7875ffa55e38', 'Combo Cơm Đùi gà GÓC 4 + 1 món tùy chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/lwcderph7piwu5paqary', 0, false, true, '1ed53792-1bb6-4e88-a0e3-9e222ea739ed'),
('3d40abaf-9564-4f9e-8c70-e284c6bb8975', 'Combo Cơm Má Gà Chiên & 1 Món Tùy Chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/meohwja2iqnulzwzualw', 0, false, true, '1ed53792-1bb6-4e88-a0e3-9e222ea739ed'),
('3f937cce-818e-47cd-bcc4-bcbd1732f181', 'Combo Gà Vừa Khao 32%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kvzheuxr2vukpucmuahy', 0, false, true, '1ed53792-1bb6-4e88-a0e3-9e222ea739ed'),
('06bb59e2-e7f2-4dc8-a2aa-189954e1ca3f', 'Combo Cơm Gà Góc 4 Đặc Biệt', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/sy1gsigsepkrwxhhg2ay', 0, false, true, 'bf7b4ae7-c801-4982-9b1a-9caf00c022d4'),
('0e2d2b47-1a4d-4b36-ba68-a6b20c6d95cd', 'Combo Đệ Nhất Khao 29%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kejtdc0dlquihfqzad46', 0, false, true, 'bf7b4ae7-c801-4982-9b1a-9caf00c022d4'),
('756f725c-07a4-4688-a006-d1b92ebbbf42', 'Combo Cơm Đùi gà GÓC 4 + 1 món tùy chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/lwcderph7piwu5paqary', 0, false, true, 'bf7b4ae7-c801-4982-9b1a-9caf00c022d4'),
('b41d572c-1d71-45b6-8400-da1ae4ed90e1', 'Combo Cơm Má Gà Chiên & 1 Món Tùy Chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/meohwja2iqnulzwzualw', 0, false, true, 'bf7b4ae7-c801-4982-9b1a-9caf00c022d4'),
('6314684f-3ef8-4c57-b13e-4e433ee2a592', 'Combo Gà Vừa Khao 32%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kvzheuxr2vukpucmuahy', 0, false, true, 'bf7b4ae7-c801-4982-9b1a-9caf00c022d4'),
('cc128a9b-2ec8-4285-848a-467fc25dfd67', 'Combo Cơm Gà Góc 4 Đặc Biệt', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/sy1gsigsepkrwxhhg2ay', 0, false, true, '37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977'),
('5110535f-7a5d-4f89-b888-36d559808f3d', 'Combo Đệ Nhất Khao 29%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kejtdc0dlquihfqzad46', 0, false, true, '37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977'),
('f2fd391e-d32a-4308-8cec-aa4e8ff7fdd0', 'Combo Cơm Đùi gà GÓC 4 + 1 món tùy chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/lwcderph7piwu5paqary', 0, false, true, '37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977'),
('1e1470a5-b85f-46e8-aabe-d04f311e8bf5', 'Combo Cơm Má Gà Chiên & 1 Món Tùy Chọn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/meohwja2iqnulzwzualw', 0, false, true, '37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977'),
('c4ba35d3-faf5-4aee-be79-e95e2261ba47', 'Combo Gà Vừa Khao 32%', 'Món ăn ngon, tươi, hấp dẫn', 'bigfood/images/foods/kvzheuxr2vukpucmuahy', 0, false, true, '37d9a6a7-ceb4-40fe-99ed-4dcf0c97c977'),
('fb2ead4b-88c5-4822-8aa5-01c6b6b006eb', 'Cơm Thố Heo Giòn Teriyaki', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/oabj66o7f5ernhabna0k', 0, false, true, 'cb7bfcfc-3d39-4ebe-a467-271e8ca09e22'),
('65e5fff0-aa82-4f8a-9130-6d9d1178a1b0', 'Cơm Thố Xá Xíu', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/lmo9bwkabcism38guqzp', 0, false, true, 'cb7bfcfc-3d39-4ebe-a467-271e8ca09e22'),
('9c222c02-24b1-42cc-9db0-b6a68c4479a6', 'Cơm Thố Gà Nướng BBQ Hàn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/hvhxvlrfhgaqzpgxi7gt', 0, false, true, 'cb7bfcfc-3d39-4ebe-a467-271e8ca09e22'),
('1f2ccf73-38f0-4437-90c3-e65ad093bf7d', 'Cơm Thố Xúc Xích Đức + Ốp La', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/z3cifytfka2zhwtdq0kx', 0, false, true, 'cb7bfcfc-3d39-4ebe-a467-271e8ca09e22'),
('37a87157-50ab-4e4a-9691-512fd9272094', 'Cơm Thố Gà Nướng + Xá Xíu', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/rfuqwqoxy5imrm1snswj', 0, false, true, 'cb7bfcfc-3d39-4ebe-a467-271e8ca09e22'),
('96bf4625-2a01-48f2-8f5c-687ca73639b3', 'Cơm Thố Heo Giòn Teriyaki', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/oabj66o7f5ernhabna0k', 0, false, true, 'ab9a3971-c03d-4790-8381-8ba382a91452'),
('9841b085-b726-4090-9651-c93e80ecb5b7', 'Cơm Thố Xá Xíu', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/lmo9bwkabcism38guqzp', 0, false, true, 'ab9a3971-c03d-4790-8381-8ba382a91452'),
('6de27eda-20c9-41fa-a573-ea765b330b83', 'Cơm Thố Gà Nướng BBQ Hàn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/hvhxvlrfhgaqzpgxi7gt', 0, false, true, 'ab9a3971-c03d-4790-8381-8ba382a91452'),
('5aae2b5e-f644-4538-8a35-d0bbec86baf3', 'Cơm Thố Xúc Xích Đức + Ốp La', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/z3cifytfka2zhwtdq0kx', 0, false, true, 'ab9a3971-c03d-4790-8381-8ba382a91452'),
('07ba6dc2-5c40-44d5-a621-02acf325eda4', 'Cơm Thố Gà Nướng + Xá Xíu', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/rfuqwqoxy5imrm1snswj', 0, false, true, 'ab9a3971-c03d-4790-8381-8ba382a91452'),
('176f86ac-e827-4753-b212-d6a811058ab9', 'Cơm Thố Heo Giòn Teriyaki', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/oabj66o7f5ernhabna0k', 0, false, true, 'c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf'),
('91ecfa23-ed79-48b7-9f64-5e8e58efad7c', 'Cơm Thố Xá Xíu', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/lmo9bwkabcism38guqzp', 0, false, true, 'c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf'),
('b73aa9b6-946b-49fe-a8aa-d9253306a045', 'Cơm Thố Gà Nướng BBQ Hàn', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/hvhxvlrfhgaqzpgxi7gt', 0, false, true, 'c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf'),
('e80e1b9f-50b6-4445-8326-01bce3d863a3', 'Cơm Thố Xúc Xích Đức + Ốp La', 'Cơm nấu ngon, kèm các món ăn kèm hấp dẫn', 'bigfood/images/foods/z3cifytfka2zhwtdq0kx', 0, false, true, 'c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf'),
('da1da13b-10fb-485f-b282-f41ab16b12d5', 'Cơm Thố Gà Nướng + Xá Xíu', 'Cơm gà nóng hôi, thịt gà mềm, nước sốt thơm', 'bigfood/images/foods/rfuqwqoxy5imrm1snswj', 0, false, true, 'c4fbe989-9c96-42b3-b5e9-80eb9cc3acaf');

-- STEP 7: Tạo food options
insert into food_options (id, name, price, is_default, food_id)
values
('aec66061-056f-4b07-b3e9-989193454344', NULL, 75000.00, true, '5a907458-a7b7-43ad-a8b4-671712505eb2'),
('18413bd1-f567-4122-8d33-2213d0f944f9', NULL, 60000.00, true, '7d6ff5b5-ab40-4657-8ec7-814a05e7a252'),
('41fd7940-c8a6-487d-95d0-9777b719af89', NULL, 83000.00, true, 'eab0745f-c104-497b-85c1-93fd3846a085'),
('40d0e2c9-3698-4229-be05-13df53ede117', NULL, 60000.00, true, '45d4970f-4bf1-4050-a58b-b9b6b05ee3c5'),
('9d62a7a1-4f1a-46f4-b77c-c93f94342876', NULL, 65000.00, true, 'a68acf05-f142-4ee4-a8f4-fe8994cbb710'),
('a10e8cc0-cd12-451e-aca3-f19aef995eaf', NULL, 75000.00, true, 'ce9a96cf-1026-447b-aab9-902e94d2af8b'),
('d3a030db-2231-41ec-b438-d9be2d8c57be', NULL, 60000.00, true, '6cebe17f-10ff-4791-9b80-a376be1a5b3c'),
('7e5d4692-117e-4dbf-9406-5e93031a0050', NULL, 83000.00, true, '1dbe9ddd-af98-4f45-9c9b-7875ffa55e38'),
('52c93ff3-1284-4148-960f-1737959ff5b5', NULL, 60000.00, true, '3d40abaf-9564-4f9e-8c70-e284c6bb8975'),
('489347db-2961-4d8a-9546-76effc61c4ab', NULL, 65000.00, true, '3f937cce-818e-47cd-bcc4-bcbd1732f181'),
('6935424f-8cdb-4f4d-b836-45656b105bb3', NULL, 75000.00, true, '06bb59e2-e7f2-4dc8-a2aa-189954e1ca3f'),
('14d95fe7-9fb1-4f7a-b441-7346cd66cd2d', NULL, 60000.00, true, '0e2d2b47-1a4d-4b36-ba68-a6b20c6d95cd'),
('394f9f4b-d3c4-426a-b53c-ef3ebe740a34', NULL, 83000.00, true, '756f725c-07a4-4688-a006-d1b92ebbbf42'),
('0bc088a6-e1de-4113-b348-b63d551f7667', NULL, 60000.00, true, 'b41d572c-1d71-45b6-8400-da1ae4ed90e1'),
('06e4c407-f542-4833-8569-b7e61715f75f', NULL, 65000.00, true, '6314684f-3ef8-4c57-b13e-4e433ee2a592'),
('6725c161-31fc-44d6-8fd7-74e1fcb4fe09', NULL, 75000.00, true, 'cc128a9b-2ec8-4285-848a-467fc25dfd67'),
('efc06fd5-c269-49c2-a328-cc982ad94c49', NULL, 60000.00, true, '5110535f-7a5d-4f89-b888-36d559808f3d'),
('3008a3d1-280c-454a-bbc1-3c7d6d6e390d', NULL, 83000.00, true, 'f2fd391e-d32a-4308-8cec-aa4e8ff7fdd0'),
('f55c85d7-5515-457e-980d-a29930d92f9c', NULL, 60000.00, true, '1e1470a5-b85f-46e8-aabe-d04f311e8bf5'),
('169af1db-5460-4b23-a733-9129c5c0d3a7', NULL, 65000.00, true, 'c4ba35d3-faf5-4aee-be79-e95e2261ba47'),
('dc32bffa-b5a0-4d75-9313-4510295918f4', NULL, 59000.00, true, 'fb2ead4b-88c5-4822-8aa5-01c6b6b006eb'),
('46674428-2318-49d6-b56f-6a72aa086235', NULL, 51999.00, true, '65e5fff0-aa82-4f8a-9130-6d9d1178a1b0'),
('945e3d8c-6231-4863-b7c6-4206b6ae7d8e', NULL, 52999.00, true, '9c222c02-24b1-42cc-9db0-b6a68c4479a6'),
('c58a7fb5-4771-4e63-a9e0-7dcb3f498007', NULL, 37000.00, true, '1f2ccf73-38f0-4437-90c3-e65ad093bf7d'),
('ea51f0fc-3efe-4bc6-82f5-042beca01afd', NULL, 64999.00, true, '37a87157-50ab-4e4a-9691-512fd9272094'),
('206954ad-aaeb-4b9b-98ea-bb03e7c13470', NULL, 59000.00, true, '96bf4625-2a01-48f2-8f5c-687ca73639b3'),
('efafa3e7-d63e-45fa-bb2a-0f1f12cb9049', NULL, 51999.00, true, '9841b085-b726-4090-9651-c93e80ecb5b7'),
('87963f71-b0fa-4087-b89f-0a1ace674448', NULL, 52999.00, true, '6de27eda-20c9-41fa-a573-ea765b330b83'),
('aa753a38-d4e0-4a37-aa2d-3a938f2def2b', NULL, 37000.00, true, '5aae2b5e-f644-4538-8a35-d0bbec86baf3'),
('0eec01f0-c8bb-4cfc-b1b6-aaedf86bfa1d', NULL, 64999.00, true, '07ba6dc2-5c40-44d5-a621-02acf325eda4'),
('e4c2ec09-95a8-43d3-ba6b-6ab6f175b983', NULL, 59000.00, true, '176f86ac-e827-4753-b212-d6a811058ab9'),
('0e9452d0-40eb-4a4b-9550-2fad1f7e6012', NULL, 51999.00, true, '91ecfa23-ed79-48b7-9f64-5e8e58efad7c'),
('8e82d699-8ecd-43e8-af26-f375f885f610', NULL, 52999.00, true, 'b73aa9b6-946b-49fe-a8aa-d9253306a045'),
('2c0a6ce6-3237-45d7-82d0-96d29f08435e', NULL, 37000.00, true, 'e80e1b9f-50b6-4445-8326-01bce3d863a3'),
('ec58accb-5421-4bb5-a098-5d742bd05027', NULL, 64999.00, true, 'da1da13b-10fb-485f-b282-f41ab16b12d5');

-- ========================================
-- Total restaurants: 2
-- Total food categories: 7
-- Total foods: 35
-- Total food options: 35
