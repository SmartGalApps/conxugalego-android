package es.sonxurxo.android.conxugalego.util;

import android.content.Context;
import es.sonxurxo.android.conxugalego.R;
import es.sonxurxo.android.conxugalego.model.VerbalTime;

public class Cheater {

	public static void cheat(Context context, String infinitive,
			VerbalTime[] verbalTimes) {
		if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggFuck))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[0] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggIFuck);
					verbalTime.getTimes()[1] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggYouDontFuck);
				}
				if (verbalTime.getName().equals("FI")) {
					verbalTime.getTimes()[0] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggIWillFuck);
					verbalTime.getTimes()[1] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggYouWontFuck);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggBuild))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[5] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggTheyBuild);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggPoisson))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("EI")) {
					verbalTime.getTimes()[5] = VerbalTime.CHEAT_MARK
							+ context
									.getString(R.string.easternEggTheyPoissoned);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggLive))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("IA")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggLetsLive);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggRain))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggItRains);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggSnow))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggItSnows);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggBe))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[1] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggYouAre);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggDrink))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("IA")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggLetsDrink);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggStare))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("FN")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggStared);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggSee))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("EI")) {
					verbalTime.getTimes()[0] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggISaw);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggSmell))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggHeSmells);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggLookLike))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[1] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggYouLookLike);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggDance))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[3] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggWeDance);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggStone))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("PI")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggHeStones);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggSail))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("IA")) {
					verbalTime.getTimes()[2] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggLetsSail);
				}
			}
		} else if (infinitive.equalsIgnoreCase(context
				.getString(R.string.easternEggTravel))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("IA")) {
					verbalTime.getTimes()[0] = VerbalTime.CHEAT_MARK
							+ context.getString(R.string.easternEggYouTravel);
				}
			}
		}

		/*
		 * Bug fixes
		 */
		if (infinitive.equalsIgnoreCase(context.getString(R.string.bugFixesGo))) {
			for (VerbalTime verbalTime : verbalTimes) {
				if (verbalTime.getName().equals("IN")) {
					verbalTime.getTimes()[0] = context
							.getString(R.string.bugFixesGoFix);
				}
			}
		}
	}
}
