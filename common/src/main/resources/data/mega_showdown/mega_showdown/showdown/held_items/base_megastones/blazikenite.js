({
  name: "Blazikenite",
	spritenum: 584,
	megaStone: { "Blaziken": "Blaziken-Mega" },
	itemUser: ["Blaziken"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 664,
	gen: 6,
	isNonstandard: "Past",
})