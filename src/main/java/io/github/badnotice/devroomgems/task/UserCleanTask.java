package io.github.badnotice.devroomgems.task;

import io.github.badnotice.devroomgems.GemsPlugin;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.database.UserDatabase;
import io.github.badnotice.devroomgems.registy.UserRegistry;

import java.util.Iterator;

public class UserCleanTask implements Runnable {

    private final UserRegistry registry;
    private final UserDatabase database;

    public UserCleanTask(GemsPlugin plugin) {
        this.registry = plugin.getUserRegistry();
        this.database = plugin.getUserDatabase();
    }

    @Override
    public void run() {
        final Iterator<User> iterator = registry.iterator();
        while (iterator.hasNext()) {
            final User next = iterator.next();
            if (!next.isDirty()) continue;

            database.insertOrUpdate(next);
            next.setDirty(false);
        }
    }

}
