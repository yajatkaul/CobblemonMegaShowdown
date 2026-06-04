({
    name: "Heracronite",
    spritenum: 590,
    megaStone: { "Heracross": "Heracross-Mega" },
    itemUser: ["Heracross"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 680,
    gen: 6,
    isNonstandard: "Past",
}) 