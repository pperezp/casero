ALTER TABLE movimiento
ADD COLUMN amount INTEGER;

ALTER TABLE movimiento
ADD COLUMN type TEXT;

UPDATE movimiento
SET amount = CAST(SUBSTR(detalle, instr(detalle, '$')+1) AS INTEGER),
type = SUBSTR(detalle, instr(detalle, '[')+1, instr(detalle, ']')-2);

CREATE TABLE transactionType(
    id INTEGER PRIMARY KEY,
    name TEXT
);

INSERT INTO transactionType VALUES
(0, 'SALE'),
(1, 'PAYMENT'),
(2, 'REFUND'),
(3, 'DEBT_FORGIVENESS'),
(4, 'INITIAL_BALANCE');

UPDATE movimiento
SET type = 4
WHERE type = '';

UPDATE movimiento
SET type = 0
WHERE type = 'Venta';

UPDATE movimiento
SET type = 1
WHERE type = 'Abono';

UPDATE movimiento
SET type = 3
WHERE type = 'CONDONACIÓN DEUDA';

UPDATE movimiento
SET type = 2
WHERE type = 'Devolución';

CREATE TABLE temporal(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente INTEGER,
    fecha TEXT,
    detalle TEXT,
	amount INTEGER,
    saldo INTEGER,
	type INTEGER,
    FOREIGN KEY(cliente) REFERENCES cliente(id),
	FOREIGN KEY(type) REFERENCES transactionType(id)
);

INSERT INTO temporal(
	id, cliente, fecha, detalle, amount, saldo, type
) SELECT id, cliente, fecha, detalle, amount, saldo, type
FROM movimiento;

DROP TABLE movimiento;

CREATE TABLE movimiento(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente INTEGER,
    fecha TEXT,
    detalle TEXT,
	amount INTEGER,
    saldo INTEGER,
	type INTEGER,
    FOREIGN KEY(cliente) REFERENCES cliente(id),
	FOREIGN KEY(type) REFERENCES transactionType(id)
);

INSERT INTO movimiento(
	id, cliente, fecha, detalle, amount, saldo, type
) SELECT id, cliente, fecha, detalle, amount, saldo, type
FROM temporal;

DROP TABLE temporal;
