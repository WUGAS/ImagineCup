USE tvect;

DELETE * FROM diseases;
DELETE * FROM user_locations;
DELETE * FROM user_infections;
DELETE * FROM top_news;
DELETE * FROM regions;
DELETE * FROM users;

DROP TABLE IF EXISTS diseases;
DROP TABLE IF EXISTS user_locations;
DROP TABLE IF EXISTS user_infections;
DROP TABLE IF EXISTS top_news;
DROP TABLE IF EXISTS regions;
DROP TABLE IF EXISTS users;

exit;