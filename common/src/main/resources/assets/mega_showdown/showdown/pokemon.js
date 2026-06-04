"use strict";
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __export = (target, all) => {
  for (var name in all)
    __defProp(target, name, { get: all[name], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);
var pokemon_exports = {};
__export(pokemon_exports, {
  Pokemon: () => Pokemon,
  RESTORATIVE_BERRIES: () => RESTORATIVE_BERRIES
});
module.exports = __toCommonJS(pokemon_exports);
var import_state = require("./state");
var import_dex = require("./dex");
/**
 * Simulator Pokemon
 * Pokemon Showdown - http://pokemonshowdown.com/
 *
 * @license MIT license
 */
const RESTORATIVE_BERRIES = /* @__PURE__ */ new Set([
  "leppaberry",
  "aguavberry",
  "enigmaberry",
  "figyberry",
  "iapapaberry",
  "magoberry",
  "sitrusberry",
  "wikiberry",
  "oranberry"
]);
class Pokemon {
  constructor(set, side) {
    this.getDetails = () => {
      const health = this.getHealth();
      let details = this.details;
      if (this.illusion) {
        const level = this.battle.ruleTable.has("illusionlevelmod") ? this.illusion.level : this.level;
        let displayedSpeciesName = this.illusion.species.name;
        if (displayedSpeciesName === "Greninja-Bond")
          displayedSpeciesName = "Greninja";
        const illusionDetails = displayedSpeciesName + "," + this.illusion.uuid + (level === 100 ? "" : ", L" + level) + (this.illusion.gender === "" ? "" : ", " + this.illusion.gender) + (this.illusion.set.shiny ? ", shiny" : "");
        details = illusionDetails;
      }
      if (this.terastallized)
        details += `, tera:${this.terastallized}`;
      return { side: health.side, secret: `${details}|${health.secret}`, shared: `${details}|${health.shared}` };
    };
    this.getHealth = () => {
      if (!this.hp)
        return { side: this.side.id, secret: "0 fnt", shared: "0 fnt" };
      let secret = `${this.hp}/${this.maxhp}`;
      let shared;
      const ratio = this.hp / this.maxhp;
      if (this.battle.reportExactHP) {
        shared = secret;
      } else if (this.battle.reportPercentages || this.battle.gen >= 8) {
        let percentage = Math.ceil(ratio * 100);
        if (percentage === 100 && ratio < 1) {
          percentage = 99;
        }
        shared = `${percentage}/100`;
      } else {
        const pixels = Math.floor(ratio * 48) || 1;
        shared = `${pixels}/48`;
        if (pixels === 9 && ratio > 0.2) {
          shared += "y";
        } else if (pixels === 24 && ratio > 0.5) {
          shared += "g";
        }
      }
      if (this.status) {
        secret += ` ${this.status}`;
        shared += ` ${this.status}`;
      }
      return { side: this.side.id, secret, shared };
    };
    this.side = side;
    this.battle = side.battle;
    this.m = {};
    const pokemonScripts = this.battle.format.pokemon || this.battle.dex.data.Scripts.pokemon;
    if (pokemonScripts)
      Object.assign(this, pokemonScripts);
    if (typeof set === "string")
      set = { name: set };
    this.baseSpecies = this.battle.dex.species.get(set.species || set.name);
    if (!this.baseSpecies.exists) {
      throw new Error(`Unidentified species: ${this.baseSpecies.name}`);
    }
    this.set = set;
    this.species = this.baseSpecies;
    if (set.name === set.species || !set.name) {
      set.name = this.baseSpecies.baseSpecies;
    }
    this.speciesState = { id: this.species.id };
    this.name = set.name.substr(0, 20);
    this.fullname = this.side.id + ": " + this.name;
    set.level = this.battle.clampIntRange(set.adjustLevel || set.level || 100, 1, 9999);
    this.level = set.level;
    const genders = { M: "M", F: "F", N: "N" };
    this.gender = genders[set.gender] || this.species.gender || (this.battle.random() * 2 < 1 ? "M" : "F");
    if (this.gender === "N")
      this.gender = "";
    this.happiness = typeof set.happiness === "number" ? this.battle.clampIntRange(set.happiness, 0, 255) : 255;
    this.pokeball = this.set.pokeball || "pokeball";
    this.dynamaxLevel = typeof set.dynamaxLevel === "number" ? this.battle.clampIntRange(set.dynamaxLevel, 0, 10) : 10;
    this.gigantamax = this.set.gigantamax || false;
    this.uuid = this.set.uuid || "";
    this.baseMoveSlots = [];
    this.moveSlots = [];
    if (!this.set.moves?.length) {
      throw new Error(`Set ${this.name} has no moves`);
    }
    let i = 0;
    for (const moveid of this.set.moves) {
      let move = this.battle.dex.moves.get(moveid);
      if (!move.id)
        continue;
      if (move.id === "hiddenpower" && move.type !== "Normal") {
        if (!set.hpType)
          set.hpType = move.type;
        move = this.battle.dex.moves.get("hiddenpower");
      }
      let basepp = move.noPPBoosts || move.isZ ? move.pp : move.pp * 8 / 5;
      if (this.battle.gen < 3)
        basepp = Math.min(61, basepp);
      this.baseMoveSlots.push({
        move: move.name,
        id: move.id,
        // COBBLED: Apply move pp
        pp: set.movesInfo[i].pp,
        // COBBLED: Apply
        maxpp: set.movesInfo[i].maxPp,
        target: move.target,
        disabled: false,
        disabledSource: "",
        used: false
      });
      i++;
    }
    this.position = 0;
    let displayedSpeciesName = this.species.name;
    if (displayedSpeciesName === "Greninja-Bond")
      displayedSpeciesName = "Greninja";
    this.details = displayedSpeciesName + ", " + this.uuid + (this.level === 100 ? "" : ", L" + this.level) + (this.gender === "" ? "" : ", " + this.gender) + (this.set.shiny ? ", shiny" : "");
    this.status = "";
    this.statusState = {};
    this.volatiles = {};
    this.showCure = void 0;
    if (!this.set.evs) {
      this.set.evs = { hp: 0, atk: 0, def: 0, spa: 0, spd: 0, spe: 0 };
    }
    if (!this.set.ivs) {
      this.set.ivs = { hp: 31, atk: 31, def: 31, spa: 31, spd: 31, spe: 31 };
    }
    const stats = { hp: 31, atk: 31, def: 31, spe: 31, spa: 31, spd: 31 };
    let stat;
    for (stat in stats) {
      if (!this.set.evs[stat])
        this.set.evs[stat] = 0;
      if (!this.set.ivs[stat] && this.set.ivs[stat] !== 0)
        this.set.ivs[stat] = 31;
    }
    for (stat in this.set.evs) {
      this.set.evs[stat] = this.battle.clampIntRange(this.set.evs[stat], 0, 255);
    }
    for (stat in this.set.ivs) {
      this.set.ivs[stat] = this.battle.clampIntRange(this.set.ivs[stat], 0, 31);
    }
    if (this.battle.gen && this.battle.gen <= 2) {
      for (stat in this.set.ivs) {
        this.set.ivs[stat] &= 30;
      }
    }
    const hpData = this.battle.dex.getHiddenPower(this.set.ivs);
    this.hpType = set.hpType || hpData.type;
    this.hpPower = hpData.power;
    this.baseHpType = this.hpType;
    this.baseHpPower = this.hpPower;
    this.baseStoredStats = null;
    this.storedStats = { atk: 0, def: 0, spa: 0, spd: 0, spe: 0 };
    this.boosts = { atk: 0, def: 0, spa: 0, spd: 0, spe: 0, accuracy: 0, evasion: 0 };
    this.baseAbility = (0, import_dex.toID)(set.ability);
    this.ability = this.baseAbility;
    this.abilityState = { id: this.ability };
    this.item = (0, import_dex.toID)(set.item);
    this.itemState = { id: this.item };
    this.lastItem = "";
    this.usedItemThisTurn = false;
    this.ateBerry = false;
    this.trapped = false;
    this.maybeTrapped = false;
    this.maybeDisabled = false;
    this.illusion = null;
    this.transformed = false;
    this.fainted = false;
    this.faintQueued = false;
    this.subFainted = null;
    this.types = this.baseSpecies.types;
    this.baseTypes = this.types;
    this.addedType = "";
    this.knownType = true;
    this.apparentType = this.baseSpecies.types.join("/");
	this.teraType = this.set.teraType || this.types[0];
    this.switchFlag = false;
    this.forceSwitchFlag = false;
    this.skipBeforeSwitchOutEventFlag = false;
    this.draggedIn = null;
    this.newlySwitched = false;
    this.beingCalledBack = false;
    this.lastMove = null;
    if (this.battle.gen === 2)
      this.lastMoveEncore = null;
    this.lastMoveUsed = null;
    this.moveThisTurn = "";
    this.statsRaisedThisTurn = false;
    this.statsLoweredThisTurn = false;
    this.hurtThisTurn = null;
    this.lastDamage = 0;
    this.attackedBy = [];
    this.timesAttacked = 0;
    this.isActive = false;
    this.activeTurns = 0;
    this.activeMoveActions = 0;
    this.previouslySwitchedIn = 0;
    this.truantTurn = false;
    this.swordBoost = false;
    this.shieldBoost = false;
    this.syrupTriggered = false;
    this.stellarBoostedTypes = [];
    this.isStarted = false;
    this.duringMove = false;
    this.weighthg = 1;
    this.speed = 0;
    this.abilityOrder = 0;
    this.canMegaEvo = this.battle.actions.canMegaEvo(this);
    this.canUltraBurst = this.battle.actions.canUltraBurst(this);
    this.canGigantamax = this.baseSpecies.canGigantamax || null;
    this.canTerastallize = this.battle.actions.canTerastallize(this);
    if (this.battle.gen === 1)
      this.modifiedStats = { atk: 0, def: 0, spa: 0, spd: 0, spe: 0 };
    this.maxhp = 0;
    this.baseMaxhp = 0;
    this.hp = 0;
    this.clearVolatile();
    if (this.set.currentHealth == 0 || this.set.currentHealth) {
      this.hp = this.set.currentHealth;
    } else {
      this.hp = this.maxhp;
    }
    if (!!this.set.status) {
      let status = this.battle.dex.conditions.get(this.set.status);
      this.status = status.id;
      this.statusState = { id: status.id, target: this };
      if (this.set.statusDuration !== -1) {
        this.statusState.duration = this.set.statusDuration;
        this.statusState.startTime = this.set.statusDuration;
        this.statusState.time = this.set.statusDuration;
      }
    }
    if (this.hp == 0) {
      this.status = "fnt";
      this.fainted = true;
    }
  }
  toJSON() {
    return import_state.State.serializePokemon(this);
  }
  get moves() {
    return this.moveSlots.map((moveSlot) => moveSlot.id);
  }
  get baseMoves() {
    return this.baseMoveSlots.map((moveSlot) => moveSlot.id);
  }
  getSlot() {
    const positionOffset = Math.floor(this.side.n / 2) * this.side.active.length;
    const positionLetter = "abcdef".charAt(this.position + positionOffset);
    return this.side.id + positionLetter;
  }
  toString() {
    const fullname = this.uuid;
    return this.isActive ? this.getSlot() + ": " + fullname : this.side.id + ": " + fullname;
  }
  updateSpeed() {
    this.speed = this.getActionSpeed();
  }
  calculateStat(statName, boost, modifier, statUser) {
    statName = (0, import_dex.toID)(statName);
    if (statName === "hp")
      throw new Error("Please read `maxhp` directly");
    let stat = this.storedStats[statName];
    if ("wonderroom" in this.battle.field.pseudoWeather) {
      if (statName === "def") {
        stat = this.storedStats["spd"];
      } else if (statName === "spd") {
        stat = this.storedStats["def"];
      }
    }
    let boosts = {};
    const boostName = statName;
    boosts[boostName] = boost;
    boosts = this.battle.runEvent("ModifyBoost", statUser || this, null, null, boosts);
    boost = boosts[boostName];
    const boostTable = [1, 1.5, 2, 2.5, 3, 3.5, 4];
    if (boost > 6)
      boost = 6;
    if (boost < -6)
      boost = -6;
    if (boost >= 0) {
      stat = Math.floor(stat * boostTable[boost]);
    } else {
      stat = Math.floor(stat / boostTable[-boost]);
    }
    return this.battle.modify(stat, modifier || 1);
  }
  getStat(statName, unboosted, unmodified) {
    statName = (0, import_dex.toID)(statName);
    if (statName === "hp")
      throw new Error("Please read `maxhp` directly");
    let stat = this.storedStats[statName];
    if (unmodified && "wonderroom" in this.battle.field.pseudoWeather) {
      if (statName === "def") {
        statName = "spd";
      } else if (statName === "spd") {
        statName = "def";
      }
    }
    if (!unboosted) {
      const boosts = this.battle.runEvent("ModifyBoost", this, null, null, { ...this.boosts });
      let boost = boosts[statName];
      const boostTable = [1, 1.5, 2, 2.5, 3, 3.5, 4];
      if (boost > 6)
        boost = 6;
      if (boost < -6)
        boost = -6;
      if (boost >= 0) {
        stat = Math.floor(stat * boostTable[boost]);
      } else {
        stat = Math.floor(stat / boostTable[-boost]);
      }
    }
    if (!unmodified) {
      const statTable = { atk: "Atk", def: "Def", spa: "SpA", spd: "SpD", spe: "Spe" };
      stat = this.battle.runEvent("Modify" + statTable[statName], this, null, null, stat);
    }
    if (statName === "spe" && stat > 1e4 && !this.battle.format.battle?.trunc)
      stat = 1e4;
    return stat;
  }
  getActionSpeed() {
    let speed = this.getStat("spe", false, false);
    if (this.battle.field.getPseudoWeather("trickroom")) {
      speed = 1e4 - speed;
    }
    return this.battle.trunc(speed, 13);
  }
  /**
   * Gets the Pokemon's best stat.
   * Moved to its own method due to frequent use of the same code.
   * Used by Beast Boost, Quark Drive, and Protosynthesis.
   */
  getBestStat(unboosted, unmodified) {
    let statName = "atk";
    let bestStat = 0;
    const stats = ["atk", "def", "spa", "spd", "spe"];
    for (const i of stats) {
      if (this.getStat(i, unboosted, unmodified) > bestStat) {
        statName = i;
        bestStat = this.getStat(i, unboosted, unmodified);
      }
    }
    return statName;
  }
  /* Commented out for now until a use for Combat Power is found in Let's Go
  getCombatPower() {
  	let statSum = 0;
  	let awakeningSum = 0;
  	for (const stat in this.stats) {
  		statSum += this.calculateStat(stat, this.boosts[stat as BoostName]);
  		awakeningSum += this.calculateStat(
  			stat, this.boosts[stat as BoostName]) + this.set.evs[stat];
  	}
  	const combatPower = Math.floor(Math.floor(statSum * this.level * 6 / 100) +
  		(Math.floor(awakeningSum) * Math.floor((this.level * 4) / 100 + 2)));
  	return this.battle.clampIntRange(combatPower, 0, 10000);
  }
  */
  getWeight() {
    const weighthg = this.battle.runEvent("ModifyWeight", this, null, null, this.weighthg);
    return Math.max(1, weighthg);
  }
  getMoveData(move) {
    move = this.battle.dex.moves.get(move);
    for (const moveSlot of this.moveSlots) {
      if (moveSlot.id === move.id) {
        return moveSlot;
      }
    }
    return null;
  }
  getMoveHitData(move) {
    if (!move.moveHitData)
      move.moveHitData = {};
    const slot = this.getSlot();
    return move.moveHitData[slot] || (move.moveHitData[slot] = {
      crit: false,
      typeMod: 0,
      zBrokeProtect: false
    });
  }
  alliesAndSelf() {
    return this.side.allies();
  }
  allies() {
    return this.side.allies().filter((ally) => ally !== this);
  }
  adjacentAllies() {
    return this.side.allies().filter((ally) => this.isAdjacent(ally));
  }
  foes(all) {
    return this.side.foes(all);
  }
  adjacentFoes() {
    if (this.battle.activePerHalf <= 2)
      return this.side.foes();
    return this.side.foes().filter((foe) => this.isAdjacent(foe));
  }
  isAlly(pokemon) {
    return !!pokemon && (this.side === pokemon.side || this.side.allySide === pokemon.side);
  }
  isAdjacent(pokemon2) {
    if (this.fainted || pokemon2.fainted)
      return false;
    if (this.battle.activePerHalf <= 2)
      return this !== pokemon2;
    if (this.side === pokemon2.side)
      return Math.abs(this.position - pokemon2.position) === 1;
    return Math.abs(this.position + pokemon2.position + 1 - this.side.active.length) <= 1;
  }
  getUndynamaxedHP(amount) {
    const hp = amount || this.hp;
    if (this.volatiles["dynamax"]) {
      return Math.ceil(hp * this.baseMaxhp / this.maxhp);
    }
    return hp;
  }
  /** Get targets for Dragon Darts */
  getSmartTargets(target, move) {
    const target2 = target.adjacentAllies()[0];
    if (!target2 || target2 === this || !target2.hp) {
      move.smartTarget = false;
      return [target];
    }
    if (!target.hp) {
      move.smartTarget = false;
      return [target2];
    }
    return [target, target2];
  }
  getAtLoc(targetLoc) {
    let side = this.battle.sides[targetLoc < 0 ? this.side.n % 2 : (this.side.n + 1) % 2];
    targetLoc = Math.abs(targetLoc);
    if (targetLoc > side.active.length) {
      targetLoc -= side.active.length;
      side = this.battle.sides[side.n + 2];
    }
    return side.active[targetLoc - 1];
  }
  /**
   * Returns a relative location: 1-3, positive for foe, and negative for ally.
   * Use `getAtLoc` to reverse.
   */
  getLocOf(target) {
    const positionOffset = Math.floor(target.side.n / 2) * target.side.active.length;
    const position = target.position + positionOffset + 1;
    const sameHalf = this.side.n % 2 === target.side.n % 2;
    return sameHalf ? -position : position;
  }
  getMoveTargets(move, target) {
    let targets = [];
    switch (move.target) {
      case "all":
      case "foeSide":
      case "allySide":
      case "allyTeam":
        if (!move.target.startsWith("foe")) {
          targets.push(...this.alliesAndSelf());
        }
        if (!move.target.startsWith("ally")) {
          targets.push(...this.foes(true));
        }
        if (targets.length && !targets.includes(target)) {
          this.battle.retargetLastMove(targets[targets.length - 1]);
        }
        break;
      case "allAdjacent":
        targets.push(...this.adjacentAllies());
      case "allAdjacentFoes":
        targets.push(...this.adjacentFoes());
        if (targets.length && !targets.includes(target)) {
          this.battle.retargetLastMove(targets[targets.length - 1]);
        }
        break;
      case "allies":
        targets = this.alliesAndSelf();
        break;
      default:
        const selectedTarget = target;
        if (!target || target.fainted && !target.isAlly(this) && this.battle.gameType !== "freeforall") {
          const possibleTarget = this.battle.getRandomTarget(this, move);
          if (!possibleTarget)
            return { targets: [], pressureTargets: [] };
          target = possibleTarget;
        }
        if (this.battle.activePerHalf > 1 && !move.tracksTarget) {
          const isCharging = move.flags["charge"] && !this.volatiles["twoturnmove"] && !(move.id.startsWith("solarb") && ["sunnyday", "desolateland"].includes(this.effectiveWeather())) && !(move.id === "electroshot" && ["raindance", "primordialsea"].includes(this.effectiveWeather())) && !(this.hasItem("powerherb") && move.id !== "skydrop");
          if (!isCharging) {
            target = this.battle.priorityEvent("RedirectTarget", this, this, move, target);
          }
        }
        if (move.smartTarget) {
          targets = this.getSmartTargets(target, move);
          target = targets[0];
        } else {
          targets.push(target);
        }
        if (target.fainted && !move.flags["futuremove"]) {
          return { targets: [], pressureTargets: [] };
        }
        if (selectedTarget !== target) {
          this.battle.retargetLastMove(target);
        }
    }
    let pressureTargets = targets;
    if (move.target === "foeSide") {
      pressureTargets = [];
    }
    if (move.flags["mustpressure"]) {
      pressureTargets = this.foes();
    }
    return { targets, pressureTargets };
  }
  ignoringAbility() {
    if (this.battle.gen >= 5 && !this.isActive)
      return true;
    if (this.getAbility().flags["notransform"] && this.transformed)
      return true;
    if (this.getAbility().flags["cantsuppress"])
      return false;
    if (this.volatiles["gastroacid"])
      return true;
    if (this.hasItem("Ability Shield") || this.ability === "neutralizinggas")
      return false;
    for (const pokemon of this.battle.getAllActive()) {
      if (pokemon.ability === "neutralizinggas" && !pokemon.volatiles["gastroacid"] && !pokemon.transformed && !pokemon.abilityState.ending && !this.volatiles["commanding"]) {
        return true;
      }
    }
    return false;
  }
  ignoringItem() {
    return !!(this.itemState.knockedOff || this.battle.gen >= 5 && !this.isActive || !this.getItem().ignoreKlutz && this.hasAbility("klutz") || this.volatiles["embargo"] || this.battle.field.pseudoWeather["magicroom"]);
  }
  deductPP(move, amount, target) {
    const gen = this.battle.gen;
    move = this.battle.dex.moves.get(move);
    const ppData = this.getMoveData(move);
    if (!ppData)
      return 0;
    ppData.used = true;
    if (!ppData.pp && gen > 1)
      return 0;
    if (!amount)
      amount = 1;
    ppData.pp -= amount;
    if (ppData.pp < 0 && gen > 1) {
      amount += ppData.pp;
      ppData.pp = 0;
    }
    return amount;
  }
  moveUsed(move, targetLoc) {
    this.lastMove = move;
    if (this.battle.gen === 2)
      this.lastMoveEncore = move;
    this.lastMoveTargetLoc = targetLoc;
    this.moveThisTurn = move.id;
  }
  gotAttacked(move, damage, source) {
    const damageNumber = typeof damage === "number" ? damage : 0;
    move = this.battle.dex.moves.get(move);
    this.attackedBy.push({
      source,
      damage: damageNumber,
      move: move.id,
      thisTurn: true,
      slot: source.getSlot(),
      damageValue: damage
    });
  }
  getLastAttackedBy() {
    if (this.attackedBy.length === 0)
      return void 0;
    return this.attackedBy[this.attackedBy.length - 1];
  }
  getLastDamagedBy(filterOutSameSide) {
    const damagedBy = this.attackedBy.filter((attacker) => typeof attacker.damageValue === "number" && (filterOutSameSide === void 0 || !this.isAlly(attacker.source)));
    if (damagedBy.length === 0)
      return void 0;
    return damagedBy[damagedBy.length - 1];
  }
  /**
   * This refers to multi-turn moves like SolarBeam and Outrage and
   * Sky Drop, which remove all choice (no dynamax, switching, etc).
   * Don't use it for "soft locks" like Choice Band.
   */
  getLockedMove() {
    const lockedMove = this.battle.runEvent("LockMove", this);
    return lockedMove === true ? null : lockedMove;
  }
  getMoves(lockedMove, restrictData) {
    if (lockedMove) {
      lockedMove = (0, import_dex.toID)(lockedMove);
      this.trapped = true;
      if (lockedMove === "recharge") {
        return [{
          move: "Recharge",
          id: "recharge"
        }];
      }
      for (const moveSlot of this.moveSlots) {
        if (moveSlot.id !== lockedMove)
          continue;
        return [{
          move: moveSlot.move,
          id: moveSlot.id
        }];
      }
      return [{
        move: this.battle.dex.moves.get(lockedMove).name,
        id: lockedMove
      }];
    }
    const moves = [];
    let hasValidMove = false;
    for (const moveSlot of this.moveSlots) {
      let moveName = moveSlot.move;
      if (moveSlot.id === "hiddenpower") {
        moveName = "Hidden Power " + this.hpType;
        if (this.battle.gen < 6)
          moveName += " " + this.hpPower;
      } else if (moveSlot.id === "return" || moveSlot.id === "frustration") {
        const basePowerCallback = this.battle.dex.moves.get(moveSlot.id).basePowerCallback;
        moveName += " " + basePowerCallback(this);
      }
      let target = moveSlot.target;
      if (moveSlot.id === "curse") {
        if (!this.hasType("Ghost")) {
          target = this.battle.dex.moves.get("curse").nonGhostTarget || moveSlot.target;
        }
      } else if (moveSlot.id === "pollenpuff" && this.volatiles["healblock"]) {
        target = "adjacentFoe";
      }
      let disabled = moveSlot.disabled;
      if (this.volatiles["dynamax"]) {
        const canCauseStruggle = ["Encore", "Disable", "Taunt", "Assault Vest", "Belch", "Stuff Cheeks"];
        disabled = this.maxMoveDisabled(moveSlot.id) || disabled && canCauseStruggle.includes(moveSlot.disabledSource);
      } else if (moveSlot.pp <= 0 && !this.volatiles["partialtrappinglock"] || disabled && this.side.active.length >= 2 && this.battle.actions.targetTypeChoices(target)) {
        disabled = true;
      }
      if (!disabled) {
        hasValidMove = true;
      } else if (disabled === "hidden" && restrictData) {
        disabled = false;
      }
      moves.push({
        move: moveName,
        id: moveSlot.id,
        pp: moveSlot.pp,
        maxpp: moveSlot.maxpp,
        target,
        disabled
      });
    }
    return hasValidMove ? moves : [];
  }
  /** This should be passed the base move and not the corresponding max move so we can check how much PP is left. */
  maxMoveDisabled(baseMove) {
    baseMove = this.battle.dex.moves.get(baseMove);
    if (!this.getMoveData(baseMove.id)?.pp)
      return true;
    return !!(baseMove.category === "Status" && (this.hasItem("assaultvest") || this.volatiles["taunt"]));
  }
  getDynamaxRequest(skipChecks) {  
    if (!skipChecks) {
      if (!this.side.canDynamaxNow())
        return;
		if (this.species.isMega || this.species.isPrimal || this.species.forme === "Ultra" || this.getItem().zMove || this.terastallized || (this.canMegaEvo && this.species.baseSpecies !== "Rayquaza")) {
        return;
	  } 
      if (this.species.cannotDynamax || this.illusion?.species.cannotDynamax)
        return;
    }
    const result = { maxMoves: [] };
    let atLeastOne = false;
    for (const moveSlot of this.moveSlots) {
      const move = this.battle.dex.moves.get(moveSlot.id);
      const maxMove = this.battle.actions.getMaxMove(move, this);
      if (maxMove) {
        if (this.maxMoveDisabled(move)) {
          result.maxMoves.push({ move: maxMove.id, target: maxMove.target, disabled: true });
        } else {
          result.maxMoves.push({ move: maxMove.id, target: maxMove.target });
          atLeastOne = true;
        }
      }
    }
    if (!atLeastOne)
      return;
    if (this.canGigantamax)
      result.gigantamax = this.canGigantamax;
    return result;
  }
  getMoveRequestData() {
    let lockedMove = this.getLockedMove();
    const isLastActive = this.isLastActive();
    const canSwitchIn = this.battle.canSwitch(this.side) > 0;
    let moves = this.getMoves(lockedMove, isLastActive);
    if (!moves.length) {
      moves = [{ move: "Struggle", id: "struggle", target: "randomNormal", disabled: false }];
      lockedMove = "struggle";
    }
    const data = {
      moves
    };
    if (isLastActive) {
      if (this.maybeDisabled) {
        data.maybeDisabled = true;
      }
      if (canSwitchIn) {
        if (this.trapped === true) {
          data.trapped = true;
        } else if (this.maybeTrapped) {
          data.maybeTrapped = true;
        }
      }
    } else if (canSwitchIn) {
      if (this.trapped)
        data.trapped = true;
    }
    if (!lockedMove) {
      if (this.canMegaEvo)
        data.canMegaEvo = true;
      if (this.canUltraBurst)
        data.canUltraBurst = true;
      const canZMove = this.battle.actions.canZMove(this);
      if (canZMove)
        data.canZMove = canZMove;
      if (this.getDynamaxRequest())
        data.canDynamax = true;
      if (data.canDynamax || this.volatiles["dynamax"])
        data.maxMoves = this.getDynamaxRequest(true);
      if (this.canTerastallize)
        data.canTerastallize = this.canTerastallize;
    }
    return data;
  }
  getSwitchRequestData(forAlly) {
    const entry = {
      ident: this.fullname,
      details: this.details,
      condition: this.getHealth().secret,
      active: this.position < this.side.active.length,
      stats: {
        atk: this.baseStoredStats["atk"],
        def: this.baseStoredStats["def"],
        spa: this.baseStoredStats["spa"],
        spd: this.baseStoredStats["spd"],
        spe: this.baseStoredStats["spe"]
      },
      moves: this[forAlly ? "baseMoves" : "moves"].map((move) => {
        if (move === "hiddenpower") {
          return move + (0, import_dex.toID)(this.hpType) + (this.battle.gen < 6 ? "" : this.hpPower);
        }
        if (move === "frustration" || move === "return") {
          const basePowerCallback = this.battle.dex.moves.get(move).basePowerCallback;
          return move + basePowerCallback(this);
        }
        return move;
      }),
      baseAbility: this.baseAbility,
      item: this.item,
      pokeball: this.pokeball,
      // COBBLED: send types and baseTypes with request
      baseTypes: this.baseTypes,
      types: this.types
      // ==================================
    };
    if (this.battle.gen > 6)
      entry.ability = this.ability;
    if (this.battle.gen >= 9) {
      entry.commanding = !!this.volatiles["commanding"] && !this.fainted;
      entry.reviving = this.isActive && !!this.side.slotConditions[this.position]["revivalblessing"];
    }
    if (this.battle.gen === 9) {
      entry.teraType = this.teraType;
      entry.terastallized = this.terastallized || "";
    }
    return entry;
  }
  isLastActive() {
    if (!this.isActive)
      return false;
    const allyActive = this.side.active;
    for (let i = this.position + 1; i < allyActive.length; i++) {
      if (allyActive[i] && !allyActive[i].fainted)
        return false;
    }
    return true;
  }
  positiveBoosts() {
    let boosts = 0;
    let boost;
    for (boost in this.boosts) {
      if (this.boosts[boost] > 0)
        boosts += this.boosts[boost];
    }
    return boosts;
  }
  getCappedBoost(boosts) {
    const cappedBoost = {};
    let boostName;
    for (boostName in boosts) {
      const boost = boosts[boostName];
      if (!boost)
        continue;
      cappedBoost[boostName] = this.battle.clampIntRange(this.boosts[boostName] + boost, -6, 6) - this.boosts[boostName];
    }
    return cappedBoost;
  }
  boostBy(boosts) {
    boosts = this.getCappedBoost(boosts);
    let delta = 0;
    let boostName;
    for (boostName in boosts) {
      delta = boosts[boostName];
      this.boosts[boostName] += delta;
    }
    return delta;
  }
  clearBoosts() {
    let boostName;
    for (boostName in this.boosts) {
      this.boosts[boostName] = 0;
    }
  }
  setBoost(boosts) {
    let boostName;
    for (boostName in boosts) {
      this.boosts[boostName] = boosts[boostName];
    }
  }
  copyVolatileFrom(pokemon, switchCause) {
    this.clearVolatile();
    if (switchCause !== "shedtail")
      this.boosts = pokemon.boosts;
    for (const i in pokemon.volatiles) {
      if (switchCause === "shedtail" && i !== "substitute")
        continue;
      if (this.battle.dex.conditions.getByID(i).noCopy)
        continue;
      this.volatiles[i] = { ...pokemon.volatiles[i] };
      if (this.volatiles[i].linkedPokemon) {
        delete pokemon.volatiles[i].linkedPokemon;
        delete pokemon.volatiles[i].linkedStatus;
        for (const linkedPoke of this.volatiles[i].linkedPokemon) {
          const linkedPokeLinks = linkedPoke.volatiles[this.volatiles[i].linkedStatus].linkedPokemon;
          linkedPokeLinks[linkedPokeLinks.indexOf(pokemon)] = this;
        }
      }
    }
    pokemon.clearVolatile();
    for (const i in this.volatiles) {
      const volatile = this.getVolatile(i);
      this.battle.singleEvent("Copy", volatile, this.volatiles[i], this);
    }
  }
  transformInto(pokemon, effect) {
    const species = pokemon.species;
    if (pokemon.fainted || this.illusion || pokemon.illusion || pokemon.volatiles["substitute"] && this.battle.gen >= 5 || pokemon.transformed && this.battle.gen >= 2 || this.transformed && this.battle.gen >= 5 || species.name === "Eternatus-Eternamax" || ["Ogerpon", "Terapagos"].includes(species.baseSpecies) && (this.terastallized || pokemon.terastallized) || this.terastallized === "Stellar") {
      return false;
    }
    if (this.battle.dex.currentMod === "gen1stadium" && (species.name === "Ditto" || this.species.name === "Ditto" && pokemon.moves.includes("transform"))) {
      return false;
    }
    if (!this.setSpecies(species, effect, true))
      return false;
    this.transformed = true;
    this.weighthg = pokemon.weighthg;
    const types = pokemon.getTypes(true, true);
    this.setType(pokemon.volatiles["roost"] ? pokemon.volatiles["roost"].typeWas : types, true);
    this.addedType = pokemon.addedType;
    this.knownType = this.isAlly(pokemon) && pokemon.knownType;
    this.apparentType = pokemon.apparentType;
    let statName;
    for (statName in this.storedStats) {
      this.storedStats[statName] = pokemon.storedStats[statName];
      if (this.modifiedStats)
        this.modifiedStats[statName] = pokemon.modifiedStats[statName];
    }
    this.moveSlots = [];
    this.hpType = this.battle.gen >= 5 ? this.hpType : pokemon.hpType;
    this.hpPower = this.battle.gen >= 5 ? this.hpPower : pokemon.hpPower;
    this.timesAttacked = pokemon.timesAttacked;
    for (const moveSlot of pokemon.moveSlots) {
      let moveName = moveSlot.move;
      if (moveSlot.id === "hiddenpower") {
        moveName = "Hidden Power " + this.hpType;
      }
      this.moveSlots.push({
        move: moveName,
        id: moveSlot.id,
        pp: moveSlot.maxpp === 1 ? 1 : 5,
        maxpp: this.battle.gen >= 5 ? moveSlot.maxpp === 1 ? 1 : 5 : moveSlot.maxpp,
        target: moveSlot.target,
        disabled: false,
        used: false,
        virtual: true
      });
    }
    let boostName;
    for (boostName in pokemon.boosts) {
      this.boosts[boostName] = pokemon.boosts[boostName];
    }
    if (this.battle.gen >= 6) {
      const volatilesToCopy = ["dragoncheer", "focusenergy", "gmaxchistrike", "laserfocus"];
      for (const volatile of volatilesToCopy) {
        if (pokemon.volatiles[volatile]) {
          this.addVolatile(volatile);
          if (volatile === "gmaxchistrike")
            this.volatiles[volatile].layers = pokemon.volatiles[volatile].layers;
        } else {
          this.removeVolatile(volatile);
        }
      }
    }
    if (effect) {
      this.battle.add("-transform", this, pokemon, "[from] " + effect.fullname);
    } else {
      this.battle.add("-transform", this, pokemon);
    }
    if (this.terastallized) {
      this.knownType = true;
      this.apparentType = this.terastallized;
    }
    if (this.battle.gen > 2)
      this.setAbility(pokemon.ability, this, true, true);
    if (this.battle.gen === 4) {
      if (this.species.num === 487) {
        if (this.species.name === "Giratina" && this.item === "griseousorb") {
          this.formeChange("Giratina-Origin");
        } else if (this.species.name === "Giratina-Origin" && this.item !== "griseousorb") {
          this.formeChange("Giratina");
        }
      }
      if (this.species.num === 493) {
        const item = this.getItem();
        const targetForme = item?.onPlate ? "Arceus-" + item.onPlate : "Arceus";
        if (this.species.name !== targetForme) {
          this.formeChange(targetForme);
        }
      }
    }
    if (this.species.baseSpecies === "Ogerpon" && this.canTerastallize)
      this.canTerastallize = false;
    if (this.species.baseSpecies === "Terapagos" && this.canTerastallize)
      this.canTerastallize = false;
    return true;
  }
  /**
   * Changes this Pokemon's species to the given speciesId (or species).
   * This function only handles changes to stats and type.
   * Use formChange to handle changes to ability and sending client messages.
   */
  setSpecies(rawSpecies, source = this.battle.effect, isTransform = false) {
    const species = this.battle.runEvent("ModifySpecies", this, null, source, rawSpecies);
    if (!species)
      return null;
    this.species = species;
    this.setType(species.types, true);
    this.apparentType = rawSpecies.types.join("/");
    this.addedType = species.addedType || "";
    this.knownType = true;
    this.weighthg = species.weighthg;
    const stats = this.battle.spreadModify(this.species.baseStats, this.set);
    if (this.species.maxHP)
      stats.hp = this.species.maxHP;
    if (!this.maxhp) {
      this.baseMaxhp = stats.hp;
      this.maxhp = stats.hp;
      this.hp = stats.hp;
    }
    if (!isTransform)
      this.baseStoredStats = stats;
    let statName;
    for (statName in this.storedStats) {
      this.storedStats[statName] = stats[statName];
      if (this.modifiedStats)
        this.modifiedStats[statName] = stats[statName];
    }
    if (this.battle.gen <= 1) {
      if (this.status === "par")
        this.modifyStat("spe", 0.25);
      if (this.status === "brn")
        this.modifyStat("atk", 0.5);
    }
    this.speed = this.storedStats.spe;
    return species;
  }
  /**
   * Changes this Pokemon's forme to match the given speciesId (or species).
   * This function handles all changes to stats, ability, type, species, etc.
   * as well as sending all relevant messages sent to the client.
   */
  formeChange(speciesId, source = this.battle.effect, isPermanent, message) {
    const rawSpecies = this.battle.dex.species.get(speciesId);
    const species = this.setSpecies(rawSpecies, source);
    if (!species)
      return false;
    if (this.battle.gen <= 2)
      return true;
    const apparentSpecies = this.illusion ? this.illusion.species.name : species.baseSpecies;
    if (isPermanent) {
      this.baseSpecies = rawSpecies;
      this.details = species.name + ", " + this.uuid + (this.level === 100 ? "" : ", L" + this.level) + (this.gender === "" ? "" : ", " + this.gender) + (this.set.shiny ? ", shiny" : "");
      let details = (this.illusion || this).details;
      if (this.terastallized)
        details += `, tera:${this.terastallized}`;
      this.battle.add("detailschange", this, details);
      if (!source) {
      } else if (source.effectType === "Item") {
        this.canTerastallize = null;
        if (source.zMove) {
          this.battle.add("-burst", this, apparentSpecies, species.requiredItem);
          this.moveThisTurnResult = true;
        } else if (source.onPrimal) {
          if (this.illusion) {
            this.ability = "";
            this.battle.add("-primal", this.illusion, species.requiredItem);
          } else {
            this.battle.add("-primal", this, species.requiredItem);
          }
        } else {
          this.battle.add("-mega", this, apparentSpecies, species.requiredItem);
          this.moveThisTurnResult = true;
        }
      } else if (source.effectType === "Status") {
        this.battle.add("-formechange", this, species.name, message);
      }
    } else {
      if (source?.effectType === "Ability") {
        this.battle.add("-formechange", this, species.name, message, `[from] ability: ${source.name}`);
      } else {
        this.battle.add("-formechange", this, this.illusion ? this.illusion.species.name : species.name, message);
      }
    }
    if (isPermanent && (!source || !["disguise", "iceface"].includes(source.id))) {
      if (this.illusion) {
        this.ability = "";
      }
      if (source || !this.getAbility().flags["cantsuppress"])
        this.setAbility(species.abilities["0"], null, true);
      this.baseAbility = (0, import_dex.toID)(species.abilities["0"]);
    }
    if (this.terastallized) {
      this.knownType = true;
      this.apparentType = this.terastallized;
    }
    return true;
  }
  clearVolatile(includeSwitchFlags = true) {
    this.boosts = {
      atk: 0,
      def: 0,
      spa: 0,
      spd: 0,
      spe: 0,
      accuracy: 0,
      evasion: 0
    };
    if (this.battle.gen === 1 && this.baseMoves.includes("mimic") && !this.transformed) {
      const moveslot = this.baseMoves.indexOf("mimic");
      const mimicPP = this.moveSlots[moveslot] ? this.moveSlots[moveslot].pp : 16;
      this.moveSlots = this.baseMoveSlots.slice();
      this.moveSlots[moveslot].pp = mimicPP;
    } else {
      this.moveSlots = this.baseMoveSlots.slice();
    }
    this.transformed = false;
    this.ability = this.baseAbility;
    this.hpType = this.baseHpType;
    this.hpPower = this.baseHpPower;
    if (this.canTerastallize === false)
      this.canTerastallize = this.teraType;
    for (const i in this.volatiles) {
      if (this.volatiles[i].linkedStatus) {
        this.removeLinkedVolatiles(this.volatiles[i].linkedStatus, this.volatiles[i].linkedPokemon);
      }
    }
    if (this.species.name === "Eternatus-Eternamax" && this.volatiles["dynamax"]) {
      this.volatiles = { dynamax: this.volatiles["dynamax"] };
    } else {
      this.volatiles = {};
    }
    if (includeSwitchFlags) {
      this.switchFlag = false;
      this.forceSwitchFlag = false;
    }
    this.lastMove = null;
    if (this.battle.gen === 2)
      this.lastMoveEncore = null;
    this.lastMoveUsed = null;
    this.moveThisTurn = "";
    this.moveLastTurnResult = void 0;
    this.moveThisTurnResult = void 0;
    this.lastDamage = 0;
    this.attackedBy = [];
    this.hurtThisTurn = null;
    this.newlySwitched = true;
    this.beingCalledBack = false;
    this.volatileStaleness = void 0;
    this.setSpecies(this.baseSpecies);
  }
  hasType(type) {
    const thisTypes = this.getTypes();
    if (typeof type === "string") {
      return thisTypes.includes(type);
    }
    for (const typeName of type) {
      if (thisTypes.includes(typeName))
        return true;
    }
    return false;
  }
  /**
   * This function only puts the pokemon in the faint queue;
   * actually setting of this.fainted comes later when the
   * faint queue is resolved.
   *
   * Returns the amount of damage actually dealt
   */
  faint(source = null, effect = null) {
    if (this.fainted || this.faintQueued)
      return 0;
    const d = this.hp;
    this.hp = 0;
    this.switchFlag = false;
    this.faintQueued = true;
    this.battle.faintQueue.push({
      target: this,
      source,
      effect
    });
	if (this.baseSpecies.forme?.startsWith("Mega")) {
		const baseForm = this.battle.dex.species.get(this.baseSpecies.baseSpecies);
		//this.battle.debug(`Reverting ${this.name} from Mega to base species ${baseForm.name} upon fainting`);
		this.formeChange(baseForm.name, null, true);
		this.baseSpecies = baseForm;
		//this.setAbility(baseForm.abilities[0]); // Reset base ability (optional but useful)
		delete this.canMegaEvo;
		//console.log(`[DEBUG] ${this.name} reverted to base species ${baseForm.name} on faint`);
    }
    return d;
  }
  capture() {
    this.hp = 0;
    this.switchFlag = false;
    this.battle.capture(this);
  }
  damage(d, source = null, effect = null) {
    if (!this.hp || isNaN(d) || d <= 0)
      return 0;
    if (d < 1 && d > 0)
      d = 1;
    d = this.battle.trunc(d);
    this.hp -= d;
    if (this.hp <= 0) {
      d += this.hp;
      this.faint(source, effect);
    }
    return d;
  }
  tryTrap(isHidden = false) {
    if (!this.runStatusImmunity("trapped"))
      return false;
    if (this.trapped && isHidden)
      return true;
    this.trapped = isHidden ? "hidden" : true;
    return true;
  }
  hasMove(moveid) {
    moveid = (0, import_dex.toID)(moveid);
    if (moveid.substr(0, 11) === "hiddenpower")
      moveid = "hiddenpower";
    for (const moveSlot of this.moveSlots) {
      if (moveid === moveSlot.id) {
        return moveid;
      }
    }
    return false;
  }
  disableMove(moveid, isHidden, sourceEffect) {
    if (!sourceEffect && this.battle.event) {
      sourceEffect = this.battle.effect;
    }
    moveid = (0, import_dex.toID)(moveid);
    for (const moveSlot of this.moveSlots) {
      if (moveSlot.id === moveid && moveSlot.disabled !== true) {
        moveSlot.disabled = isHidden || true;
        moveSlot.disabledSource = sourceEffect?.name || moveSlot.move;
      }
    }
  }
  /** Returns the amount of damage actually healed */
  heal(d, source = null, effect = null) {
    if (!this.hp)
      return false;
    d = this.battle.trunc(d);
    if (isNaN(d))
      return false;
    if (d <= 0)
      return false;
    if (this.hp >= this.maxhp)
      return false;
    this.hp += d;
    if (this.hp > this.maxhp) {
      d -= this.hp - this.maxhp;
      this.hp = this.maxhp;
    }
    return d;
  }
  /** Sets HP, returns delta */
  sethp(d) {
    if (!this.hp)
      return 0;
    d = this.battle.trunc(d);
    if (isNaN(d))
      return;
    if (d < 1)
      d = 1;
    d = d - this.hp;
    this.hp += d;
    if (this.hp > this.maxhp) {
      d -= this.hp - this.maxhp;
      this.hp = this.maxhp;
    }
    return d;
  }
  trySetStatus(status, source = null, sourceEffect = null) {
    return this.setStatus(this.status || status, source, sourceEffect);
  }
  /** Unlike clearStatus, gives cure message */
  cureStatus(silent = false) {
    if (!this.hp || !this.status)
      return false;
    this.battle.add("-curestatus", this, this.status, silent ? "[silent]" : "[msg]");
    if (this.status === "slp" && this.removeVolatile("nightmare")) {
      this.battle.add("-end", this, "Nightmare", "[silent]");
    }
    this.setStatus("");
    return true;
  }
  setStatus(status, source = null, sourceEffect = null, ignoreImmunities = false) {
    if (!this.hp)
      return false;
    status = this.battle.dex.conditions.get(status);
    if (this.battle.event) {
      if (!source)
        source = this.battle.event.source;
      if (!sourceEffect)
        sourceEffect = this.battle.effect;
    }
    if (!source)
      source = this;
    if (this.status === status.id) {
      if (sourceEffect?.status === this.status) {
        this.battle.add("-fail", this, this.status);
      } else if (sourceEffect?.status) {
        this.battle.add("-fail", source);
        this.battle.attrLastMove("[still]");
      }
      return false;
    }
    if (!ignoreImmunities && status.id && !(source?.hasAbility("corrosion") && ["tox", "psn"].includes(status.id))) {
      if (!this.runStatusImmunity(status.id === "tox" ? "psn" : status.id)) {
        this.battle.debug("immune to status");
        if (sourceEffect?.status) {
          this.battle.add("-immune", this);
        }
        return false;
      }
    }
    const prevStatus = this.status;
    const prevStatusState = this.statusState;
    if (status.id) {
      const result = this.battle.runEvent("SetStatus", this, source, sourceEffect, status);
      if (!result) {
        this.battle.debug("set status [" + status.id + "] interrupted");
        return result;
      }
    }
    this.status = status.id;
    this.statusState = { id: status.id, target: this };
    if (source)
      this.statusState.source = source;
    if (status.duration)
      this.statusState.duration = status.duration;
    if (status.durationCallback) {
      this.statusState.duration = status.durationCallback.call(this.battle, this, source, sourceEffect);
    }
    if (status.id && !this.battle.singleEvent("Start", status, this.statusState, this, source, sourceEffect)) {
      this.battle.debug("status start [" + status.id + "] interrupted");
      this.status = prevStatus;
      this.statusState = prevStatusState;
      return false;
    }
    if (status.id && !this.battle.runEvent("AfterSetStatus", this, source, sourceEffect, status)) {
      return false;
    }
    return true;
  }
  updatePP() {
    this.battle.add("pp_update", `${this.side.id}: ${this.uuid}`, this.moveSlots.map((move) => `${move.id}: ${move.pp}`).join(", "));
  }
  /**
   * Unlike cureStatus, does not give cure message
   */
  clearStatus() {
    if (!this.hp || !this.status)
      return false;
    if (this.status === "slp" && this.removeVolatile("nightmare")) {
      this.battle.add("-end", this, "Nightmare", "[silent]");
    }
    this.setStatus("");
    return true;
  }
  getStatus() {
    return this.battle.dex.conditions.getByID(this.status);
  }
  eatItem(force, source, sourceEffect) {
    if (!this.item || this.itemState.knockedOff)
      return false;
    if (!this.hp && this.item !== "jabocaberry" && this.item !== "rowapberry" || !this.isActive)
      return false;
    if (!sourceEffect && this.battle.effect)
      sourceEffect = this.battle.effect;
    if (!source && this.battle.event && this.battle.event.target)
      source = this.battle.event.target;
    const item = this.getItem();
    if (this.battle.runEvent("UseItem", this, null, null, item) && (force || this.battle.runEvent("TryEatItem", this, null, null, item))) {
      this.battle.add("-enditem", this, item, "[eat]");
      this.battle.singleEvent("Eat", item, this.itemState, this, source, sourceEffect);
      this.battle.runEvent("EatItem", this, null, null, item);
      if (RESTORATIVE_BERRIES.has(item.id)) {
        switch (this.pendingStaleness) {
          case "internal":
            if (this.staleness !== "external")
              this.staleness = "internal";
            break;
          case "external":
            this.staleness = "external";
            break;
        }
        this.pendingStaleness = void 0;
      }
      this.lastItem = this.item;
      this.item = "";
      this.itemState = { id: "", target: this };
      this.usedItemThisTurn = true;
      this.ateBerry = true;
      this.battle.runEvent("AfterUseItem", this, null, null, item);
      return true;
    }
    return false;
  }
  useItem(source, sourceEffect) {
    if (!this.hp && !this.getItem().isGem || !this.isActive)
      return false;
    if (!this.item || this.itemState.knockedOff)
      return false;
    if (!sourceEffect && this.battle.effect)
      sourceEffect = this.battle.effect;
    if (!source && this.battle.event && this.battle.event.target)
      source = this.battle.event.target;
    const item = this.getItem();
    if (this.battle.runEvent("UseItem", this, null, null, item)) {
      switch (item.id) {
        case "redcard":
          this.battle.add("-enditem", this, item, "[of] " + source);
          break;
        default:
          if (item.isGem) {
            this.battle.add("-enditem", this, item, "[from] gem");
          } else {
            this.battle.add("-enditem", this, item);
          }
          break;
      }
      if (item.boosts) {
        this.battle.boost(item.boosts, this, source, item);
      }
      this.battle.singleEvent("Use", item, this.itemState, this, source, sourceEffect);
      this.lastItem = this.item;
      this.item = "";
      this.itemState = { id: "", target: this };
      this.usedItemThisTurn = true;
      this.battle.runEvent("AfterUseItem", this, null, null, item);
      return true;
    }
    return false;
  }
  takeItem(source) {
    if (!this.isActive)
      return false;
    if (!this.item || this.itemState.knockedOff)
      return false;
    if (!source)
      source = this;
    if (this.battle.gen === 4) {
      if ((0, import_dex.toID)(this.ability) === "multitype")
        return false;
      if ((0, import_dex.toID)(source.ability) === "multitype")
        return false;
    }
    const item = this.getItem();
    if (this.battle.runEvent("TakeItem", this, source, null, item)) {
      this.item = "";
      const oldItemState = this.itemState;
      this.itemState = { id: "", target: this };
      this.pendingStaleness = void 0;
      this.battle.singleEvent("End", item, oldItemState, this);
      this.battle.runEvent("AfterTakeItem", this, null, null, item);
      return item;
    }
    return false;
  }
  setItem(item, source, effect) {
    if (!this.hp || !this.isActive)
      return false;
    if (this.itemState.knockedOff)
      return false;
    if (typeof item === "string")
      item = this.battle.dex.items.get(item);
    const effectid = this.battle.effect ? this.battle.effect.id : "";
    if (RESTORATIVE_BERRIES.has("leppaberry")) {
      const inflicted = ["trick", "switcheroo"].includes(effectid);
      const external = inflicted && source && !source.isAlly(this);
      this.pendingStaleness = external ? "external" : "internal";
    } else {
      this.pendingStaleness = void 0;
    }
    const oldItem = this.getItem();
    const oldItemState = this.itemState;
    this.item = item.id;
    this.itemState = { id: item.id, target: this };
    if (oldItem.exists)
      this.battle.singleEvent("End", oldItem, oldItemState, this);
    if (item.id) {
      this.battle.singleEvent("Start", item, this.itemState, this, source, effect);
    }
    return true;
  }
  getItem() {
    return this.battle.dex.items.getByID(this.item);
  }
  hasItem(item) {
    if (Array.isArray(item)) {
      if (!item.map(import_dex.toID).includes(this.item))
        return false;
    } else {
      if ((0, import_dex.toID)(item) !== this.item)
        return false;
    }
    return !this.ignoringItem();
  }
  clearItem() {
    return this.setItem("");
  }
  setAbility(ability, source, isFromFormeChange = false, isTransform = false) {
    if (!this.hp)
      return false;
    if (typeof ability === "string")
      ability = this.battle.dex.abilities.get(ability);
    const oldAbility = this.ability;
    if (!isFromFormeChange) {
      if (ability.flags["cantsuppress"] || this.getAbility().flags["cantsuppress"])
        return false;
    }
    if (!isFromFormeChange && !isTransform) {
      const setAbilityEvent = this.battle.runEvent("SetAbility", this, source, this.battle.effect, ability);
      if (!setAbilityEvent)
        return setAbilityEvent;
    }
    this.battle.singleEvent("End", this.battle.dex.abilities.get(oldAbility), this.abilityState, this, source);
    if (this.battle.effect && this.battle.effect.effectType === "Move" && !isFromFormeChange) {
      this.battle.add("-endability", this, this.battle.dex.abilities.get(oldAbility), "[from] move: " + this.battle.dex.moves.get(this.battle.effect.id));
    }
    this.ability = ability.id;
    this.abilityState = { id: ability.id, target: this };
    if (ability.id && this.battle.gen > 3 && (!isTransform || oldAbility !== ability.id || this.battle.gen <= 4)) {
      this.battle.singleEvent("Start", ability, this.abilityState, this, source);
    }
    this.abilityOrder = this.battle.abilityOrder++;
    return oldAbility;
  }
  getAbility() {
    return this.battle.dex.abilities.getByID(this.ability);
  }
  hasAbility(ability) {
    if (Array.isArray(ability)) {
      if (!ability.map(import_dex.toID).includes(this.ability))
        return false;
    } else {
      if ((0, import_dex.toID)(ability) !== this.ability)
        return false;
    }
    return !this.ignoringAbility();
  }
  clearAbility() {
    return this.setAbility("");
  }
  getNature() {
    return this.battle.dex.natures.get(this.set.nature);
  }
  addVolatile(status, source = null, sourceEffect = null, linkedStatus = null) {
    let result;
    status = this.battle.dex.conditions.get(status);
    if (!this.hp && !status.affectsFainted)
      return false;
    if (linkedStatus && source && !source.hp)
      return false;
    if (this.battle.event) {
      if (!source)
        source = this.battle.event.source;
      if (!sourceEffect)
        sourceEffect = this.battle.effect;
    }
    if (!source)
      source = this;
    if (this.volatiles[status.id]) {
      if (!status.onRestart)
        return false;
      return this.battle.singleEvent("Restart", status, this.volatiles[status.id], this, source, sourceEffect);
    }
    if (!this.runStatusImmunity(status.id)) {
      this.battle.debug("immune to volatile status");
      if (sourceEffect?.status) {
        this.battle.add("-immune", this);
      }
      return false;
    }
    result = this.battle.runEvent("TryAddVolatile", this, source, sourceEffect, status);
    if (!result) {
      this.battle.debug("add volatile [" + status.id + "] interrupted");
      return result;
    }
    this.volatiles[status.id] = { id: status.id, name: status.name, target: this };
    if (source) {
      this.volatiles[status.id].source = source;
      this.volatiles[status.id].sourceSlot = source.getSlot();
    }
    if (sourceEffect)
      this.volatiles[status.id].sourceEffect = sourceEffect;
    if (status.duration)
      this.volatiles[status.id].duration = status.duration;
    if (status.durationCallback) {
      this.volatiles[status.id].duration = status.durationCallback.call(this.battle, this, source, sourceEffect);
    }
    result = this.battle.singleEvent("Start", status, this.volatiles[status.id], this, source, sourceEffect);
    if (!result) {
      delete this.volatiles[status.id];
      return result;
    }
    if (linkedStatus && source) {
      if (!source.volatiles[linkedStatus.toString()]) {
        source.addVolatile(linkedStatus, this, sourceEffect);
        source.volatiles[linkedStatus.toString()].linkedPokemon = [this];
        source.volatiles[linkedStatus.toString()].linkedStatus = status;
      } else {
        source.volatiles[linkedStatus.toString()].linkedPokemon.push(this);
      }
      this.volatiles[status.toString()].linkedPokemon = [source];
      this.volatiles[status.toString()].linkedStatus = linkedStatus;
    }
    return true;
  }
  getVolatile(status) {
    status = this.battle.dex.conditions.get(status);
    if (!this.volatiles[status.id])
      return null;
    return status;
  }
  removeVolatile(status) {
    if (!this.hp)
      return false;
    status = this.battle.dex.conditions.get(status);
    if (!this.volatiles[status.id])
      return false;
    this.battle.singleEvent("End", status, this.volatiles[status.id], this);
    const linkedPokemon = this.volatiles[status.id].linkedPokemon;
    const linkedStatus = this.volatiles[status.id].linkedStatus;
    delete this.volatiles[status.id];
    if (linkedPokemon) {
      this.removeLinkedVolatiles(linkedStatus, linkedPokemon);
    }
    return true;
  }
  removeLinkedVolatiles(linkedStatus, linkedPokemon) {
    linkedStatus = linkedStatus.toString();
    for (const linkedPoke of linkedPokemon) {
      const volatileData = linkedPoke.volatiles[linkedStatus];
      if (!volatileData)
        continue;
      volatileData.linkedPokemon.splice(volatileData.linkedPokemon.indexOf(this), 1);
      if (volatileData.linkedPokemon.length === 0) {
        linkedPoke.removeVolatile(linkedStatus);
      }
    }
  }
  /**
   * Sets a type (except on Arceus, who resists type changes)
   */
  setType(newType, enforce = false) {
    if (!enforce) {
      if (typeof newType === "string" ? newType === "Stellar" : newType.includes("Stellar"))
        return false;
      if (this.battle.gen >= 5 && (this.species.num === 493 || this.species.num === 773) || this.battle.gen === 4 && this.hasAbility("multitype")) {
        return false;
      }
      if (this.terastallized)
        return false;
    }
    if (!newType)
      throw new Error("Must pass type to setType");
    this.types = typeof newType === "string" ? [newType] : newType;
    this.addedType = "";
    this.knownType = true;
    this.apparentType = this.types.join("/");
    return true;
  }
  /** Removes any types added previously and adds another one. */
  addType(newType) {
    if (this.terastallized)
      return false;
    this.addedType = newType;
    return true;
  }
  getTypes(excludeAdded, preterastallized) {
    if (!preterastallized && this.terastallized && this.terastallized !== "Stellar") {
      return [this.terastallized];
    }
    const types = this.battle.runEvent("Type", this, null, null, this.types);
    if (!excludeAdded && this.addedType)
      return types.concat(this.addedType);
    if (types.length)
      return types;
    return [this.battle.gen >= 5 ? "Normal" : "???"];
  }
  isGrounded(negateImmunity = false) {
    if ("gravity" in this.battle.field.pseudoWeather)
      return true;
    if ("ingrain" in this.volatiles && this.battle.gen >= 4)
      return true;
    if ("smackdown" in this.volatiles)
      return true;
    const item = this.ignoringItem() ? "" : this.item;
    if (item === "ironball")
      return true;
    if (!negateImmunity && this.hasType("Flying") && !(this.hasType("???") && "roost" in this.volatiles))
      return false;
    if (this.hasAbility("levitate") && !this.battle.suppressingAbility(this))
      return null;
    if ("magnetrise" in this.volatiles)
      return false;
    if ("telekinesis" in this.volatiles)
      return false;
    return item !== "airballoon";
  }
  isSemiInvulnerable() {
    return this.volatiles["fly"] || this.volatiles["bounce"] || this.volatiles["dive"] || this.volatiles["dig"] || this.volatiles["phantomforce"] || this.volatiles["shadowforce"] || this.isSkyDropped();
  }
  isSkyDropped() {
    if (this.volatiles["skydrop"])
      return true;
    for (const foeActive of this.side.foe.active) {
      if (foeActive.volatiles["skydrop"] && foeActive.volatiles["skydrop"].source === this) {
        return true;
      }
    }
    return false;
  }
  /** Specifically: is protected against a single-target damaging move */
  isProtected() {
    return !!(this.volatiles["protect"] || this.volatiles["detect"] || this.volatiles["maxguard"] || this.volatiles["kingsshield"] || this.volatiles["spikyshield"] || this.volatiles["banefulbunker"] || this.volatiles["obstruct"] || this.volatiles["silktrap"] || this.volatiles["burningbulwark"]);
  }
  /**
   * Like Field.effectiveWeather(), but ignores sun and rain if
   * the Utility Umbrella is active for the Pokemon.
   */
  effectiveWeather(sourceEffect, message) {
    if (!sourceEffect && this.battle.effect) sourceEffect = this.battle.effect;
    const weather = this.battle.field.effectiveWeather();
    if (
      this.battle.activePokemon?.hasAbility('megasol') &&
      sourceEffect &&
      (sourceEffect.id === 'megasol' ||
        sourceEffect.effectType === 'Move' ||
        sourceEffect.effectType === 'Weather') &&
      sourceEffect.id !== 'electroshot'
    ) {
      if (weather !== 'sunnyday' && message)
        this.battle.add('-activate', this, 'ability: Mega Sol');
      return 'sunnyday';
    }
    switch (weather) {
      case 'sunnyday':
      case 'raindance':
      case 'desolateland':
      case 'primordialsea':
        if (this.hasItem('utilityumbrella')) return '';
    }
    return weather;
  }
  runEffectiveness(move) {
    if (this.terastallized && move.type === "Stellar")
      return 1;
    let totalTypeMod = 0;
    for (const type of this.getTypes()) {
      let typeMod = this.battle.dex.getEffectiveness(move, type);
      typeMod = this.battle.singleEvent("Effectiveness", move, null, this, type, move, typeMod);
      totalTypeMod += this.battle.runEvent("Effectiveness", this, type, move, typeMod);
    }
    return totalTypeMod;
  }
  /** false = immune, true = not immune */
  runImmunity(type, message) {
    if (!type || type === "???")
      return true;
    if (!this.battle.dex.types.isName(type)) {
      throw new Error("Use runStatusImmunity for " + type);
    }
    if (this.fainted)
      return false;
    const negateImmunity = !this.battle.runEvent("NegateImmunity", this, type);
    const notImmune = type === "Ground" ? this.isGrounded(negateImmunity) : negateImmunity || this.battle.dex.getImmunity(type, this);
    if (notImmune)
      return true;
    if (!message)
      return false;
    if (notImmune === null) {
      this.battle.add("-immune", this, "[from] ability: Levitate");
    } else {
      this.battle.add("-immune", this);
    }
    return false;
  }
  runStatusImmunity(type, message) {
    if (this.fainted)
      return false;
    if (!type)
      return true;
    if (!this.battle.dex.getImmunity(type, this)) {
      this.battle.debug("natural status immunity");
      if (message) {
        this.battle.add("-immune", this);
      }
      return false;
    }
    const immunity = this.battle.runEvent("Immunity", this, null, null, type);
    if (!immunity) {
      this.battle.debug("artificial status immunity");
      if (message && immunity !== null) {
        this.battle.add("-immune", this);
      }
      return false;
    }
    return true;
  }
  destroy() {
    this.battle = null;
    this.side = null;
  }
}
//# sourceMappingURL=pokemon.js.map
