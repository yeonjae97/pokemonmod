package com.dreamer.pokemonmod;

import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.init.PixelmonInitEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PokemonMod.MOD_ID)
public class PokemonLoad {

    @SubscribeEvent
    public void oneLoadPokemonInfo(PixelmonDeletedEvent event){
        ServerPlayerEntity player = event.player;
        Pokemon pokemon = event.pokemon;
        player.sendMessage(ITextComponent.getTextComponentOrEmpty(pokemon.getFormattedNickname()+"포켓몬을 얻었습니다."), player.getUniqueID());
        player.sendMessage(ITextComponent.getTextComponentOrEmpty(
                "=========== 포켓몬 정보 =========="
        + pokemon.getAbilityName()), player.getUniqueID());
    }
}
