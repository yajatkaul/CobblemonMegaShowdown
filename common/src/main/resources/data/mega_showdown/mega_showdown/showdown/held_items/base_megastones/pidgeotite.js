({
    name: "Pidgeotite",
    spritenum: 622,
    megaStone: { "Pidgeot": "Pidgeot-Mega" },
    itemUser: ["Pidgeot"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 762,
    gen: 6,
    isNonstandard: "Past",
}) 