({
    name: "Mewtwonite Y",
    spritenum: 601,
    megaStone: { "Mewtwo": "Mewtwo-Mega-Y" },
    itemUser: ["Mewtwo"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 663,
    gen: 6,
    isNonstandard: "Past",
}) 