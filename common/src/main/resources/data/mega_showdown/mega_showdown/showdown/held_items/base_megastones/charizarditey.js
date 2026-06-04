({
  name: "Charizardite Y",
	spritenum: 586,
	megaStone: { "Charizard": "Charizard-Mega-Y" },
	itemUser: ["Charizard"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 678,
	gen: 6,
	isNonstandard: "Past",
})