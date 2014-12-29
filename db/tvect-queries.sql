--USERS

-- /users/create
INSERT INTO users (email, password) 
VALUES(?, ?);


-- /users/locations


-- /users/locations/add
INSERT INTO user_locations (email, longit, lat) 
VALUES (?,?,?);

INSERT INTO user_locations (email, longit, lat, rec_time) 
VALUES (?,?,?,?);


--/users/infections
SELECT * FROM user_infections as us
WHERE us.email =?
ORDER BY infection_start DESC;

-- /users/infections/add
INSERT INTO user_infections (email, disease_name) 
VALUES (?,?);

INSERT INTO user_infections (email, disease_name, infection_start) 
VALUES (?,?,?);




--REGIONS

-- /regions
SELECT * FROM regions
ORDER BY region_name ASC;

-- /regions/country
SELECT DISTINCT country FROM regions
ORDER BY country ASC;

-- /regions/country/subregions
SELECT region_id, region_name, latitude, longitude, radius, altitude 
FROM regions
WHERE country=?
ORDER BY region_name ASC; 

-- /regions/infections
SELECT lat, longit 
FROM region_infections 
WHERE region_id=?;

-- /regions/news
SELECT article_id, title, news, create_time, tags 
FROM top_news 
WHERE region_id=? AND deleted=0
ORDER BY create_time DESC;



--Diseases

-- /diseases
SELECT disease_name FROM diseases;

-- /diseases/info
SELECT * FROM diseases;

SELECT * FROM diseases WHERE disease_name=?;