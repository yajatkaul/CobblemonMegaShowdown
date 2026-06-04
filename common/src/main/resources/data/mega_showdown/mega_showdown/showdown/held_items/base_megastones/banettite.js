({
  name: "Banettite",
	spritenum: 582,
	megaStone: { "Banette": "Banette-Mega" },
	itemUser: ["Banette"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 668,
	gen: 6,
	isNonstandard: "Past",
})