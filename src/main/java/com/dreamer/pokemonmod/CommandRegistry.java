package com.dreamer.pokemonmod;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = PokemonMod.MOD_ID)
public class CommandRegistry {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
//        new SummonPokemon(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

}
