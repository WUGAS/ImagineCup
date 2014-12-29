var DBNAME = 'tvect_beta';
var DBHOST = 'localhost';
var DBUSER = 'root';
var DBPASS = '';
var num_user = 0;

var express = require("express"),
	app = express(),
	http = require('http'),
	server = http.createServer(app),
	io = require('socket.io').listen(server),
	mysql = require('mysql'),
	bodyParser = require('body-parser'),
	connection = mysql.createConnection({
		host: DBHOST,
		user: DBUSER,
		password: DBPASS,
		database: DBNAME
	}),
	disconnect = function() {
		SOCKET.leave(data.room);
		console.log('A user disconnected from: ' + data.room);
	},
	connect = function(data) {
		SOCKET.join(num_user);
		io.to(num_user).emit('number', num_user);
		console.log(num_user);
		console.log(data.id);
		num_user++;
	},
	stripTags = function(str) {
		return str.replace(/</g, "&lt;").replace(/>/g, "&gt;");
	},
	updateUser = function(data) {
		var query = 'UPDATE in_group SET in_group.group_score=' + data.score + ' WHERE in_group.in_id="' + data.group + '"';

		connection.query(query, function(err, rows, fields) {
			if (err) throw err;

			console.log(rows);
		});
	};

io.on('connection', function(socket) {
	SOCKET = socket;
	SOCKET.on('disconnect', disconnect);
	SOCKET.on('connect', joinRoom);
});


app.use(bodyParser.urlencoded({
	extended: true
}));

/*****************USERS***************************/

app.get('/users/create', function(req, res) {
	var post = [req.query.email, req.query.password];
	var query = 'INSERT INTO users (email, password) VALUES(?, ?);';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/users/locations/add', function(req, res) {
	var post = [req.query.email, req.query.longit, req.query.lat];
	if (req.query.rec_time) {
		post.push(req.query.rec_time);
		var query = 'INSERT INTO user_locations (email, longit, lat) VALUES (?,?,?,?);';
	} else
		var query = 'INSERT INTO user_locations (email, longit, lat) VALUES (?,?,?);';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/users/infections', function(req, res) {
	var post = [req.query.email];
	var query = 'SELECT * FROM user_infections AS us WHERE us.email= ? ORDER BY infection_start DESC;';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/users/infections/add', function(req, res) {
	var post = [req.query.email, req.query.disease];
	if (req.query.start_time) {
		post.push(req.query.start_time);
		var query = 'INSERT INTO user_infections (email, disease_name, infection_start) VALUES (?,?,?);';
	} else
		var query = 'INSERT INTO user_infections (email, disease_name) VALUES (?,?);';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});





/*****************REGIONS***************************/

app.get('/regions', function(req, res) {
	var query = 'SELECT * FROM regions ORDER BY region_name ASC;';

	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/regions/country', function(req, res) {
	var query = 'SELECT DISTINCT country FROM regions ORDER BY country ASC;';

	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/regions/country/subregions', function(req, res) {
	var query = 'SELECT region_id, region_name, latitude, longitude, radius, altitude \
				FROM regions \
				WHERE country=? \
				ORDER BY region_name ASC;';

	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});

app.get('/regions/infections', function(req, res) {
	var post = [req.query.region_id];
	var query = 'SELECT lat, longit \
				FROM region_infections \
				WHERE region_id=?;';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});


app.get('/regions/news', function(req, res) {
	var post = [req.query.region_id];
	var query = 'SELECT article_id, title, news, create_time, tags \
				FROM top_news \
				WHERE region_id=? AND deleted=0 \
				ORDER BY create_time DESC;';

	connection.query(query, post, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});



/*****************DISEASES***************************/


app.get('/diseases', function(req, res) {
	var query = 'SELECT disease_name FROM diseases;';

	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});


app.get('/diseases/info', function(req, res) {
	if (req.query.disease_name) {
		var post = [req.query.disease_name];
		var query = 'SELECT * FROM diseases WHERE disease_name=?;';

		connection.query(query, post, function(err, rows, fields) {
			if (err) throw err;
			res.send(rows);
		});
	} else {
		var query = 'SELECT * FROM diseases;';

		connection.query(query, function(err, rows, fields) {
			if (err) throw err;
			res.send(rows);
		});
	}
});


/*
app.get('/users/create2', function(req, res) {
	var post = [ req.query.email, req.query.password];
	var query = 
	'INSERT INTO ' + DBNAME + '.users (users.username, users.password) VALUES ("' + 
		req.query.name + '","' + req.query.password + '")'; 

	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
		res.send(rows);
	});
});
*/

server.listen(3000, "0.0.0.0", function() {
	console.log('listening on *:3000');
	// tell node which database to use
	var query = 'use ' + DBNAME;
	connection.query(query, function(err, rows, fields) {
		if (err) throw err;
	});
});