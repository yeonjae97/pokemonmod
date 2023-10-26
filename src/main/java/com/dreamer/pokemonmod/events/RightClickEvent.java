package com.dreamer.pokemonmod.events;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.log.LogFile;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.PixelmonEventHandler;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom.PixelmonInteraction;
import com.pixelmonmod.pixelmon.listener.PixelmonPlayerTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.http.config.Registry;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RightClickEvent {

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) throws CommandSyntaxException {
        if (event.getItemStack().getTag() == null || !event.getItemStack().getTag().contains("PokeBookName")) {
            return;
        }
        if (event.getSide() == LogicalSide.SERVER) {
            if (event.getHand() == Hand.MAIN_HAND) {
                ItemStack pokeBook = event.getItemStack();
                ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                pokeBook.shrink(1);
                CompoundNBT tag = JsonToNBT.getTagFromJson(pokeBook.getTag().getString("PokemonNBT"));
                Pokemon pokemon = PokemonBuilder.builder().species(event.getItemStack().getTag().getInt("PokeBookName")).build();
                pokemon.readFromNBT(tag);
                StorageProxy.getParty(player.getUniqueID()).add(pokemon);
                player.sendMessage(new StringTextComponent("§6아이템을 포켓몬으로 변환하였습니다 !"), player.getUniqueID());
                LogFile.Use(pokemon, player, new Date());
            } else {
                return;
            }
        }
    }
}
