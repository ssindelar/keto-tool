CREATE EXTENSION pg_trgm;

CREATE TABLE products
(
    id text PRIMARY KEY,
	name text,
	quantity text,
	brands text,
	stores text,
	imageurl text,
	thumburl text,
	energy_100g real,
	fat_100g real,
	carbohydrate_100g real,
	protein_100g real
);

CREATE INDEX trgm_idx_products_name ON products USING gin (name gin_trgm_ops);