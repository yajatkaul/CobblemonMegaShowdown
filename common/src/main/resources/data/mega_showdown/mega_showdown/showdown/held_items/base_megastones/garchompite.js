({
  name: "Garchompite",
  spritenum: 573,
  megaStone: { "Garchomp": "Garchomp-Mega" },
  itemUser: ["Garchomp"],
  onTakeItem(item, source) {
  	return !item.megaStone?.[source.baseSpecies.baseSpecies];
  },
  num: 683,
  gen: 6,
  isNonstandard: "Past",
})