db.getCollection("user_preferences").createIndex(
    { userId: 1 },
    {
      name: "userId_1",
      unique: true
    }
);