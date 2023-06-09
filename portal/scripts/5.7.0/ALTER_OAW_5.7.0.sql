ALTER TABLE observatorio_template_range
ADD COLUMN min_position_value FLOAT(4,2) NULL DEFAULT 0 AFTER max_value_operator,
ADD COLUMN max_position_value FLOAT(4,2) NULL DEFAULT 10 AFTER min_position_value,
ADD COLUMN min_position_value_operator VARCHAR(255) NOT NULL AFTER max_position_value,
ADD COLUMN max_position_value_operator VARCHAR(255) NOT NULL AFTER min_position_value_operator;