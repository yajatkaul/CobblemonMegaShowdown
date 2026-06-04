({
  name: "Gengarite",
  spritenum: 588,
  megaStone: { "Gengar": "Gengar-Mega" },
  itemUser: ["Gengar"],
  onTakeItem(item, source) {
  	return !item.megaStone?.[source.baseSpecies.baseSpecies];
  },
  num: 656,
  gen: 6,
  isNonstandard: "Past",
})