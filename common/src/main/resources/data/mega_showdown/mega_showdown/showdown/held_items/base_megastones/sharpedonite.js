({
    name: "Sharpedonite",
    spritenum: 619,
    megaStone: { "Sharpedo": "Sharpedo-Mega" },
    itemUser: ["Sharpedo"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 759,
    gen: 6,
    isNonstandard: "Past",
}) 