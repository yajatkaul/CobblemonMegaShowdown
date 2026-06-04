({
  name: "Floettite",
	spritenum: 562,
	megaStone: { "Floette-Eternal": "Floette-Mega" },
	itemUser: ["Floette-Eternal"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2579,
	gen: 9,
	isNonstandard: "Future",
})