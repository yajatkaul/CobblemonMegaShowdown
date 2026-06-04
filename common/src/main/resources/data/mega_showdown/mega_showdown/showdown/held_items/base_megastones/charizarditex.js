({
  name: "Charizardite X",
	spritenum: 585,
	megaStone: { "Charizard": "Charizard-Mega-X" },
	itemUser: ["Charizard"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 660,
	gen: 6,
	isNonstandard: "Past",
})