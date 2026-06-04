({
    name: "Slowbronite",
    spritenum: 620,
    megaStone: { "Slowbro": "Slowbro-Mega" },
    itemUser: ["Slowbro"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 760,
    gen: 6,
    isNonstandard: "Past",
}) 