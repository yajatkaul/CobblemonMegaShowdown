({
  name: "Galladite",
	spritenum: 616,
	megaStone: { "Gallade": "Gallade-Mega" },
	itemUser: ["Gallade"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 756,
	gen: 6,
	isNonstandard: "Past",
})