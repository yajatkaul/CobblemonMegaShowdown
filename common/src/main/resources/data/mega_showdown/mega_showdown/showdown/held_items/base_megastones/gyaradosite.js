({
    name: "Gyaradosite",
    spritenum: 589,
    megaStone: { "Gyarados": "Gyarados-Mega" },
    itemUser: ["Gyarados"],
    onTakeItem(item, source) {
    	return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 676,
    gen: 6,
    isNonstandard: "Past",
}) 