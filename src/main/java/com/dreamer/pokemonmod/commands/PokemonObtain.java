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
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 *
 * @author 박연재
 * @apiNote 포켓몬 지급 명령어
 *
 */
public class PokemonObtain{
    public PokemonObtain(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("포켓몬설정")
                .then(Commands.literal("지급")
                        .then(Commands.argument("닉네임", StringArgumentType.string())
                                .then(Commands.argument("포켓몬이름", StringArgumentType.string())
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
        PCStorage pcs = StorageProxy.getPCForPlayer(player);                // 스토리지



        PokemonSpecification spec = PokemonSpecificationProxy.create(pokemonName);
        if (!spec.getValue(SpeciesRequirement.class).isPresent()) {
            throw new CommandException(new StringTextComponent("그런 이름을 가진 포켓몬이 존재하지 않습니다."));
        }

        Pokemon pokemon = spec.create();

//        if (player != null && Pixelmon.EVENT_BUS.post(new PokemonReceivedEvent(player, pokemon, "Command"))) {
//            return 0;
//        }
//
//        if(!pps.hasSpace()) {
//            // 엔트리에 공간이 없을 경우 pc로 보내짐
//            StorageProxy.getParty(profile.getId()).add(pokemon);
//        }

//        if(!pcs.hasSpace())
//            throw new CommandException(new StringTextComponent( "PC의 여유 공간이 존재하지 않습니다."));

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

        player.sendMessage(new StringTextComponent(nickName + "님, " + pokemonName + "포켓몬 지급이 완료되었습니다."), player.getUniqueID());
//        PixelmonCommandUtils.notifyCommandListener(player, this, 0, "pixelmon.command.give.notifygive" + (pokemon.isEgg() ? "egg" : ""), new Object[]{profile.getName(), profile.getName(), pokemon.getSpecies().getTranslatedName()});
//        PixelmonCommandUtils.sendMessage(source, "pixelmon.command.give.givesuccess" + (pokemon.isEgg() ? "egg" : ""), new Object[]{profile.getName(), pokemon.getSpecies().getTranslatedName()});
//        PixelmonCommandUtils.notifyCommandListener(source, this, 0, "pixelmon.command.give.notifygive" + (pokemon.isEgg() ? "egg" : ""), new Object[]{source.get, profile.getName(), pokemon.getSpecies().getTranslatedName()});

//        source.sendFeedback(new StringTextComponent(sb.toString()), true);
        return 1;
    }

}
