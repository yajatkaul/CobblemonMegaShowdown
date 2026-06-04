({
  name: "Excadrite",
	spritenum: 553,
	megaStone: { "Excadrill": "Excadrill-Mega" },
	itemUser: ["Excadrill"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2570,
	gen: 9,
	isNonstandard: "Future",
})