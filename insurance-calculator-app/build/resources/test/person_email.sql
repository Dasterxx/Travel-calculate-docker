-- Создать таблицу, если не существует
CREATE TABLE IF NOT EXISTS person_entity (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    person_code VARCHAR(20),
    black_listed BOOLEAN NOT NULL DEFAULT FALSE
    -- Добавьте другие необходимые поля
);

-- Вставить тестового пользователя, если его ещё нет
INSERT INTO person_entity (email, password, enabled, first_name, last_name, person_code, black_listed)
SELECT 'testuser@example.com',
       '$2a$10$DowJ8xW1hQ8z6B7Qj7D1UuK6R3W9q8Y8aG9QWzYQ0G6YF5x7P9v6K', -- BCrypt-хэш для 'testpassword'
       TRUE,
       'Test',
       'User',
       'T-00001',
       FALSE
WHERE NOT EXISTS (
    SELECT 1 FROM person_entity WHERE email = 'testuser@example.com'
);
