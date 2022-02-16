package apollyon.apollyon;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Apollyon extends JavaPlugin implements Listener {
    public ConsoleCommandSender console;
    @Override
    public void onEnable() {
        console = Bukkit.getServer().getConsoleSender();
        console.sendMessage("[ApollyonBoss] has been ENABLED!");
            getServer().getPluginManager().registerEvents(new Boss(this),this);
            getServer().getPluginManager().registerEvents(new DarkKnight(this),this);
    }

    @Override
    public void onDisable() {
        console.sendMessage("[ApollyonBoss] has been DISABLED!");
    }
}