({
  name: "Beedrillite",
	spritenum: 628,
	megaStone: { "Beedrill": "Beedrill-Mega" },
	itemUser: ["Beedrill"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 770,
	gen: 6,
	isNonstandard: "Past",
})