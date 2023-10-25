package com.dreamer.pokemonmod;


import com.dreamer.pokemonmod.commands.PokeBook;
import com.dreamer.pokemonmod.config.Config;
import com.dreamer.pokemonmod.events.rightClickEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PokemonMod.MOD_ID)
@Mod.EventBusSubscriber(modid = PokemonMod.MOD_ID)
public class PokemonMod {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "pokemonmod";

    private static PokemonMod instance;

    private Config config;

    public PokemonMod() {
        instance = this;

        reloadConfig();


        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(PokemonMod::onModLoad);
    }

    public static void onModLoad(FMLCommonSetupEvent event) {
        // 이벤트를 추가 하고 메시지를 전송합니다.
        MinecraftForge.EVENT_BUS.register(new rightClickEvent());
        System.out.println("픽셀몬 북 모드가 로드가 완료 됐습니다.");
    }


    public void reloadConfig() {
        // 콘피그 리로드 (모드가 시작될때 발동됩니다.)
        try {
            this.config = YamlConfigFactory.getInstance(Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onEnable(FMLServerStartingEvent event) {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        String rawMessage = "§6[Pokemonmod 플러그인이 활성화 중입니다]";
        byte[] bytes = rawMessage.getBytes(StandardCharsets.UTF_8);
        String utf8Message = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info(utf8Message);
    }

    @SubscribeEvent
    public void onDisable(FMLServerStoppingEvent event) {
        String rawMessage = "§6[Pokemonmod 플러그인이 비활성화 중입니다]";
        byte[] bytes = rawMessage.getBytes(StandardCharsets.UTF_8);
        String utf8Message = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info(utf8Message);
    }
    public static PokemonMod getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Config getConfig() {
        return instance.config;
    }

}
