({
  name: "Altarianite",
	spritenum: 615,
	megaStone: { "Altaria": "Altaria-Mega" },
	itemUser: ["Altaria"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 755,
	gen: 6,
	isNonstandard: "Past",
})