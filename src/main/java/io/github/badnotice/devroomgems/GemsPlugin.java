package io.github.badnotice.devroomgems;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.badnotice.devroomgems.command.GemsCommand;
import io.github.badnotice.devroomgems.database.UserDatabase;
import io.github.badnotice.devroomgems.listener.BlockBreakListener;
import io.github.badnotice.devroomgems.listener.TrafficListener;
import io.github.badnotice.devroomgems.placeholderapi.UserExpansion;
import io.github.badnotice.devroomgems.registry.RewardRegistry;
import io.github.badnotice.devroomgems.registry.UserRegistry;
import io.github.badnotice.devroomgems.task.UserCleanTask;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
public final class GemsPlugin extends JavaPlugin {

    private MongoCollection<Document> collection;

    private UserDatabase userDatabase;

    private UserRegistry userRegistry;
    private RewardRegistry rewardRegistry;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        setupMongoDB();
        setupRegistry();

        registerListeners();
        registerCommands();

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new UserCleanTask(this), 0L, 500L);

        new UserExpansion(this.userRegistry).register();
    }

    @Override
    public void onDisable() {
        userDatabase.close(this.userRegistry);
    }

    private void setupMongoDB() {
        MongoClientURI clientURI = new MongoClientURI("mongodb+srv://<user>:<password>@cluster0.9k6ya.mongodb.net/<database>?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(clientURI);

        final MongoDatabase mongoDatabase = mongoClient.getDatabase("Test");
        collection = mongoDatabase.getCollection("minecraft");
    }

    private void setupRegistry() {
        userDatabase = new UserDatabase(this.collection);

        userRegistry = new UserRegistry(this.userDatabase);
        rewardRegistry = new RewardRegistry(this.getConfig());
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new TrafficListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
    }

    private void registerCommands() {
        final PluginCommand gems = getCommand("gems");
        if (gems == null) return;

        gems.setExecutor(new GemsCommand(this));
    }

}
