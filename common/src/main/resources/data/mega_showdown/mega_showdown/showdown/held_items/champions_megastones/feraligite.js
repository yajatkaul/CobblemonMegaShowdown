({
  name: "Feraligite",
	spritenum: 549,
	megaStone: { "Feraligatr": "Feraligatr-Mega" },
	itemUser: ["Feraligatr"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2564,
	gen: 9,
	isNonstandard: "Future",
})