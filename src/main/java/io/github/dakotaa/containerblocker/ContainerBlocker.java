package io.github.dakotaa.containerblocker;

import org.bukkit.plugin.java.JavaPlugin;

public final class ContainerBlocker extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ContainerListeners(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
