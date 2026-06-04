({
    name: "Lucarionite",
    spritenum: 594,
    megaStone: { "Lucario": "Lucario-Mega" },
    itemUser: ["Lucario"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 673,
    gen: 6,
    isNonstandard: "Past",
}) 