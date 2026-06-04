({
    name: "Scizorite",
    spritenum: 605,
    megaStone: { "Scizor": "Scizor-Mega" },
    itemUser: ["Scizor"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 670,
    gen: 6,
    isNonstandard: "Past",
}) 