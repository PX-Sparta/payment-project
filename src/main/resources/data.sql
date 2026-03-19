-- 데이터가 없을 때만 들어가도록 (MySQL 기준)
INSERT IGNORE INTO membership_grade_policies (grade_code, grade_name, min_paid_amount, max_paid_amount, point_rate, is_active, created_at)
VALUES
('NORMAL', '일반 등급', 0, 50000, 0.01, true, NOW()),
('VIP', '우수 등급', 50001, 150000, 0.05, true, NOW()),
('VVIP', '최우수 등급', 150001, 999999999, 0.10, true, NOW());