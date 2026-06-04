({
  name: "Audinite",
	spritenum: 617,
	megaStone: { "Audino": "Audino-Mega" },
	itemUser: ["Audino"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 757,
	gen: 6,
	isNonstandard: "Past",
})