-- drop DATABASE IF EXISTS bigfood;
-- CREATE DATABASE IF NOT EXISTS bigfood;


update reviews set sentiment = 'NEGATIVE'
WHERE sentiment is not null;