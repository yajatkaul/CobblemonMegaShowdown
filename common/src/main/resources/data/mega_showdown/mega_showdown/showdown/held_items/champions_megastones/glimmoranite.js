({
  name: "Glimmoranite",
	spritenum: 512,
	megaStone: { "Glimmora": "Glimmora-Mega" },
	itemUser: ["Glimmora"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2650,
	gen: 9,
	isNonstandard: "Future",
})