package com.dreamer.pokemonmod.events;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.commands.*;
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
        new PokemonObtain(event.getDispatcher());
        new PokemonAmount(event.getDispatcher());
        new PokemonCheckLegend(event.getDispatcher());
        new PokemonChangeIvs(event.getDispatcher());
//        new PokemonAmount(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
