({
  name: "Chesnaughtite",
	spritenum: 558,
	megaStone: { "Chesnaught": "Chesnaught-Mega" },
	itemUser: ["Chesnaught"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2575,
	gen: 9,
	isNonstandard: "Future",
})