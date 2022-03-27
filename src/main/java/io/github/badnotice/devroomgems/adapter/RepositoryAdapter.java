package io.github.badnotice.devroomgems.adapter;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.database.UserDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

public class RepositoryAdapter {

    private final UserDatabase database;

    public RepositoryAdapter(UserDatabase database) {
        this.database = database;
    }

    public User read(UUID uuid) {
        final Document document = database.getMongoCollection()
                .find(Filters.eq("uuid", uuid.toString()))
                .first();

        if (document == null) return null;

        final UUID uniqueId = UUID.fromString(document.getString("uuid"));
        final String name = document.getString("name");
        final double balance = document.getDouble("balance");

        return new User(uniqueId, name, balance);
    }

    public void write(User user) {
        final Document document = new Document("uuid", user.getUniqueId().toString());
        document.append("name", user.getName());
        document.append("balance", user.getBalance());

        final Bson bson = Filters.eq("uuid", user.getUniqueId().toString());
        database.getMongoCollection().replaceOne(bson, document, new UpdateOptions().upsert(true));
    }

}
