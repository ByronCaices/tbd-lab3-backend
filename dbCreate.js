db = db.getSiblingDB('ecommercedb');

db.createUser({
  user: "root",
  pwd: "123admin",
  roles: [{ role: "readWrite", db: "ecommercedb" }]
});

db.createCollection("addresses");
