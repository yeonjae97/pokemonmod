package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
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
 * @apiNote 포켓몬 개체치 값 랜덤으로 변경 명령어
 *
 */
public class PokemonChangeIvs{
    public PokemonChangeIvs(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬설정")
                .then(Commands.literal("개체변경")
                        .then(Commands.argument("슬롯", IntegerArgumentType.integer())
                                .executes((command) -> {
                                    int slot = command.getArgument("슬롯", Integer.class);
                                    return changeIvsAndValue(slot, command.getSource());
                                }))));

    }

    private int changeIvsAndValue(int slotNumber, CommandSource source) throws CommandException, CommandSyntaxException {

        if (slotNumber < 1 || slotNumber > 6) {
            throw new CommandException(new StringTextComponent("유효하지 않는 숫자입니다!"));
        }

        ServerPlayerEntity player = source.asPlayer();

        GameProfile profile;

        profile = player.getGameProfile();
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());    // 프로필

        Pokemon pokemon = pps.get(slotNumber-1);

        int hp = (int)(Math.random()*32);
        int atk = (int)(Math.random()*32);
        int def = (int)(Math.random()*32);
        int specAtk = (int)(Math.random()*32);
        int specDef = (int)(Math.random()*32);
        int speed = (int)(Math.random()*32);

        pokemon.getIVs().setStat(BattleStatsType.HP, hp);
        pokemon.getIVs().setStat(BattleStatsType.ATTACK, atk);
        pokemon.getIVs().setStat(BattleStatsType.DEFENSE, def);
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_ATTACK, specAtk);
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_DEFENSE, specDef);
        pokemon.getIVs().setStat(BattleStatsType.SPEED, speed);

        StringBuilder sb = new StringBuilder();
        sb.append("================= 변경된 §a" + pokemon.getDisplayName() + "§b의 개체값 §f================\n")
            .append("§bhp : §e" + hp  + "\n")
            .append("§b공격력 : §e" + atk  + "\n")
            .append("§b방어력 : §e" + def  + "\n")
            .append("§b특수 공격력 : §e" + specAtk  + "\n")
            .append("§b특수 방어력 : §e" + specDef  + "\n")
            .append("§b이동 속도 : §e" + speed  + "\n")
                .append("§f=========================================================");
        if(pokemon.isLegendary()){
            player.sendMessage(new StringTextComponent(sb.toString()), player.getUniqueID());
        } else {
            player.sendMessage(new StringTextComponent(sb.toString()), player.getUniqueID());
        }
        return 1;
    }

}
