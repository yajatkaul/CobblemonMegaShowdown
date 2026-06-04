({
  name: "Meganiumite",
	spritenum: 548,
	megaStone: { "Meganium": "Meganium-Mega" },
	itemUser: ["Meganium"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2563,
	gen: 9,
	isNonstandard: "Future",
})