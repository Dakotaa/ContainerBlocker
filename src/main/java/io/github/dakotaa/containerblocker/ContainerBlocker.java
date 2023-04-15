package io.github.dakotaa.containerblocker;

import org.bukkit.plugin.java.JavaPlugin;

public final class ContainerBlocker extends JavaPlugin {

    public static ContainerBlocker plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ContainerListeners(), this);
        getServer().getPluginManager().registerEvents(new ChestShopListener(),this);
        ItemCheck.registerGroups(this);
    }

    @Override
    public void onDisable() {}
}
