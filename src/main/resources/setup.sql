create database rabo_db; -- Creates the  database
create user 'rabouser'@'%' identified by 'rabopass'; -- Creates the user
grant all on rabo_db.* to 'rabouser'@'%'; -- Grants all privileges to the new user on the database