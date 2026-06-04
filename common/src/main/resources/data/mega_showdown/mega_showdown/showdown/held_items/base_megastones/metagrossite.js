({
    name: "Metagrossite",
    spritenum: 618,
    megaStone: { "Metagross": "Metagross-Mega" },
    itemUser: ["Metagross"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 758,
    gen: 6,
    isNonstandard: "Past",
}) 