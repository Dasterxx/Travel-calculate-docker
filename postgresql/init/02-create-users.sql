CREATE USER insurance_calculator_db WITH ENCRYPTED PASSWORD 'userpass';
GRANT ALL PRIVILEGES ON DATABASE insurance_calculator_db TO insurance_calculator_db;

CREATE USER black_list_db WITH ENCRYPTED PASSWORD 'userpass';
GRANT ALL PRIVILEGES ON DATABASE black_list_db TO black_list_db;
