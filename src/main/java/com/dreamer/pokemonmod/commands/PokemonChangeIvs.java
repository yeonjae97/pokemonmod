package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
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

        profile = PixelmonCommandUtils.findProfile(player.getScoreboardName());
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());    // 프로필

        Pokemon pokemon = pps.get(slotNumber-1);

        IVStore iv = pokemon.getIVs().createRandomNewIVs();

        pokemon.getIVs().setStat(BattleStatsType.HP, iv.getStat(BattleStatsType.HP));
        pokemon.getIVs().setStat(BattleStatsType.ATTACK, iv.getStat(BattleStatsType.ATTACK));
        pokemon.getIVs().setStat(BattleStatsType.DEFENSE, iv.getStat(BattleStatsType.DEFENSE));
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_ATTACK, iv.getStat(BattleStatsType.SPECIAL_ATTACK));
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_DEFENSE, iv.getStat(BattleStatsType.SPECIAL_DEFENSE));
        pokemon.getIVs().setStat(BattleStatsType.SPEED, iv.getStat(BattleStatsType.SPEED));

        int realHp = pokemon.getStats().getHP();
        int realAtk = pokemon.getStats().getAttack();
        int realDef = pokemon.getStats().getDefense();
        int realSpecAtk = pokemon.getStats().getSpecialAttack();
        int realSpecDef = pokemon.getStats().getSpecialDefense();
        int realSpeed = pokemon.getStats().getSpeed();

        StringBuilder sb = new StringBuilder();
        sb.append("================= 변경된 §a" + pokemon.getDisplayName() + "§b의 개체값 §f================\n")
            .append("§bhp : §e" + pokemon.getIVs().getStat(BattleStatsType.HP)  + "\n")
            .append("§b공격력 : §e" + pokemon.getIVs().getStat(BattleStatsType.ATTACK)   + "\n")
            .append("§b방어력 : §e" + pokemon.getIVs().getStat(BattleStatsType.DEFENSE)  + "\n")
            .append("§b특수 공격력 : §e" + pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK)  + "\n")
            .append("§b특수 방어력 : §e" + pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE)  + "\n")
            .append("§b이동 속도 : §e" + pokemon.getIVs().getStat(BattleStatsType.SPEED)  + "\n")
                .append("§f================= 변경된 §a" + pokemon.getDisplayName() + "§b의 실제값 §f================\n")
                .append("§bhp : §e" + realHp  + "\n")
                .append("§b공격력 : §e" + realAtk  + "\n")
                .append("§b방어력 : §e" + realDef  + "\n")
                .append("§b특수 공격력 : §e" + realSpecAtk  + "\n")
                .append("§b특수 방어력 : §e" + realSpecDef  + "\n")
                .append("§b이동 속도 : §e" + realSpeed  + "\n")
                .append("§f=========================================================");
        return 1;
    }

}
