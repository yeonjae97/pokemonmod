package com.dreamer.pokemonmod.config;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@ConfigPath("config/PokeBook/config.yml")
public class Config extends AbstractYamlConfig {

    private List<String> banPokemon = Lists.newArrayList("Arceus", "Ditto");

    public Config() {
        super();
    }

    public List<String> getBanPokemon() {
        return this.banPokemon;
    }
}
