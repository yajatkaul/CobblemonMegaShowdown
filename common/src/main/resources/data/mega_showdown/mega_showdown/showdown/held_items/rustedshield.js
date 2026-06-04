({
  name: 'Rusted Shield',
  spritenum: 699,
  onStart(pokemon) {
    if (pokemon.baseSpecies.name !== 'Zamazenta-Crowned') return;
    const size = pokemon.moveSlots.length;
    for (let i = 0; i < size; i++) {
      const moveSlot = pokemon.moveSlots[i];
      if (moveSlot.id !== 'ironhead') continue;
      const oldMove = this.dex.moves.get(moveSlot.id);
      const newMove = this.dex.moves.get('behemothbash');
      if (!newMove.exists) continue;
      const ppRatio = oldMove.pp ? moveSlot.maxpp / oldMove.pp : 1;
      const newMaxPP = Math.floor(newMove.pp * ppRatio);
      moveSlot.id = newMove.id;
      moveSlot.pp = newMaxPP;
      moveSlot.maxpp = newMaxPP;
    }
  },
  onTakeItem(item, pokemon, source) {
    if (
      (source && source.baseSpecies.num === 889) || pokemon.baseSpecies.num === 889) {
      return false;
    }
    return true;
  },
  itemUser: ['Zamazenta-Crowned'],
  num: 1104,
  gen: 8,
})