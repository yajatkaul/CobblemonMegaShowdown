({
  name: "Froslassite",
	spritenum: 551,
	megaStone: { "Froslass": "Froslass-Mega" },
	itemUser: ["Froslass"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 2566,
	gen: 9,
	isNonstandard: "Future",
})