-- CREATE TABLE statements
CREATE TABLE Businesses (
	businessID VARCHAR2(30) PRIMARY KEY,
	address VARCHAR2(500),
	available VARCHAR2(10),
	city VARCHAR2(100),
	bstate VARCHAR2(5),
	name VARCHAR2(100),
	rating NUMBER
);

CREATE TABLE BusinessHours (
	businessID VARCHAR2(30),
	businessDay VARCHAR2(20),
	bopen VARCHAR2(10),
	bclose VARCHAR(10),
	PRIMARY KEY(businessID, businessDay),
	FOREIGN KEY(businessID) REFERENCES Businesses(businessID)
);

CREATE TABLE BusinessCategory (
	businessID VARCHAR2(30),
	category VARCHAR2(50),
	PRIMARY KEY(businessID, category),
	FOREIGN KEY(businessID) REFERENCES Businesses(businessID)
);

CREATE TABLE BusinessAttributes (
	businessID VARCHAR2(30),
	attribute VARCHAR2(50),
	bool VARCHAR2(25),
	PRIMARY KEY(businessID, attribute),
	FOREIGN KEY(businessID) REFERENCES Businesses(businessID)
);

CREATE TABLE YelpUser (
	yelpID VARCHAR2(30) PRIMARY KEY,
	name VARCHAR2(50)
);

CREATE TABLE Reviews (
	reviewID VARCHAR2(30) PRIMARY KEY,
	rating INTEGER,
	author VARCHAR2(30),
	businessID VARCHAR2(30),
	publishDate DATE,
	funnyVotes INTEGER,
	usefulVotes INTEGER,
	coolVotes INTEGER,
	reviewText VARCHAR2(1000),
	FOREIGN KEY(businessID) REFERENCES Businesses(businessID),
	FOREIGN KEY(author) REFERENCES YelpUser(yelpID)
);

