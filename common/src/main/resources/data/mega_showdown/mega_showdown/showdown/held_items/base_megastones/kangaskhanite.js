({
    name: "Kangaskhanite",
    spritenum: 592,
    megaStone: { "Kangaskhan": "Kangaskhan-Mega" },
    itemUser: ["Kangaskhan"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 675,
    gen: 6,
    isNonstandard: "Past",
}) 