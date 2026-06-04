({
    name: "Tyranitarite",
    spritenum: 607,
    megaStone: { "Tyranitar": "Tyranitar-Mega" },
    itemUser: ["Tyranitar"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 669,
    gen: 6,
    isNonstandard: "Past",
}) 