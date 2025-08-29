// VictoryDialog.kt
package dev.bearcat.basetap

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun VictoryDialog(
    player1Name: String,
    player2Name: String,
    player1Life: Int,
    player2Life: Int,
    player1BaseId: Int,
    player2BaseId: Int,
    player1LeaderImage: Int,
    player2LeaderImage: Int,
    onWinnerSelected: (winnerIndex: Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Game Over!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A base has been defeated.\nWho won this game?",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Player 1 Button
                PlayerWinButton(
                    playerName = player1Name,
                    playerLife = player1Life,
                    baseId = player1BaseId,
                    leaderImage = player1LeaderImage,
                    onClick = { onWinnerSelected(0) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Player 2 Button
                PlayerWinButton(
                    playerName = player2Name,
                    playerLife = player2Life,
                    baseId = player2BaseId,
                    leaderImage = player2LeaderImage,
                    onClick = { onWinnerSelected(1) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Cancel/Continue Playing Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Continue Playing")
                }
            }
        }
    }
}

@Composable
private fun PlayerWinButton(
    playerName: String,
    playerLife: Int,
    baseId: Int,
    leaderImage: Int,
    onClick: () -> Unit
) {
    // Get the leader name from the image resource
    val leaderName = getLeaderNameFromImage(leaderImage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = Color.Green,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Green.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = playerName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Life: $playerLife",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Base ID: $baseId",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = leaderName,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper function to get leader name from image resource
private fun getLeaderNameFromImage(imageResource: Int): String {
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
        // Misc
        R.drawable.vader_prize_wall_banner to "Vader Prize Wall Banner"
    )

    return images.find { it.first == imageResource }?.second ?: "Unknown Leader"
}