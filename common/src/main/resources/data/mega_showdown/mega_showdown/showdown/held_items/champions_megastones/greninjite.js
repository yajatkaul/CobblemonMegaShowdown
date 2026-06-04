({
  name: "Greninjite",
	spritenum: 560,
	megaStone: { "Greninja": "Greninja-Mega" },
	itemUser: ["Greninja"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2577,
	gen: 9,
	isNonstandard: "Future",
})