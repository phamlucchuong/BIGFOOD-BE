

-- drop DATABASE if EXISTS bigfood;
-- CREATE DATABASE IF NOT EXISTS bigfood;
-- use bigfood;

use bigfood;
delete from history_search
where id = '0fffe3ae-5897-43a7-95ad-509062a832c9' 
or id = '14f75558-b914-49aa-8ab3-5f254f745e82'
or id = 'b237a707-31d7-4b84-8986-17656d5c2b7d'
or id = 'e53290c3-a5b9-42ff-98c9-2da3bc08c4e8';