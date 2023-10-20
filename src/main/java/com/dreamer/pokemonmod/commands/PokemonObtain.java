package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.api.pokemon.requirement.impl.SpeciesRequirement;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.storage.*;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author 박연재
 * @apiNote 포켓몬 지급 명령어
 *
 */
public class PokemonObtain {

    public PokemonObtain(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬설정")
                .then(Commands.literal("지급")
                        .then(Commands.argument("닉네임", StringArgumentType.string())
                                .then(Commands.argument( "포켓몬이름", StringArgumentType.greedyString())
                                        .executes((command) -> {

                                            String nickName = command.getArgument("닉네임", String.class);
                                            String pokemonName = command.getArgument("포켓몬이름", String.class);
                                            return obtain(nickName, pokemonName, command.getSource());
                                        })))));

    }

    private int obtain(String nickName, String pokemonName, CommandSource source) throws CommandException, CommandSyntaxException {

        GameProfile profile;
        profile = PixelmonCommandUtils.findProfile(nickName);

        if (profile == null) {
            throw new CommandException(new StringTextComponent("해당 닉네임을 가진 프로필이 존재하지 않습니다."));
        }

        ServerPlayerEntity player = PixelmonCommandUtils.getEntityPlayer(profile.getId());
        PlayerPartyStorage pps = StorageProxy.getParty(player);    // 프로필

        List<Species> species = Arrays.stream(Pokedex.actualPokedex).collect(Collectors.toList());
        for (Species s : species){
            if(pokemonName.equals(s.getLocalizedName())){
                pokemonName = s.getName();
                break;
            }
        }

        PokemonSpecification spec = PokemonSpecificationProxy.create(pokemonName);  // 영어 이름
        if (!spec.getValue(SpeciesRequirement.class).isPresent()) {
            throw new CommandException(new StringTextComponent("그런 이름을 가진 포켓몬이 존재하지 않습니다."));
        }

        Pokemon pokemon = spec.create();

        if (player != null && BattleRegistry.getBattle(player) != null) {
            StorageProxy.getPCForPlayer(profile.getId()).add(pokemon);
        } else {
            StorageProxy.getParty(profile.getId()).add(pokemon);
        }

        if (!pokemon.isEgg()) {
            PokedexEvent.Pre preEvent = new PokedexEvent.Pre(pps.uuid, pokemon, PokedexRegistrationStatus.CAUGHT, "commandGiven");
            if (!Pixelmon.EVENT_BUS.post(preEvent)) {
                pps.playerPokedex.set(preEvent.getPokemon(), preEvent.getNewStatus());
                pps.playerPokedex.update();
                Pixelmon.EVENT_BUS.post(new PokedexEvent.Post(player.getUniqueID(), preEvent.getOldStatus(), preEvent.getPokemon(), preEvent.getNewStatus(), preEvent.getCause()));
            }
        }
        PixelmonCommandUtils.sendMessage(source,"§e" + profile.getName() + "§f님이, §e" + nickName + "§f님에게 §b" + pokemon.getDisplayName() + " §f포켓몬이 지급되었습니다.", null);
        PixelmonCommandUtils.sendMessage(source, "pixelmon.command.give.givesuccess" + (pokemon.isEgg() ? "egg" : ""), new Object[]{profile.getName(), pokemon.getSpecies().getTranslatedName()});

        return 1;

    }

}
