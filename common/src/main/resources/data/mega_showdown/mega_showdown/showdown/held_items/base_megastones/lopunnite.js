({
    name: "Lopunnite",
    spritenum: 626,
    megaStone: { "Lopunny": "Lopunny-Mega" },
    itemUser: ["Lopunny"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 768,
    gen: 6,
    isNonstandard: "Past",
}) 