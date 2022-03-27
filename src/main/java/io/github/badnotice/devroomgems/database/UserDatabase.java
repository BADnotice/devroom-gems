package io.github.badnotice.devroomgems.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.github.badnotice.devroomgems.adapter.RepositoryAdapter;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.registy.UserRegistry;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
public class UserDatabase {

    private final RepositoryAdapter adapter;

    private final MongoCollection<Document> mongoCollection;

    public UserDatabase(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
        this.adapter = new RepositoryAdapter(this);
    }

    public List<User> loadAll() {
        final List<User> userList = new ArrayList<>();

        final MongoCursor<Document> iterator = mongoCollection
                .find()
                .iterator();

        while (iterator.hasNext()) {
            final Document next = iterator.next();

            final User read = adapter.read(UUID.fromString(
                    next.getString("uuid")
            ));

            if (read == null) continue;

            userList.add(read);
        }

        return userList;
    }

    public void insertOrUpdate(User user) {
        adapter.write(user);
    }

    public void close(UserRegistry registry) {
        final Iterator<User> iterator = registry.iterator();
        while (iterator.hasNext()) {
            final User next = iterator.next();
            if (!next.isDirty()) continue;

            insertOrUpdate(next);
        }
    }

}
