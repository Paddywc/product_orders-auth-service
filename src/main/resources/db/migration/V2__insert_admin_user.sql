INSERT INTO auth_users (id, email, password_hash, user_status)
VALUES (
           UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
           'admin@example.com',
           '$2a$12$nKpbAKikuQqR2NjA5p1LEOZLTVR7DhWYxW6d/9XTBOIvJgJHmMkqK',
           'ACTIVE'
       );

INSERT INTO user_roles (user_id, role)
VALUES (
           UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
           'ADMIN'
       );

INSERT INTO user_roles (user_id, role)
VALUES (
           UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
           'USER'
       );
