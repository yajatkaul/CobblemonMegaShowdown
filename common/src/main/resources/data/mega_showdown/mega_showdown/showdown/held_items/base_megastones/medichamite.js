({
    name: "Medichamite",
    spritenum: 599,
    megaStone: { "Medicham": "Medicham-Mega" },
    itemUser: ["Medicham"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 665,
    gen: 6,
    isNonstandard: "Past",
}) 