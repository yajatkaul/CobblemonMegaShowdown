({
    name: "Venusaurite",
    spritenum: 608,
    megaStone: { "Venusaur": "Venusaur-Mega" },
    itemUser: ["Venusaur"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 659,
    gen: 6,
    isNonstandard: "Past",
}) 