ALTER TABLE observatorio_template_range
ADD COLUMN min_position_value FLOAT(4,2) NULL AFTER max_value_operator,
ADD COLUMN max_position_value FLOAT(4,2) NULL AFTER min_position_value,
ADD COLUMN min_position_value_operator VARCHAR(255) NULL AFTER max_position_value,
ADD COLUMN max_position_value_operator VARCHAR(255) NULL AFTER min_position_value_operator;