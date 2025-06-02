CREATE TABLE permission (
    id BIGINT(20) PRIMARY KEY,
    description VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_permission (
    id_user BIGINT(20) NOT NULL,
    id_permission BIGINT(20) NOT NULL,
    PRIMARY KEY (id_user, id_permission),
    FOREIGN KEY (id_user) REFERENCES user(id),
    FOREIGN KEY (id_permission) REFERENCES permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Admin user (password: admin)
INSERT INTO user (id, name, email, password, birth_date, gender, active, web3_address)
    values (3, 'Administrador', 'admin@ifsp.edu.br', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.', '2000-01-01', 'MASCULINO', 1, NULL);

-- Permissions
INSERT INTO permission (id, description) values (1, 'ROLE_REGISTER_USER');
INSERT INTO permission (id, description) values (2, 'ROLE_REMOVE_USER');
INSERT INTO permission (id, description) values (3, 'ROLE_SEARCH_USER');

INSERT INTO permission (id, description) values (4, 'ROLE_REGISTER_ACTIVITY');
INSERT INTO permission (id, description) values (5, 'ROLE_REMOVE_ACTIVITY');
INSERT INTO permission (id, description) values (6, 'ROLE_SEARCH_ACTIVITY');

INSERT INTO permission (id, description) values (7, 'ROLE_REGISTER_EXERCISE'); -- NOVO
INSERT INTO permission (id, description) values (8, 'ROLE_REMOVE_EXERCISE');   -- NOVO
INSERT INTO permission (id, description) values (9, 'ROLE_SEARCH_EXERCISE');   -- NOVO

INSERT INTO permission (id, description) values (10, 'ROLE_REGISTER_WORKOUT');  -- NOVO
INSERT INTO permission (id, description) values (11, 'ROLE_REMOVE_WORKOUT');    -- NOVO
INSERT INTO permission (id, description) values (12, 'ROLE_SEARCH_WORKOUT');    -- NOVO


-- User-Permission mapping
-- Admin has all permissions
INSERT INTO user_permission (id_user, id_permission) values (3, 1);
INSERT INTO user_permission (id_user, id_permission) values (3, 2);
INSERT INTO user_permission (id_user, id_permission) values (3, 3);
INSERT INTO user_permission (id_user, id_permission) values (3, 4);
INSERT INTO user_permission (id_user, id_permission) values (3, 5);
INSERT INTO user_permission (id_user, id_permission) values (3, 6);
INSERT INTO user_permission (id_user, id_permission) values (3, 7);
INSERT INTO user_permission (id_user, id_permission) values (3, 8);
INSERT INTO user_permission (id_user, id_permission) values (3, 9);
INSERT INTO user_permission (id_user, id_permission) values (3, 10);
INSERT INTO user_permission (id_user, id_permission) values (3, 11);
INSERT INTO user_permission (id_user, id_permission) values (3, 12);


-- Fernando (User) - pode registrar/buscar usuário e atividades, e buscar exercícios/treinos
INSERT INTO user_permission (id_user, id_permission) values (1, 1); -- REG_USER
INSERT INTO user_permission (id_user, id_permission) values (1, 3); -- SEARCH_USER
INSERT INTO user_permission (id_user, id_permission) values (1, 4); -- REG_ACTIVITY
INSERT INTO user_permission (id_user, id_permission) values (1, 6); -- SEARCH_ACTIVITY
INSERT INTO user_permission (id_user, id_permission) values (1, 9); -- SEARCH_EXERCISE
INSERT INTO user_permission (id_user, id_permission) values (1, 12); -- SEARCH_WORKOUT


-- Juliana (User) - similar ao Fernando
INSERT INTO user_permission (id_user, id_permission) values (2, 1); -- REG_USER
INSERT INTO user_permission (id_user, id_permission) values (2, 3); -- SEARCH_USER
INSERT INTO user_permission (id_user, id_permission) values (2, 4); -- REG_ACTIVITY
INSERT INTO user_permission (id_user, id_permission) values (2, 6); -- SEARCH_ACTIVITY
INSERT INTO user_permission (id_user, id_permission) values (2, 9); -- SEARCH_EXERCISE
INSERT INTO user_permission (id_user, id_permission) values (2, 12); -- SEARCH_WORKOUT