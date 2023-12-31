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
import java.text.DecimalFormat;


/**
 *
 * @author 박연재
 * @apiNote 포켓몬 정보 불러오기 병령어
 *
 */
public class PokemonInfo {

    public PokemonInfo(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬")
                .then(Commands.argument("숫자",IntegerArgumentType.integer())
                                .executes((command) -> {
            int pokeNumber = command.getArgument("숫자", Integer.class);
            return pokemonInfo(pokeNumber, command.getSource());
        })));
    }
    private int pokemonInfo(Integer number, CommandSource source) throws CommandSyntaxException {

        if (number < 1 || number > 6) {
            throw new CommandException(new StringTextComponent("유효하지 않는 숫자입니다!"));
        }

        ServerPlayerEntity player = source.getPlayerOrException();

        GameProfile profile;

        profile = player.getGameProfile();
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());

        Pokemon p = pps.get(number-1); // 해당 엔트리 슬롯의 포켓몬을 불러옴

        if (p == null){
            throw new CommandException(new StringTextComponent("해당 슬롯의 엔트리 포켓몬이 존재하지 않습니다!"));
        }

        String ownerName = p.getOwnerName();    // 소유자 이름
        String pokemonName = p.getDisplayName();   // 포켓몬 이름
        String abilityName = p.getAbilityName();    // 능력 이름
        String growName = p.getGrowth().name(); // 성장 이름
        String gender = p.getGender().toString();
        
        if(gender.equalsIgnoreCase("male")){
            gender = "남자";
        } else {
            gender = "여자";
        }
        
        
        String trainer = p.getOriginalTrainer();    // 트레이너명
        String playerUUID = p.getOwnerPlayerUUID().toString();  // 플레이어 UUID
        String trainerUUID = p.getOriginalTrainerUUID().toString(); // 트레이너 UUID
        String speciesName = p.getSpecies().getName();   // 포켓몬 종 이름
        int friendshipVal = p.getFriendship();   // 포켓몬 호감도
        int pokemonLvl = p.getPokemonLevel();   // 포켓몬 레벨

        String healthRate = new DecimalFormat("##.##").format(p.getHealthPercentage());  // 포켓몬 최대 체력
        int hp = p.getIVs().getStat(BattleStatsType.HP);
        int atk = p.getIVs().getStat(BattleStatsType.ATTACK);
        int def = p.getIVs().getStat(BattleStatsType.DEFENSE);
        int specDef = p.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK);
        int specAtk = p.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE);
        int speed = p.getIVs().getStat(BattleStatsType.SPEED);

        int maxHP = p.getMaxHealth();   // 포켓몬 체력
        int currentExp = p.getExperience(); // 포켓몬 현재 경험치
        int leftExp = p.getExperienceToLevelUp();   // 다음 레벨업까지 남은 경험치
        int totalExp =  currentExp + leftExp;   // 총 경험치

        StringBuilder sb = new StringBuilder();
        String header = "========= " + number + "번 포켓몬 정보 ===========";
        String footer = "==================================";
        sb.append(header + "\n").append("포켓몬명 : " + pokemonName + "\n")
                .append("§6소유자명 : " + ownerName + "\n")
                .append("트레이너명 : " + trainer  + "\n")
                .append("플레이어UUID : " + playerUUID  + "\n")
                .append("트레이너UUID : " + trainerUUID  + "\n")
                .append("능력 이름 : " + abilityName  + "\n")
                .append("성장 이름 : " + growName  + "\n")
                .append("포켓몬 종 이름 : " + speciesName  + "\n")
                .append("포켓몬 성별 : " + gender  + "\n")
                .append("포켓몬 레벨 : " + pokemonLvl  + "\n")
                .append("포켓몬 호감도 : " + friendshipVal  + "\n")
                .append("포켓몬 최대 체력 비율 : " + healthRate  + "\n")
                .append("포켓몬 최대 체력 : " + maxHP  + "\n")
                .append("현재 경험치 : " + currentExp  + "\n")
                .append("다음 레벨업까지 남은 경험치 : " + leftExp  + "\n")
                .append("총 경험치 : " + totalExp  + "\n")
                .append("==================================\n")
                .append("================= 개체값 ================\n")
                .append("hp : " + hp  + "\n")
                .append("공격력 : " + atk  + "\n")
                .append("방어력 : " + def  + "\n")
                .append("특수 공격력 : " + specAtk  + "\n")
                .append("특수 방어력 : " + specDef  + "\n")
                .append("이동 속도 : " + speed  + "\n")
                .append(footer);
        player.sendMessage(new StringTextComponent(sb.toString()), player.getUUID());
        return 1;
    }
}
