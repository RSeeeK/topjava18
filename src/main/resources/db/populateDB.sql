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
    (100000, '2019.10.20 08:30:00', 'Пользователь позавтракал', 500);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100000, '2019.10.20 14:30:00', 'Пользователь пообедал', 1000);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100000, '2019.10.20 21:30:10', 'Пользователь поужинал', 500);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100001, '2019.10.20 12:00:00', 'Админ позавтракал', 1500);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100001, '2019.10.20 18:00:00', 'Админ пообедал', 2000);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100001, '2019.10.20 22:00:00', 'Админ поужинал', 2500);
