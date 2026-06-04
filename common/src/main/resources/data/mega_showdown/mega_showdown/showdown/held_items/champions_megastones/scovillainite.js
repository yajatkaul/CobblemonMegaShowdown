({
  name: "Scovillainite",
	spritenum: 511,
	megaStone: { "Scovillain": "Scovillain-Mega" },
	itemUser: ["Scovillain"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2647,
	gen: 9,
	isNonstandard: "Future",
})