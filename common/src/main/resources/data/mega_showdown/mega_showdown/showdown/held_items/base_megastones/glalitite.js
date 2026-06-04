({
    name: "Glalitite",
    spritenum: 623,
    megaStone: { "Glalie": "Glalie-Mega" },
    itemUser: ["Glalie"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 763,
    gen: 6,
    isNonstandard: "Past",
})