CREATE TABLE IF NOT EXISTS countries (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS country_default_day_premiums (
    country_id INT PRIMARY KEY,
    default_day_premium DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS insurance_limit_coefficients (
    insurance_limit_group VARCHAR(10) PRIMARY KEY,
    insurance_limit_coefficient DECIMAL(5, 2)
);

INSERT INTO countries (id, name)
SELECT 1, 'Test Country'
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE id = 1);

INSERT INTO country_default_day_premiums (country_id, default_day_premium)
SELECT 1, 10.00
WHERE NOT EXISTS (SELECT 1 FROM country_default_day_premiums WHERE country_id = 1);

INSERT INTO insurance_limit_coefficients (insurance_limit_group, insurance_limit_coefficient)
SELECT 'low', 1.0
WHERE NOT EXISTS (SELECT 1 FROM insurance_limit_coefficients WHERE insurance_limit_group = 'low');

INSERT INTO insurance_limit_coefficients (insurance_limit_group, insurance_limit_coefficient)
SELECT 'medium', 1.5
WHERE NOT EXISTS (SELECT 1 FROM insurance_limit_coefficients WHERE insurance_limit_group = 'medium');

INSERT INTO insurance_limit_coefficients (insurance_limit_group, insurance_limit_coefficient)
SELECT 'high', 2.0
WHERE NOT EXISTS (SELECT 1 FROM insurance_limit_coefficients WHERE insurance_limit_group = 'high');
