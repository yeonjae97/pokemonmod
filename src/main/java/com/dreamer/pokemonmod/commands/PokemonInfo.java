package com.dreamer.pokemonmod.commands;


import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.PixelmonPlayerUtils;
import com.pixelmonmod.pixelmon.command.PixelmonCommands;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

        ServerPlayerEntity player = source.asPlayer();

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
        int hp = p.getStats().getHP();
        int atk = p.getStats().getAttack();
        int def = p.getStats().getDefense();
        int specDef = p.getStats().getSpecialDefense();
        int specAtk = p.getStats().getSpecialAttack();
        int speed = p.getStats().getSpeed();

        int maxHP = p.getMaxHealth();   // 포켓몬 체력
        int currentExp = p.getExperience(); // 포켓몬 현재 경험치
        int leftExp = p.getExperienceToLevelUp();   // 다음 레벨업까지 남은 경험치
        int totalExp =  currentExp + leftExp;   // 총 경험치

        String defaultBase = p.getSpecies().getDefaultForm().getDefaultBaseForm();

        StringBuilder sb = new StringBuilder();
        String header = "========= " + number + "번 포켓몬 정보 ===========";
        String footer = "==================================";
        sb.append(header + "\n").append("포켓몬명 : " + pokemonName + "\n")
                .append("소유자명 : " + ownerName + "\n")
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
        source.sendFeedback(new StringTextComponent(sb.toString()), true);
        return 1;
    }
}
