
use tvect_beta;

INSERT INTO IF EXISTS regions(id, country, region_name, latitude, longitude, altitude)
SELECT id, country, city, latitude, longitude, altitude FROM location;

exit;
