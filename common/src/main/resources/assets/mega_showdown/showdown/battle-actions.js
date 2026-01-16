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
  if ((from && typeof from === "object") || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, {
          get: () => from[key],
          enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable,
        });
  }
  return to;
};
var __toCommonJS = (mod) =>
  __copyProps(__defProp({}, "__esModule", { value: true }), mod);
var battle_actions_exports = {};
__export(battle_actions_exports, {
  BattleActions: () => BattleActions,
  gmaxMap: () => gmaxMap,
  MAX_MOVES: () => MAX_MOVES,
  Z_MOVES: () => Z_MOVES
});
module.exports = __toCommonJS(battle_actions_exports);
var import_dex = require("./dex");
const CHOOSABLE_TARGETS = /* @__PURE__ */ new Set([
  "normal",
  "any",
  "adjacentAlly",
  "adjacentAllyOrSelf",
  "adjacentFoe",
]);
const gmaxMap = {
  venusaur: "gmaxvinelash",
  charizard: "gmaxwildfire",
  blastoise: "gmaxcannonade",
  butterfree: "gmaxbefuddle",
  pikachu: "gmaxvoltcrash",
  meowth: "gmaxgoldrush",
  machamp: "gmaxchistrike",
  gengar: "gmaxterror",
  kingler: "gmaxfoamburst",
  lapras: "gmaxresonance",
  eevee: "gmaxcuddle",
  snorlax: "gmaxreplenish",
  garbodor: "gmaxmalodor",
  melmetal: "gmaxmeltdown",
  copperajah: "gmaxsteelsurge",
  duraludon: "gmaxdepletion",
  corviknight: "gmaxwindrage",
  orbeetle: "gmaxgravitas",
  toxtricity: "gmaxstunshock",
  toxtricitylowkey: "gmaxstunshock",
  centiskorch: "gmaxcentiferno",
  hatterene: "gmaxsmite",
  grimmsnarl: "gmaxsnooze",
  alcremie: "gmaxfinale",
  alcremierubycream: "gmaxfinale",
  alcremiematchacream: "gmaxfinale",
  alcremiemintcream: "gmaxfinale",
  alcremielemoncream: "gmaxfinale",
  alcremiesaltedcream: "gmaxfinale",
  alcremierubyswirl: "gmaxfinale",
  alcremiecaramelswirl: "gmaxfinale",
  alcremierainbowswirl: "gmaxfinale",
  drednaw: "gmaxstonesurge",
  coalossal: "gmaxvolcalith",
  flapple: "gmaxtartness",
  appletun: "gmaxsweetness",
  sandaconda: "gmaxsandblast",
  inteleon: "gmaxhydrosnipe",
  cinderace: "gmaxfireball",
  rillaboom: "gmaxdrumsolo",
  urshifu: "gmaxoneblow",
  urshifurapidstrike: "gmaxrapidflow",
};
const MAX_MOVES = {
    Flying: "Max Airstream",
    Dark: "Max Darkness",
    Fire: "Max Flare",
    Bug: "Max Flutterby",
    Water: "Max Geyser",
    Status: "Max Guard",
    Ice: "Max Hailstorm",
    Fighting: "Max Knuckle",
    Electric: "Max Lightning",
    Psychic: "Max Mindstorm",
    Poison: "Max Ooze",
    Grass: "Max Overgrowth",
    Ghost: "Max Phantasm",
    Ground: "Max Quake",
    Rock: "Max Rockfall",
    Fairy: "Max Starfall",
    Steel: "Max Steelspike",
    Normal: "Max Strike",
    Dragon: "Max Wyrmwind",
    Newt: "Twinkle Tackle"
};
const Z_MOVES = {
    Poison: "Acid Downpour",
    Fighting: "All-Out Pummeling",
    Dark: "Black Hole Eclipse",
    Grass: "Bloom Doom",
    Normal: "Breakneck Blitz",
    Rock: "Continental Crush",
    Steel: "Corkscrew Crash",
    Dragon: "Devastating Drake",
    Electric: "Gigavolt Havoc",
    Water: "Hydro Vortex",
    Fire: "Inferno Overdrive",
    Ghost: "Never-Ending Nightmare",
    Bug: "Savage Spin-Out",
    Psychic: "Shattered Psyche",
    Ice: "Subzero Slammer",
    Flying: "Supersonic Skystrike",
    Ground: "Tectonic Rage",
    Fairy: "Twinkle Tackle",
    Newt: "Twinkle Tackle"
};
class BattleActions {
  constructor(battle) {
    this.battle = battle;
    this.dex = battle.dex;
    if (this.dex.data.Scripts.actions)
      Object.assign(this, this.dex.data.Scripts.actions);
    if (battle.format.actions) Object.assign(this, battle.format.actions);
  }
  // #region SWITCH
  // ==================================================================
  switchIn(pokemon, pos, sourceEffect = null, isDrag) {
    if (!pokemon || pokemon.isActive) {
      this.battle.hint(
        "A switch failed because the Pok\xE9mon trying to switch in is already in."
      );
      return false;
    }
    const side = pokemon.side;
    if (pos >= side.active.length) {
      throw new Error(`Invalid switch position ${pos} / ${side.active.length}`);
    }
    const oldActive = side.active[pos];
    const unfaintedActive = oldActive?.hp ? oldActive : null;
    if (unfaintedActive) {
      oldActive.beingCalledBack = true;
      let switchCopyFlag = false;
      if (sourceEffect && typeof sourceEffect.selfSwitch === "string") {
        switchCopyFlag = sourceEffect.selfSwitch;
      }
      if (!oldActive.skipBeforeSwitchOutEventFlag && !isDrag) {
        this.battle.runEvent("BeforeSwitchOut", oldActive);
        if (this.battle.gen >= 5) {
          this.battle.eachEvent("Update");
        }
      }
      oldActive.skipBeforeSwitchOutEventFlag = false;
      if (!this.battle.runEvent("SwitchOut", oldActive)) {
        return false;
      }
      if (!oldActive.hp) {
        return "pursuitfaint";
      }
      oldActive.illusion = null;
      this.battle.singleEvent(
        "End",
        oldActive.getAbility(),
        oldActive.abilityState,
        oldActive
      );
      this.battle.queue.cancelAction(oldActive);
      let newMove = null;
      if (this.battle.gen === 4 && sourceEffect) {
        newMove = oldActive.lastMove;
      }
      if (switchCopyFlag) {
        pokemon.copyVolatileFrom(oldActive, switchCopyFlag);
      }
      if (newMove) pokemon.lastMove = newMove;
      oldActive.clearVolatile();
    }
    if (oldActive) {
      oldActive.isActive = false;
      oldActive.isStarted = false;
      oldActive.usedItemThisTurn = false;
      oldActive.statsRaisedThisTurn = false;
      oldActive.statsLoweredThisTurn = false;
      oldActive.position = pokemon.position;
      pokemon.position = pos;
      side.pokemon[pokemon.position] = pokemon;
      side.pokemon[oldActive.position] = oldActive;
    }
    pokemon.isActive = true;
    side.active[pos] = pokemon;
    pokemon.activeTurns = 0;
    pokemon.activeMoveActions = 0;
    for (const moveSlot of pokemon.moveSlots) {
      moveSlot.used = false;
    }
    this.battle.runEvent("BeforeSwitchIn", pokemon);
    var optionals = [
      sourceEffect ? `[from] ${sourceEffect}` : null,
      pokemon.illusion ? `[is] ${pokemon.illusion}` : null,
    ];
    this.battle.add(
      isDrag ? "drag" : "switch",
      pokemon,
      pokemon.getDetails,
      ...optionals.filter(Boolean)
    );
    pokemon.abilityOrder = this.battle.abilityOrder++;
    if (isDrag && this.battle.gen === 2) pokemon.draggedIn = this.battle.turn;
    pokemon.previouslySwitchedIn++;
    if (isDrag && this.battle.gen >= 5) {
      this.battle.singleEvent(
        "PreStart",
        pokemon.getAbility(),
        pokemon.abilityState,
        pokemon
      );
      this.runSwitch(pokemon);
    } else {
      this.battle.queue.insertChoice({ choice: "runUnnerve", pokemon });
      this.battle.queue.insertChoice({ choice: "runSwitch", pokemon });
    }
    return true;
  }
  dragIn(side, pos) {
    const pokemon = this.battle.getRandomSwitchable(side);
    if (!pokemon || pokemon.isActive) return false;
    const oldActive = side.active[pos];
    if (!oldActive) throw new Error(`nothing to drag out`);
    if (!oldActive.hp) return false;
    if (!this.battle.runEvent("DragOut", oldActive)) {
      return false;
    }
    if (!this.switchIn(pokemon, pos, null, true)) return false;
    return true;
  }
  runSwitch(pokemon) {
    this.battle.runEvent("Swap", pokemon);
    if (this.battle.gen >= 5) {
      this.battle.runEvent("SwitchIn", pokemon);
    }
    this.battle.runEvent("EntryHazard", pokemon);
    if (this.battle.gen <= 4) {
      this.battle.runEvent("SwitchIn", pokemon);
    }
    if (this.battle.gen <= 2) {
      for (const poke of this.battle.getAllActive()) poke.lastMove = null;
      if (
        !pokemon.side.faintedThisTurn &&
        pokemon.draggedIn !== this.battle.turn
      ) {
        this.battle.runEvent("AfterSwitchInSelf", pokemon);
      }
    }
    if (!pokemon.hp) return false;
    pokemon.isStarted = true;
    if (!pokemon.fainted) {
      this.battle.singleEvent(
        "Start",
        pokemon.getAbility(),
        pokemon.abilityState,
        pokemon
      );
      this.battle.singleEvent(
        "Start",
        pokemon.getItem(),
        pokemon.itemState,
        pokemon
      );
    }
    if (this.battle.gen === 4) {
      for (const foeActive of pokemon.foes()) {
        foeActive.removeVolatile("substitutebroken");
      }
    }
    pokemon.draggedIn = null;
    return true;
  }
  // #endregion
  // #region MOVES
  // ==================================================================
  /**
   * runMove is the "outside" move caller. It handles deducting PP,
   * flinching, full paralysis, etc. All the stuff up to and including
   * the "POKEMON used MOVE" message.
   *
   * For details of the difference between runMove and useMove, see
   * useMove's info.
   *
   * externalMove skips LockMove and PP deduction, mostly for use by
   * Dancer.
   */
  runMove(
    moveOrMoveName,
    pokemon,
    targetLoc,
    sourceEffect,
    zMove,
    externalMove,
    maxMove,
    originalTarget
  ) {
    pokemon.activeMoveActions++;
    let target = this.battle.getTarget(
      pokemon,
      maxMove || zMove || moveOrMoveName,
      targetLoc,
      originalTarget
    );
    let baseMove = this.dex.getActiveMove(moveOrMoveName);
    const pranksterBoosted = baseMove.pranksterBoosted;
    if (baseMove.id !== "struggle" && !zMove && !maxMove && !externalMove) {
      const changedMove = this.battle.runEvent(
        "OverrideAction",
        pokemon,
        target,
        baseMove
      );
      if (changedMove && changedMove !== true) {
        baseMove = this.dex.getActiveMove(changedMove);
        if (pranksterBoosted) baseMove.pranksterBoosted = pranksterBoosted;
        target = this.battle.getRandomTarget(pokemon, baseMove);
      }
    }
    let move = baseMove;
    if (zMove) {
      move = this.getActiveZMove(baseMove, pokemon);
    } else if (maxMove) {
      move = this.getActiveMaxMove(baseMove, pokemon);
    }
    move.isExternal = externalMove;
    this.battle.setActiveMove(move, pokemon, target);
    const willTryMove = this.battle.runEvent(
      "BeforeMove",
      pokemon,
      target,
      move
    );
    if (!willTryMove) {
      this.battle.runEvent("MoveAborted", pokemon, target, move);
      this.battle.clearActiveMove(true);
      pokemon.moveThisTurnResult = willTryMove;
      return;
    }
    if (move.flags["cantusetwice"] && pokemon.lastMove?.id === move.id) {
      pokemon.addVolatile(move.id);
    }
    if (move.beforeMoveCallback) {
      if (move.beforeMoveCallback.call(this.battle, pokemon, target, move)) {
        this.battle.clearActiveMove(true);
        pokemon.moveThisTurnResult = false;
        return;
      }
    }
    pokemon.lastDamage = 0;
    let lockedMove;
    if (!externalMove) {
      lockedMove = this.battle.runEvent("LockMove", pokemon);
      if (lockedMove === true) lockedMove = false;
      if (!lockedMove) {
        if (
          !pokemon.deductPP(baseMove, null, target) &&
          move.id !== "struggle"
        ) {
          this.battle.add("cant", pokemon, "nopp", move);
          this.battle.clearActiveMove(true);
          pokemon.moveThisTurnResult = false;
          return;
        }
      } else {
        sourceEffect = this.dex.conditions.get("lockedmove");
      }
      pokemon.moveUsed(move, targetLoc);
    }
    const noLock = externalMove && !pokemon.volatiles["lockedmove"];
    if (zMove) {
      if (pokemon.illusion) {
        this.battle.singleEvent(
          "End",
          this.dex.abilities.get("Illusion"),
          pokemon.abilityState,
          pokemon
        );
      }
      this.battle.add("-zpower", pokemon);
      pokemon.side.zMoveUsed = true;
    }
    const oldActiveMove = move;
    const moveDidSomething = this.useMove(
      baseMove,
      pokemon,
      target,
      sourceEffect,
      zMove,
      maxMove
    );
    this.battle.lastSuccessfulMoveThisTurn = moveDidSomething
      ? this.battle.activeMove && this.battle.activeMove.id
      : null;
    if (this.battle.activeMove) move = this.battle.activeMove;
    this.battle.singleEvent("AfterMove", move, null, pokemon, target, move);
    this.battle.runEvent("AfterMove", pokemon, target, move);
    if (move.flags["cantusetwice"] && pokemon.removeVolatile(move.id)) {
      this.battle.add(
        "-hint",
        `Some effects can force a Pokemon to use ${move.name} again in a row.`
      );
    }
    if (move.flags["dance"] && moveDidSomething && !move.isExternal) {
      const dancers = [];
      for (const currentPoke of this.battle.getAllActive()) {
        if (pokemon === currentPoke) continue;
        if (
          currentPoke.hasAbility("dancer") &&
          !currentPoke.isSemiInvulnerable()
        ) {
          dancers.push(currentPoke);
        }
      }
      dancers.sort(
        (a, b) =>
          -(b.storedStats["spe"] - a.storedStats["spe"]) ||
          b.abilityOrder - a.abilityOrder
      );
      const targetOf1stDance = this.battle.activeTarget;
      for (const dancer of dancers) {
        if (this.battle.faintMessages()) break;
        if (dancer.fainted) continue;
        this.battle.add("-activate", dancer, "ability: Dancer");
        const dancersTarget =
          !targetOf1stDance.isAlly(dancer) && pokemon.isAlly(dancer)
            ? targetOf1stDance
            : pokemon;
        const dancersTargetLoc = dancer.getLocOf(dancersTarget);
        this.runMove(
          move.id,
          dancer,
          dancersTargetLoc,
          this.dex.abilities.get("dancer"),
          void 0,
          true
        );
      }
    }
    if (noLock && pokemon.volatiles["lockedmove"])
      delete pokemon.volatiles["lockedmove"];
    this.battle.faintMessages();
    this.battle.checkWin();
    if (this.battle.gen <= 4) {
      this.battle.activeMove = oldActiveMove;
    }
  }
  /**
   * useMove is the "inside" move caller. It handles effects of the
   * move itself, but not the idea of using the move.
   *
   * Most caller effects, like Sleep Talk, Nature Power, Magic Bounce,
   * etc use useMove.
   *
   * The only ones that use runMove are Instruct, Pursuit, and
   * Dancer.
   */
  useMove(move, pokemon, target, sourceEffect, zMove, maxMove) {
    pokemon.moveThisTurnResult = void 0;
    const oldMoveResult = pokemon.moveThisTurnResult;
    const moveResult = this.useMoveInner(
      move,
      pokemon,
      target,
      sourceEffect,
      zMove,
      maxMove
    );
    if (oldMoveResult === pokemon.moveThisTurnResult)
      pokemon.moveThisTurnResult = moveResult;
    return moveResult;
  }
  useMoveInner(moveOrMoveName, pokemon, target, sourceEffect, zMove, maxMove) {
    if (!sourceEffect && this.battle.effect.id)
      sourceEffect = this.battle.effect;
    if (sourceEffect && ["instruct", "custapberry"].includes(sourceEffect.id))
      sourceEffect = null;
    let move = this.dex.getActiveMove(moveOrMoveName);
    pokemon.lastMoveUsed = move;
    if (move.id === "weatherball" && zMove) {
      this.battle.singleEvent(
        "ModifyType",
        move,
        null,
        pokemon,
        target,
        move,
        move
      );
      if (move.type !== "Normal") sourceEffect = move;
    }
    if (
      zMove ||
      (move.category !== "Status" && sourceEffect && sourceEffect.isZ)
    ) {
      move = this.getActiveZMove(move, pokemon);
    }
    if (maxMove && move.category !== "Status") {
      this.battle.singleEvent(
        "ModifyType",
        move,
        null,
        pokemon,
        target,
        move,
        move
      );
      this.battle.runEvent("ModifyType", pokemon, target, move, move);
    }
    if (
      maxMove ||
      (move.category !== "Status" && sourceEffect && sourceEffect.isMax)
    ) {
      move = this.getActiveMaxMove(move, pokemon);
    }
    if (this.battle.activeMove) {
      move.priority = this.battle.activeMove.priority;
      if (!move.hasBounced)
        move.pranksterBoosted = this.battle.activeMove.pranksterBoosted;
    }
    const baseTarget = move.target;
    let targetRelayVar = { target };
    targetRelayVar = this.battle.runEvent(
      "ModifyTarget",
      pokemon,
      target,
      move,
      targetRelayVar,
      true
    );
    if (targetRelayVar.target !== void 0) target = targetRelayVar.target;
    if (target === void 0) target = this.battle.getRandomTarget(pokemon, move);
    if (move.target === "self" || move.target === "allies") {
      target = pokemon;
    }
    if (sourceEffect) {
      move.sourceEffect = sourceEffect.id;
      move.ignoreAbility = sourceEffect.ignoreAbility;
    }
    let moveResult = false;
    this.battle.setActiveMove(move, pokemon, target);
    this.battle.singleEvent(
      "ModifyType",
      move,
      null,
      pokemon,
      target,
      move,
      move
    );
    this.battle.singleEvent(
      "ModifyMove",
      move,
      null,
      pokemon,
      target,
      move,
      move
    );
    if (baseTarget !== move.target) {
      target = this.battle.getRandomTarget(pokemon, move);
    }
    move = this.battle.runEvent("ModifyType", pokemon, target, move, move);
    move = this.battle.runEvent("ModifyMove", pokemon, target, move, move);
    if (baseTarget !== move.target) {
      target = this.battle.getRandomTarget(pokemon, move);
    }
    if (!move || pokemon.fainted) {
      return false;
    }
    let attrs = "";
    let movename = move.name;
    if (move.id === "hiddenpower") movename = "Hidden Power";
    if (sourceEffect) attrs += `|[from]${sourceEffect.fullname}`;
    if (zMove && move.isZ === true) {
      attrs = "|[anim]" + movename + attrs;
      movename = "Z-" + movename;
    }
    this.battle.addMove("move", pokemon, movename, target + attrs);
    if (zMove) this.runZPower(move, pokemon);
    if (!target) {
      this.battle.attrLastMove("[notarget]");
      this.battle.add(this.battle.gen >= 5 ? "-fail" : "-notarget", pokemon);
      return false;
    }
    const { targets, pressureTargets } = pokemon.getMoveTargets(move, target);
    if (targets.length) {
      target = targets[targets.length - 1];
    }
    const callerMoveForPressure =
      sourceEffect && sourceEffect.pp ? sourceEffect : null;
    if (
      !sourceEffect ||
      callerMoveForPressure ||
      sourceEffect.id === "pursuit"
    ) {
      let extraPP = 0;
      for (const source of pressureTargets) {
        const ppDrop = this.battle.runEvent("DeductPP", source, pokemon, move);
        if (ppDrop !== true) {
          extraPP += ppDrop || 0;
        }
      }
      if (extraPP > 0) {
        pokemon.deductPP(callerMoveForPressure || moveOrMoveName, extraPP);
      }
    }
    if (
      !this.battle.singleEvent("TryMove", move, null, pokemon, target, move) ||
      !this.battle.runEvent("TryMove", pokemon, target, move)
    ) {
      move.mindBlownRecoil = false;
      return false;
    }
    this.battle.singleEvent(
      "UseMoveMessage",
      move,
      null,
      pokemon,
      target,
      move
    );
    if (move.ignoreImmunity === void 0) {
      move.ignoreImmunity = move.category === "Status";
    }
    if (this.battle.gen !== 4 && move.selfdestruct === "always") {
      this.battle.faint(pokemon, pokemon, move);
    }
    let damage = false;
    if (
      move.target === "all" ||
      move.target === "foeSide" ||
      move.target === "allySide" ||
      move.target === "allyTeam"
    ) {
      damage = this.tryMoveHit(targets, pokemon, move);
      if (damage === this.battle.NOT_FAIL) pokemon.moveThisTurnResult = null;
      if (damage || damage === 0 || damage === void 0) moveResult = true;
    } else {
      if (!targets.length) {
        this.battle.attrLastMove("[notarget]");
        this.battle.add(this.battle.gen >= 5 ? "-fail" : "-notarget", pokemon);
        return false;
      }
      if (this.battle.gen === 4 && move.selfdestruct === "always") {
        this.battle.faint(pokemon, pokemon, move);
      }
      moveResult = this.trySpreadMoveHit(targets, pokemon, move);
    }
    if (move.selfBoost && moveResult)
      this.moveHit(pokemon, pokemon, move, move.selfBoost, false, true);
    if (!pokemon.hp) {
      this.battle.faint(pokemon, pokemon, move);
    }
    if (!moveResult) {
      this.battle.singleEvent("MoveFail", move, null, target, pokemon, move);
      return false;
    }
    if (
      !move.negateSecondary &&
      !(move.hasSheerForce && pokemon.hasAbility("sheerforce")) &&
      !move.flags["futuremove"]
    ) {
      const originalHp = pokemon.hp;
      this.battle.singleEvent(
        "AfterMoveSecondarySelf",
        move,
        null,
        pokemon,
        target,
        move
      );
      this.battle.runEvent("AfterMoveSecondarySelf", pokemon, target, move);
      if (pokemon && pokemon !== target && move.category !== "Status") {
        if (pokemon.hp <= pokemon.maxhp / 2 && originalHp > pokemon.maxhp / 2) {
          this.battle.runEvent("EmergencyExit", pokemon, pokemon);
        }
      }
    }
    return true;
  }
  /** NOTE: includes single-target moves */
  trySpreadMoveHit(targets, pokemon, move, notActive) {
    if (targets.length > 1 && !move.smartTarget) move.spreadHit = true;
    const moveSteps = [
      // 0. check for semi invulnerability
      this.hitStepInvulnerabilityEvent,
      // 1. run the 'TryHit' event (Protect, Magic Bounce, Volt Absorb, etc.) (this is step 2 in gens 5 & 6, and step 4 in gen 4)
      this.hitStepTryHitEvent,
      // 2. check for type immunity (this is step 1 in gens 4-6)
      this.hitStepTypeImmunity,
      // 3. check for various move-specific immunities
      this.hitStepTryImmunity,
      // 4. check accuracy
      this.hitStepAccuracy,
      // 5. break protection effects
      this.hitStepBreakProtect,
      // 6. steal positive boosts (Spectral Thief)
      this.hitStepStealBoosts,
      // 7. loop that processes each hit of the move (has its own steps per iteration)
      this.hitStepMoveHitLoop,
    ];
    if (this.battle.gen <= 6) {
      [moveSteps[1], moveSteps[2]] = [moveSteps[2], moveSteps[1]];
    }
    if (this.battle.gen === 4) {
      [moveSteps[2], moveSteps[4]] = [moveSteps[4], moveSteps[2]];
    }
    if (notActive) this.battle.setActiveMove(move, pokemon, targets[0]);
    const hitResult =
      this.battle.singleEvent("Try", move, null, pokemon, targets[0], move) &&
      this.battle.singleEvent(
        "PrepareHit",
        move,
        {},
        targets[0],
        pokemon,
        move
      ) &&
      this.battle.runEvent("PrepareHit", pokemon, targets[0], move);
    if (!hitResult) {
      if (hitResult === false) {
        this.battle.add("-fail", pokemon);
        this.battle.attrLastMove("[still]");
      }
      return hitResult === this.battle.NOT_FAIL;
    }
    let atLeastOneFailure = false;
    for (const step of moveSteps) {
      const hitResults = step.call(this, targets, pokemon, move);
      if (!hitResults) continue;
      targets = targets.filter(
        (val, i) => hitResults[i] || hitResults[i] === 0
      );
      atLeastOneFailure =
        atLeastOneFailure || hitResults.some((val) => val === false);
      if (move.smartTarget && atLeastOneFailure) move.smartTarget = false;
      if (!targets.length) {
        break;
      }
    }
    const moveResult = !!targets.length;
    if (!moveResult && !atLeastOneFailure) pokemon.moveThisTurnResult = null;
    const hitSlot = targets.map((p) => p.getSlot());
    if (move.spreadHit)
      this.battle.attrLastMove("[spread] " + hitSlot.join(","));
    return moveResult;
  }
  hitStepInvulnerabilityEvent(targets, pokemon, move) {
    if (move.id === "helpinghand") return new Array(targets.length).fill(true);
    const hitResults = [];
    for (const [i, target] of targets.entries()) {
      if (target.volatiles["commanding"]) {
        hitResults[i] = false;
      } else if (
        this.battle.gen >= 8 &&
        move.id === "toxic" &&
        pokemon.hasType("Poison")
      ) {
        hitResults[i] = true;
      } else {
        hitResults[i] = this.battle.runEvent(
          "Invulnerability",
          target,
          pokemon,
          move
        );
      }
      if (hitResults[i] === false) {
        if (move.smartTarget) {
          move.smartTarget = false;
        } else {
          if (!move.spreadHit) this.battle.attrLastMove("[miss]");
          this.battle.add("-miss", pokemon, target);
        }
      }
    }
    return hitResults;
  }
  hitStepTryHitEvent(targets, pokemon, move) {
    const hitResults = this.battle.runEvent("TryHit", targets, pokemon, move);
    if (!hitResults.includes(true) && hitResults.includes(false)) {
      this.battle.add("-fail", pokemon);
      this.battle.attrLastMove("[still]");
    }
    for (const i of targets.keys()) {
      if (hitResults[i] !== this.battle.NOT_FAIL)
        hitResults[i] = hitResults[i] || false;
    }
    return hitResults;
  }
  hitStepTypeImmunity(targets, pokemon, move) {
    if (move.ignoreImmunity === void 0) {
      move.ignoreImmunity = move.category === "Status";
    }
    const hitResults = [];
    for (const i of targets.keys()) {
      hitResults[i] =
        (move.ignoreImmunity &&
          (move.ignoreImmunity === true || move.ignoreImmunity[move.type])) ||
        targets[i].runImmunity(move.type, !move.smartTarget);
    }
    return hitResults;
  }
  hitStepTryImmunity(targets, pokemon, move) {
    const hitResults = [];
    for (const [i, target] of targets.entries()) {
      if (
        this.battle.gen >= 6 &&
        move.flags["powder"] &&
        target !== pokemon &&
        !this.dex.getImmunity("powder", target)
      ) {
        this.battle.debug("natural powder immunity");
        this.battle.add("-immune", target);
        hitResults[i] = false;
      } else if (
        !this.battle.singleEvent("TryImmunity", move, {}, target, pokemon, move)
      ) {
        this.battle.add("-immune", target);
        hitResults[i] = false;
      } else if (
        this.battle.gen >= 7 &&
        move.pranksterBoosted &&
        pokemon.hasAbility("prankster") &&
        !targets[i].isAlly(pokemon) &&
        !this.dex.getImmunity("prankster", target)
      ) {
        this.battle.debug("natural prankster immunity");
        if (
          target.illusion ||
          !(move.status && !this.dex.getImmunity(move.status, target))
        ) {
          this.battle.hint("Since gen 7, Dark is immune to Prankster moves.");
        }
        this.battle.add("-immune", target);
        hitResults[i] = false;
      } else {
        hitResults[i] = true;
      }
    }
    return hitResults;
  }
  hitStepAccuracy(targets, pokemon, move) {
    const hitResults = [];
    for (const [i, target] of targets.entries()) {
      this.battle.activeTarget = target;
      let accuracy = move.accuracy;
      if (move.ohko) {
        if (!target.isSemiInvulnerable()) {
          accuracy = 30;
          if (
            move.ohko === "Ice" &&
            this.battle.gen >= 7 &&
            !pokemon.hasType("Ice")
          ) {
            accuracy = 20;
          }
          if (
            !target.volatiles["dynamax"] &&
            pokemon.level >= target.level &&
            (move.ohko === true || !target.hasType(move.ohko))
          ) {
            accuracy += pokemon.level - target.level;
          } else {
            this.battle.add("-immune", target, "[ohko]");
            hitResults[i] = false;
            continue;
          }
        }
      } else {
        accuracy = this.battle.runEvent(
          "ModifyAccuracy",
          target,
          pokemon,
          move,
          accuracy
        );
        if (accuracy !== true) {
          let boost = 0;
          if (!move.ignoreAccuracy) {
            const boosts = this.battle.runEvent(
              "ModifyBoost",
              pokemon,
              null,
              null,
              { ...pokemon.boosts }
            );
            boost = this.battle.clampIntRange(boosts["accuracy"], -6, 6);
          }
          if (!move.ignoreEvasion) {
            const boosts = this.battle.runEvent(
              "ModifyBoost",
              target,
              null,
              null,
              { ...target.boosts }
            );
            boost = this.battle.clampIntRange(boost - boosts["evasion"], -6, 6);
          }
          if (boost > 0) {
            accuracy = this.battle.trunc((accuracy * (3 + boost)) / 3);
          } else if (boost < 0) {
            accuracy = this.battle.trunc((accuracy * 3) / (3 - boost));
          }
        }
      }
      if (
        move.alwaysHit ||
        (move.id === "toxic" &&
          this.battle.gen >= 8 &&
          pokemon.hasType("Poison")) ||
        (move.target === "self" &&
          move.category === "Status" &&
          !target.isSemiInvulnerable())
      ) {
        accuracy = true;
      } else {
        accuracy = this.battle.runEvent(
          "Accuracy",
          target,
          pokemon,
          move,
          accuracy
        );
      }
      if (accuracy !== true && !this.battle.randomChance(accuracy, 100)) {
        if (move.smartTarget) {
          move.smartTarget = false;
        } else {
          if (!move.spreadHit) this.battle.attrLastMove("[miss]");
          this.battle.add("-miss", pokemon, target);
        }
        if (
          !move.ohko &&
          pokemon.hasItem("blunderpolicy") &&
          pokemon.useItem()
        ) {
          this.battle.boost({ spe: 2 }, pokemon);
        }
        hitResults[i] = false;
        continue;
      }
      hitResults[i] = true;
    }
    return hitResults;
  }
  hitStepBreakProtect(targets, pokemon, move) {
    if (move.breaksProtect) {
      for (const target of targets) {
        let broke = false;
        for (const effectid of [
          "banefulbunker",
          "burningbulwark",
          "kingsshield",
          "obstruct",
          "protect",
          "silktrap",
          "spikyshield",
        ]) {
          if (target.removeVolatile(effectid)) broke = true;
        }
        if (this.battle.gen >= 6 || !target.isAlly(pokemon)) {
          for (const effectid of [
            "craftyshield",
            "matblock",
            "quickguard",
            "wideguard",
          ]) {
            if (target.side.removeSideCondition(effectid)) broke = true;
          }
        }
        if (broke) {
          if (move.id === "feint") {
            this.battle.add("-activate", target, "move: Feint");
          } else {
            this.battle.add(
              "-activate",
              target,
              "move: " + move.name,
              "[broken]"
            );
          }
          if (this.battle.gen >= 6) delete target.volatiles["stall"];
        }
      }
    }
    return void 0;
  }
  hitStepStealBoosts(targets, pokemon, move) {
    const target = targets[0];
    if (move.stealsBoosts) {
      const boosts = {};
      let stolen = false;
      let statName;
      for (statName in target.boosts) {
        const stage = target.boosts[statName];
        if (stage > 0) {
          boosts[statName] = stage;
          stolen = true;
        }
      }
      if (stolen) {
        this.battle.attrLastMove("[still]");
        this.battle.add(
          "-clearpositiveboost",
          target,
          pokemon,
          "move: " + move.name
        );
        this.battle.boost(boosts, pokemon, pokemon);
        let statName2;
        for (statName2 in boosts) {
          boosts[statName2] = 0;
        }
        target.setBoost(boosts);
        this.battle.addMove("-anim", pokemon, "Spectral Thief", target);
      }
    }
    return void 0;
  }
  afterMoveSecondaryEvent(targets, pokemon, move) {
    if (
      !move.negateSecondary &&
      !(move.hasSheerForce && pokemon.hasAbility("sheerforce"))
    ) {
      this.battle.singleEvent(
        "AfterMoveSecondary",
        move,
        null,
        targets[0],
        pokemon,
        move
      );
      this.battle.runEvent("AfterMoveSecondary", targets, pokemon, move);
    }
    return void 0;
  }
  /** NOTE: used only for moves that target sides/fields rather than pokemon */
  tryMoveHit(targetOrTargets, pokemon, move) {
    const target = Array.isArray(targetOrTargets)
      ? targetOrTargets[0]
      : targetOrTargets;
    const targets = Array.isArray(targetOrTargets) ? targetOrTargets : [target];
    this.battle.setActiveMove(move, pokemon, targets[0]);
    let hitResult =
      this.battle.singleEvent("Try", move, null, pokemon, target, move) &&
      this.battle.singleEvent("PrepareHit", move, {}, target, pokemon, move) &&
      this.battle.runEvent("PrepareHit", pokemon, target, move);
    if (!hitResult) {
      if (hitResult === false) {
        this.battle.add("-fail", pokemon);
        this.battle.attrLastMove("[still]");
      }
      return false;
    }
    const isFFAHazard =
      move.target === "foeSide" && this.battle.gameType === "freeforall";
    if (move.target === "all") {
      hitResult = this.battle.runEvent("TryHitField", target, pokemon, move);
    } else if (isFFAHazard) {
      const hitResults = this.battle.runEvent(
        "TryHitSide",
        targets,
        pokemon,
        move
      );
      if (hitResults.some((result) => !result)) return false;
      hitResult = true;
    } else {
      hitResult = this.battle.runEvent("TryHitSide", target, pokemon, move);
    }
    if (!hitResult) {
      if (hitResult === false) {
        this.battle.add("-fail", pokemon);
        this.battle.attrLastMove("[still]");
      }
      return false;
    }
    return this.moveHit(isFFAHazard ? targets : target, pokemon, move);
  }
  hitStepMoveHitLoop(targets, pokemon, move) {
    let damage = [];
    for (const i of targets.keys()) {
      damage[i] = 0;
    }
    move.totalDamage = 0;
    pokemon.lastDamage = 0;
    let targetHits = move.multihit || 1;
    if (Array.isArray(targetHits)) {
      if (targetHits[0] === 2 && targetHits[1] === 5) {
        if (this.battle.gen >= 5) {
          targetHits = this.battle.sample([
            2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5,
          ]);
          if (targetHits < 4 && pokemon.hasItem("loadeddice")) {
            targetHits = 5 - this.battle.random(2);
          }
        } else {
          targetHits = this.battle.sample([2, 2, 2, 3, 3, 3, 4, 5]);
        }
      } else {
        targetHits = this.battle.random(targetHits[0], targetHits[1] + 1);
      }
    }
    if (targetHits === 10 && pokemon.hasItem("loadeddice"))
      targetHits -= this.battle.random(7);
    targetHits = Math.floor(targetHits);
    let nullDamage = true;
    let moveDamage = [];
    const isSleepUsable =
      move.sleepUsable || this.dex.moves.get(move.sourceEffect).sleepUsable;
    let targetsCopy = targets.slice(0);
    let hit;
    for (hit = 1; hit <= targetHits; hit++) {
      if (damage.includes(false)) break;
      if (
        hit > 1 &&
        pokemon.status === "slp" &&
        (!isSleepUsable || this.battle.gen === 4)
      )
        break;
      if (targets.every((target2) => !target2?.hp)) break;
      move.hit = hit;
      if (move.smartTarget && targets.length > 1) {
        targetsCopy = [targets[hit - 1]];
        damage = [damage[hit - 1]];
      } else {
        targetsCopy = targets.slice(0);
      }
      const target = targetsCopy[0];
      if (target && typeof move.smartTarget === "boolean") {
        if (hit > 1) {
          this.battle.addMove("-anim", pokemon, move.name, target);
        } else {
          this.battle.retargetLastMove(target);
        }
      }
      if (target && move.multiaccuracy && hit > 1) {
        let accuracy = move.accuracy;
        const boostTable = [1, 4 / 3, 5 / 3, 2, 7 / 3, 8 / 3, 3];
        if (accuracy !== true) {
          if (!move.ignoreAccuracy) {
            const boosts = this.battle.runEvent(
              "ModifyBoost",
              pokemon,
              null,
              null,
              { ...pokemon.boosts }
            );
            const boost = this.battle.clampIntRange(boosts["accuracy"], -6, 6);
            if (boost > 0) {
              accuracy *= boostTable[boost];
            } else {
              accuracy /= boostTable[-boost];
            }
          }
          if (!move.ignoreEvasion) {
            const boosts = this.battle.runEvent(
              "ModifyBoost",
              target,
              null,
              null,
              { ...target.boosts }
            );
            const boost = this.battle.clampIntRange(boosts["evasion"], -6, 6);
            if (boost > 0) {
              accuracy /= boostTable[boost];
            } else if (boost < 0) {
              accuracy *= boostTable[-boost];
            }
          }
        }
        accuracy = this.battle.runEvent(
          "ModifyAccuracy",
          target,
          pokemon,
          move,
          accuracy
        );
        if (!move.alwaysHit) {
          accuracy = this.battle.runEvent(
            "Accuracy",
            target,
            pokemon,
            move,
            accuracy
          );
          if (accuracy !== true && !this.battle.randomChance(accuracy, 100))
            break;
        }
      }
      const moveData = move;
      if (!moveData.flags) moveData.flags = {};
      let moveDamageThisHit;
      [moveDamageThisHit, targetsCopy] = this.spreadMoveHit(
        targetsCopy,
        pokemon,
        move,
        moveData
      );
      if (move.smartTarget) {
        moveDamage.push(...moveDamageThisHit);
      } else {
        moveDamage = moveDamageThisHit;
      }
      if (!moveDamage.some((val) => val !== false)) break;
      nullDamage = false;
      for (const [i, md] of moveDamage.entries()) {
        if (move.smartTarget && i !== hit - 1) continue;
        damage[i] = md === true || !md ? 0 : md;
        move.totalDamage += damage[i];
      }
      if (move.mindBlownRecoil) {
        const hpBeforeRecoil = pokemon.hp;
        this.battle.damage(
          Math.round(pokemon.maxhp / 2),
          pokemon,
          pokemon,
          this.dex.conditions.get(move.id),
          true
        );
        move.mindBlownRecoil = false;
        if (
          pokemon.hp <= pokemon.maxhp / 2 &&
          hpBeforeRecoil > pokemon.maxhp / 2
        ) {
          this.battle.runEvent("EmergencyExit", pokemon, pokemon);
        }
      }
      this.battle.eachEvent("Update");
      if (!pokemon.hp && targets.length === 1) {
        hit++;
        break;
      }
    }
    if (hit === 1) return damage.fill(false);
    if (nullDamage) damage.fill(false);
    this.battle.faintMessages(false, false, !pokemon.hp);
    if (move.multihit && typeof move.smartTarget !== "boolean") {
      this.battle.add("-hitcount", targets[0], hit - 1);
    }
    if ((move.recoil || move.id === "chloroblast") && move.totalDamage) {
      const hpBeforeRecoil = pokemon.hp;
      this.battle.damage(
        this.calcRecoilDamage(move.totalDamage, move, pokemon),
        pokemon,
        pokemon,
        "recoil"
      );
      if (
        pokemon.hp <= pokemon.maxhp / 2 &&
        hpBeforeRecoil > pokemon.maxhp / 2
      ) {
        this.battle.runEvent("EmergencyExit", pokemon, pokemon);
      }
    }
    if (move.struggleRecoil) {
      const hpBeforeRecoil = pokemon.hp;
      let recoilDamage;
      if (this.dex.gen >= 5) {
        recoilDamage = this.battle.clampIntRange(
          Math.round(pokemon.baseMaxhp / 4),
          1
        );
      } else {
        recoilDamage = this.battle.clampIntRange(
          this.battle.trunc(pokemon.maxhp / 4),
          1
        );
      }
      this.battle.directDamage(recoilDamage, pokemon, pokemon, {
        id: "strugglerecoil",
      });
      if (
        pokemon.hp <= pokemon.maxhp / 2 &&
        hpBeforeRecoil > pokemon.maxhp / 2
      ) {
        this.battle.runEvent("EmergencyExit", pokemon, pokemon);
      }
    }
    if (move.smartTarget) {
      targetsCopy = targets.slice(0);
    }
    for (const [i, target] of targetsCopy.entries()) {
      if (target && pokemon !== target) {
        target.gotAttacked(move, moveDamage[i], pokemon);
        if (typeof moveDamage[i] === "number") {
          target.timesAttacked += move.smartTarget ? 1 : hit - 1;
        }
      }
    }
    if (move.ohko && !targets[0].hp) this.battle.add("-ohko");
    if (!damage.some((val) => !!val || val === 0)) return damage;
    this.battle.eachEvent("Update");
    this.afterMoveSecondaryEvent(
      targetsCopy.filter((val) => !!val),
      pokemon,
      move
    );
    if (
      !move.negateSecondary &&
      !(move.hasSheerForce && pokemon.hasAbility("sheerforce"))
    ) {
      for (const [i, d] of damage.entries()) {
        const curDamage = targets.length === 1 ? move.totalDamage : d;
        if (typeof curDamage === "number" && targets[i].hp) {
          const targetHPBeforeDamage =
            (targets[i].hurtThisTurn || 0) + curDamage;
          if (
            targets[i].hp <= targets[i].maxhp / 2 &&
            targetHPBeforeDamage > targets[i].maxhp / 2
          ) {
            this.battle.runEvent("EmergencyExit", targets[i], pokemon);
          }
        }
      }
    }
    return damage;
  }
  spreadMoveHit(
    targets,
    pokemon,
    moveOrMoveName,
    hitEffect,
    isSecondary,
    isSelf
  ) {
    const target = targets[0];
    let damage = [];
    for (const i of targets.keys()) {
      damage[i] = true;
    }
    const move = this.dex.getActiveMove(moveOrMoveName);
    let hitResult = true;
    let moveData = hitEffect;
    if (!moveData) moveData = move;
    if (!moveData.flags) moveData.flags = {};
    if (move.target === "all" && !isSelf) {
      hitResult = this.battle.singleEvent(
        "TryHitField",
        moveData,
        {},
        target || null,
        pokemon,
        move
      );
    } else if (
      (move.target === "foeSide" ||
        move.target === "allySide" ||
        move.target === "allyTeam") &&
      !isSelf
    ) {
      hitResult = this.battle.singleEvent(
        "TryHitSide",
        moveData,
        {},
        target || null,
        pokemon,
        move
      );
    } else if (target) {
      hitResult = this.battle.singleEvent(
        "TryHit",
        moveData,
        {},
        target,
        pokemon,
        move
      );
    }
    if (!hitResult) {
      if (hitResult === false) {
        this.battle.add("-fail", pokemon);
        this.battle.attrLastMove("[still]");
      }
      return [[false], targets];
    }
    if (!isSecondary && !isSelf) {
      if (
        move.target !== "all" &&
        move.target !== "allyTeam" &&
        move.target !== "allySide" &&
        move.target !== "foeSide"
      ) {
        damage = this.tryPrimaryHitEvent(
          damage,
          targets,
          pokemon,
          move,
          moveData,
          isSecondary
        );
      }
    }
    for (const i of targets.keys()) {
      if (damage[i] === this.battle.HIT_SUBSTITUTE) {
        damage[i] = true;
        targets[i] = null;
      }
      if (targets[i] && isSecondary && !moveData.self) {
        damage[i] = true;
      }
      if (!damage[i]) targets[i] = false;
    }
    damage = this.getSpreadDamage(
      damage,
      targets,
      pokemon,
      move,
      moveData,
      isSecondary,
      isSelf
    );
    for (const i of targets.keys()) {
      if (damage[i] === false) targets[i] = false;
    }
    damage = this.battle.spreadDamage(damage, targets, pokemon, move);
    for (const i of targets.keys()) {
      if (damage[i] === false) targets[i] = false;
    }
    damage = this.runMoveEffects(
      damage,
      targets,
      pokemon,
      move,
      moveData,
      isSecondary,
      isSelf
    );
    for (const i of targets.keys()) {
      if (!damage[i] && damage[i] !== 0) targets[i] = false;
    }
    const activeTarget = this.battle.activeTarget;
    if (moveData.self && !move.selfDropped)
      this.selfDrops(targets, pokemon, move, moveData, isSecondary);
    if (moveData.secondaries)
      this.secondaries(targets, pokemon, move, moveData, isSelf);
    this.battle.activeTarget = activeTarget;
    if (moveData.forceSwitch)
      damage = this.forceSwitch(damage, targets, pokemon, move);
    for (const i of targets.keys()) {
      if (!damage[i] && damage[i] !== 0) targets[i] = false;
    }
    const damagedTargets = [];
    const damagedDamage = [];
    for (const [i, t] of targets.entries()) {
      if (typeof damage[i] === "number" && t) {
        damagedTargets.push(t);
        damagedDamage.push(damage[i]);
      }
    }
    const pokemonOriginalHP = pokemon.hp;
    if (damagedDamage.length && !isSecondary && !isSelf) {
      this.battle.runEvent(
        "DamagingHit",
        damagedTargets,
        pokemon,
        move,
        damagedDamage
      );
      if (moveData.onAfterHit) {
        for (const t of damagedTargets) {
          this.battle.singleEvent("AfterHit", moveData, {}, t, pokemon, move);
        }
      }
      if (
        pokemon.hp &&
        pokemon.hp <= pokemon.maxhp / 2 &&
        pokemonOriginalHP > pokemon.maxhp / 2
      ) {
        this.battle.runEvent("EmergencyExit", pokemon);
      }
    }
    return [damage, targets];
  }
  tryPrimaryHitEvent(damage, targets, pokemon, move, moveData, isSecondary) {
    for (const [i, target] of targets.entries()) {
      if (!target) continue;
      damage[i] = this.battle.runEvent(
        "TryPrimaryHit",
        target,
        pokemon,
        moveData
      );
    }
    return damage;
  }
  getSpreadDamage(
    damage,
    targets,
    source,
    move,
    moveData,
    isSecondary,
    isSelf
  ) {
    for (const [i, target] of targets.entries()) {
      if (!target) continue;
      this.battle.activeTarget = target;
      damage[i] = void 0;
      const curDamage = this.getDamage(source, target, moveData);
      if (curDamage === false || curDamage === null) {
        if (damage[i] === false && !isSecondary && !isSelf) {
          this.battle.add("-fail", source);
          this.battle.attrLastMove("[still]");
        }
        this.battle.debug("damage calculation interrupted");
        damage[i] = false;
        continue;
      }
      damage[i] = curDamage;
    }
    return damage;
  }
  runMoveEffects(damage, targets, source, move, moveData, isSecondary, isSelf) {
    let didAnything = damage.reduce(this.combineResults);
    for (const [i, target] of targets.entries()) {
      if (target === false) continue;
      let hitResult;
      let didSomething = void 0;
      if (target) {
        if (moveData.boosts && !target.fainted) {
          hitResult = this.battle.boost(
            moveData.boosts,
            target,
            source,
            move,
            isSecondary,
            isSelf
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.heal && !target.fainted) {
          if (target.hp >= target.maxhp) {
            this.battle.add("-fail", target, "heal");
            this.battle.attrLastMove("[still]");
            damage[i] = this.combineResults(damage[i], false);
            didAnything = this.combineResults(didAnything, null);
            continue;
          }
          const amount =
            (target.baseMaxhp * moveData.heal[0]) / moveData.heal[1];
          const d = target.heal(
            (this.battle.gen < 5 ? Math.floor : Math.round)(amount)
          );
          if (!d && d !== 0) {
            this.battle.add("-fail", source);
            this.battle.attrLastMove("[still]");
            this.battle.debug("heal interrupted");
            damage[i] = this.combineResults(damage[i], false);
            didAnything = this.combineResults(didAnything, null);
            continue;
          }
          this.battle.add("-heal", target, target.getHealth);
          didSomething = true;
        }
        if (moveData.status) {
          hitResult = target.trySetStatus(
            moveData.status,
            source,
            moveData.ability ? moveData.ability : move
          );
          if (!hitResult && move.status) {
            damage[i] = this.combineResults(damage[i], false);
            didAnything = this.combineResults(didAnything, null);
            continue;
          }
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.forceStatus) {
          hitResult = target.setStatus(moveData.forceStatus, source, move);
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.volatileStatus) {
          hitResult = target.addVolatile(moveData.volatileStatus, source, move);
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.sideCondition) {
          hitResult = target.side.addSideCondition(
            moveData.sideCondition,
            source,
            move
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.slotCondition) {
          hitResult = target.side.addSlotCondition(
            target,
            moveData.slotCondition,
            source,
            move
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.weather) {
          hitResult = this.battle.field.setWeather(
            moveData.weather,
            source,
            move
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.terrain) {
          hitResult = this.battle.field.setTerrain(
            moveData.terrain,
            source,
            move
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.pseudoWeather) {
          hitResult = this.battle.field.addPseudoWeather(
            moveData.pseudoWeather,
            source,
            move
          );
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (moveData.forceSwitch) {
          hitResult = !!this.battle.canSwitch(target.side);
          didSomething = this.combineResults(didSomething, hitResult);
        }
        if (move.target === "all" && !isSelf) {
          if (moveData.onHitField) {
            hitResult = this.battle.singleEvent(
              "HitField",
              moveData,
              {},
              target,
              source,
              move
            );
            didSomething = this.combineResults(didSomething, hitResult);
          }
        } else if (
          (move.target === "foeSide" || move.target === "allySide") &&
          !isSelf
        ) {
          if (moveData.onHitSide) {
            hitResult = this.battle.singleEvent(
              "HitSide",
              moveData,
              {},
              target.side,
              source,
              move
            );
            didSomething = this.combineResults(didSomething, hitResult);
          }
        } else {
          if (moveData.onHit) {
            hitResult = this.battle.singleEvent(
              "Hit",
              moveData,
              {},
              target,
              source,
              move
            );
            didSomething = this.combineResults(didSomething, hitResult);
          }
          if (!isSelf && !isSecondary) {
            this.battle.runEvent("Hit", target, source, move);
          }
        }
      }
      if (moveData.selfdestruct === "ifHit" && damage[i] !== false) {
        this.battle.faint(source, source, move);
      }
      if (moveData.selfSwitch) {
        if (
          this.battle.canSwitch(source.side) &&
          !source.volatiles["commanded"]
        ) {
          didSomething = true;
        } else {
          didSomething = this.combineResults(didSomething, false);
        }
      }
      if (didSomething === void 0) didSomething = true;
      damage[i] = this.combineResults(
        damage[i],
        didSomething === null ? false : didSomething
      );
      didAnything = this.combineResults(didAnything, didSomething);
    }
    if (
      !didAnything &&
      didAnything !== 0 &&
      !moveData.self &&
      !moveData.selfdestruct
    ) {
      if (!isSelf && !isSecondary) {
        if (didAnything === false) {
          this.battle.add("-fail", source);
          this.battle.attrLastMove("[still]");
        }
      }
      this.battle.debug("move failed because it did nothing");
    } else if (move.selfSwitch && source.hp && !source.volatiles["commanded"]) {
      source.switchFlag = move.id;
    }
    return damage;
  }
  selfDrops(targets, source, move, moveData, isSecondary) {
    for (const target of targets) {
      if (target === false) continue;
      if (moveData.self && !move.selfDropped) {
        if (!isSecondary && moveData.self.boosts) {
          const secondaryRoll = this.battle.random(100);
          if (
            typeof moveData.self.chance === "undefined" ||
            secondaryRoll < moveData.self.chance
          ) {
            this.moveHit(
              source,
              source,
              move,
              moveData.self,
              isSecondary,
              true
            );
          }
          if (!move.multihit) move.selfDropped = true;
        } else {
          this.moveHit(source, source, move, moveData.self, isSecondary, true);
        }
      }
    }
  }
  secondaries(targets, source, move, moveData, isSelf) {
    if (!moveData.secondaries) return;
    for (const target of targets) {
      if (target === false) continue;
      const secondaries = this.battle.runEvent(
        "ModifySecondaries",
        target,
        source,
        moveData,
        moveData.secondaries.slice()
      );
      for (const secondary of secondaries) {
        const secondaryRoll = this.battle.random(100);
        const secondaryOverflow =
          (secondary.boosts || secondary.self) && this.battle.gen <= 8;
        if (
          typeof secondary.chance === "undefined" ||
          secondaryRoll <
            (secondaryOverflow ? secondary.chance % 256 : secondary.chance)
        ) {
          this.moveHit(target, source, move, secondary, true, isSelf);
        }
      }
    }
  }
  forceSwitch(damage, targets, source, move) {
    for (const [i, target] of targets.entries()) {
      if (
        target &&
        target.hp > 0 &&
        source.hp > 0 &&
        this.battle.canSwitch(target.side)
      ) {
        const hitResult = this.battle.runEvent("DragOut", target, source, move);
        if (hitResult) {
          target.forceSwitchFlag = true;
        } else if (hitResult === false && move.category === "Status") {
          this.battle.add("-fail", source);
          this.battle.attrLastMove("[still]");
          damage[i] = false;
        }
      }
    }
    return damage;
  }
  moveHit(targets, pokemon, moveOrMoveName, moveData, isSecondary, isSelf) {
    if (!Array.isArray(targets)) targets = [targets];
    const retVal = this.spreadMoveHit(
      targets,
      pokemon,
      moveOrMoveName,
      moveData,
      isSecondary,
      isSelf
    )[0][0];
    return retVal === true ? void 0 : retVal;
  }
  calcRecoilDamage(damageDealt, move, pokemon) {
    if (move.id === "chloroblast") return Math.round(pokemon.maxhp / 2);
    return this.battle.clampIntRange(
      Math.round((damageDealt * move.recoil[0]) / move.recoil[1]),
      1
    );
  }
  getZMove(move, pokemon, skipChecks) {
    const item = pokemon.getItem();
    if (!skipChecks) {
      if (pokemon.side.zMoveUsed) return;
      if (!item.zMove) return;
      if (item.itemUser && !item.itemUser.includes(pokemon.species.name))
        return;
      const moveData = pokemon.getMoveData(move);
      if (!moveData?.pp) return;
    }
    if (item.zMoveFrom) {
      if (move.name === item.zMoveFrom) return item.zMove;
    } else if (item.zMove === true) {
      if (move.type === item.zMoveType) {
        if (move.category === "Status") {
          return move.name;
        } else if (move.zMove?.basePower) {
          return Z_MOVES[move.type];
        }
      }
    }
  }
  getActiveZMove(move, pokemon) {
    if (pokemon) {
      const item = pokemon.getItem();
      if (move.name === item.zMoveFrom) {
        const zMove2 = this.dex.getActiveMove(item.zMove);
        zMove2.isZOrMaxPowered = true;
        return zMove2;
      }
    }
    if (move.category === "Status") {
      const zMove2 = this.dex.getActiveMove(move);
      zMove2.isZ = true;
      zMove2.isZOrMaxPowered = true;
      return zMove2;
    }
    const zMove = this.dex.getActiveMove(Z_MOVES[move.type]);
    zMove.basePower = move.zMove.basePower;
    zMove.category = move.category;
    zMove.priority = move.priority;
    zMove.isZOrMaxPowered = true;
    return zMove;
  }
  canZMove(pokemon) {
    if (
      pokemon.side.zMoveUsed ||
      pokemon.terastallized ||
      (pokemon.species.baseSpecies === "Rayquaza" && pokemon.canMegaEvo) ||
      (pokemon.transformed &&
        (pokemon.species.isMega ||
          pokemon.species.isPrimal ||
          pokemon.species.forme === "Ultra"))
    )
      return;
    const item = pokemon.getItem();
    if (!item.zMove) return;
    if (item.itemUser && !item.itemUser.includes(pokemon.species.name)) return;
    let atLeastOne = false;
    let mustStruggle = true;
    const zMoves = [];
    for (const moveSlot of pokemon.moveSlots) {
      if (moveSlot.pp <= 0) {
        zMoves.push(null);
        continue;
      }
      if (!moveSlot.disabled) {
        mustStruggle = false;
      }
      const move = this.dex.moves.get(moveSlot.move);
      let zMoveName = this.getZMove(move, pokemon, true) || "";
      if (zMoveName) {
        const zMove = this.dex.moves.get(zMoveName);
        if (!zMove.isZ && zMove.category === "Status")
          zMoveName = "Z-" + zMoveName;
        zMoves.push({ move: zMoveName, target: zMove.target });
      } else {
        zMoves.push(null);
      }
      if (zMoveName) atLeastOne = true;
    }
    if (atLeastOne && !mustStruggle) return zMoves;
  }
  getMaxMove(move, pokemon) {
    if (typeof move === "string") move = this.dex.moves.get(move);
    if (move.name === "Struggle") return move;
    // Assign the Gigantamax move based on species
    let GigantamaxMove = null;
    const GIGANTAMAX_MOVES = gmaxMap;

    const speciesName = this.dex.toID(pokemon.species?.name);
    if (speciesName && pokemon.gigantamax && GIGANTAMAX_MOVES[speciesName]) {
      GigantamaxMove = GIGANTAMAX_MOVES[speciesName];
    }

    if (GigantamaxMove && move.category !== "Status") {
      const gMaxMove = this.dex.moves.get(GigantamaxMove);
      if (gMaxMove.exists && gMaxMove.type === move.type) return gMaxMove;
    }
    const maxMove = this.dex.moves.get(
      MAX_MOVES[move.category === "Status" ? move.category : move.type]
    );
    if (move.name == "Absorb") {
        console.log("====")
        console.log(move.name);
        console.log(move.type);
        console.log(maxMove);
        console.log(JSON.stringify(MAX_MOVES));
        console.log("====")
    }

    if (maxMove.exists) return maxMove;
  }
  getActiveMaxMove(move, pokemon) {
    if (typeof move === "string") move = this.dex.getActiveMove(move);
    if (move.name === "Struggle") return this.dex.getActiveMove(move);

    // Assign the Gigantamax move based on species
    let GigantamaxMove = null;
    const GIGANTAMAX_MOVES = gmaxMap;

    const speciesName = this.dex.toID(pokemon.species?.name);
    if (speciesName && pokemon.gigantamax && GIGANTAMAX_MOVES[speciesName]) {
      GigantamaxMove = GIGANTAMAX_MOVES[speciesName];
    }

    let maxMove = this.dex.getActiveMove(
      MAX_MOVES[move.category === "Status" ? move.category : move.type]
    );
    if (move.category !== "Status") {
      if (GigantamaxMove) {
        const gMaxMove = this.dex.getActiveMove(GigantamaxMove);
        if (gMaxMove.exists && gMaxMove.type === move.type) maxMove = gMaxMove;
      }
      if (!move.maxMove?.basePower)
        throw new Error(`${move.name} doesn't have a maxMove basePower`);
      // Only update basePower if it's not a special-case G-Max move
      if (
        !["gmaxdrumsolo", "gmaxfireball", "gmaxhydrosnipe"].includes(maxMove.id)
      ) {
        maxMove.basePower = move.maxMove.basePower;
      }
      maxMove.category = move.category;
    }
    maxMove.baseMove = move.id;
    maxMove.priority = move.priority;
    maxMove.isZOrMaxPowered = true;
    return maxMove;
  }
  runZPower(move, pokemon) {
    const zPower = this.dex.conditions.get("zpower");
    if (move.category !== "Status") {
      this.battle.attrLastMove("[zeffect]");
    } else if (move.zMove?.boost) {
      this.battle.boost(move.zMove.boost, pokemon, pokemon, zPower);
    } else if (move.zMove?.effect) {
      switch (move.zMove.effect) {
        case "heal":
          this.battle.heal(pokemon.maxhp, pokemon, pokemon, zPower);
          break;
        case "healreplacement":
          pokemon.side.addSlotCondition(
            pokemon,
            "healreplacement",
            pokemon,
            move
          );
          break;
        case "clearnegativeboost":
          const boosts = {};
          let i;
          for (i in pokemon.boosts) {
            if (pokemon.boosts[i] < 0) {
              boosts[i] = 0;
            }
          }
          pokemon.setBoost(boosts);
          this.battle.add("-clearnegativeboost", pokemon, "[zeffect]");
          break;
        case "redirect":
          pokemon.addVolatile("followme", pokemon, zPower);
          break;
        case "crit2":
          pokemon.addVolatile("focusenergy", pokemon, zPower);
          break;
        case "curse":
          if (pokemon.hasType("Ghost")) {
            this.battle.heal(pokemon.maxhp, pokemon, pokemon, zPower);
          } else {
            this.battle.boost({ atk: 1 }, pokemon, pokemon, zPower);
          }
      }
    }
  }
  targetTypeChoices(targetType) {
    return CHOOSABLE_TARGETS.has(targetType);
  }
  combineResults(left, right) {
    const NOT_FAILURE = "string";
    const NULL = "object";
    const resultsPriorities = [
      "undefined",
      NOT_FAILURE,
      NULL,
      "boolean",
      "number",
    ];
    if (
      resultsPriorities.indexOf(typeof left) >
      resultsPriorities.indexOf(typeof right)
    ) {
      return left;
    } else if (left && !right && right !== 0) {
      return left;
    } else if (typeof left === "number" && typeof right === "number") {
      return left + right;
    } else {
      return right;
    }
  }
  /**
   * 0 is a success dealing 0 damage, such as from False Swipe at 1 HP.
   *
   * Normal PS return value rules apply:
   * undefined = success, null = silent failure, false = loud failure
   */
  getDamage(source, target, move, suppressMessages = false) {
    if (typeof move === "string") move = this.dex.getActiveMove(move);
    if (typeof move === "number") {
      const basePower2 = move;
      move = new import_dex.Dex.Move({
        basePower: basePower2,
        type: "???",
        category: "Physical",
        willCrit: false,
      });
      move.hit = 0;
    }
    if (
      !move.ignoreImmunity ||
      (move.ignoreImmunity !== true && !move.ignoreImmunity[move.type])
    ) {
      if (!target.runImmunity(move.type, !suppressMessages)) {
        return false;
      }
    }
    if (move.ohko) return target.maxhp;
    if (move.damageCallback)
      return move.damageCallback.call(this.battle, source, target);
    if (move.damage === "level") {
      return source.level;
    } else if (move.damage) {
      return move.damage;
    }
    const category = this.battle.getCategory(move);
    let basePower = move.basePower;
    if (move.basePowerCallback) {
      basePower = move.basePowerCallback.call(
        this.battle,
        source,
        target,
        move
      );
    }
    if (!basePower) return basePower === 0 ? void 0 : basePower;
    basePower = this.battle.clampIntRange(basePower, 1);
    let critMult;
    let critRatio = this.battle.runEvent(
      "ModifyCritRatio",
      source,
      target,
      move,
      move.critRatio || 0
    );
    if (this.battle.gen <= 5) {
      critRatio = this.battle.clampIntRange(critRatio, 0, 5);
      critMult = [0, 16, 8, 4, 3, 2];
    } else {
      critRatio = this.battle.clampIntRange(critRatio, 0, 4);
      if (this.battle.gen === 6) {
        critMult = [0, 16, 8, 2, 1];
      } else {
        critMult = [0, 24, 8, 2, 1];
      }
    }
    const moveHit = target.getMoveHitData(move);
    moveHit.crit = move.willCrit || false;
    if (move.willCrit === void 0) {
      if (critRatio) {
        moveHit.crit = this.battle.randomChance(1, critMult[critRatio]);
      }
    }
    if (moveHit.crit) {
      moveHit.crit = this.battle.runEvent("CriticalHit", target, null, move);
    }
    basePower = this.battle.runEvent(
      "BasePower",
      source,
      target,
      move,
      basePower,
      true
    );
    if (!basePower) return 0;
    basePower = this.battle.clampIntRange(basePower, 1);
    if (
      (!source.volatiles["dynamax"] && move.isMax) ||
      (move.isMax && this.dex.moves.get(move.baseMove).isMax)
    ) {
      basePower = 0;
    }
    if (
      basePower < 60 &&
      source.getTypes(true).includes(move.type) &&
      source.terastallized &&
      move.priority <= 0 && // Hard move.basePower check for moves like Dragon Energy that have variable BP
      !move.multihit &&
      !(
        (move.basePower === 0 || move.basePower === 150) &&
        move.basePowerCallback
      )
    ) {
      basePower = 60;
    }
    const level = source.level;
    const attacker =
      move.overrideOffensivePokemon === "target" ? target : source;
    const defender =
      move.overrideDefensivePokemon === "source" ? source : target;
    const isPhysical = move.category === "Physical";
    let attackStat = move.overrideOffensiveStat || (isPhysical ? "atk" : "spa");
    const defenseStat =
      move.overrideDefensiveStat || (isPhysical ? "def" : "spd");
    const statTable = {
      atk: "Atk",
      def: "Def",
      spa: "SpA",
      spd: "SpD",
      spe: "Spe",
    };
    let atkBoosts = attacker.boosts[attackStat];
    let defBoosts = defender.boosts[defenseStat];
    let ignoreNegativeOffensive = !!move.ignoreNegativeOffensive;
    let ignorePositiveDefensive = !!move.ignorePositiveDefensive;
    if (moveHit.crit) {
      ignoreNegativeOffensive = true;
      ignorePositiveDefensive = true;
    }
    const ignoreOffensive = !!(
      move.ignoreOffensive ||
      (ignoreNegativeOffensive && atkBoosts < 0)
    );
    const ignoreDefensive = !!(
      move.ignoreDefensive ||
      (ignorePositiveDefensive && defBoosts > 0)
    );
    if (ignoreOffensive) {
      this.battle.debug("Negating (sp)atk boost/penalty.");
      atkBoosts = 0;
    }
    if (ignoreDefensive) {
      this.battle.debug("Negating (sp)def boost/penalty.");
      defBoosts = 0;
    }
    let attack = attacker.calculateStat(attackStat, atkBoosts, 1, source);
    let defense = defender.calculateStat(defenseStat, defBoosts, 1, target);
    attackStat = category === "Physical" ? "atk" : "spa";
    attack = this.battle.runEvent(
      "Modify" + statTable[attackStat],
      source,
      target,
      move,
      attack
    );
    defense = this.battle.runEvent(
      "Modify" + statTable[defenseStat],
      target,
      source,
      move,
      defense
    );
    if (
      this.battle.gen <= 4 &&
      ["explosion", "selfdestruct"].includes(move.id) &&
      defenseStat === "def"
    ) {
      defense = this.battle.clampIntRange(Math.floor(defense / 2), 1);
    }
    const tr = this.battle.trunc;
    const baseDamage = tr(
      tr(tr(tr((2 * level) / 5 + 2) * basePower * attack) / defense) / 50
    );
    return this.modifyDamage(
      baseDamage,
      source,
      target,
      move,
      suppressMessages
    );
  }
  modifyDamage(baseDamage, pokemon, target, move, suppressMessages = false) {
    const tr = this.battle.trunc;
    if (!move.type) move.type = "???";
    const type = move.type;
    baseDamage += 2;
    if (move.spreadHit) {
      const spreadModifier =
        move.spreadModifier ||
        (this.battle.gameType === "freeforall" ? 0.5 : 0.75);
      this.battle.debug("Spread modifier: " + spreadModifier);
      baseDamage = this.battle.modify(baseDamage, spreadModifier);
    } else if (move.multihitType === "parentalbond" && move.hit > 1) {
      const bondModifier = this.battle.gen > 6 ? 0.25 : 0.5;
      this.battle.debug(`Parental Bond modifier: ${bondModifier}`);
      baseDamage = this.battle.modify(baseDamage, bondModifier);
    }
    baseDamage = this.battle.runEvent(
      "WeatherModifyDamage",
      pokemon,
      target,
      move,
      baseDamage
    );
    const isCrit = target.getMoveHitData(move).crit;
    if (isCrit) {
      baseDamage = tr(
        baseDamage * (move.critModifier || (this.battle.gen >= 6 ? 1.5 : 2))
      );
    }
    baseDamage = this.battle.randomizer(baseDamage);
    if (type !== "???") {
      let stab = 1;
      const tera =
        pokemon.teraType.charAt(0).toUpperCase() +
        pokemon.teraType.slice(1).toLowerCase();
      // Ensure stellarBoostedTypes is initialized
      if (!pokemon.stellarBoostedTypes) {
        pokemon.stellarBoostedTypes = [];
      }
      const isSTAB =
        move.forceSTAB ||
        pokemon.hasType(type) ||
        pokemon.getTypes(false, true).includes(type) ||
        (pokemon.terastallized && tera === type);
      if (isSTAB) {
        stab = 1.5;
      }
      if (pokemon.terastallized && tera === "Stellar" && pokemon.species.name === "Terapagos-Stellar") {
		if (isSTAB && move.name !== "Tera Starstorm") {
			stab = 2;
		} else {
			stab = 1.2;
		}
	  } else if (pokemon.terastallized && tera === "Stellar") {
		  if (!pokemon.stellarBoostedTypes.includes(type)) {	
		    if (isSTAB) {
			  stab = 2;
		    } else {
			  stab = 1.2;
		    }
			pokemon.stellarBoostedTypes.push(type);
		 }
	  }
      if (
        pokemon.terastallized &&
        tera === type &&
        pokemon.getTypes(false, true).includes(type)
      ) {
        stab = 2;
        //console.log(`[STAB] Tera Type and Type matches move Type - Type: ${type}, Boost: 2x`);
      }
      stab = this.battle.runEvent("ModifySTAB", pokemon, target, move, stab);
	  baseDamage = this.battle.modify(baseDamage, stab);
    }
    let typeMod = target.runEffectiveness(move);
    typeMod = this.battle.clampIntRange(typeMod, -6, 6);
    target.getMoveHitData(move).typeMod = typeMod;
    if (!suppressMessages) {
      if (typeMod >= 2) {
        this.battle.add("-extremelyeffective", target);
      } else if (typeMod <= -2) {
        this.battle.add("-mostlyineffective", target);
      }
    }
    if (typeMod > 0) {
      if (!suppressMessages && typeMod < 2)
        this.battle.add("-supereffective", target);
      for (let i = 0; i < typeMod; i++) {
        baseDamage *= 2;
      }
    }
    if (typeMod < 0) {
      if (!suppressMessages && typeMod > -2)
        this.battle.add("-resisted", target);
      for (let i = 0; i > typeMod; i--) {
        baseDamage = tr(baseDamage / 2);
      }
    }
    if (isCrit && !suppressMessages) this.battle.add("-crit", target);
    if (
      pokemon.status === "brn" &&
      move.category === "Physical" &&
      !pokemon.hasAbility("guts")
    ) {
      if (this.battle.gen < 6 || move.id !== "facade") {
        baseDamage = this.battle.modify(baseDamage, 0.5);
      }
    }
    if (this.battle.gen === 5 && !baseDamage) baseDamage = 1;
    baseDamage = this.battle.runEvent(
      "ModifyDamage",
      pokemon,
      target,
      move,
      baseDamage
    );
    if (move.isZOrMaxPowered && target.getMoveHitData(move).zBrokeProtect) {
      baseDamage = this.battle.modify(baseDamage, 0.25);
      this.battle.add("-zbroken", target);
    }
    if (this.battle.gen !== 5 && !baseDamage) return 1;
    return tr(baseDamage, 16);
  }
  /**
   * Confusion damage is unique - most typical modifiers that get run when calculating
   * damage (e.g. Huge Power, Life Orb, critical hits) don't apply. It also uses a 16-bit
   * context for its damage, unlike the regular damage formula (though this only comes up
   * for base damage).
   */
  getConfusionDamage(pokemon, basePower) {
    const tr = this.battle.trunc;
    const attack = pokemon.calculateStat("atk", pokemon.boosts["atk"]);
    const defense = pokemon.calculateStat("def", pokemon.boosts["def"]);
    const level = pokemon.level;
    const baseDamage =
      tr(tr(tr(tr((2 * level) / 5 + 2) * basePower * attack) / defense) / 50) +
      2;
    let damage = tr(baseDamage, 16);
    damage = this.battle.randomizer(damage);
    return Math.max(1, damage);
  }
  // #endregion
  // #region MEGA EVOLUTION
  // ==================================================================
  canMegaEvo(pokemon) {
    const species = pokemon.species;
    const item = pokemon.getItem();
    if (
      species.baseSpecies === "Rayquaza" &&
      (pokemon.terastallized || pokemon.getItem().zMove)
    ) {
      return null;
    }
    if (
      species.baseSpecies === "Rayquaza" &&
      pokemon.baseMoves.includes("dragonascent")
    ) {
      return "Rayquaza-Mega";
    }
    const megaKey = species.otherFormes?.find((form) =>
      /.*-Mega(-[a-zA-Z])?/.test(form)
    );
    const megaForme = megaKey && this.dex.species.get(megaKey);

    if (
      (this.battle.gen <= 7 || this.battle.ruleTable.has("+pokemontag:past")) &&
      megaForme?.requiredMove &&
      pokemon.baseMoves.includes(
        (0, import_dex.toID)(megaForme.requiredMove)
      ) &&
      !item.zMove
    ) {
      return megaForme.name;
    }
    if (
      item.megaEvolves?.includes(species.name) &&
      item.megaStone !== species.name
    ) {
      return item.megaStone;
    }
    return null;
  }
  canUltraBurst(pokemon) {
    if (
      ["Necrozma-Dawn-Wings", "Necrozma-Dusk-Mane"].includes(
        pokemon.baseSpecies.name
      ) &&
      pokemon.getItem().id === "ultranecroziumz"
    ) {
      return "Necrozma-Ultra";
    }
    return null;
  }
  runMegaEvo(pokemon) {
    const speciesid = pokemon.canMegaEvo || pokemon.canUltraBurst;
    if (!speciesid) return false;
    pokemon.formeChange(speciesid, pokemon.getItem(), true);
    const wasMega = pokemon.canMegaEvo;
    for (const ally of pokemon.side.pokemon) {
      if (wasMega) {
        ally.canMegaEvo = null;
      } else {
        ally.canUltraBurst = null;
      }
    }
    this.battle.runEvent("AfterMega", pokemon);
    return true;
  }
  canTerastallize(pokemon) {
    if (
      pokemon.species.baseSpecies === "Rayquaza" &&
      !pokemon.getItem().zMove
    ) {
      return pokemon.teraType;
    }
    if (
      pokemon.species.name === "Groudon-Primal" ||
      pokemon.species.name === "Kyogre-Primal"
    ) {
      return null;
    }
    if (
      pokemon.getItem().zMove ||
      pokemon.canMegaEvo ||
      this.dex.gen !== 9 ||
      pokemon.volatiles["dynamax"]
    ) {
      return null;
    }
    return pokemon.teraType;
  }
  terastallize(pokemon) {
    if (
      pokemon.illusion &&
      ["Ogerpon", "Terapagos"].includes(pokemon.illusion.species.baseSpecies)
    ) {
      this.battle.singleEvent(
        "End",
        this.dex.abilities.get("Illusion"),
        pokemon.abilityState,
        pokemon
      );
    }
    const type =
      pokemon.teraType.charAt(0).toUpperCase() +
      pokemon.teraType.slice(1).toLowerCase();
    //console.log(`[Tera] ${pokemon.name} is terastallizing into: ${type}`);
    this.battle.add("-terastallize", pokemon, type);
    pokemon.canMegaEvo = null;
    pokemon.terastallized = type;
    for (const ally of pokemon.side.pokemon) {
      ally.canTerastallize = null;
    }

    pokemon.addedType = "";
    pokemon.knownType = true;
    pokemon.apparentType = type;
    if (pokemon.species.baseSpecies === "Ogerpon") {
      const tera = pokemon.species.id === "ogerpon" ? "tealtera" : "tera";
      pokemon.formeChange(pokemon.species.id + tera, null, true);
    }
    if (pokemon.species.name === "Terapagos-Terastal" && type === "Stellar") {
      pokemon.formeChange("Terapagos-Stellar", null, true);
      pokemon.baseMaxhp = Math.floor(
        (Math.floor(
          2 * pokemon.species.baseStats["hp"] +
            pokemon.set.ivs["hp"] +
            Math.floor(pokemon.set.evs["hp"] / 4) +
            100
        ) *
          pokemon.level) /
          100 +
          10
      );
      const newMaxHP = pokemon.baseMaxhp;
      pokemon.hp = newMaxHP - (pokemon.maxhp - pokemon.hp);
      pokemon.maxhp = newMaxHP;
      this.battle.add("-heal", pokemon, pokemon.getHealth, "[silent]");
    }

    this.battle.runEvent("AfterTerastallization", pokemon);
  }
  // #endregion
}
//# sourceMappingURL=battle-actions.js.map
