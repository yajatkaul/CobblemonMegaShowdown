({
    name: "Meowsticite",
    spritenum: 506,
    megaStone: {
        "Meowstic": "Meowstic-M-Mega",
        "Meowstic-F": "Meowstic-F-Mega"
    },
    itemUser: ["Meowstic", "Meowstic-F"],
    onTakeItem(item, source) {
        return !item.megaStone?.[source.baseSpecies.baseSpecies];
    },
    num: 2643,
    gen: 9,
    isNonstandard: "Future",
})