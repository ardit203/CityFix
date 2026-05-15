insert into users
(created_at, updated_at, username, password, email, role, failed_login_attempts, lock_level, locked_until, notifications_enabled)
values
    (now(), now(), 'admin1',    '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'admin1@cityfix.com',    'ROLE_ADMINISTRATOR', 0, 0, null, true),

    (now(), now(), 'manager1',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'manager1@cityfix.com',  'ROLE_MANAGER',       0, 0, null, true),
    (now(), now(), 'manager2',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'manager2@cityfix.com',  'ROLE_MANAGER',       0, 0, null, true),

    (now(), now(), 'employee1', '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'employee1@cityfix.com', 'ROLE_EMPLOYEE',      0, 0, null, true),
    (now(), now(), 'employee2', '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'employee2@cityfix.com', 'ROLE_EMPLOYEE',      0, 0, null, true),
    (now(), now(), 'employee3', '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'employee3@cityfix.com', 'ROLE_EMPLOYEE',      0, 0, null, true),
    (now(), now(), 'employee4', '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'employee4@cityfix.com', 'ROLE_EMPLOYEE',      0, 0, null, true),

    (now(), now(), 'citizen1',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen1@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen2',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen2@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen3',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen3@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen4',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen4@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen5',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen5@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen6',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen6@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen7',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen7@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true),
    (now(), now(), 'citizen8',  '$2a$10$7QJ8FqK0qMTnW.bTJ7kpWOnOCdYfL.vPM9eWw0DhxygXZ5s5vK5xG', 'citizen8@cityfix.com',  'ROLE_CITIZEN',       0, 0, null, true);


insert into user_profiles
(user_id, name, surname, phone_number, street, city, postal_code, date_of_birth, gender, profile_picture_id)
select id,
       'Name ' || id,
       'Surname ' || id,
       '+38970100' || lpad(id::text, 3, '0'),
       'Street ' || id,
       'Skopje',
       '1000',
       date '2000-01-01',
       'OTHER',
       null
from users
where email in (
                'admin1@cityfix.com',
                'manager1@cityfix.com',
                'manager2@cityfix.com',
                'employee1@cityfix.com',
                'employee2@cityfix.com',
                'employee3@cityfix.com',
                'employee4@cityfix.com',
                'citizen1@cityfix.com',
                'citizen2@cityfix.com',
                'citizen3@cityfix.com',
                'citizen4@cityfix.com',
                'citizen5@cityfix.com',
                'citizen6@cityfix.com',
                'citizen7@cityfix.com',
                'citizen8@cityfix.com'
    );