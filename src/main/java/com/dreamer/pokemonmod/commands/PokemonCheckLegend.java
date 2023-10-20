package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 *
 * @author 박연재
 * @apiNote 포켓몬 레전더리 체크 명령어
 *
 */
public class PokemonCheckLegend {
    public PokemonCheckLegend(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬설정")
                .then(Commands.literal("검사")
                        .then(Commands.argument("슬롯", IntegerArgumentType.integer())
                                        .executes((command) -> {
                                            int slot = command.getArgument("슬롯", Integer.class);
                                            return check(slot, command.getSource());
                                        }))));

    }

    private int check(int slotNumber, CommandSource source) throws CommandException, CommandSyntaxException {

        if (slotNumber < 1 || slotNumber > 6) {
            throw new CommandException(new StringTextComponent("유효하지 않는 숫자입니다!"));
        }

        ServerPlayerEntity player = source.asPlayer();

        GameProfile profile;

        profile = player.getGameProfile();
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());    // 프로필

        Pokemon pokemon = pps.get(slotNumber-1);

        if(pokemon.isLegendary()){
            player.sendMessage(new StringTextComponent("§b" + pokemon.getDisplayName() + " §f의 등급은 §6레전더리 §f입니다"), player.getUniqueID());
        } else {
            player.sendMessage(new StringTextComponent("§e" + pokemon.getDisplayName() + " §f의 등급은 레전더리가 아닙니다!"), player.getUniqueID());
        }
        return 1;
    }

}
