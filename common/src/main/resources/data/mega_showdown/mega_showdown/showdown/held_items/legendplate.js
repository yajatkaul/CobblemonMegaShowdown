{
  name: "Legend Plate",
  onTryMove(pokemon, target, move) {
    if (pokemon.name !== "Arceus" || move.id !== "judgment" || !pokemon.hasItem("legendplate")) {
      return;
    }

    function getForme(type) {
      const forme = `Arceus-${type}`;
      const species = this.dex.species.get(`arceus${type.toLowerCase()}`);
      if (species.exists && this.dex.species.get(forme).exists) {
        return forme;
      }
      return null;
    }

    function targetVisualTypes() {
      if (target.terastallized) {
        return [target.terastallized];
      } else if (target.illusion) {
        /* Ignores type-altering effects such as Soak */
        /* Consistent with in-game UI */
        return target.illusion.species.types;
      } else {
        return target.getTypes();
      }
    }
    const seenTypes = targetVisualTypes();

    /**
     * @param {string} attack
     * @param {string[] | string} defend
     * @returns {number} 0 neutral, +1 for each effective, -1 for each resist, -Infinity for immunity
     */
    function computeEffectiveness(attack, defend) {
      return !this.dex.getImmunity(attack, defend) ? -Infinity : this.dex.getEffectiveness(attack, defend);
    }

    /**
     * Returns the attacking types with the best effectiveness against the defending types
     * @param {string[]} attackTypes
     * @param {string[] | string} defend
     * @returns {string[]}
     */
    function mostEffectiveTypes(attackTypes, defend) {
      let best = [];
      let bestEffectiveness = undefined;
      for (const attackType of attackTypes) {
        const effectiveness = computeEffectiveness.call(this, attackType, defend);
        if (bestEffectiveness === undefined || effectiveness > bestEffectiveness) {
          bestEffectiveness = effectiveness;
          best = [attackType];
        } else if (effectiveness === bestEffectiveness) {
          best.push(attackType);
        }
      }
      return best;
    }

    /**
     * Returns the defending types with the best resistance against the attack type
     * @param {string} attackType
     * @param {string[]} defendTypes
     * @returns {string[]}
     */
    function mostResistantTypes(attackType, defendTypes) {
      let best = [];
      let bestEffectiveness = undefined;
      for (const defendType of defendTypes) {
        const effectiveness = computeEffectiveness.call(this, attackType, defendType);
        if (bestEffectiveness === undefined || effectiveness < bestEffectiveness) {
          bestEffectiveness = effectiveness;
          best = [defendType];
        } else if (effectiveness === bestEffectiveness) {
          best.push(defendType);
        }
      }
      return best;
    }

    function computeJudgementType() {
      if (seenTypes.length === 0) {
        return "Normal";
      }

      /* 1. Choose the type that has the best effectiveness against the target */
      const possibleTypes = this.dex.types.names().filter(type => getForme.call(this, type) !== null);
      let bestTypes = mostEffectiveTypes.call(this, possibleTypes, seenTypes);
      if (bestTypes.length === 1) {
        return bestTypes[0];
      }

      /* 2. If tie, choose the type that best resists the target's primary type, then the target's secondary type, then tertiary type */
      for (const toResist of seenTypes) {
        const newBestTypes = mostResistantTypes.call(this, toResist, bestTypes);
        if (newBestTypes.length === 1) {
          return newBestTypes[0];
        }
        bestTypes = newBestTypes;
      }

      /* 3. If tie, choose randomly */
      if (bestTypes.length === 0) {
        return "Normal";
      }
      return this.sample(bestTypes);
    }

    const judgementType = computeJudgementType.call(this);
    const forme = getForme.call(this, judgementType);
    if (forme !== null && pokemon.species.name !== forme) {
      pokemon.formeChange(forme, null, true);
    }
    move.type = judgementType;
  },
  onTakeItem: false,
  num: 2000,
  gen: 9,
}
