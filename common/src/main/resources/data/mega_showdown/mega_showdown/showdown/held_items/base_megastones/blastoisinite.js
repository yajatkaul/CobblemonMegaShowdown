({
  name: "Blastoisinite",
	spritenum: 583,
	megaStone: { "Blastoise": "Blastoise-Mega" },
	itemUser: ["Blastoise"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 661,
	gen: 6,
	isNonstandard: "Past",
})