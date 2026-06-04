({
    name: "Chandelurite",
	  spritenum: 557,
	  megaStone: { "Chandelure": "Chandelure-Mega" },
	  itemUser: ["Chandelure"],
	  onTakeItem(item, source) {
	    return !item.megaStone?.[source.baseSpecies.baseSpecies];
	  },
	  num: 2574,
	  gen: 9,
	  isNonstandard: "Future",
})