package com.dreamer.pokemonmod.utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.items.BottlecapItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Lore {
    public static String getMoves(Pokemon pokemon) {
        StringBuilder movesBuilder = new StringBuilder();
        for (Attack attack : pokemon.getMoveset()) {
            if(attack != null) {
                String pps = "[" + attack.pp + "/" + attack.getMaxPP() + "]";
                movesBuilder.append(attack.getActualMove().getLocalizedName()).append(pps).append(" , ");
                continue;
            }
            movesBuilder.append("없음 ,");
        }

        String moves = movesBuilder.toString();
        moves = moves.substring(0, moves.length() - 2);
        return moves;
    }

    public static String getDesc(ServerPlayerEntity senderPlayer, Pokemon p){

        String pokemonName = p.getLocalizedName();
        String gender = p.getGender().getLocalizedName();  // 성별
        short friendship = (short)p.getFriendship();  // 친밀도
        int lvl = p.getPokemonLevel();    // 레벨
        String pokemonsBall = p.getBall().getLocalizedName();    // 볼타임
        String growth = p.getGrowth().getLocalizedName();  // 성장 타입
        String checkIrochi = p.isShiny() ? "O":"X";    // 이로치
        String nature = p.getNature().getLocalizedName(); // 성격
        String form = p.isDefaultForm() ? "없음" : "있음";
        String skin = p.isDefaultPalette() ? "없음" : "있음";
        String heldItem = p.getHeldItemAsItemHeld().getLocalizedName();   // 툴팁 이름 가져옴
        String ability = p.getAbility().getLocalizedName();                 // 특성 이름 가져옴 ( 현재 영어로 가져옴 )

        StringBuilder sb = new StringBuilder();
        sb.append("§c폼 §f: " + form + " §f| ").append("§e이로치 §f: " + checkIrochi + " §f| ").append("§b스킨 : §f" + skin  + "\n")
                .append("§b성별 §f: " + gender  + "\n")
                .append("§3성격 §f: " + nature  + "\n")
                .append("§c크기 §f: " + growth  + "\n")
                .append("§c지닌물건 §f: " + (heldItem.equals("item.minecraft.air") ? "없음" : heldItem) + "\n")
                .append("§4특성 §f: " + ability + "\n")
                .append("§6기술 §f: ");

        // 기술과 관련된건 질문!
        Attack[] attacks = p.getMoveset().attacks;
        for (int i = 0; i < attacks.length; i++) {
            if (attacks[i] == null) {
                break;
            }
            if (i == attacks.length-1){
                sb.append(attacks[i].getMove().getLocalizedName() + "[" + attacks[i].pp + "/" + attacks[i].getActualMove().getPPBase() + "]");
                break;
            }
            sb.append(attacks[i].getMove().getLocalizedName() + "[" + attacks[i].pp + "/" + attacks[i].getActualMove().getPPBase() + "], ");
        }
        sb.append("\n§c개체값 §f: ");
        int ivHyperAmount = 0;
        for (int i = 0; i < p.getIVs().getArray().length; i++) {
            sb.append(p.getIVs().getStat(BattleStatsType.EV_IV_STATS[i]));
            BottlecapItem bci = PixelmonItems.silver_bottle_cap;

            if(p.getIVs().getStat(BattleStatsType.EV_IV_STATS[i]) == p.getIVs().MAX_IVS) {
                ivHyperAmount += 1;
            }
            if (i == p.getIVs().getArray().length-1) {
                sb.append(" §6(" + ivHyperAmount + "V)");
                break;
            }
            sb.append(" / ");

        }

        sb.append("\n§d노력치 §f: ");
        for (int i = 0; i < p.getEVs().getArray().length; i++) {
            sb.append(p.getEVs().getStat(BattleStatsType.EV_IV_STATS[i]));
            if (i == p.getIVs().getArray().length-1) {
                float percentage = (p.getEVs().getTotal() / 510) * 100;
                String sPer = new DecimalFormat("0.0").format(percentage);
                sb.append(" §6(" + p.getEVs().getTotal() + "/510 " + "[" + sPer + "%])");
                break;
            }
            sb.append(" / ");
        }

        sb.append("\n§9왕관 §f: ");
        for (int i = 0;  i < p.getIVs().getArray().length; i++) {
            if (p.getIVs().isHyperTrained(BattleStatsType.EV_IV_STATS[i])) {
                sb.append("§bO");
            } else {
                sb.append("§bX");
            }
        }

        sb.append("\n§b친밀도 §f: " + friendship  + " §f| ").append("§2포켓볼 §f: " + pokemonsBall  + "\n");
        return sb.toString();
    }


}
