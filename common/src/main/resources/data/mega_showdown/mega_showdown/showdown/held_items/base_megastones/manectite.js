({
    name: "Manectite",
    spritenum: 596,
    megaStone: { "Manectric": "Manectric-Mega" },
    itemUser: ["Manectric"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 682,
    gen: 6,
    isNonstandard: "Past",
}) 