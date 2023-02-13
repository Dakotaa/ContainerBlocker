package io.github.dakotaa.containerblocker;

import org.bukkit.plugin.java.JavaPlugin;

public final class ContainerBlocker extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ContainerListeners(), this);
        ItemCheck.registerGroups(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
