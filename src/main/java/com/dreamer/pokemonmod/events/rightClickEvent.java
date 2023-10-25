package com.dreamer.pokemonmod.events;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.log.LogFile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;

public class rightClickEvent {

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) throws CommandSyntaxException {
        if (event.getItemStack().getTag() == null || !event.getItemStack().getTag().contains("PokeBookName")) {
            return;
        }
//        if (event.getWorld().isRemote()){
        ItemStack pokeBook = event.getItemStack();
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        pokeBook.shrink(1);
        CompoundNBT tag = JsonToNBT.getTagFromJson(pokeBook.getTag().getString("PokemonNBT"));
        Pokemon pokemon = PokemonBuilder.builder().species(event.getItemStack().getTag().getInt("PokeBookName")).build();
        pokemon.readFromNBT(tag);
        StorageProxy.getParty(player).add(pokemon);
        LogFile.Use(pokemon, player, new Date());

    }

}
