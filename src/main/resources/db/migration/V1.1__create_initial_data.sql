INSERT INTO conferences (id, name, startDate, endDate, active, roomCapacity)
VALUES (100, 'Tech Conference', '2024-09-10 10:00:00', '2024-09-12 10:00:00', true, 11),
       (200, 'Business Summit', '2024-09-20 10:00:00', '2024-09-14 10:00:00', true,
        12),
       (300, 'Health Workshop', '2024-09-30 10:00:00', '2024-09-15 10:00:00', true, 13),
       (400, 'Education Expo', '2024-10-10 10:00:00', '2024-09-16 10:00:00', true,
        15),
       (500, 'Art Festival', '2024-10-20 10:00:00', '2024-09-17 10:00:00', true,
        11);

INSERT INTO participants (id,personalCode, firstName, lastName, email, conference_id)
VALUES (1,'1234567890', 'John', 'Doe', 'email2@ww.ff', 100),
       (2,'0987654321', 'Jane', 'Smith', 'email3@ww.ff', 100),
       (3,'1357924680', 'Alice', 'Johnson', 'email4@ww.ff', 200),
       (4,'2468135790', 'Bob', 'Brown', 'email@ww.ff', 300),
       (5,'9876543210', 'Charlie', 'White', 'email5@ww.ff', 400);

INSERT INTO users (id, username, password)
VALUES (1, 'john_doe', '$2a$10$0RNIulfHe1TCP7JDje8LaewlyaWNToRS6Vdf8fmVfP/phnRytB.ny'),
       (2, 'jane_smith', '$2a$10$PZQaG/ioi3iXW9nEHHdWgedi44qnoMZzs7znIuKJBE38G0VhNjlG6'),
       (3, 'admin_user', '$2a$10$hLufwnLuKqxya6xxqdrHzeD./lXzhOQyQWO.W/Cma.BQel5.u2wMq');

INSERT INTO roles (role_id, authority)
VALUES (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'LOCAL_USER');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 3),
       (2, 2),
       (3, 1);