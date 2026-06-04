({
  name: "Ampharosite",
	spritenum: 580,
	megaStone: { "Ampharos": "Ampharos-Mega" },
	itemUser: ["Ampharos"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 658,
	gen: 6,
	isNonstandard: "Past",
})