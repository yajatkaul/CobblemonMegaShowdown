({
  name: "Diancite",
	spritenum: 624,
	megaStone: { "Diancie": "Diancie-Mega" },
	itemUser: ["Diancie"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 764,
	gen: 6,
	isNonstandard: "Past",
})