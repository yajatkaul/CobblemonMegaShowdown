({
  name: 'Rusted Sword',
  spritenum: 698,
  onStart(pokemon) {
    if (pokemon.baseSpecies.name !== 'Zacian-Crowned') return;
    const size = pokemon.moveSlots.length;
    for (let i = 0; i < size; i++) {
      const moveSlot = pokemon.moveSlots[i];
      if (moveSlot.id !== 'ironhead') continue;
      const oldMove = this.dex.moves.get(moveSlot.id);
      const newMove = this.dex.moves.get('behemothblade');
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
      (source && source.baseSpecies.num === 888) || pokemon.baseSpecies.num === 888) {
      return false;
    }
    return true;
  },
  itemUser: ['Zacian-Crowned'],
  num: 1103,
  gen: 8
})