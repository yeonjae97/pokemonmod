package com.dreamer.pokemonmod.commands;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.log.LogFile;
import com.dreamer.pokemonmod.objects.CreateBook;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.api.registry.RegistryManager;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.events.pokemon.BottleCapEvent;
import com.pixelmonmod.pixelmon.api.events.pokemon.EVsGainedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.PixelmonPokemonProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.ribbon.Ribbon;
import com.pixelmonmod.pixelmon.api.pokemon.species.abilities.Abilities;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.species.tags.FormTags;
import com.pixelmonmod.pixelmon.api.pokemon.stats.*;
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.registries.PixelmonRegistries;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.registries.PixelmonTileEntities;
import com.pixelmonmod.pixelmon.api.storage.*;
import com.pixelmonmod.pixelmon.api.util.NBTTools;
import com.pixelmonmod.pixelmon.api.util.PixelmonPlayerUtils;
import com.pixelmonmod.pixelmon.api.util.helpers.NBTHelper;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonClientData;
import com.pixelmonmod.pixelmon.client.gui.inventory.InventoryPixelmon;
import com.pixelmonmod.pixelmon.client.gui.inventory.InventoryPixelmonBase;
import com.pixelmonmod.pixelmon.client.gui.inventory.InventoryPixelmonExtendedScreen;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelHolder;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.items.SpriteItemModel;
import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.Texture;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ExitBattlePacket;
import com.pixelmonmod.pixelmon.datafix.ItemStackPokeBallFix;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumPixelmonParticles;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.init.registry.PixelmonRegistry;
import com.pixelmonmod.pixelmon.items.*;
import com.pixelmonmod.pixelmon.items.group.PixelmonItemGroups;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.bukkit.Material;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

public class PokeBook {


    public PokeBook(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓북")
                .then(Commands.argument("슬롯", IntegerArgumentType.integer(1,6))
                                .executes((command) -> {
                                    int slotNumber = command.getArgument("슬롯", Integer.class);
                                    return convert(slotNumber, command.getSource());
                                })));

    }

    private int convert(int slotNumber, CommandSource source) throws CommandException, CommandSyntaxException {

        int index = slotNumber-1;
        GameProfile profile;
//        ServerPlayerEntity senderPlayer = source.asPlayer();
        ServerPlayerEntity senderPlayer = PixelmonCommandUtils.requireEntityPlayer(source);
        profile = senderPlayer.getGameProfile();
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());        // 본인의 엔트리
        Pokemon p = pps.get(index);
        if (p == null) {
            throw new CommandException(new StringTextComponent("해당 슬롯의 포켓몬이 존재하지 않습니다."));
        }



        PlayerInventory ownerInv = senderPlayer.inventory;
//        ItemStack pokeItem = SpriteItemHelper.getPhoto(p);   // pokeItem은 창고에 들어갈 아이템
        ItemStack pokeBook = CreateBook.getStack(senderPlayer, p);

        if(senderPlayer.addItemStackToInventory(pokeBook)){
            pps.set(index, null);
            senderPlayer.sendMessage(new StringTextComponent("§f[§bSystem§f] §6" + p.getLocalizedName() + "§f을(를) 아이템으로 변환하였습니다 !"), senderPlayer.getUniqueID());
            LogFile.pb(p, senderPlayer, new Date());
        } else {
            senderPlayer.sendMessage(new StringTextComponent("§f[&bSystem&f] 인벤토리의 공간이 부족합니다 !"), senderPlayer.getUniqueID());
        }
//        pps.set(index, null);
        return 1;
    }

}
