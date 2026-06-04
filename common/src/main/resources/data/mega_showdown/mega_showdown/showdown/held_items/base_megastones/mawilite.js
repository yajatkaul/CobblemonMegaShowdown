({
    name: "Mawilite",
    spritenum: 598,
    megaStone: { "Mawile": "Mawile-Mega" },
    itemUser: ["Mawile"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 681,
    gen: 6,
    isNonstandard: "Past",
}) 