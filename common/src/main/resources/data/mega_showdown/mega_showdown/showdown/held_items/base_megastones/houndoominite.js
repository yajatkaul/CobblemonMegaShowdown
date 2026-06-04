({
    name: "Houndoominite",
    spritenum: 591,
    megaStone: { "Houndoom": "Houndoom-Mega" },
    itemUser: ["Houndoom"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 666,
    gen: 6,
    isNonstandard: "Past",
}) 