({
  name: "Cameruptite",
	spritenum: 625,
	megaStone: { "Camerupt": "Camerupt-Mega" },
	itemUser: ["Camerupt"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 767,
	gen: 6,
	isNonstandard: "Past",
})