package dev.bearcat.basetap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class DamageAccumulator {
    private var lastDamageTime = 0L
    private var accumulatedDamage = 0

    fun addDamage(damage: Int): Int {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDamageTime > 1000) {
            // Reset if more than 1 second has passed
            accumulatedDamage = damage
        } else {
            accumulatedDamage += damage
        }
        lastDamageTime = currentTime
        return accumulatedDamage
    }
}

enum class Bases(val id: Int, val baseName: String, val maxHealth: Int) {
    CHOPPER_BASE(1, "Chopper Base(Cunning)", 30),
    ADMINISTRATORS_TOWER(2, "Administrator's Tower(Cunning)", 30),
    JEDHA_CITY(3, "Jedha City(Cunning)", 25),
    KESTRO_CITY(4, "Kestro City(Aggression)", 30),
    CATACOMBS_OF_CADERA(5, "Catacombs of Cadera(Aggression)", 30),
    TARKINTOWN(6, "Tarkintown(Aggression)", 25),
    ECHO_BASE(7, "Echo Base(Command)", 30),
    COMMAND_CENTER(8, "Command Center(Command)", 30),
    ENERGY_LAB(9, "Energy Conversion Lab(Command)", 25),
    DAGOBAH_SWAMP(10, "Dagobah Swamp(Vigilance)", 30),
    CAPITAL_CITY(11, "Capital City(Vigilance)", 30),
    SECURITY_COMPLEX(12, "Security Complex(Vigilance)", 25),
    JABBAS_PALACE(13, "Jabba's Palace(Cunning)", 30),
    CORONET_CITY(14, "Coronet City(Cunning)", 30),
    SPICE_MINES(15, "Spice Mines(Aggression)", 30),
    DEATH_WATCH_HIDEOUT(16, "Death Watch Hideout(Aggression)", 30),
    NEVARRO_CITY(17, "Nevarro City(Command)", 30),
    MAZ_CASTLE(18, "Maz Kanata's Castle(Command)", 30),
    REMOTE_VILLAGE(19, "Remote Village(Vigilance)", 30),
    REMNANT_FACILITY(20, "Remnant Science Facility(Vigilance)", 30),
    PYKE_PALACE(21, "Pyke Palace(Cunning)", 30),
    LEVEL_1313(22, "Level 1313(Cunning)", 30),
    PETRANAKI_ARENA(23, "Petranaki Arena(Cunning)", 26),
    THE_NEST(24, "The Nest(Aggression)", 30),
    KCM_FACILITY(25, "KCM Mining Facility(Aggression)", 30),
    SHADOW_CAMP(26, "Shadow Collective Camp(Aggression)", 25),
    TIPOCA_CITY(27, "Tipoca City(Command)", 30),
    GRIEVOUS_LAIR(28, "Lair of Grievous(Command)", 30),
    DROID_MANUFACTORY(29, "Droid Manufactory(Command)", 24),
    CRYSTAL_CITY(30, "The Crystal City(Vigilance)", 30),
    SUNDARI(31, "Sundari(Vigilance)", 30),
    PAU_CITY(32, "Pau City(Vigilance)", 26),
    // Set 4
    COLOSSUS(33, "Colossus(Vigilance)", 35),
    DATA_VAULT(34, "Data Vault(Command)", 33),
    THERMAL_OSCILLATOR(35, "Thermal Oscillator(Aggression)", 27),
    NABAT_VILLAGE(36, "Nabat Village(Cunning)", 27),
    LAKE_COUNTRY(37, "Lake Country", 34),
    CITY_IN_THE_CLOUDS(38, "City in the Clouds(Vigilance)", 30),
    SHIELD_GENERATOR_COMPLEX(39, "Shield Generator Complex(Vigilance)", 30),
    RESISTANCE_HEADQUARTERS(40, "Resistance Headquarters(Command)", 30),
    THEED_PALACE(41, "Theed Palace(Command)", 30),
    MASSASSI_TEMPLE(42, "Massassi Temple(Aggression)", 30),
    NADIRI_DOCKYARDS(43, "Nadiri Dockyards(Aggression)", 30),
    MOS_EISLEY(44, "Mos Eisley(Cunning)", 30),

    // Set 5
    VERGENCE_TEMPLE(45, "Vergence Temple(Vigilance)", 25),
    NIGHTSISTER_LAIR(46, "Nightsister Lair(Vigilance)", 28),
    SHADOWED_UNDERCITY(47, "Shadowed Undercity(Vigilance)", 28),
    MYSTIC_MONASTERY(48, "Mystic Monastery(Command)", 25),
    JEDI_TEMPLE(49, "Jedi Temple(Command)", 28 ),
    STARLIGHT_TEMPLE(50, "Starlight Temple(Command)", 28),
    TEMPLE_OF_DESTRUCTION(51, "Temple of Destruction(Aggression)", 25),
    FORTRESS_VADER(52, "Fortress Vader(Aggression)", 28),
    STRANGLED_CLIFFS(53, "Strangled Cliffs(Aggression)", 28),
    TOMB_OF_EILRAM(54, "Tomb of Eilram(Cunning)", 25),
    CRYSTAL_CAVES(55, "Crystal Caves(Cunning)", 28),
    THE_HOLY_CITY(56, "The Holy City(Cunning)", 28),

    // Set 6
    RIX_ROAD(57, "Rix Road(Vigilance)", 30),
    USCRU_ENTERTAINMENT_DISTRICT(58, "Uscru Entertainment District(Vigilance)", 30),
    REPUBLIC_CITY(59, "Republic City(Command)", 30),
    SENATE_ROTUNDA(60, "Senate Rotunda(Command)", 30),
    IMERIAL_PRISON_COMPLEX(61, "Imperial Prison Complex(Aggression)", 30),
    NAVAL_INTELLIGENCE_HQ(62, "Naval Intelligence HQ(Aggression", 30),
    AMNESTY_HOUSING(63, "Amnesty Housing(Cunning)", 30),
    MOUNT_TANTISS(64, "Mount Tantiss(Cunning)", 30)
    ;

    companion object {
        fun findById(id: Int): Bases? = values().find { it.id == id }
    }
}

@Composable
fun DamageIndicator(
    damage: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    rotation: Float = 0f
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Text(
            text = if (damage > 0) "+$damage" else "$damage",
            color = if (damage < 0) Color.Green else Color.Red,
            fontSize = 36.sp,
            modifier = Modifier.rotate(rotation)
        )
    }
}

fun getBaseTypeColor(baseName: String): Color {
    return when {
        baseName.contains("(Command)") -> Color(0xFF4CAF50)    // Green
        baseName.contains("(Cunning)") -> Color(0xFFFFEB3B)    // Yellow
        baseName.contains("(Vigilance)") -> Color(0xFF2196F3)  // Blue
        baseName.contains("(Aggression)") -> Color(0xFFE53935) // Red
        else -> Color.Black                                     // Default color
    }
}

@Composable
fun LifeCounter(
    modifier: Modifier = Modifier,
    isRightSide: Boolean,
    isTopPlayer: Boolean = false,
    backgroundImageId: Int,
    baseId:Int,
    life: Int,
    playerName: String,
    onNameChange: (String) -> Unit,
    onImageChange: (Int) -> Unit,
    onLifeChange: (Int) -> Unit,
    rotation: Float = 0f,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit,
    playerId: Int,
    onBaseChange: (Int) -> Unit,
) {
    var showNameDialog by remember { mutableStateOf(false) }
    var showDamageIndicator by remember { mutableStateOf(false) }
    var currentDamage by remember { mutableStateOf(0) }
    val damageAccumulator = remember { DamageAccumulator() }
    val coroutineScope = rememberCoroutineScope()
    // Base Max Health (For grey out)
    val baseMaxHealth = Bases.findById(baseId)?.maxHealth ?: 30

    // For player name color
    val baseName = Bases.findById(baseId)?.baseName ?: "Select Base"
    val baseTypeColor = getBaseTypeColor(baseName)

    // Shake animation state
    var shakeKey by remember { mutableStateOf(0f) }
    //var shouldShake by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = shakeKey,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    fun handleLifeChange(newDamage: Int) {
        // If player is already dead (at or above max health), do nothing
        // +1 to allow for accidents
        if (life >= baseMaxHealth + 1) return

        if (life + newDamage >= 0) {
            val accumulatedDamage = damageAccumulator.addDamage(newDamage)
            currentDamage = accumulatedDamage
            showDamageIndicator = true
            onLifeChange(life + newDamage)

            // Trigger shake animation when taking damage
            if (newDamage > 0) {
                // Toggle between 1f and 2f to force animation restart
                shakeKey = if (shakeKey == 1f) 2f else 1f
            }

            coroutineScope.launch {
                delay(3000)
                showDamageIndicator = false
            }
        }
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (if (shakeKey != 0f) ((shakeKey - shakeOffset) * 15).roundToInt() else 0),
                    y = 0
                )
            }
            .background(
                color = Color.White.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.large
            )
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.large
            )
    ) {
        // Background Image

        Image(
            painter = painterResource(id = backgroundImageId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .run {
                    if (isTopPlayer) rotate(180f) else this
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Dead player overlay
        if (life >= baseMaxHealth) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            )
        }

        // Card Banner - positioned at the top of the section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .run {
                    if (isTopPlayer) rotate(180f) else this
                }
        ) {
            CardBanner(
                modifier = Modifier
                    .align(if (isTopPlayer) Alignment.BottomCenter else Alignment.BottomCenter),
                baseId = baseId
            )
        }

        // Clickable areas for life adjustment
        if (!isRightSide) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { handleLifeChange(-1) }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { handleLifeChange(1) }
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { handleLifeChange(1) }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { handleLifeChange(-1) }
                )
            }
        }

        // Life total with damage indicator
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = if (isRightSide) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(), // Use full width
                //.padding(horizontal = 8.dp), // Add some horizontal padding
                contentAlignment = Alignment.Center
            ) {
                val textRotation = when {
                    isTopPlayer -> 0f
                    isRightSide -> -90f
                    else -> 90f
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.rotate(textRotation)
                ) {
                    Text(
                        text = if (life >= baseMaxHealth) getRandomDeathMessage() else "-$life+",
                        color = if (life >= baseMaxHealth) Color.Red else Color.White,
                        fontSize = if (life >= baseMaxHealth) 30.sp else 70.sp,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = Shadow(color = Color.Black, blurRadius = 12f),
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 3,
                        softWrap = true
                    )
                    DamageIndicator(
                        damage = currentDamage,
                        isVisible = showDamageIndicator,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth() // Use full width
        ) {
            // Initiative Token
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = if (initiativePlayer == playerId)
                            Color.Yellow.copy(alpha = 0.7f)
                        else
                            Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { onInitiativeClaimed(playerId) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.initiative_token),
                    contentDescription = "Initiative Token",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Player Name
            Text(
                text = playerName,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = baseTypeColor.copy(alpha = 0.7f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { showNameDialog = true }
                    .padding(16.dp),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    shadow = Shadow(color = Color.Black, blurRadius = 12f)
                ),
                maxLines = 1,
                softWrap = false,
            )
        }

        if (showNameDialog) {
            LeaderNameDialog(
                initialName = playerName,
                currentImageId = backgroundImageId,
                baseId = baseId,
                onDismissRequest = { showNameDialog = false },
                onConfirm = {
                    onNameChange(it)
                    showNameDialog = false
                },
                onImageChange = onImageChange,
                onBaseChange = onBaseChange
            )
        }
    }
    }
}

@Composable
fun LeaderNameDialog(
    initialName: String,
    currentImageId: Int,
    baseId: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    onImageChange: (Int) -> Unit,
    onBaseChange: (Int) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var showLeaderSelector by remember { mutableStateOf(false) }
    var showBaseSelector by remember { mutableStateOf(false) }

    if (showLeaderSelector) {
        LeaderSelectorForPlayer(
            onDismissRequest = { showLeaderSelector = false },
            onImageSelected = {
                onImageChange(it)
                showLeaderSelector = false
            },
            currentImageId = currentImageId
        )
    }

    if (showBaseSelector) {
        BaseSelectorDialog(
            onDismissRequest = { showBaseSelector = false },
            onBaseSelected = {
                onBaseChange(it)
                showBaseSelector = false
            },
            currentBaseId = baseId
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Player") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Button(
                    onClick = { showLeaderSelector = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Leader")
                }
                Button(
                    onClick = { showBaseSelector = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Base")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BaseSelectorDialog(
    onDismissRequest: () -> Unit,
    onBaseSelected: (Int) -> Unit,
    currentBaseId: Int
) {
    var searchText by remember { mutableStateOf("") }

    // Filter bases based on search text
    val filteredBases = remember(searchText) {
        if (searchText.isBlank()) {
            Bases.values().toList()
        } else {
            Bases.values().filter { it.baseName.contains(searchText, ignoreCase = true) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Choose Base") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search Bases") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredBases) { base ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onBaseSelected(base.id) }
                                .background(
                                    if (currentBaseId == base.id)
                                        Color.Gray.copy(alpha = 0.5f)
                                    else Color.Transparent
                                )
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = base.baseName,
                                    fontSize = 16.sp
                                )
                            }
                            if (currentBaseId == base.id) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Green
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun LeaderSelectorForPlayer(
    onDismissRequest: () -> Unit,
    onImageSelected: (Int) -> Unit,
    currentImageId: Int
) {
    // Available images and their labels
    val images = listOf(
        R.drawable.yoda_sensing_darkness to "Yoda, Sensing Darkness",
        R.drawable.captain_rex_fighting_for_his_brothers to "Captain Rex, Fighting for His Brothers",
        R.drawable.darth_vader_dark_lord_of_the_sith to "Darth Vader, Dark Lord of the Sith",
        R.drawable.luke_skywalker_faithful_friend to "Luke Skywalker, Faithful Friend",
        R.drawable.han_solo_audacious_smuggler to "Han Solo, Audacious Smuggler",
        R.drawable.obi_wan_patient_mentor to "Obi-Wan Kenobi, Patient Mentor",
        R.drawable.emperor_palpatine_galactic_ruler to "Emperor Palpatine, Galactic Ruler",
        R.drawable.ahsoka_tano_snips to "Ahsoka Tano, Snips",
        R.drawable.boba_fett_daimyo to "Boba Fett, Daimyo",
        R.drawable.maul_a_rival_in_darkness to "Maul, a Rival in Darkness",
        R.drawable.cassian_andor_dedicated_to_the_rebellion to "Cassian Andor, Dedicated to the Rebellion",
        R.drawable.jyn_erso_resisting_oppression to "Jyn Erso, Resisting Oppression",
        R.drawable.sabine_wren_galvanized_revolutionary to "Sabine Wren, Galvanized Revolutionary",
        R.drawable.boba_fett_collecting_the_bounty to "Boba Fett, Collecting the Bounty",
        R.drawable.ig_88_ruthless_bounty_hunter to "IG-88, Ruthless Bounty Hunter",
        R.drawable.grand_inquisitor_hunting_the_jedi to "Grand Inquisitor, Hunting the Jedi",
        R.drawable.leia_organa_alliance_general to "Leia Organa, Alliance General",
        R.drawable.hera_syndulla_spectre_two to "Hera Syndulla, Spectre Two",
        R.drawable.grand_moff_tarkin_oversector_governor to "Grand Moff Tarkin, Oversector Governor",
        R.drawable.chirrut_imwe_one_with_the_force to "Chirrut Imwe, One with the Force",
        R.drawable.chewbacca_walking_carpet to "Chewbacca, Walking Carpet",
        R.drawable.iden_versio_inferno_squad_commander to "Iden Versio, Inferno Squad Commander",
        R.drawable.director_krennic_aspiring_to_authority to "Director Krennic, Aspiring to Authority",
        R.drawable.the_mandalorian_sworn_to_the_creed to "The Mandalorian, Sworn to the Creed",
        R.drawable.lando_calrissian_with_impeccable_taste to "Lando Calrissian, With Impeccable Taste",
        R.drawable.fennec_shand_honoring_the_deal to "Fennec Shand, Honoring the Deal",
        R.drawable.doctor_aphra_rapacious_archaeologist to "Doctor Aphra, Rapacious Archaeologist",
        R.drawable.cad_bane_he_who_needs_no_introduction to "Cad Bane, He Who Needs No Introduction",
        R.drawable.han_solo_worth_the_risk to "Han Solo, Worth the Risk",
        R.drawable.bo_katan_kryze_princess_in_exile to "Bo Katan Kryze, Princess in Exile",
        R.drawable.kylo_ren_rash_and_deadly to "Kylo Ren, Rash and Deadly",
        R.drawable.bossk_hunting_his_prey to "Bossk, Hunting His Prey",
        R.drawable.hunter_outcast_sergeant to "Hunter, Outcast Sergeant",
        R.drawable.moff_gideon_formidable_commander to "Moff Gideon, Formidable Commander",
        R.drawable.jabba_the_hutt_his_high_exaltedness to "Jabba the Hutt, His High Exaltedness",
        R.drawable.hondo_ohnaka_thats_good_business to "Hondo Ohnaka, That's Good Business",
        R.drawable.rey_more_than_a_scavenger to "Rey, More Than a Scavenger",
        R.drawable.finn_this_is_a_rescue to "Finn, This is a Rescue",
        R.drawable.qira_i_alone_survived to "Qira, I Alone Survived",
        R.drawable.gar_saxon_viceroy_of_mandalore to "Gar Saxon, Viceroy of Mandalore",
        R.drawable.quinlan_vos_sticking_the_landing to "Quinlan Vos, Sticking the Landing",
        R.drawable.chancellor_palpatine_playing_both_sides to "Chancellor Palpatine, Playing Both Sides",
        R.drawable.jango_fett_concealing_the_conspiracy to "Jango Fett, Concealing the Conspiracy",
        R.drawable.general_grievous_general_of_the_droid_armies to "General Grievous, General of the Droid Armies",
        R.drawable.asajj_ventress_unparalleled_adversary to "Asajj Ventress, Unparalleled Adversary",
        R.drawable.mace_windu_vaapad_form_master to "Mace Windu, Vaapad Form Master",
        R.drawable.anakin_skywalker_what_it_takes_to_win to "Anakin Skywalker, What It Takes to Win",
        R.drawable.pre_vizsla_pursuing_the_throne to "Pre Vizsla, Pursuing the Throne",
        R.drawable.padme_amidala_serving_the_republic to "Padme Amidala, Serving the Republic",
        R.drawable.wat_tambor_techno_union_foreman to "Wat Tambor, Techno Union Foreman",
        R.drawable.count_dooku_face_of_the_confederacy to "Count Dooku, Face of the Confederacy",
        R.drawable.nute_gunray_vindictive_viceroy to "Nute Gunray, Vindictive Viceroy",
        R.drawable.nala_se_clone_engineer to "Nala Se, Clone Engineer",
        // Jump to Lightspeed leaders
        R.drawable.asajj_ventress_i_work_alone to "Asajj Ventress, I Work Alone",
        R.drawable.grand_admiral_thrawn_how_unfortunate to "Grand Admiral Thrawn, How Unfortunate",
        R.drawable.rose_tico_saving_what_we_love to "Rose Tico, Saving What We Love",
        R.drawable.lando_calrissian_buying_time to "Lando Calrissian, Buying Time",
        R.drawable.admiral_piett_commanding_the_armada to "Admiral Piett, Commanding the Armada",
        R.drawable.admiral_holdo_were_not_alone to "Admiral Holdo, We're Not Alone",
        R.drawable.wedge_antilles_leader_of_red_sqaudron to "Wedge Antilles, Leader of Red Sqaudron",
        R.drawable.darth_vader_victor_squadron_leader to "Darth Vader, Victor Squadron Leader",
        R.drawable.boba_fett_any_methods_necessary to "Boba Fett, Any Methods Necessary",
        R.drawable.poe_dameron_i_can_fly_anything to "Poe Dameron, I Can Fly Anything",
        R.drawable.luke_skywalker_hero_of_yavin to "Luke Skywalker, Hero of Yavin",
        R.drawable.major_vonreg_red_baron to "Major Vonreg, Red Baron",
        R.drawable.captain_phasma_chrome_dome to "Captain Phasma, Chrome Dome",
        R.drawable.admiral_trench_chk_chk_chk_chk to "Admiral Trench, Chk-Chk-Chk-Chk",
        R.drawable.rio_durant_wisecracking_wheelman to "Rio Durant, Wisecracking Wheelman",
        R.drawable.admiral_ackbar_its_a_trap to "Admiral Ackbar, It's a Trap!",
        R.drawable.han_solo_never_tell_me_the_odds to "Han Solo, Never Tell Me the Odds",
        R.drawable.kazuda_xiono_best_pilot_in_the_galaxy to "Kazuda Xiono, Best Pilot in the Galaxy",
        // Legends of the Force Leaders
        R.drawable.third_sister_seething_with_ambition to "Third Sister, Seething With Ambition",
        R.drawable.qui_gon_jinn_student_of_the_living_force to "Qui-Gon Jinn, Student of the Living Force",
        R.drawable.darth_revan_scourge_of_the_old_republic to "Darth Revan, Scourge of the Old Republic",
        R.drawable.kanan_jarrus_help_us_survive to "Kanan Jarrus, Help Us Survive",
        R.drawable.obi_wan_kenobi_courage_makes_heroes to "Obi-Wan Kenobi, Courage Makes Heroes",
        R.drawable.darth_maul_sith_revealed to "Darth Maul, Sith Revealed",
        R.drawable.anakin_skywalker_tempted_by_the_dark_side to "Anakin Skywalker, Tempted by the Dark Side",
        R.drawable.cal_kestis_i_cant_keep_hiding to "Cal Kestis, I Can't Keep Hiding",
        R.drawable.barriss_offee_we_have_become_villians to "Barriss Offee, We Have Become Villians",
        R.drawable.rey_nobody to "Rey, Nobody",
        R.drawable.kit_fisto_focused_jedi_master to "Kit Fisto, Focused Jedi Master",
        R.drawable.avar_kiss_marshal_of_starlight to "Avar Kiss, Marshal of Starlight",
        R.drawable.supreme_leader_snoke_in_the_seat_of_power to "Supreme Leader Snoke, In the Seat of Power",
        R.drawable.morgan_elsbeth_following_the_call to "Morgan Elspeth, Following the Call",
        R.drawable.ahsoka_tano_fighting_for_peace to "Ahsoka Tano, Fighting for Peace",
        R.drawable.mother_talzin_power_through_magic to "Mother Talzin, Power Through Magick",
        R.drawable.kylo_ren_were_not_done_yet to "Kylo Ren, We're Not Done Yet",

        // Showcase images
        R.drawable.yoda_sensing_darkness_showcase to "Yoda, Sensing Darkness (Showcase)",
        R.drawable.captain_rex_fighting_for_his_brothers_showcase to "Captain Rex, Fighting for His Brothers (Showcase)",
        R.drawable.han_solo_audacious_smuggler_showcase to "Han Solo, Audacious Smuggler (Showcase)",
        R.drawable.obi_wan_kenobi_patient_mentor_showcase to "Obi-Wan Kenobi, Patient Mentor (Showcase)",
        R.drawable.emperor_palpatine_galactic_ruler_showcase to "Emperor Palpatine, Galactic Ruler (Showcase)",
        R.drawable.ahsoka_tano_snips_showcase to "Ahsoka Tano, Snips (Showcase)",
        R.drawable.boba_fett_daimyo_showcase to "Boba Fett, Daimyo (Showcase)",
        R.drawable.maul_a_rival_in_darkness_showcase to "Maul, a Rival in Darkness (Showcase)",
        R.drawable.cassian_andor_dedicated_to_the_rebellion_showcase to "Cassian Andor, Dedicated to the Rebellion (Showcase)",
        R.drawable.jyn_erso_resisting_oppression_showcase to "Jyn Erso, Resisting Oppression (Showcase)",
        R.drawable.sabine_wren_galvanized_revolutionary_showcase to "Sabine Wren, Galvanized Revolutionary (Showcase)",
        R.drawable.boba_fett_collecting_the_bounty_showcase to "Boba Fett, Collecting the Bounty (Showcase)",
        R.drawable.ig_88_ruthless_bounty_hunter_showcase to "IG-88, Ruthless Bounty Hunter (Showcase)",
        R.drawable.grand_inquisitor_hunting_the_jedi_showcase to "Grand Inquisitor, Hunting the Jedi (Showcase)",
        R.drawable.leia_organa_alliance_general_showcase to "Leia Organa, Alliance General (Showcase)",
        R.drawable.hera_syndulla_spectre_two_showcase to "Hera Syndulla, Spectre Two (Showcase)",
        R.drawable.grand_moff_tarkin_oversector_governor_showcase to "Grand Moff Tarkin, Oversector Governor (Showcase)",
        R.drawable.chirrut_imwe_one_with_the_force_showcase to "Chirrut Imwe, One with the Force (Showcase)",
        R.drawable.chewbacca_walking_carpet_showcase to "Chewbacca, Walking Carpet (Showcase)",
        R.drawable.iden_versio_inferno_squad_commander_showcase to "Iden Versio, Inferno Squad Commander (Showcase)",
        R.drawable.director_krennic_aspiring_to_authority_showcase to "Director Krennic, Aspiring to Authority (Showcase)",
        R.drawable.the_mandalorian_sworn_to_the_creed_showcase to "The Mandalorian, Sworn to the Creed (Showcase)",
        R.drawable.lando_calrissian_with_impeccable_taste_showcase to "Lando Calrissian, With Impeccable Taste (Showcase)",
        R.drawable.fennec_shand_honoring_the_deal_showcase to "Fennec Shand, Honoring the Deal (Showcase)",
        R.drawable.doctor_aphra_rapacious_archaeologist_showcase to "Doctor Aphra, Rapacious Archaeologist (Showcase)",
        R.drawable.cad_bane_he_who_needs_no_introduction_showcase to "Cad Bane, He Who Needs No Introduction (Showcase)",
        R.drawable.han_solo_worth_the_risk_showcase to "Han Solo, Worth the Risk (Showcase)",
        R.drawable.bo_katan_kryze_princess_in_exile_showcase to "Bo Katan Kryze, Princess in Exile (Showcase)",
        R.drawable.kylo_ren_rash_and_deadly_showcase to "Kylo Ren, Rash and Deadly (Showcase)",
        R.drawable.bossk_hunting_his_prey_showcase to "Bossk, Hunting His Prey (Showcase)",
        R.drawable.hunter_outcast_sergeant_showcase to "Hunter, Outcast Sergeant (Showcase)",
        R.drawable.moff_gideon_formidable_commander_showcase to "Moff Gideon, Formidable Commander (Showcase)",
        R.drawable.jabba_the_hutt_his_high_exaltedness_showcase to "Jabba the Hutt, His High Exaltedness (Showcase)",
        R.drawable.hondo_ohnaka_thats_good_business_showcase to "Hondo Ohnaka, That's Good Business (Showcase)",
        R.drawable.rey_more_than_a_scavenger_showcase to "Rey, More Than a Scavenger (Showcase)",
        R.drawable.finn_this_is_a_rescue_showcase to "Finn, This is a Rescue (Showcase)",
        R.drawable.qira_i_alone_survived_showcase to "Qira, I Alone Survived (Showcase)",
        R.drawable.gar_saxon_viceroy_of_mandalore_showcase to "Gar Saxon, Viceroy of Mandalore (Showcase)",
        R.drawable.quinlan_vos_sticking_the_landing_showcase to "Quinlan Vos, Sticking the Landing (Showcase)",
        R.drawable.chancellor_palpatine_playing_both_sides_showcase to "Chancellor Palpatine, Playing Both Sides (Showcase)",
        R.drawable.jango_fett_concealing_the_conspiracy_showcase to "Jango Fett, Concealing the Conspiracy (Showcase)",
        R.drawable.general_grievous_general_of_the_droid_armies_showcase to "General Grievous, General of the Droid Armies (Showcase)",
        R.drawable.asajj_ventress_unparalleled_adversary_showcase to "Asajj Ventress, Unparalleled Adversary (Showcase)",
        R.drawable.mace_windu_vaapad_form_master_showcase to "Mace Windu, Vaapad Form Master (Showcase)",
        R.drawable.anakin_skywalker_what_it_takes_to_win_showcase to "Anakin Skywalker, What It Takes to Win (Showcase)",
        R.drawable.pre_vizsla_pursuing_the_throne_showcase to "Pre Vizsla, Pursuing the Throne (Showcase)",
        R.drawable.padme_amidala_serving_the_republic_showcase to "Padme Amidala, Serving the Republic (Showcase)",
        R.drawable.wat_tambor_techno_union_foreman_showcase to "Wat Tambor, Techno Union Foreman (Showcase)",
        R.drawable.count_dooku_face_of_the_confederacy_showcase to "Count Dooku, Face of the Confederacy (Showcase)",
        R.drawable.nute_gunray_vindictive_viceroy_showcase to "Nute Gunray, Vindictive Viceroy (Showcase)",
        R.drawable.nala_se_clone_engineer_showcase to "Nala Se, Clone Engineer (Showcase)",
        // Set 4 Showcases
        R.drawable.asajj_ventress_i_work_alone_showcase to "Asajj Ventress, I Work Alone (Showcase)",
        R.drawable.grand_admiral_thrawn_how_unfortunate_showcase to "Grand Admiral Thrawn, ...How Unfortunate (Showcase)",
        R.drawable.lando_calrissian_buying_time_showcase to "Lando Calrissian, Buying Time (Showcase)",
        R.drawable.rose_tico_saving_what_we_love_showcase to "Rose Tico, Saving What We Love (Showcase)",
        R.drawable.admiral_piett_commanding_the_armada_showcase to "Admiral Piett, Commanding the Armada (Showcase)",
        //R.drawable.darth_vader_victor_squadron_leader_showcase to "Darth Vader, Victor Squadron Leader (Showcase)",
        R.drawable.admiral_holdo_were_not_alone_showcase to "Admiral Holdo, We're Not Alone (Showcase)",
        R.drawable.wedge_antilles_leader_of_red_squadron_showcase to "Wedge Antilles, Leader of Red Squadron (Showcase)",
        R.drawable.boba_fett_any_methods_necessary_showcase to "Boba Fett, Any Methods Necessary (Showcase)",
        R.drawable.captain_phasma_chrome_dome_showcase to "Captain Phasma, Chrome Dome (Showcase)",
        R.drawable.luke_skywalker_hero_of_yavin_showcase to "Luke Skywalker, Hero of Yavin (Showcase)",
        R.drawable.poe_dameron_i_can_fly_anything_showcase to "Poe Dameron, I Can Fly Anything (Showcase)",
        R.drawable.admiral_trench_chk_chk_chk_chk_showcase to "Admiral Trench, Chk Chk Chk Chk (Showcase)",
        R.drawable.rio_durant_wisecracking_wheelman_showcase to "Rio Durant, Wisecracking Wheelman (Showcase)",
        R.drawable.admiral_ackbar_its_a_trap_showcase to "Admiral Ackbar, It's a Trap! (Showcase)",
        R.drawable.han_solo_never_tell_me_the_odds_showcase to "Han Solo, Never Tell Me The Odds (Showcase)",
        R.drawable.kazuda_xiono_best_pilot_in_the_galaxy_showcase to "Kazuda Xiono, Best Pilot in the Galaxy (Showcase)",
        R.drawable.major_vonreg_red_baron_showcase to "Major Vonreg, Red Baron (Showcase)",
        // LOF Showcases
        R.drawable.kylo_ren_were_not_done_yet_showcase to "Kylo Ren, We're Not Done Yet (Showcase)",
        R.drawable.luke_skywalker_faithful_friend_prize_wall to "Luke Skywalker, Faithful Friend (Showcase Prize Wall)",
        R.drawable.darth_vader_dark_lord_of_the_sith_prize_wall to "Darth Vader, Dark Lord of the Sith (Showcase Prize Wall)",
        R.drawable.mother_talzin_power_through_magick_showcase to "Mother Talzin, Power Through Magick (Showcase)",
        R.drawable.ahsoka_tano_fighting_for_peace_showcase to "Ahsoka Tano, Fighting for Peace (Showcase)",
        R.drawable.morgan_elsbeth_following_the_call_showcase to "Morgan Elsbeth, Following the Call (Showcase)",
        R.drawable.supreme_leader_snoke_in_the_seat_of_power_showcase to "Supreme Leader Snoke, In the Seat of Power (Showcase)",
        R.drawable.avar_kriss_marshal_of_starlight_showcase to "Avar Kriss, Marshall of Starlight (Showcase)",
        R.drawable.kit_fisto_focused_jedi_master_showcase to "Kit Fisto, Focused Jedi Master (Showcase)",
        R.drawable.rey_nobody_showcase to "Rey, Nobody (Showcase)",
        R.drawable.barris_ofee_we_have_become_villians_showcase to "Barriss Offee, We Have Become Villians (Showcase)",
        R.drawable.cal_kestis_i_cant_keep_hiding_showcase to "Cal Kestis, I Can't Keep Hiding (Showcase)",
        R.drawable.anakin_skywalke_tempted_by_the_dark_side_showcase to "Anakin Skywalker, Tempted by the Dark Side (Showcase)",
        R.drawable.darth_maul_sith_revealed_showcase to "Darth Maul, Sith Revealed (Showcase)",
        R.drawable.obi_wan_kenobi_courage_makes_heroes_showcase to "Obi-Wan Kenobi, Courage Makes Heroes (Showcase)",
        R.drawable.kanan_jarrus_help_us_survive_showcase to "Kanan Jarrus, Help Us Survive (Showcase)",
        R.drawable.darth_revan_scourge_of_the_old_republic_showcase to "Darth Revan, Scourge of the Old Republic (Showcase)",
        R.drawable.qui_gon_jinn_student_of_the_living_force_showcase to "Qui Gon Jinn, Student of the Living Force (Showcase)",
        R.drawable.third_sister_seething_with_ambition_showcase to "Third Sister, Seething with Ambition (Showcase)",

        // Set 6
        R.drawable.chancellor_palpatine_how_liberty_dies to "Chancellor Palptatine, How Liberty Dies",
        R.drawable.jabba_the_hutt_wonderful_human_being to "Jabba the Hutt, Wonderful Human Being",
        R.drawable.lama_su_we_modified_their_genetics to "Lama Su, We Modified Their Genetics",
        R.drawable.leia_organa_of_a_secret_bloodline to "Leia Organa, Of a Secret Bloodline",
        R.drawable.satine_kryze_standing_on_principles to "Satine Kryze, Standing on Principles",
        R.drawable.colonel_yularen_this_is_why_we_plan to "Colonel Yularen, This is Why We Plan",
        R.drawable.dryden_vos_i_never_ask_twice to "Dryden Vos, I Never Ask Twice",
        R.drawable.bail_organa_doing_everything_he_can to "Bail Organa, Doing Everything He Can",
        R.drawable.mon_mothma_forming_a_coalition to "Mon Mothma, Forming a Coalition",
        R.drawable.dedra_meero_not_wasting_time to "Dedra Meero, Not Wasting Time",
        R.drawable.governor_pryce_tyrant_of_lothal to "Governor Pryce, Tyrant of Lothal",
        R.drawable.cassian_andor_climb to "Cassian Andor, Climb!",
        R.drawable.luthen_rael_dont_you_want_to_fight_for_real to "Luthen Rael, Don't You Want to Fight For Real?",
        R.drawable.sly_moore_cipher_in_the_dark to "Sly Moore, Cipher in the Dark",
        R.drawable.c3po_human_cyborg_relations to "C-3PO, Human-Cyborg Relations",
        R.drawable.padme_amidala_what_do_you_have_to_hide to "Padme Amidala, What Do You Have to Hide?",
        R.drawable.sabe_queens_shadow to "Sabe, Queen's Shadow",
        R.drawable.dj_need_a_lift to "DJ, Need a Lift?",


        // Misc
        R.drawable.vader_prize_wall_banner to "Vader Prize Wall Banner"
        )

    var searchText by remember { mutableStateOf("") }

    // Filter images based on search text
    val filteredImages = remember(searchText) {
        if (searchText.isBlank()) {
            images
        } else {
            images.filter { (_, label) ->
                label.contains(searchText, ignoreCase = true)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Choose Leader") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search TextField
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search Leaders") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Leaders Grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredImages) { (imageRes, label) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onImageSelected(imageRes) }
                                    .background(
                                        if (currentImageId == imageRes)
                                            Color.Gray.copy(alpha = 0.5f)
                                        else
                                            Color.Transparent
                                    )
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = label,
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(label, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PlayerCountDialog(
    onDismissRequest: () -> Unit,
    onPlayerCountSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Number of Players") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button disabled until implemented
                Button(
                    onClick = { onPlayerCountSelected(2) },
                    modifier = Modifier.fillMaxWidth(),
                    //enabled = false  // Disable the button
                ) {
                    Text("2 Players")
                }
                Button(
                    onClick = { onPlayerCountSelected(3) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("3 Players")
                }
                Button(
                    onClick = { onPlayerCountSelected(4) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("4 Players")
                }
                Button(
                    onClick = { onPlayerCountSelected(5) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("5 Players")
                }
                Button(
                    onClick = { onPlayerCountSelected(6) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("6 Players")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun InitiativeButton(
    playerId: Int,
    hasInitiative: Boolean,
    onInitiativeClaimed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onInitiativeClaimed() }
            .background(
                if (hasInitiative) Color.Yellow.copy(alpha = 0.7f)
                else Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.initiative_token),
            contentDescription = "Initiative Token",
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun CardBanner(
    modifier: Modifier = Modifier,
    baseId: Int
) {
    val baseName = Bases.findById(baseId)?.baseName ?: "Select Base"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = baseName,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            style = MaterialTheme.typography.bodyLarge.copy(
                shadow = Shadow(color = Color.Black, blurRadius = 8f)
            )
        )
    }
}

fun getRandomDeathMessage(): String {
    val deathMessages = listOf(
        "Defeated.",
        "K.O.'d!",
        "Game Over",
        "Base Destroyed.",
        "Mission Failed.",
        "Oof.",
        "RIP.",
        "Should've watched for wrist rockets..",
        ":("
    )
    return deathMessages.random()
}
