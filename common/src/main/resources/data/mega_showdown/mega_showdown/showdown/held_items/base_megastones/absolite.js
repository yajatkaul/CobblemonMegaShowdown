({
  name: "Absolite",
	spritenum: 576,
	megaStone: { "Absol": "Absol-Mega" },
	itemUser: ["Absol"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 677,
	gen: 6,
	isNonstandard: "Past",
})