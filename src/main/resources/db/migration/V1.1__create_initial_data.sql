INSERT INTO conferences (id, name, startDate, endDate, active)
VALUES (1, 'Tech Conference', '2024-09-10 10:00:00', '2024-09-12 10:00:00', true),
       (2, 'Business Summit', '2024-09-20 10:00:00', '2024-09-14 10:00:00', true),
       (3, 'Health Workshop', '2024-09-30 10:00:00', '2024-09-15 10:00:00', true),
       (4, 'Education Expo', '2024-10-10 10:00:00', '2024-09-16 10:00:00', true),
       (5, 'Art Festival', '2024-10-20 10:00:00', '2024-09-17 10:00:00', true);

INSERT INTO participants (personalCode, firstName, lastName, email, conference_id)
VALUES ('1234567890', 'John', 'Doe', 'email2@ww.ff', 1),
       ('0987654321', 'Jane', 'Smith', 'email3@ww.ff', 1),
       ('1357924680', 'Alice', 'Johnson', 'email4@ww.ff', 2),
       ('2468135790', 'Bob', 'Brown', 'email@ww.ff', 3),
       ('9876543210', 'Charlie', 'White', 'email5@ww.ff', 4);

INSERT INTO rooms (name, capacity, conference_id)
VALUES ('Room A', 50, 1),
       ('Room B', 30, 2),
       ('Room C', 70, 3),
       ('Room D', 100, 4),
       ('Room E', 150, 5);

INSERT INTO users (id, username, password)
VALUES (1, 'john_doe', '$2a$10$0RNIulfHe1TCP7JDje8LaewlyaWNToRS6Vdf8fmVfP/phnRytB.ny'),
       (2, 'jane_smith', '$2a$10$PZQaG/ioi3iXW9nEHHdWgedi44qnoMZzs7znIuKJBE38G0VhNjlG6'),
       (3, 'admin_user', '$2a$10$hLufwnLuKqxya6xxqdrHzeD./lXzhOQyQWO.W/Cma.BQel5.u2wMq');

INSERT INTO roles (role_id, authority)
VALUES (1, 'ADMIN'),
       (2, 'USER');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2);