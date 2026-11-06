package org.notima.generic.businessobjects.accounting.rule.se;

import static org.notima.generic.businessobjects.accounting.statement.StatementNode.line;
import static org.notima.generic.businessobjects.accounting.statement.StatementNode.result;
import static org.notima.generic.businessobjects.accounting.statement.StatementNode.section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.notima.generic.businessobjects.accounting.statement.AccountPlacement;
import org.notima.generic.businessobjects.accounting.statement.StatementMapping;
import org.notima.generic.businessobjects.accounting.statement.StatementTemplate;

/**
 * The income statement (kostnadsslagsindelad) and balance sheet of a K2 annual report
 * (BFNAR 2016:10, ÅRL bilaga 2 and 3), and the BAS mapping of accounts to their lines.
 *
 * <p>Lines without amounts in either year aren't shown, so the templates have every line a
 * small company may need. The rounding difference of the result goes on Nettoomsättning.
 */
public final class SeK2Statements {

	// Income statement
	public static final String RR = "SE-K2-RR";
	public static final String RORELSEINTAKTER = "RORELSEINTAKTER";
	public static final String NETTOOMSATTNING = "NETTOOMSATTNING";
	public static final String LAGERFORANDRING = "LAGERFORANDRING";
	public static final String AKTIVERAT_ARBETE = "AKTIVERAT_ARBETE";
	public static final String OVRIGA_RORELSEINTAKTER = "OVRIGA_RORELSEINTAKTER";
	public static final String RORELSEKOSTNADER = "RORELSEKOSTNADER";
	public static final String RAVAROR = "RAVAROR";
	public static final String HANDELSVAROR = "HANDELSVAROR";
	public static final String OVRIGA_EXTERNA_KOSTNADER = "OVRIGA_EXTERNA_KOSTNADER";
	public static final String PERSONALKOSTNADER = "PERSONALKOSTNADER";
	public static final String AVSKRIVNINGAR = "AVSKRIVNINGAR";
	public static final String OVRIGA_RORELSEKOSTNADER = "OVRIGA_RORELSEKOSTNADER";
	public static final String RORELSERESULTAT = "RORELSERESULTAT";
	public static final String FINANSIELLA_POSTER = "FINANSIELLA_POSTER";
	public static final String RESULTAT_KONCERNFORETAG = "RESULTAT_KONCERNFORETAG";
	public static final String RESULTAT_INTRESSEFORETAG = "RESULTAT_INTRESSEFORETAG";
	public static final String RESULTAT_OVRIGA_FINANSIELLA = "RESULTAT_OVRIGA_FINANSIELLA";
	public static final String OVRIGA_RANTEINTAKTER = "OVRIGA_RANTEINTAKTER";
	public static final String RANTEKOSTNADER = "RANTEKOSTNADER";
	public static final String RESULTAT_EFTER_FINANSIELLA = "RESULTAT_EFTER_FINANSIELLA";
	public static final String BOKSLUTSDISPOSITIONER = "BOKSLUTSDISPOSITIONER";
	public static final String ERHALLNA_KONCERNBIDRAG = "ERHALLNA_KONCERNBIDRAG";
	public static final String LAMNADE_KONCERNBIDRAG = "LAMNADE_KONCERNBIDRAG";
	public static final String FORANDRING_PERIODISERINGSFONDER = "FORANDRING_PERIODISERINGSFONDER";
	public static final String FORANDRING_OVERAVSKRIVNINGAR = "FORANDRING_OVERAVSKRIVNINGAR";
	public static final String OVRIGA_BOKSLUTSDISPOSITIONER = "OVRIGA_BOKSLUTSDISPOSITIONER";
	public static final String RESULTAT_FORE_SKATT = "RESULTAT_FORE_SKATT";
	public static final String SKATTER = "SKATTER";
	public static final String SKATT_PA_ARETS_RESULTAT = "SKATT_PA_ARETS_RESULTAT";
	public static final String OVRIGA_SKATTER = "OVRIGA_SKATTER";
	public static final String ARETS_RESULTAT = "ARETS_RESULTAT";

	// Balance sheet
	public static final String BR = "SE-K2-BR";
	public static final String TILLGANGAR = "TILLGANGAR";
	public static final String ANLAGGNINGSTILLGANGAR = "ANLAGGNINGSTILLGANGAR";
	public static final String IMMATERIELLA = "IMMATERIELLA";
	public static final String BALANSERADE_UTGIFTER = "BALANSERADE_UTGIFTER";
	public static final String KONCESSIONER = "KONCESSIONER";
	public static final String HYRESRATTER = "HYRESRATTER";
	public static final String GOODWILL = "GOODWILL";
	public static final String FORSKOTT_IMMATERIELLA = "FORSKOTT_IMMATERIELLA";
	public static final String MATERIELLA = "MATERIELLA";
	public static final String BYGGNADER_OCH_MARK = "BYGGNADER_OCH_MARK";
	public static final String FORBATTRINGSUTGIFTER = "FORBATTRINGSUTGIFTER";
	public static final String MASKINER = "MASKINER";
	public static final String INVENTARIER = "INVENTARIER";
	public static final String PAGAENDE_NYANLAGGNINGAR = "PAGAENDE_NYANLAGGNINGAR";
	public static final String OVRIGA_MATERIELLA = "OVRIGA_MATERIELLA";
	public static final String FINANSIELLA_ANLAGGNINGSTILLGANGAR = "FINANSIELLA_ANLAGGNINGSTILLGANGAR";
	public static final String ANDELAR_KONCERNFORETAG = "ANDELAR_KONCERNFORETAG";
	public static final String FORDRINGAR_KONCERNFORETAG_LANG = "FORDRINGAR_KONCERNFORETAG_LANG";
	public static final String ANDELAR_INTRESSEFORETAG = "ANDELAR_INTRESSEFORETAG";
	public static final String FORDRINGAR_INTRESSEFORETAG_LANG = "FORDRINGAR_INTRESSEFORETAG_LANG";
	public static final String ANDRA_LANGFRISTIGA_VARDEPAPPER = "ANDRA_LANGFRISTIGA_VARDEPAPPER";
	public static final String LAN_DELAGARE = "LAN_DELAGARE";
	public static final String ANDRA_LANGFRISTIGA_FORDRINGAR = "ANDRA_LANGFRISTIGA_FORDRINGAR";
	public static final String OMSATTNINGSTILLGANGAR = "OMSATTNINGSTILLGANGAR";
	public static final String VARULAGER = "VARULAGER";
	public static final String LAGER_RAVAROR = "LAGER_RAVAROR";
	public static final String LAGER_VAROR_UNDER_TILLVERKNING = "LAGER_VAROR_UNDER_TILLVERKNING";
	public static final String LAGER_FARDIGA_VAROR = "LAGER_FARDIGA_VAROR";
	public static final String PAGAENDE_ARBETE_TILLGANG = "PAGAENDE_ARBETE_TILLGANG";
	public static final String FORSKOTT_TILL_LEVERANTORER = "FORSKOTT_TILL_LEVERANTORER";
	public static final String KORTFRISTIGA_FORDRINGAR = "KORTFRISTIGA_FORDRINGAR";
	public static final String KUNDFORDRINGAR = "KUNDFORDRINGAR";
	public static final String FORDRINGAR_KONCERNFORETAG_KORT = "FORDRINGAR_KONCERNFORETAG_KORT";
	public static final String FORDRINGAR_INTRESSEFORETAG_KORT = "FORDRINGAR_INTRESSEFORETAG_KORT";
	public static final String OVRIGA_FORDRINGAR = "OVRIGA_FORDRINGAR";
	public static final String UPPARBETAD_EJ_FAKTURERAD = "UPPARBETAD_EJ_FAKTURERAD";
	public static final String TECKNAT_EJ_INBETALT = "TECKNAT_EJ_INBETALT";
	public static final String FORUTBETALDA_KOSTNADER = "FORUTBETALDA_KOSTNADER";
	public static final String KORTFRISTIGA_PLACERINGAR = "KORTFRISTIGA_PLACERINGAR";
	public static final String ANDELAR_KONCERNFORETAG_KORT = "ANDELAR_KONCERNFORETAG_KORT";
	public static final String OVRIGA_KORTFRISTIGA_PLACERINGAR = "OVRIGA_KORTFRISTIGA_PLACERINGAR";
	public static final String KASSA_OCH_BANK_AVSNITT = "KASSA_OCH_BANK_AVSNITT";
	public static final String KASSA_OCH_BANK = "KASSA_OCH_BANK";
	public static final String EGET_KAPITAL_OCH_SKULDER = "EGET_KAPITAL_OCH_SKULDER";
	public static final String EGET_KAPITAL = "EGET_KAPITAL";
	public static final String BUNDET_EGET_KAPITAL = "BUNDET_EGET_KAPITAL";
	public static final String AKTIEKAPITAL = "AKTIEKAPITAL";
	public static final String UPPSKRIVNINGSFOND = "UPPSKRIVNINGSFOND";
	public static final String RESERVFOND = "RESERVFOND";
	public static final String FOND_UTVECKLINGSUTGIFTER = "FOND_UTVECKLINGSUTGIFTER";
	public static final String FRITT_EGET_KAPITAL = "FRITT_EGET_KAPITAL";
	public static final String OVERKURSFOND = "OVERKURSFOND";
	public static final String BALANSERAT_RESULTAT = "BALANSERAT_RESULTAT";
	public static final String ARETS_RESULTAT_EK = "ARETS_RESULTAT_EK";
	public static final String OBESKATTADE_RESERVER = "OBESKATTADE_RESERVER";
	public static final String PERIODISERINGSFONDER = "PERIODISERINGSFONDER";
	public static final String OVERAVSKRIVNINGAR = "OVERAVSKRIVNINGAR";
	public static final String OVRIGA_OBESKATTADE_RESERVER = "OVRIGA_OBESKATTADE_RESERVER";
	public static final String AVSATTNINGAR = "AVSATTNINGAR";
	public static final String AVSATTNINGAR_PENSIONER = "AVSATTNINGAR_PENSIONER";
	public static final String OVRIGA_AVSATTNINGAR = "OVRIGA_AVSATTNINGAR";
	public static final String LANGFRISTIGA_SKULDER = "LANGFRISTIGA_SKULDER";
	public static final String OBLIGATIONSLAN = "OBLIGATIONSLAN";
	public static final String CHECKRAKNINGSKREDIT_LANG = "CHECKRAKNINGSKREDIT_LANG";
	public static final String SKULDER_KREDITINSTITUT_LANG = "SKULDER_KREDITINSTITUT_LANG";
	public static final String SKULDER_KONCERNFORETAG_LANG = "SKULDER_KONCERNFORETAG_LANG";
	public static final String SKULDER_INTRESSEFORETAG_LANG = "SKULDER_INTRESSEFORETAG_LANG";
	public static final String OVRIGA_SKULDER_LANG = "OVRIGA_SKULDER_LANG";
	public static final String KORTFRISTIGA_SKULDER = "KORTFRISTIGA_SKULDER";
	public static final String CHECKRAKNINGSKREDIT_KORT = "CHECKRAKNINGSKREDIT_KORT";
	public static final String SKULDER_KREDITINSTITUT_KORT = "SKULDER_KREDITINSTITUT_KORT";
	public static final String FORSKOTT_FRAN_KUNDER = "FORSKOTT_FRAN_KUNDER";
	public static final String PAGAENDE_ARBETE_SKULD = "PAGAENDE_ARBETE_SKULD";
	public static final String FAKTURERAD_EJ_UPPARBETAD = "FAKTURERAD_EJ_UPPARBETAD";
	public static final String LEVERANTORSSKULDER = "LEVERANTORSSKULDER";
	public static final String VAXELSKULDER = "VAXELSKULDER";
	public static final String SKULDER_KONCERNFORETAG_KORT = "SKULDER_KONCERNFORETAG_KORT";
	public static final String SKULDER_INTRESSEFORETAG_KORT = "SKULDER_INTRESSEFORETAG_KORT";
	public static final String SKATTESKULDER = "SKATTESKULDER";
	public static final String OVRIGA_SKULDER = "OVRIGA_SKULDER";
	public static final String UPPLUPNA_KOSTNADER = "UPPLUPNA_KOSTNADER";

	private static final StatementTemplate INCOME_STATEMENT = new StatementTemplate(RR, "Resultaträkning",
			StatementTemplate.Type.INCOME_STATEMENT, true,
			section(RORELSEINTAKTER, "Rörelseintäkter, lagerförändringar m.m.", "Summa rörelseintäkter, lagerförändringar m.m.",
					line(NETTOOMSATTNING, "Nettoomsättning"),
					line(LAGERFORANDRING, "Förändring av lager av produkter i arbete, färdiga varor och pågående arbete för annans räkning"),
					line(AKTIVERAT_ARBETE, "Aktiverat arbete för egen räkning"),
					line(OVRIGA_RORELSEINTAKTER, "Övriga rörelseintäkter")),
			section(RORELSEKOSTNADER, "Rörelsekostnader", "Summa rörelsekostnader",
					line(RAVAROR, "Råvaror och förnödenheter"),
					line(HANDELSVAROR, "Handelsvaror"),
					line(OVRIGA_EXTERNA_KOSTNADER, "Övriga externa kostnader"),
					line(PERSONALKOSTNADER, "Personalkostnader"),
					line(AVSKRIVNINGAR, "Av- och nedskrivningar av materiella och immateriella anläggningstillgångar"),
					line(OVRIGA_RORELSEKOSTNADER, "Övriga rörelsekostnader")),
			result(RORELSERESULTAT, "Rörelseresultat"),
			section(FINANSIELLA_POSTER, "Finansiella poster", "Summa finansiella poster",
					line(RESULTAT_KONCERNFORETAG, "Resultat från andelar i koncernföretag"),
					line(RESULTAT_INTRESSEFORETAG, "Resultat från andelar i intresseföretag och gemensamt styrda företag"),
					line(RESULTAT_OVRIGA_FINANSIELLA, "Resultat från övriga finansiella anläggningstillgångar"),
					line(OVRIGA_RANTEINTAKTER, "Övriga ränteintäkter och liknande resultatposter"),
					line(RANTEKOSTNADER, "Räntekostnader och liknande resultatposter")),
			result(RESULTAT_EFTER_FINANSIELLA, "Resultat efter finansiella poster"),
			section(BOKSLUTSDISPOSITIONER, "Bokslutsdispositioner", "Summa bokslutsdispositioner",
					line(ERHALLNA_KONCERNBIDRAG, "Erhållna koncernbidrag"),
					line(LAMNADE_KONCERNBIDRAG, "Lämnade koncernbidrag"),
					line(FORANDRING_PERIODISERINGSFONDER, "Förändring av periodiseringsfonder"),
					line(FORANDRING_OVERAVSKRIVNINGAR, "Förändring av överavskrivningar"),
					line(OVRIGA_BOKSLUTSDISPOSITIONER, "Övriga bokslutsdispositioner")),
			result(RESULTAT_FORE_SKATT, "Resultat före skatt"),
			section(SKATTER, "Skatter", null,
					line(SKATT_PA_ARETS_RESULTAT, "Skatt på årets resultat"),
					line(OVRIGA_SKATTER, "Övriga skatter")),
			result(ARETS_RESULTAT, "Årets resultat"))
			.roundingLine(NETTOOMSATTNING);

	private static final StatementTemplate BALANCE_SHEET = new StatementTemplate(BR, "Balansräkning",
			StatementTemplate.Type.BALANCE_SHEET, false,
			section(TILLGANGAR, "TILLGÅNGAR", "SUMMA TILLGÅNGAR",
					section(ANLAGGNINGSTILLGANGAR, "Anläggningstillgångar", "Summa anläggningstillgångar",
							section(IMMATERIELLA, "Immateriella anläggningstillgångar", "Summa immateriella anläggningstillgångar",
									line(BALANSERADE_UTGIFTER, "Balanserade utgifter för utvecklingsarbeten och liknande arbeten"),
									line(KONCESSIONER, "Koncessioner, patent, licenser, varumärken samt liknande rättigheter"),
									line(HYRESRATTER, "Hyresrätter och liknande rättigheter"),
									line(GOODWILL, "Goodwill"),
									line(FORSKOTT_IMMATERIELLA, "Förskott avseende immateriella anläggningstillgångar")),
							section(MATERIELLA, "Materiella anläggningstillgångar", "Summa materiella anläggningstillgångar",
									line(BYGGNADER_OCH_MARK, "Byggnader och mark"),
									line(FORBATTRINGSUTGIFTER, "Förbättringsutgifter på annans fastighet"),
									line(MASKINER, "Maskiner och andra tekniska anläggningar"),
									line(INVENTARIER, "Inventarier, verktyg och installationer"),
									line(PAGAENDE_NYANLAGGNINGAR, "Pågående nyanläggningar och förskott avseende materiella anläggningstillgångar"),
									line(OVRIGA_MATERIELLA, "Övriga materiella anläggningstillgångar")),
							section(FINANSIELLA_ANLAGGNINGSTILLGANGAR, "Finansiella anläggningstillgångar", "Summa finansiella anläggningstillgångar",
									line(ANDELAR_KONCERNFORETAG, "Andelar i koncernföretag"),
									line(FORDRINGAR_KONCERNFORETAG_LANG, "Fordringar hos koncernföretag"),
									line(ANDELAR_INTRESSEFORETAG, "Andelar i intresseföretag och gemensamt styrda företag"),
									line(FORDRINGAR_INTRESSEFORETAG_LANG, "Fordringar hos intresseföretag och gemensamt styrda företag"),
									line(ANDRA_LANGFRISTIGA_VARDEPAPPER, "Andra långfristiga värdepappersinnehav"),
									line(LAN_DELAGARE, "Lån till delägare eller närstående"),
									line(ANDRA_LANGFRISTIGA_FORDRINGAR, "Andra långfristiga fordringar"))),
					section(OMSATTNINGSTILLGANGAR, "Omsättningstillgångar", "Summa omsättningstillgångar",
							section(VARULAGER, "Varulager m.m.", "Summa varulager m.m.",
									line(LAGER_RAVAROR, "Råvaror och förnödenheter"),
									line(LAGER_VAROR_UNDER_TILLVERKNING, "Varor under tillverkning"),
									line(LAGER_FARDIGA_VAROR, "Färdiga varor och handelsvaror"),
									line(PAGAENDE_ARBETE_TILLGANG, "Pågående arbete för annans räkning"),
									line(FORSKOTT_TILL_LEVERANTORER, "Förskott till leverantörer")),
							section(KORTFRISTIGA_FORDRINGAR, "Kortfristiga fordringar", "Summa kortfristiga fordringar",
									line(KUNDFORDRINGAR, "Kundfordringar"),
									line(FORDRINGAR_KONCERNFORETAG_KORT, "Fordringar hos koncernföretag"),
									line(FORDRINGAR_INTRESSEFORETAG_KORT, "Fordringar hos intresseföretag och gemensamt styrda företag"),
									line(OVRIGA_FORDRINGAR, "Övriga fordringar"),
									line(UPPARBETAD_EJ_FAKTURERAD, "Upparbetad men ej fakturerad intäkt"),
									line(TECKNAT_EJ_INBETALT, "Tecknat men ej inbetalt kapital"),
									line(FORUTBETALDA_KOSTNADER, "Förutbetalda kostnader och upplupna intäkter")),
							section(KORTFRISTIGA_PLACERINGAR, "Kortfristiga placeringar", "Summa kortfristiga placeringar",
									line(ANDELAR_KONCERNFORETAG_KORT, "Andelar i koncernföretag"),
									line(OVRIGA_KORTFRISTIGA_PLACERINGAR, "Övriga kortfristiga placeringar")),
							section(KASSA_OCH_BANK_AVSNITT, "Kassa och bank", "Summa kassa och bank",
									line(KASSA_OCH_BANK, "Kassa och bank")))).creditNormal(false),
			section(EGET_KAPITAL_OCH_SKULDER, "EGET KAPITAL OCH SKULDER", "SUMMA EGET KAPITAL OCH SKULDER",
					section(EGET_KAPITAL, "Eget kapital", "Summa eget kapital",
							section(BUNDET_EGET_KAPITAL, "Bundet eget kapital", "Summa bundet eget kapital",
									line(AKTIEKAPITAL, "Aktiekapital"),
									line(UPPSKRIVNINGSFOND, "Uppskrivningsfond"),
									line(RESERVFOND, "Reservfond"),
									line(FOND_UTVECKLINGSUTGIFTER, "Fond för utvecklingsutgifter")),
							section(FRITT_EGET_KAPITAL, "Fritt eget kapital", "Summa fritt eget kapital",
									line(OVERKURSFOND, "Överkursfond"),
									line(BALANSERAT_RESULTAT, "Balanserat resultat"),
									line(ARETS_RESULTAT_EK, "Årets resultat").receivesUnclosedResult())),
					section(OBESKATTADE_RESERVER, "Obeskattade reserver", "Summa obeskattade reserver",
							line(PERIODISERINGSFONDER, "Periodiseringsfonder"),
							line(OVERAVSKRIVNINGAR, "Ackumulerade överavskrivningar"),
							line(OVRIGA_OBESKATTADE_RESERVER, "Övriga obeskattade reserver")),
					section(AVSATTNINGAR, "Avsättningar", "Summa avsättningar",
							line(AVSATTNINGAR_PENSIONER, "Avsättningar för pensioner och liknande förpliktelser enligt lagen (1967:531) om tryggande av pensionsutfästelse m.m."),
							line(OVRIGA_AVSATTNINGAR, "Övriga avsättningar")),
					section(LANGFRISTIGA_SKULDER, "Långfristiga skulder", "Summa långfristiga skulder",
							line(OBLIGATIONSLAN, "Obligationslån"),
							line(CHECKRAKNINGSKREDIT_LANG, "Checkräkningskredit"),
							line(SKULDER_KREDITINSTITUT_LANG, "Övriga skulder till kreditinstitut"),
							line(SKULDER_KONCERNFORETAG_LANG, "Skulder till koncernföretag"),
							line(SKULDER_INTRESSEFORETAG_LANG, "Skulder till intresseföretag och gemensamt styrda företag"),
							line(OVRIGA_SKULDER_LANG, "Övriga skulder")),
					section(KORTFRISTIGA_SKULDER, "Kortfristiga skulder", "Summa kortfristiga skulder",
							line(CHECKRAKNINGSKREDIT_KORT, "Checkräkningskredit"),
							line(SKULDER_KREDITINSTITUT_KORT, "Övriga skulder till kreditinstitut"),
							line(FORSKOTT_FRAN_KUNDER, "Förskott från kunder"),
							line(PAGAENDE_ARBETE_SKULD, "Pågående arbete för annans räkning"),
							line(FAKTURERAD_EJ_UPPARBETAD, "Fakturerad men ej upparbetad intäkt"),
							line(LEVERANTORSSKULDER, "Leverantörsskulder"),
							line(VAXELSKULDER, "Växelskulder"),
							line(SKULDER_KONCERNFORETAG_KORT, "Skulder till koncernföretag"),
							line(SKULDER_INTRESSEFORETAG_KORT, "Skulder till intresseföretag och gemensamt styrda företag"),
							line(SKATTESKULDER, "Skatteskulder"),
							line(OVRIGA_SKULDER, "Övriga skulder"),
							line(UPPLUPNA_KOSTNADER, "Upplupna kostnader och förutbetalda intäkter"))).creditNormal(true))
			.sides(TILLGANGAR, EGET_KAPITAL_OCH_SKULDER);

	/** Income tax receivables (1640-1649) and liabilities (25xx) are netted. */
	static final String INCOME_TAX_GROUP = "INCOME_TAX";

	private static final StatementMapping BAS_MAPPING = new BasMapping();

	private SeK2Statements() {}

	public static StatementTemplate incomeStatement() {
		return INCOME_STATEMENT;
	}

	public static StatementTemplate balanceSheet() {
		return BALANCE_SHEET;
	}

	/** @return	The income statement and the balance sheet. */
	public static List<StatementTemplate> templates() {
		return Arrays.asList(INCOME_STATEMENT, BALANCE_SHEET);
	}

	/** @return	The mapping of BAS accounts to the lines of the K2 statements. */
	public static StatementMapping basMapping() {
		return BAS_MAPPING;
	}

	/**
	 * Places BAS accounts by account number ranges. The first range that contains the account
	 * wins, so more specific ranges come first.
	 */
	private static final class BasMapping implements StatementMapping {

		private final List<int[]> ranges = new ArrayList<int[]>();
		private final List<AccountPlacement> placements = new ArrayList<AccountPlacement>();

		BasMapping() {
			// Balance sheet: assets
			on(1010, 1019, BALANSERADE_UTGIFTER);
			on(1020, 1059, KONCESSIONER);
			on(1060, 1069, HYRESRATTER);
			on(1070, 1079, GOODWILL);
			on(1000, 1099, FORSKOTT_IMMATERIELLA);
			on(1120, 1129, FORBATTRINGSUTGIFTER);
			on(1100, 1199, BYGGNADER_OCH_MARK);
			on(1200, 1219, MASKINER);
			on(1220, 1279, INVENTARIER);
			on(1280, 1289, PAGAENDE_NYANLAGGNINGAR);
			on(1290, 1299, OVRIGA_MATERIELLA);
			on(1310, 1319, ANDELAR_KONCERNFORETAG);
			on(1320, 1329, FORDRINGAR_KONCERNFORETAG_LANG);
			on(1330, 1339, ANDELAR_INTRESSEFORETAG);
			on(1340, 1349, FORDRINGAR_INTRESSEFORETAG_LANG);
			on(1350, 1359, ANDRA_LANGFRISTIGA_VARDEPAPPER);
			on(1360, 1369, LAN_DELAGARE);
			on(1300, 1399, ANDRA_LANGFRISTIGA_FORDRINGAR);
			on(1400, 1439, LAGER_RAVAROR);
			on(1440, 1449, LAGER_VAROR_UNDER_TILLVERKNING);
			on(1450, 1469, LAGER_FARDIGA_VAROR);
			on(1470, 1479, PAGAENDE_ARBETE_TILLGANG);
			on(1480, 1499, FORSKOTT_TILL_LEVERANTORER);
			on(1560, 1569, FORDRINGAR_KONCERNFORETAG_KORT);
			on(1570, 1579, FORDRINGAR_INTRESSEFORETAG_KORT);
			on(1500, 1599, KUNDFORDRINGAR);
			on(1620, 1629, UPPARBETAD_EJ_FAKTURERAD);
			add(1640, 1649, AccountPlacement.bySign(INCOME_TAX_GROUP, OVRIGA_FORDRINGAR, SKATTESKULDER));
			on(1660, 1669, FORDRINGAR_KONCERNFORETAG_KORT);
			on(1670, 1679, FORDRINGAR_INTRESSEFORETAG_KORT);
			on(1690, 1699, TECKNAT_EJ_INBETALT);
			on(1600, 1699, OVRIGA_FORDRINGAR);
			on(1700, 1799, FORUTBETALDA_KOSTNADER);
			on(1860, 1869, ANDELAR_KONCERNFORETAG_KORT);
			on(1800, 1899, OVRIGA_KORTFRISTIGA_PLACERINGAR);
			// 1900-1999 is placed per account by sign, see place()

			// Balance sheet: equity and liabilities
			on(2080, 2084, AKTIEKAPITAL);
			on(2085, 2085, UPPSKRIVNINGSFOND);
			on(2086, 2088, RESERVFOND);
			on(2089, 2089, FOND_UTVECKLINGSUTGIFTER);
			on(2097, 2097, OVERKURSFOND);
			on(2099, 2099, ARETS_RESULTAT_EK);
			on(2090, 2098, BALANSERAT_RESULTAT);
			on(2110, 2149, PERIODISERINGSFONDER);
			on(2150, 2159, OVERAVSKRIVNINGAR);
			on(2160, 2199, OVRIGA_OBESKATTADE_RESERVER);
			on(2210, 2239, AVSATTNINGAR_PENSIONER);
			on(2200, 2299, OVRIGA_AVSATTNINGAR);
			on(2300, 2329, OBLIGATIONSLAN);
			on(2330, 2339, CHECKRAKNINGSKREDIT_LANG);
			on(2340, 2359, SKULDER_KREDITINSTITUT_LANG);
			on(2360, 2369, SKULDER_KONCERNFORETAG_LANG);
			on(2370, 2379, SKULDER_INTRESSEFORETAG_LANG);
			on(2380, 2399, OVRIGA_SKULDER_LANG);
			on(2400, 2419, SKULDER_KREDITINSTITUT_KORT);
			on(2420, 2429, FORSKOTT_FRAN_KUNDER);
			on(2430, 2439, PAGAENDE_ARBETE_SKULD);
			on(2440, 2449, LEVERANTORSSKULDER);
			on(2450, 2459, FAKTURERAD_EJ_UPPARBETAD);
			on(2460, 2469, SKULDER_KONCERNFORETAG_KORT);
			on(2470, 2479, SKULDER_INTRESSEFORETAG_KORT);
			on(2480, 2489, CHECKRAKNINGSKREDIT_KORT);
			on(2492, 2492, VAXELSKULDER);
			on(2490, 2499, OVRIGA_SKULDER);
			add(2500, 2599, AccountPlacement.bySign(INCOME_TAX_GROUP, OVRIGA_FORDRINGAR, SKATTESKULDER));
			on(2860, 2869, SKULDER_KONCERNFORETAG_KORT);
			on(2870, 2879, SKULDER_INTRESSEFORETAG_KORT);
			on(2600, 2899, OVRIGA_SKULDER);
			on(2900, 2999, UPPLUPNA_KOSTNADER);

			// Income statement
			on(3000, 3799, NETTOOMSATTNING);
			on(3800, 3899, AKTIVERAT_ARBETE);
			on(3900, 3999, OVRIGA_RORELSEINTAKTER);
			on(4940, 4979, LAGERFORANDRING);
			on(4000, 4999, RAVAROR);
			on(5000, 6999, OVRIGA_EXTERNA_KOSTNADER);
			on(7000, 7699, PERSONALKOSTNADER);
			on(7700, 7899, AVSKRIVNINGAR);
			on(7900, 7999, OVRIGA_RORELSEKOSTNADER);
			on(8000, 8099, RESULTAT_KONCERNFORETAG);
			on(8100, 8199, RESULTAT_INTRESSEFORETAG);
			on(8200, 8299, RESULTAT_OVRIGA_FINANSIELLA);
			on(8300, 8399, OVRIGA_RANTEINTAKTER);
			on(8400, 8499, RANTEKOSTNADER);
			on(8810, 8819, FORANDRING_PERIODISERINGSFONDER);
			on(8820, 8829, ERHALLNA_KONCERNBIDRAG);
			on(8830, 8839, LAMNADE_KONCERNBIDRAG);
			on(8850, 8859, FORANDRING_OVERAVSKRIVNINGAR);
			on(8800, 8899, OVRIGA_BOKSLUTSDISPOSITIONER);
			on(8980, 8989, OVRIGA_SKATTER);
			add(8990, 8999, AccountPlacement.CLOSING);
			on(8900, 8979, SKATT_PA_ARETS_RESULTAT);
		}

		private void on(int from, int to, String lineId) {
			add(from, to, AccountPlacement.on(lineId));
		}

		private void add(int from, int to, AccountPlacement placement) {
			ranges.add(new int[] { from, to });
			placements.add(placement);
		}

		@Override
		public AccountPlacement place(String accountNo) {
			int no = accountNumber(accountNo);
			if (no < 0) return null;
			if (no >= 1900 && no <= 1999) {
				// A bank account with a negative balance is an overdraft
				return AccountPlacement.bySign("BANK_" + accountNo, KASSA_OCH_BANK, CHECKRAKNINGSKREDIT_KORT);
			}
			for (int i = 0; i < ranges.size(); i++) {
				int[] r = ranges.get(i);
				if (no >= r[0] && no <= r[1]) return placements.get(i);
			}
			return null;
		}

		/** @return	The four digit account number, or -1 if the account isn't a BAS account. */
		private static int accountNumber(String accountNo) {
			if (accountNo == null || accountNo.length() < 4) return -1;
			for (int i = 0; i < 4; i++) {
				if (!Character.isDigit(accountNo.charAt(i))) return -1;
			}
			if (accountNo.length() > 4 && Character.isDigit(accountNo.charAt(4))) return -1;
			return Integer.parseInt(accountNo.substring(0, 4));
		}

	}

}
