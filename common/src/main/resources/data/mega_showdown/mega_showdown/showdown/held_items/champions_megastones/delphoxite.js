({
  name: "Delphoxite",
	spritenum: 559,
	megaStone: { "Delphox": "Delphox-Mega" },
	itemUser: ["Delphox"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2576,
	gen: 9,
	isNonstandard: "Future",
})