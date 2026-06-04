({
  name: "Raichunite Y",
	spritenum: 496,
  megaStone: { "Raichu": "Raichu-Mega-Y" },
  itemUser: ["Raichu"],
    onTakeItem(item, source) {
    return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
	num: 2635,
	gen: 9,
	isNonstandard: "Future"
}) 