({
    name: "Pinsirite",
    spritenum: 602,
    megaStone: { "Pinsir": "Pinsir-Mega" },
    itemUser: ["Pinsir"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 671,
    gen: 6,
    isNonstandard: "Past",
}) 