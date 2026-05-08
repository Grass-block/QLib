package org.atcraftmc.qlib.language;

import java.util.*;
import java.util.function.Function;

public final class MinecraftLocale {
    public static final Set<String> MINECRAFT_LANGUAGE_IDS = new HashSet<>(ids());
    public static final Map<String, List<String>> LANGUAGE_TYPE_TREE = new HashMap<>();
    public static final Map<String, MinecraftLocale> INSTANCES = new HashMap<>();

    //primary languages
    public static final MinecraftLocale EN_US = new MinecraftLocale("en_us");
    public static final MinecraftLocale ZH_CN = new MinecraftLocale("zh_cn");
    public static final MinecraftLocale JA_JP = new MinecraftLocale("ja_jp");

    //other minecraft-implemented languages
    public static final MinecraftLocale AF_ZA = new MinecraftLocale("af_za");
    public static final MinecraftLocale AR_SA = new MinecraftLocale("ar_sa");
    public static final MinecraftLocale AST_ES = new MinecraftLocale("ast_es");
    public static final MinecraftLocale AZ_AZ = new MinecraftLocale("az_az");
    public static final MinecraftLocale BA_RU = new MinecraftLocale("ba_ru");
    public static final MinecraftLocale BAR = new MinecraftLocale("bar");
    public static final MinecraftLocale BE_BY = new MinecraftLocale("be_by");
    public static final MinecraftLocale BG_BG = new MinecraftLocale("bg_bg");
    public static final MinecraftLocale BR_FR = new MinecraftLocale("br_fr");
    public static final MinecraftLocale BRB = new MinecraftLocale("brb");
    public static final MinecraftLocale BS_BA = new MinecraftLocale("bs_ba");
    public static final MinecraftLocale CA_ES = new MinecraftLocale("ca_es");
    public static final MinecraftLocale CS_CZ = new MinecraftLocale("cs_cz");
    public static final MinecraftLocale CY_GB = new MinecraftLocale("cy_gb");
    public static final MinecraftLocale DA_DK = new MinecraftLocale("da_dk");
    public static final MinecraftLocale DE_AT = new MinecraftLocale("de_at");
    public static final MinecraftLocale DE_CH = new MinecraftLocale("de_ch");
    public static final MinecraftLocale DE_DE = new MinecraftLocale("de_de");
    public static final MinecraftLocale EL_GR = new MinecraftLocale("el_gr");
    public static final MinecraftLocale EN_AU = new MinecraftLocale("en_au");
    public static final MinecraftLocale EN_CA = new MinecraftLocale("en_ca");
    public static final MinecraftLocale EN_GB = new MinecraftLocale("en_gb");
    public static final MinecraftLocale EN_NZ = new MinecraftLocale("en_nz");
    public static final MinecraftLocale EN_PT = new MinecraftLocale("en_pt");
    public static final MinecraftLocale EN_UD = new MinecraftLocale("en_ud");
    public static final MinecraftLocale ENP = new MinecraftLocale("enp");
    public static final MinecraftLocale ENWS = new MinecraftLocale("enws");
    public static final MinecraftLocale EO_UY = new MinecraftLocale("eo_uy");
    public static final MinecraftLocale ES_AR = new MinecraftLocale("es_ar");
    public static final MinecraftLocale ES_CL = new MinecraftLocale("es_cl");
    public static final MinecraftLocale ES_EC = new MinecraftLocale("es_ec");
    public static final MinecraftLocale ES_ES = new MinecraftLocale("es_es");
    public static final MinecraftLocale ES_MX = new MinecraftLocale("es_mx");
    public static final MinecraftLocale ES_UY = new MinecraftLocale("es_uy");
    public static final MinecraftLocale ES_VE = new MinecraftLocale("es_ve");
    public static final MinecraftLocale ESAN = new MinecraftLocale("esan");
    public static final MinecraftLocale ET_EE = new MinecraftLocale("et_ee");
    public static final MinecraftLocale EU_ES = new MinecraftLocale("eu_es");
    public static final MinecraftLocale FA_IR = new MinecraftLocale("fa_ir");
    public static final MinecraftLocale FI_FI = new MinecraftLocale("fi_fi");
    public static final MinecraftLocale FIL_PH = new MinecraftLocale("fil_ph");
    public static final MinecraftLocale FO_FO = new MinecraftLocale("fo_fo");
    public static final MinecraftLocale FR_CA = new MinecraftLocale("fr_ca");
    public static final MinecraftLocale FR_FR = new MinecraftLocale("fr_fr");
    public static final MinecraftLocale FRA_DE = new MinecraftLocale("fra_de");
    public static final MinecraftLocale FUR_IT = new MinecraftLocale("fur_it");
    public static final MinecraftLocale FY_NL = new MinecraftLocale("fy_nl");
    public static final MinecraftLocale GA_IE = new MinecraftLocale("ga_ie");
    public static final MinecraftLocale GD_GB = new MinecraftLocale("gd_gb");
    public static final MinecraftLocale GL_ES = new MinecraftLocale("gl_es");
    public static final MinecraftLocale HAW_US = new MinecraftLocale("haw_us");
    public static final MinecraftLocale HE_IL = new MinecraftLocale("he_il");
    public static final MinecraftLocale HI_IN = new MinecraftLocale("hi_in");
    public static final MinecraftLocale HR_HR = new MinecraftLocale("hr_hr");
    public static final MinecraftLocale HU_HU = new MinecraftLocale("hu_hu");
    public static final MinecraftLocale HY_AM = new MinecraftLocale("hy_am");
    public static final MinecraftLocale ID_ID = new MinecraftLocale("id_id");
    public static final MinecraftLocale IG_NG = new MinecraftLocale("ig_ng");
    public static final MinecraftLocale IO_EN = new MinecraftLocale("io_en");
    public static final MinecraftLocale IS_IS = new MinecraftLocale("is_is");
    public static final MinecraftLocale ISV = new MinecraftLocale("isv");
    public static final MinecraftLocale IT_IT = new MinecraftLocale("it_it");
    public static final MinecraftLocale JBO_EN = new MinecraftLocale("jbo_en");
    public static final MinecraftLocale KA_GE = new MinecraftLocale("ka_ge");
    public static final MinecraftLocale KK_KZ = new MinecraftLocale("kk_kz");
    public static final MinecraftLocale KN_IN = new MinecraftLocale("kn_in");
    public static final MinecraftLocale KO_KR = new MinecraftLocale("ko_kr");
    public static final MinecraftLocale KSH = new MinecraftLocale("ksh");
    public static final MinecraftLocale KW_GB = new MinecraftLocale("kw_gb");
    public static final MinecraftLocale LA_LA = new MinecraftLocale("la_la");
    public static final MinecraftLocale LB_LU = new MinecraftLocale("lb_lu");
    public static final MinecraftLocale LI_LI = new MinecraftLocale("li_li");
    public static final MinecraftLocale LMO = new MinecraftLocale("lmo");
    public static final MinecraftLocale LOL_US = new MinecraftLocale("lol_us");
    public static final MinecraftLocale LT_LT = new MinecraftLocale("lt_lt");
    public static final MinecraftLocale LV_LV = new MinecraftLocale("lv_lv");
    public static final MinecraftLocale LZH = new MinecraftLocale("lzh");
    public static final MinecraftLocale MK_MK = new MinecraftLocale("mk_mk");
    public static final MinecraftLocale MN_MN = new MinecraftLocale("mn_mn");
    public static final MinecraftLocale MS_MY = new MinecraftLocale("ms_my");
    public static final MinecraftLocale MT_MT = new MinecraftLocale("mt_mt");
    public static final MinecraftLocale NDS_DE = new MinecraftLocale("nds_de");
    public static final MinecraftLocale NL_BE = new MinecraftLocale("nl_be");
    public static final MinecraftLocale NL_NL = new MinecraftLocale("nl_nl");
    public static final MinecraftLocale NN_NO = new MinecraftLocale("nn_no");
    public static final MinecraftLocale NO_NO = new MinecraftLocale("no_no");
    public static final MinecraftLocale OC_FR = new MinecraftLocale("oc_fr");
    public static final MinecraftLocale OVD = new MinecraftLocale("ovd");
    public static final MinecraftLocale PL_PL = new MinecraftLocale("pl_pl");
    public static final MinecraftLocale PT_BR = new MinecraftLocale("pt_br");
    public static final MinecraftLocale PT_PT = new MinecraftLocale("pt_pt");
    public static final MinecraftLocale QYA_AA = new MinecraftLocale("qya_aa");
    public static final MinecraftLocale RO_RO = new MinecraftLocale("ro_ro");
    public static final MinecraftLocale RPR = new MinecraftLocale("rpr");
    public static final MinecraftLocale RU_RU = new MinecraftLocale("ru_ru");
    public static final MinecraftLocale SE_NO = new MinecraftLocale("se_no");
    public static final MinecraftLocale SK_SK = new MinecraftLocale("sk_sk");
    public static final MinecraftLocale SL_SI = new MinecraftLocale("sl_si");
    public static final MinecraftLocale SO_SO = new MinecraftLocale("so_so");
    public static final MinecraftLocale SQ_AL = new MinecraftLocale("sq_al");
    public static final MinecraftLocale SR_SP = new MinecraftLocale("sr_sp");
    public static final MinecraftLocale SV_SE = new MinecraftLocale("sv_se");
    public static final MinecraftLocale SXU = new MinecraftLocale("sxu");
    public static final MinecraftLocale SZL = new MinecraftLocale("szl");
    public static final MinecraftLocale TA_IN = new MinecraftLocale("ta_in");
    public static final MinecraftLocale TH_TH = new MinecraftLocale("th_th");
    public static final MinecraftLocale TL_PH = new MinecraftLocale("tl_ph");
    public static final MinecraftLocale TLH_AA = new MinecraftLocale("tlh_aa");
    public static final MinecraftLocale TOK = new MinecraftLocale("tok");
    public static final MinecraftLocale TR_TR = new MinecraftLocale("tr_tr");
    public static final MinecraftLocale TT_RU = new MinecraftLocale("tt_ru");
    public static final MinecraftLocale UK_UA = new MinecraftLocale("uk_ua");
    public static final MinecraftLocale VAL_ES = new MinecraftLocale("val_es");
    public static final MinecraftLocale VEC_IT = new MinecraftLocale("vec_it");
    public static final MinecraftLocale VI_VN = new MinecraftLocale("vi_vn");
    public static final MinecraftLocale YI_DE = new MinecraftLocale("yi_de");
    public static final MinecraftLocale YO_NG = new MinecraftLocale("yo_ng");
    public static final MinecraftLocale ZH_HK = new MinecraftLocale("zh_hk");
    public static final MinecraftLocale ZH_TW = new MinecraftLocale("zh_tw");
    public static final MinecraftLocale ZLM_ARAB = new MinecraftLocale("zlm_arab");

    private final String id;

    private MinecraftLocale(String id) {
        this.id = id;
        register(id, this);
    }

    public static List<String> ids() {
        return List.of(
                "af_za",
                "ar_sa",
                "ast_es",
                "az_az",
                "ba_ru",
                "bar",
                "be_by",
                "bg_bg",
                "br_fr",
                "brb",
                "bs_ba",
                "ca_es",
                "cs_cz",
                "cy_gb",
                "da_dk",
                "de_at",
                "de_ch",
                "de_de",
                "el_gr",
                "en_au",
                "en_ca",
                "en_gb",
                "en_nz",
                "en_pt",
                "en_ud",
                "en_us",
                "enp",
                "enws",
                "eo_uy",
                "es_ar",
                "es_cl",
                "es_ec",
                "es_es",
                "es_mx",
                "es_uy",
                "es_ve",
                "esan",
                "et_ee",
                "eu_es",
                "fa_ir",
                "fi_fi",
                "fil_ph",
                "fo_fo",
                "fr_ca",
                "fr_fr",
                "fra_de",
                "fur_it",
                "fy_nl",
                "ga_ie",
                "gd_gb",
                "gl_es",
                "haw_us",
                "he_il",
                "hi_in",
                "hr_hr",
                "hu_hu",
                "hy_am",
                "id_id",
                "ig_ng",
                "io_en",
                "is_is",
                "isv",
                "it_it",
                "ja_jp",
                "jbo_en",
                "ka_ge",
                "kk_kz",
                "kn_in",
                "ko_kr",
                "ksh",
                "kw_gb",
                "la_la",
                "lb_lu",
                "li_li",
                "lmo",
                "lol_us",
                "lt_lt",
                "lv_lv",
                "lzh",
                "mk_mk",
                "mn_mn",
                "ms_my",
                "mt_mt",
                "nds_de",
                "nl_be",
                "nl_nl",
                "nn_no",
                "no_no",
                "oc_fr",
                "ovd",
                "pl_pl",
                "pt_br",
                "pt_pt",
                "qya_aa",
                "ro_ro",
                "rpr",
                "ru_ru",
                "se_no",
                "sk_sk",
                "sl_si",
                "so_so",
                "sq_al",
                "sr_sp",
                "sv_se",
                "sxu",
                "szl",
                "ta_in",
                "th_th",
                "tl_ph",
                "tlh_aa",
                "tok",
                "tr_tr",
                "tt_ru",
                "uk_ua",
                "val_es",
                "vec_it",
                "vi_vn",
                "yi_de",
                "yo_ng",
                "zh_cn",
                "zh_hk",
                "zh_tw",
                "zlm_arab"
        );
    }

    public static MinecraftLocale minecraft(String id) {
        if (INSTANCES.containsKey(id)) {
            return INSTANCES.get(id);
        }

        return new MinecraftLocale(id);
    }

    public static MinecraftLocale locale(Locale locale) {
        return minecraft(LocaleMapping.minecraft(locale));
    }

    public static String remap(String input, Function<String, Boolean> checker) {
        if (checker.apply(input)) {
            return input;
        }

        var root = root(input);

        if (!LANGUAGE_TYPE_TREE.containsKey(root)) {
            return remap("en_us", checker);
        }

        for (String lang : LANGUAGE_TYPE_TREE.get(root)) {
            if (checker.apply(lang)) {
                return lang;
            }
        }

        if (input.equals("en_us")) {
            return "zh_cn";
        }

        return remap("en_us", checker);
    }

    public static String root(String id) {
        var root = id.contains("_") ? id.split("_")[0] : id;

        if (id.equals("lzh")) {
            root = "zh";
        }

        return root;
    }

    private static void register(String id, MinecraftLocale locale) {
        var root = root(id);
        var list = LANGUAGE_TYPE_TREE.computeIfAbsent(root, (k) -> new ArrayList<>());

        if (!list.contains(id)) {
            list.add(id);
        }

        INSTANCES.put(id, locale);
        MINECRAFT_LANGUAGE_IDS.add(id);
    }

    public String minecraft() {
        return this.id;
    }

    public Locale locale() {
        return LocaleMapping.locale(this.minecraft());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MinecraftLocale locale)) {
            return false;
        }

        return Objects.equals(locale.id, this.id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
