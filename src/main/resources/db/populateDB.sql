DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

DELETE FROM meals;

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100000, '2019.10.20 22:40:10', 'Пользователь поужинал', 1500);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100001, '2019.10.20 22:40:25', 'Админ пожрал', 1200);
