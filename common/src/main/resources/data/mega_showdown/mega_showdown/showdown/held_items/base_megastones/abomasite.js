({
  name: "Abomasite",
	spritenum: 575,
	megaStone: { "Abomasnow": "Abomasnow-Mega" },
	itemUser: ["Abomasnow"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 674,
	gen: 6,
	isNonstandard: "Past",
})