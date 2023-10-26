package com.dreamer.pokemonmod.objects;

import com.dreamer.pokemonmod.utils.Lore;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

public class CreateBook {

    public static ItemStack getStack(ServerPlayerEntity player, Pokemon pokemon) {

        ItemStack photo = SpriteItemHelper.getPhoto(pokemon);   // 아이템 뽑음
        photo.getTag().putInt("PokeBookName", pokemon.getSpecies().getDex());   // 덱스 번호 가져옴
        CompoundNBT tag = new CompoundNBT();
        pokemon.writeToNBT(tag);                                     // 포켓몬 내에 들어있던 기본 데이터 태그로 저장시킴
        photo.getTag().putString("PokemonNBT", tag.toString());     // 덱스 번호하고 기본 데이터들 다 포함되어 있음

        // 툴팁에 띄워질 설명란이 필요함
        ListNBT lore = new ListNBT();
        String baseLore = Lore.getDesc(player, pokemon);
        lore.add(StringNBT.valueOf( "\"" + baseLore + "\""));
        String name = pokemon.getLocalizedName() + " §6Lv." + pokemon.getPokemonLevel();
        photo.setHoverName(new StringTextComponent("§f[ §6ICY System §f] §a" + name));
        photo.getOrCreateTagElement("display").put("Lore", lore);
        return photo;
    }
}
