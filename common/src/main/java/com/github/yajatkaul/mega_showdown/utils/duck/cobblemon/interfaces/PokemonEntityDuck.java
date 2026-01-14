package com.github.yajatkaul.mega_showdown.utils.duck.cobblemon.interfaces;

import com.github.yajatkaul.mega_showdown.render.layerEntities.states.TeraCrystalState;

public interface PokemonEntityDuck {
    boolean mega_showdown$isTeraCrystalPlayed();

    void mega_showdown$setTeraCrystalPlayed(boolean value);

    boolean mega_showdown$isTeraCrystalPass();

    void mega_showdown$setTeraCrystalPass(boolean value);

    double mega_showdown$getAnimCrystalSeconds();

    void mega_showdown$setAnimCrystalSeconds(double value);

    long mega_showdown$getLastCrystalTimeNs();

    void mega_showdown$setLastCrystalTimeNs(long value);

    TeraCrystalState mega_showdown$getTeraCrystalState();
}
