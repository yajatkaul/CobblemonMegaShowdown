({
  name: "Victreebelite",
	spritenum: 545,
	megaStone: { "Victreebel": "Victreebel-Mega" },
	itemUser: ["Victreebel"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2560,
	gen: 9,
	isNonstandard: "Future",
})