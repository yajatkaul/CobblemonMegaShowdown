({
    name: "Mewtwonite X",
    spritenum: 600,
    megaStone: { "Mewtwo": "Mewtwo-Mega-X" },
    itemUser: ["Mewtwo"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 662,
    gen: 6,
    isNonstandard: "Past",
}) 