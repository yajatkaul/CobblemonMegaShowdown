({
  name: "Aerodactylite",
	spritenum: 577,
	megaStone: { "Aerodactyl": "Aerodactyl-Mega" },
	itemUser: ["Aerodactyl"],
	onTakeItem(item, source) {
		return !item.megaStone?.[source.baseSpecies.baseSpecies];
	},
	num: 672,
	gen: 6,
	isNonstandard: "Past",
})