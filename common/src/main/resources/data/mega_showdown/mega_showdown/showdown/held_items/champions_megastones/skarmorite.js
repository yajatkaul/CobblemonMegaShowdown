({
  name: "Skarmorite",
	spritenum: 550,
	megaStone: { "Skarmory": "Skarmory-Mega" },
	itemUser: ["Skarmory"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2565,
	gen: 9,
	isNonstandard: "Future",
})