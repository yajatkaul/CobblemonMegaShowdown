({
  name: "Dragoninite",
	spritenum: 547,
	megaStone: { "Dragonite": "Dragonite-Mega" },
	itemUser: ["Dragonite"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2562,
	gen: 9,
	isNonstandard: "Future",
})