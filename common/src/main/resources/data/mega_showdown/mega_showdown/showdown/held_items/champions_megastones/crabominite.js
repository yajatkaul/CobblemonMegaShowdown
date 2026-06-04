({
  name: "Crabominite",
	spritenum: 507,
	megaStone: { "Crabominable": "Crabominable-Mega" },
	itemUser: ["Crabominable"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2644,
	gen: 9,
	isNonstandard: "Future",
})