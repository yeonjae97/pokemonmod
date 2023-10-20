package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
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
 * @apiNote 포켓몬 총 보유 갯수 명령어
 *
 */
public class PokemonAmount{
    public PokemonAmount(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬설정")
                .then(Commands.literal("전체검사")
                        .then(Commands.argument("닉네임", StringArgumentType.string())
                                .executes((command) -> {
                                    String nickName = command.getArgument("닉네임", String.class);
                                    return changeIvsAndValue(nickName, command.getSource());
                                }))));

    }

    private int changeIvsAndValue(String nickName, CommandSource source) throws CommandException, CommandSyntaxException {

        GameProfile profile;

        profile = PixelmonCommandUtils.findProfile(nickName);

        if (profile == null) {
            throw new CommandException(new StringTextComponent("그런 이름을 가진 프로필이 존재하지 않습니다."));
        }

        ServerPlayerEntity player = PixelmonCommandUtils.getEntityPlayer(profile.getId());
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());    // 프로필
        PCStorage pcs = StorageProxy.getPCForPlayer(profile.getId());                // 스토리지

        int pokemonEntryAmount = pps.countPokemon();
        int pokemonInBoxAmount = pcs.countPokemon();
        int totalPokemon = pokemonEntryAmount + pokemonInBoxAmount;

        player.sendMessage(new StringTextComponent("§b현재 &a" + nickName + " §f님의 총 포켓몬 갯수는 §a" + totalPokemon + "개 입니다."), player.getUniqueID());
        return 1;
    }
}
