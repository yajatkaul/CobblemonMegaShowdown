({
    name: "Swampertite",
    spritenum: 612,
    megaStone: { "Swampert": "Swampert-Mega" },
    itemUser: ["Swampert"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 752,
    gen: 6,
    isNonstandard: "Past",
}) 