({
    name: "Steelixite",
    spritenum: 621,
    megaStone: { "Steelix": "Steelix-Mega" },
    itemUser: ["Steelix"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 761,
    gen: 6,
    isNonstandard: "Past",
}) 