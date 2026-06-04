({
  name: "Golurkite",
	spritenum: 505,
	megaStone: { "Golurk": "Golurk-Mega" },
	itemUser: ["Golurk"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2642,
	gen: 9,
	isNonstandard: "Future",
})