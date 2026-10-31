db.getCollection("users").createIndex(
{ login: 1 },
{ name: "login_1" }
);