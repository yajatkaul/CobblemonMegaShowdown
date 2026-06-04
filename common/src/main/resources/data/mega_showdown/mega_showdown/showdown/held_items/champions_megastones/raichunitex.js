({
  name: "Raichunite X",
	spritenum: 497,
  megaStone: { "Raichu": "Raichu-Mega-X" },
  itemUser: ["Raichu"],
    onTakeItem(item, source) {
    return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
	num: 2636,
	gen: 9,
	isNonstandard: "Future"
}) 