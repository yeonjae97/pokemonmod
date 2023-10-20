package com.dreamer.pokemonmod;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PokemonPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
//        getCommand("포켓몬설정").setExecutor(new PokemonObtain());
        getServer().getConsoleSender().sendMessage("§a안녕, 난 포켓몬 명령어 한글화 플러그인 이라고 해");
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if(args.length != 0){
            p.sendMessage("");
        }

        return super.onCommand(sender, command, label, args);
    }




}
