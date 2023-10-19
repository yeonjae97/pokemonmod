package com.dreamer.pokemonmod;


import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PokemonMod.MOD_ID)
public class PokemonMod {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "pokemonmod";

    public PokemonMod() {


        Pixelmon.EVENT_BUS.register(this);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


    }


    @SubscribeEvent
    public void onEnable(FMLServerStartingEvent event) {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        String rawMessage = "[Pokemonmod 플러그인이 활성화 중입니다]";
        byte[] bytes = rawMessage.getBytes(StandardCharsets.UTF_8);
        String utf8Message = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info(utf8Message);
    }

    @SubscribeEvent
    public void onDisable(FMLServerStoppingEvent event) {
        String rawMessage = "[Pokemonmod 플러그인이 비활성화 중입니다]";
        byte[] bytes = rawMessage.getBytes(StandardCharsets.UTF_8);
        String utf8Message = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info(utf8Message);
    }


}
