({
  name: "Hawluchanite",
	spritenum: 566,
	megaStone: { "Hawlucha": "Hawlucha-Mega" },
	itemUser: ["Hawlucha"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2583,
	gen: 9,
	isNonstandard: "Future",
})