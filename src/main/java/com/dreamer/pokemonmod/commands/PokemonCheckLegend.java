package com.dreamer.pokemonmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.api.pokemon.requirement.impl.SpeciesRequirement;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
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


        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(source);

        GameProfile profile;

        profile = player.getGameProfile();
        PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());    // 프로필
        PCStorage pcs = StorageProxy.getPCForPlayer(player);                // 스토리지

        Pokemon pokemon = pps.get(slotNumber-1);
        if(pokemon.isLegendary()){
            player.sendMessage(new StringTextComponent("§b" + pokemon.getDisplayName() + " §f의 등급은 §6레전더리 §f입니다"), player.getUUID());
        } else {
            player.sendMessage(new StringTextComponent("§e" + pokemon.getDisplayName() + " §f의 등급은 레전더리가 아닙니다!"), player.getUUID());
        }
        return 1;
    }

}
