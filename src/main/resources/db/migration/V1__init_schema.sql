CREATE TABLE `auth_users` (
    `id` binary(16) NOT NULL,
    `email` varchar(255) NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `user_status` enum('ACTIVE','DISABLED','PENDING') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk-auth-users-email` (`email`)
);

CREATE TABLE `user_roles` (
    `user_id` binary(16) NOT NULL,
    `role` enum('ADMIN','USER') NOT NULL,
    PRIMARY KEY (`user_id`,`role`),
    CONSTRAINT `fk-user-roles-user-id-auth-users` FOREIGN KEY (`user_id`) REFERENCES `auth_users` (`id`)
);
