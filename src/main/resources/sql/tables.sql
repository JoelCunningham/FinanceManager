PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS type;
DROP TABLE IF EXISTS header;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS budget;
DROP TABLE IF EXISTS cashflow;
DROP TABLE IF EXISTS year;
DROP TABLE IF EXISTS category_year;

PRAGMA foreign_keys = ON;

CREATE TABLE type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE header (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type_id INTEGER NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type(id)
    UNIQUE (name, type_id)
);

CREATE TABLE category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    header_id INTEGER,
    FOREIGN KEY (header_id) REFERENCES header(id),
    UNIQUE (name, header_id)
);

CREATE TABLE budget (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    amount DECIMAL NOT NULL,
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES category(id),
    UNIQUE (category_id, month, year)
);

CREATE TABLE cashflow (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    date TEXT NOT NULL,
    details TEXT,
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE year (
    year INTEGER PRIMARY KEY
);

CREATE TABLE category_year (
    category_id INTEGER,
    year INTEGER,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (year) REFERENCES year(year),
    PRIMARY KEY (category_id, year)
);