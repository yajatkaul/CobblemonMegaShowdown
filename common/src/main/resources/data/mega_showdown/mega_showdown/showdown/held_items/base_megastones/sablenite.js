({
    name: "Sablenite",
    spritenum: 614,
    megaStone: { "Sableye": "Sableye-Mega" },
    itemUser: ["Sableye"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 754,
    gen: 6,
    isNonstandard: "Past",
}) 