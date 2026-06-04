({
    name: "Latiasite",
    spritenum: 629,
    megaStone: { "Latias": "Latias-Mega" },
    itemUser: ["Latias"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 684,
    gen: 6,
    isNonstandard: "Past",
}) 