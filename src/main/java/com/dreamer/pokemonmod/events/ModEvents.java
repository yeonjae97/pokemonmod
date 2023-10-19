package com.dreamer.pokemonmod.events;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.commands.PokemonInfo;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = PokemonMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new PokemonInfo(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
