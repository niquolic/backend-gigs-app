db.getCollection("gigs").createIndex(
{ userId: 1 },
{ name: "userId_1" }
);