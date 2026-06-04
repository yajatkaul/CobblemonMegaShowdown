({
  name: "Gardevoirite",
  spritenum: 587,
  megaStone: { "Gardevoir": "Gardevoir-Mega" },
  itemUser: ["Gardevoir"],
  onTakeItem(item, source) {
  	return !item.megaStone?.[source.baseSpecies.baseSpecies];
  },
  num: 657,
  gen: 6,
  isNonstandard: "Past",
})