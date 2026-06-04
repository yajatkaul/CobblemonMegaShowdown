({
    name: "Salamencite",
    spritenum: 627,
    megaStone: { "Salamence": "Salamence-Mega" },
    itemUser: ["Salamence"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 769,
    gen: 6,
    isNonstandard: "Past",
}) 