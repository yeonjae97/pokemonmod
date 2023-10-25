package com.dreamer.pokemonmod.objects;

import com.dreamer.pokemonmod.utils.Lore;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class CreateBook {

    public static ItemStack getStack(ServerPlayerEntity player, Pokemon pokemon) {

        ItemStack photo = SpriteItemHelper.getPhoto(pokemon);   // 아이템 뽑음
        photo.getTag().putInt("PokeBookName", pokemon.getSpecies().getDex());
        CompoundNBT tag = new CompoundNBT();
        pokemon.writeToNBT(tag);
        photo.getTag().putString("PokemonNBT", tag.toString());

        ListNBT lore = new ListNBT();
        String baseLore = Lore.getDesc(player, pokemon);

        lore.add(StringNBT.valueOf( "\"" + baseLore + "\""));

        String name = pokemon.getLocalizedName() + " &6Lv." + pokemon.getPokemonLevel();
        photo.setDisplayName(new StringTextComponent("&f[ &6ICY System &f] &a" + name));
        photo.getOrCreateChildTag("display").put("Lore", lore);
        return photo;

    }
}
