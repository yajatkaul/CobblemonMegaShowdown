({
    name: "Latiosite",
    spritenum: 630,
    megaStone: { "Latios": "Latios-Mega" },
    itemUser: ["Latios"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 685,
    gen: 6,
    isNonstandard: "Past",
}) 