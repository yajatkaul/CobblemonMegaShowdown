# MegaShowdown — MaxBond

An unofficial fork of [Cobblemon Mega Showdown](https://github.com/yajatkaul/CobblemonMegaShowdown), created by **Yajat Kaul**.

**I am not the author of the original mod.** This repository does not document or claim the base mod's features — it contains a single behavioral change on top of it. For anything about the original mod (features, datapacks, wiki, support), go to the author's project.

---

## Why this fork exists

In the anime and the series, Mega Evolution and Z-Moves never come down to just holding the stone or the crystal. They depend on the **bond** between trainer and Pokémon: the item is the channel, but what ignites the transformation is the relationship between the two. It's the central theme of the entire Kalos arc, and the reason a trainer holding the right stone can still fail to Mega Evolve.

The original mod doesn't reflect that — equipping the item is enough. This fork adds the missing requirement, so the game mechanic matches the story's.

**In one line: your Pokémon only Mega Evolves or uses a Z-Move if its friendship with you is maxed out.**

---

## What it does

A **player-owned** Pokémon needs maximum friendship to access Mega Evolution and Z-Moves. Below that threshold, those options are simply unavailable.

| | Bond required |
|---|---|
| Mega Evolution | Yes |
| Z-Moves | Yes |
| Dynamax / Gigantamax | No — still gated by the Dynamax Band |
| Terastallization | No |
| Ultra Burst | No |

Pokémon **not** owned by a player (wild and NPC trainer Pokémon) are exempt by design. Without that exemption, trainer battles would lose their mechanics and fights would end up lopsided.

---

## Configuration

The threshold is **fully configurable** and lives in the mod's own config file:

```
config/mega_showdown/config.json  →  "megaFriendshipRequirement"
```

It's a plain friendship value, so you decide how demanding the bond is:

| Value | Effect |
|---|---|
| `255` | Absolute max friendship — the strictest reading of the anime rule |
| `200` | Demanding, but reachable well before the cap |
| `160` | Same bar Cobblemon uses for friendship evolutions |
| `0` | Requirement effectively disabled |

On first launch the value defaults to Cobblemon's `maxPokemonFriendship`, so out of the box the rule is "maxed friendship". From then on the key sits in your `config.json` and **your value always wins** — the default never overrides it again.

This is deliberately independent from Cobblemon's own settings: changing it affects Mega Evolution and Z-Moves only, and leaves Cobblemon's friendship cap and friendship-based evolutions untouched.

> **Heads-up:** don't set it above Cobblemon's `maxPokemonFriendship`. Friendship is hard-capped at that value, so a higher requirement can never be met and Mega Evolution and Z-Moves would be permanently unavailable.

> **Note:** the `minBondingRequired` key in the same file does **not** control this. It belongs to the original mod and only affects the Ash Cap item.

---

## How it's enforced

The requirement is checked at three layers so server and client stay consistent:

| Layer | File | Role |
|---|---|---|
| General Mega gate | `gimmick/MegaGimmick.java` | Blocks Mega Evolution at the source |
| Battle request | `mixin/battle/ShowdownActionRequestMixin.java` | Strips the gimmicks from the request, so the buttons never appear |
| Action validation | `mixin/battle/MoveActionResponseMixin.java` | Rejects the action server-side if it arrives anyway |

The shared rule lives in a single place, `gimmick/MaxBond.java`, so the three layers can't drift apart.

---

## Building

```bash
./gradlew build
```

Artifacts land in:

```
fabric/build/libs/
neoforge/build/libs/
```

| | |
|---|---|
| Minecraft | 1.21.1 |
| Cobblemon | 1.7.3+1.21.1 |
| Platforms | Fabric · NeoForge |

---

## Credits and license

The original mod, its code and its assets are the work of **Yajat Kaul** and contributors. All credit for the base mod belongs to them.

This fork is distributed under the same **MEGA SHOWDOWN LICENSE v2.1** included in [`LICENSE.md`](LICENSE.md), whose terms remain in force. Read it before redistributing: it requires prior written permission to publish any part of the software — modified or not — on public platforms, and prohibits commercial use. The mod's original visual assets are licensed under CC BY-NC-SA 4.0.
