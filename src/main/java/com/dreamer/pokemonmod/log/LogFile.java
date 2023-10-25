package com.dreamer.pokemonmod.log;

import com.dreamer.pokemonmod.utils.Lore;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.sun.javafx.binding.StringFormatter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class LogFile {

    // 기존 코드
//    public UsePokemonItemLog(ItemStack stack) {
//        LocalDateTime timeNow = LocalDateTime.now();
//        String sDate = DateTimeFormatter.ISO_LOCAL_DATE.format(timeNow);
//        String sTime = DateTimeFormatter.ISO_LOCAL_TIME.format(timeNow);
//
//        try {
//            String pathName = "./config/PokeBook/PokeBookLog/" + sDate +".txt";
//
//            DataInputStream bis = null;
//            DataOutputStream bos = null;
//
//            File file = new File(pathName);
//            if(!file.exists()){ // 파일이 존재하지 않으면
//                file.createNewFile(); // 신규생성
//            }
//            bis = new DataInputStream(new FileInputStream(file));
//            bos = new DataOutputStream(new FileOutputStream(file));
//
//            String line = stack.getTag().getString("lore");
//            while ((line = bis.readUTF()) != null){
//                bos.writeUTF("[" + sTime + "]" + line + "\n");
//            }
//
//            bos.flush();
//
//            bis.close();
//            bos.close();
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }
    public static void Use(Pokemon pokemon, ServerPlayerEntity player, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("[HH시 mm분]");
        String dateStr = simpleDateFormat.format(date);
        String path = "./config/PokeBook/PokeBookLog/" + dateStr;
        File Folder = new File(path);
        File Log = new File(path + "\\PokeToItem.txt");
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
                Log.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter wr = new FileWriter(Log, true);
            String now = simpleDateFormat2.format(date);
            ArrayList<Boolean> crown = new ArrayList<Boolean>();
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.HP));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.ATTACK));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.DEFENSE));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPECIAL_ATTACK));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPECIAL_DEFENSE));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPEED));
            wr.write(now + " [유저 : " + player.getDisplayName().getString() + " ] < 사용 > 포켓몬 : " + pokemon.getLocalizedName() + " 포켓몬이름 : " + pokemon.getFormattedNickname().getString() + " 특성 : " + pokemon.getAbility().getLocalizedName() + " 개체값 : " + pokemon.getIVs().getArray()[0] + " " + pokemon.getIVs().getArray()[1] + " " + pokemon.getIVs().getArray()[2] + " " + pokemon.getIVs().getArray()[3] + " " + pokemon.getIVs().getArray()[4] + " " + pokemon.getIVs().getArray()[5] + " 노력치 : " + pokemon.getEVs().getArray()[0] + " " + pokemon.getEVs().getArray()[1] + " " + pokemon.getEVs().getArray()[2] + " " + pokemon.getEVs().getArray()[3] + " " + pokemon.getEVs().getArray()[4] + " " + pokemon.getEVs().getArray()[5] + " 기술 : " + Lore.getMoves(pokemon) + " 왕관 : " + crown.get(0) + " " + crown.get(1) + " " + crown.get(2) + " " + crown.get(3) + " " + crown.get(4) + " " + crown.get(5) + " 성별 : " + pokemon.getGender().getLocalizedName() + " 성격 : " + pokemon.getNature().getLocalizedName() + " 볼 : " + pokemon.getBall().getLocalizedName() + " 크기 : " + pokemon.getGrowth().getLocalizedName() + " 지닌도구 : " + pokemon.getHeldItem().getDisplayName().getString() + "\n");
            wr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pb(Pokemon pokemon, ServerPlayerEntity player, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("[HH시 mm분]");
        String dateStr = simpleDateFormat.format(date);
        String path = "./config/PokeBook/PokeBookLog/" + dateStr;
        File Folder = new File(path);
        File Log = new File(path + "\\ItemToPoke.txt");
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
                Log.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter wr = new FileWriter(Log, true);
            String now = simpleDateFormat2.format(date);
            ArrayList<Boolean> crown = new ArrayList<Boolean>();
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.HP));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.ATTACK));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.DEFENSE));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPECIAL_ATTACK));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPECIAL_DEFENSE));
            crown.add(pokemon.getIVs().isHyperTrained(BattleStatsType.SPEED));
            wr.write(now + " [유저 : " + player.getDisplayName().getString() + " ] < 변환 > 포켓몬 : " + pokemon.getLocalizedName() + " 포켓몬이름 : " + pokemon.getNickname() + " 특성 : " + pokemon.getAbility().getLocalizedName() + " 개체값 : " + pokemon.getIVs().getArray()[0] + " " + pokemon.getIVs().getArray()[1] + " " + pokemon.getIVs().getArray()[2] + " " + pokemon.getIVs().getArray()[3] + " " + pokemon.getIVs().getArray()[4] + " " + pokemon.getIVs().getArray()[5] + " 노력치 : " + pokemon.getEVs().getArray()[0] + " " + pokemon.getEVs().getArray()[1] + " " + pokemon.getEVs().getArray()[2] + " " + pokemon.getEVs().getArray()[1] + " " + pokemon.getEVs().getArray()[3] + " " + pokemon.getEVs().getArray()[4] + " " + pokemon.getEVs().getArray()[5] + " 기술 : " + Lore.getMoves(pokemon) + " 왕관 : " + crown.get(0) + " " + crown.get(1) + " " + crown.get(2) + " " + crown.get(3) + " " + crown.get(4) + " " + crown.get(5) + " 성별 : " + pokemon.getGender().getLocalizedName() + " 성격 : " + pokemon.getNature().getLocalizedName() + " 볼 : " + pokemon.getBall().getLocalizedName() + " 크기 : " + pokemon.getGrowth().getLocalizedName() + " 지닌도구 : " + pokemon.getHeldItem().getDisplayName().getString() + "\n");
            wr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
