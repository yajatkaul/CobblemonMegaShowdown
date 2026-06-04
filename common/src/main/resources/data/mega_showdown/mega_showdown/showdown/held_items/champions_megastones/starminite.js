({
  name: "Starminite",
	spritenum: 546,
	megaStone: { "Starmie": "Starmie-Mega" },
	itemUser: ["Starmie"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2561,
	gen: 9,
	isNonstandard: "Future",
})