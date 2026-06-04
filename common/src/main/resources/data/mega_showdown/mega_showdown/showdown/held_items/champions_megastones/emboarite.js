({
  name: "Emboarite",
	spritenum: 552,
	megaStone: { "Emboar": "Emboar-Mega" },
	itemUser: ["Emboar"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2569,
	gen: 9,
	isNonstandard: "Future",
})