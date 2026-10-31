db.createCollection("user_preferences", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["userId", "language", "currency"],
      properties: {
        userId: {
          bsonType: "objectId",
          description: "Reference to the user in the users collection"
        },
        language: {
          bsonType: "string",
          pattern: "^[a-z]{2}(-[A-Z]{2})?$"
        },
        currency: {
          bsonType: "string",
          pattern: "^[A-Z]{3}$"
        }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
});