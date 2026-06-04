({
  name: "Alakazite",
	spritenum: 579,
	megaStone: { "Alakazam": "Alakazam-Mega" },
	itemUser: ["Alakazam"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 679,
	gen: 6,
	isNonstandard: "Past",
})