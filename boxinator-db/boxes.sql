CREATE TABLE boxes (
    id uuid primary key,
    receiver varchar(255) NOT NULL,
    weight decimal(6, 3) CHECK (weight > 0),
    color char(7) NOT NULL,
    country varchar(64) NOT NULL
);

--Max weight with Postnord is 20 kg for normal boxes but frontend allow up to 100 kg. Might change.
--Lowest allowed weight is 1 gram (0.001 kg)
--Therefore limiting weight to 6 significant digits, including 3 decimals, is more than enough for current needs.