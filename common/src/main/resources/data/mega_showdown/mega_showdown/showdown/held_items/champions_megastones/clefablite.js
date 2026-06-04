({
  name: "Clefablite",
	spritenum: 544,
	megaStone: { "Clefable": "Clefable-Mega" },
	itemUser: ["Clefable"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2559,
	gen: 9,
	isNonstandard: "Future",
})