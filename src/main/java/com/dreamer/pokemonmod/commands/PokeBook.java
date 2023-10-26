package com.dreamer.pokemonmod.commands;

import com.dreamer.pokemonmod.PokemonMod;
import com.dreamer.pokemonmod.log.LogFile;
import com.dreamer.pokemonmod.objects.CreateBook;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.*;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import java.util.*;

public class PokeBook {

    public PokeBook(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓북")
                .then(Commands.argument("슬롯", IntegerArgumentType.integer(1, 6))
                        .executes((command) -> {
                            int slotNumber = command.getArgument("슬롯", Integer.class);
                            return convert(slotNumber, command.getSource());
                        })));
    }

    private int convert(int slotNumber, CommandSource source) throws CommandException, CommandSyntaxException {
        int index = slotNumber-1;

        GameProfile profile;

        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(source);
        profile = player.getGameProfile();

        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());        // 본인의 엔트리
        Pokemon p = PixelmonCommandUtils.require(pps.get(index), "pixelmon.command.partyslot.nothing", player.getName().getString());

        if (pps.countPokemon() == 1){
            throw new CommandException(new StringTextComponent("엔트리의 포켓몬은 최소한 하나는 존재하여야 합니다."));
        }

        if (p == null) {
            throw new CommandException(new StringTextComponent("해당 슬롯의 포켓몬이 존재하지 않습니다."));
        }

        if (PokemonMod.getConfig().getBanPokemon().contains(p.getSpecies().getName())){
            throw new CommandException(new StringTextComponent("변환 금지 포켓몬입니다!"));
        }

        if (p.hasFlag("untradeable")) {
            throw new CommandException(new StringTextComponent("§f[§bSystem§f] 금지 포켓몬은 변환할 수 없습니다."));
        }

        ItemStack pokeBook = CreateBook.getStack(player, p);
        if(player.addItem(pokeBook)){
            pps.set(index, null);
            player.sendMessage(new StringTextComponent("§f[§bSystem§f] §6" + p.getLocalizedName() + "§f을(를) 아이템으로 변환하였습니다 !"), player.getUUID());
            LogFile.pb(p, player, new Date());
        } else {
            player.sendMessage(new StringTextComponent("§f[&bSystem&f] 인벤토리의 공간이 부족합니다 !"), player.getUUID());
        }
        return 1;
    }



}
