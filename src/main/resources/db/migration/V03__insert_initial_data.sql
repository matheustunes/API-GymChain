INSERT INTO user (id, name, email, password, birth_date, gender, active, web3_address)
    values (1, 'Fernando Duarte', 'fernandoduarte@ifsp.edu.br', '$2a$10$Ot4XGuyPP7r82nN3WXA0bOL1Qk9gShKDlVuPoyp89HoFnHcwO4Tji', '1975-11-16', 'MASCULINO', 1, '0xFernandoEthAddress');
INSERT INTO user (id, name, email, password, birth_date, gender, active, web3_address)
    values (2, 'Juliana Silva', 'julianasilva@ifsp.edu.br', '$2a$10$Ot4XGuyPP7r82nN3WXA0bOL1Qk9gShKDlVuPoyp89HoFnHcwO4Tji', '1980-01-01', 'FEMININO', 1, '0xJulianaEthAddress');

INSERT INTO exercise (id, name, description, muscle_group, difficulty_level) VALUES
    (1, 'Supino Reto com Barra', 'Exercício fundamental para o peito.', 'PEITO', 'INTERMEDIARIO'),
    (2, 'Agachamento Livre', 'Rei dos exercícios, trabalha todo o corpo inferior.', 'PERNA', 'AVANCADO'),
    (3, 'Remada Curvada', 'Ótimo para as costas e postura.', 'COSTAS', 'INTERMEDIARIO'),
    (4, 'Elevação Lateral', 'Isolamento para ombros.', 'OMBROS', 'INICIANTE');