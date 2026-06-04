({
  name: "Drampanite",
	spritenum: 569,
	megaStone: { "Drampa": "Drampa-Mega" },
	itemUser: ["Drampa"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2585,
	gen: 9,
	isNonstandard: "Future",
})
