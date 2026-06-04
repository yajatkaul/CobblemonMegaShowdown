({
  name: "Aggronite",
	spritenum: 578,
	megaStone: { "Aggron": "Aggron-Mega" },
	itemUser: ["Aggron"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 667,
	gen: 6,
	isNonstandard: "Past",
})