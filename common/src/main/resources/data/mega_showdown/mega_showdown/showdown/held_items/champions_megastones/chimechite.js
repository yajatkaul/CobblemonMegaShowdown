({
  name: "Chimechite",
	spritenum: 498,
	megaStone: { "Chimecho": "Chimecho-Mega" },
	itemUser: ["Chimecho"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2637,
	gen: 9,
	isNonstandard: "Future",
})