({
    name: "Sceptilite",
    spritenum: 613,
    megaStone: { "Sceptile": "Sceptile-Mega" },
    itemUser: ["Sceptile"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 753,
    gen: 6,
    isNonstandard: "Past",
}) 