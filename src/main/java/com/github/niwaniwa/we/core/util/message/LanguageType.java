package com.github.niwaniwa.we.core.util.message;

/**
 * Minecraft内で使用されている言語
 *
 * @author niwaniwa
 */
public enum LanguageType {

    af_za("af_za"),
    ar_sa("ar_sa"),
    as_es("as_es"),
    bg_bg("bg_bg"),
    ca_es("ca_es"),
    cs_cz("cs_cz"),
    cy_gb("cy_gb"),
    da_dk("da_dk"),
    de_de("de_de"),
    el_gr("el_gr"),
    en_au("en_au"),
    en_ca("en_ca"),
    en_gb("en_gb"),
    en_Pt("en_pt"),
    en_us("en_us"),
    eo_uy("eo_uy"),
    es_ar("es_ar"),
    es_es("es_es"),
    es_mx("es_mx"),
    es_uy("es_uy"),
    es_ve("es_ve"),
    et_ee("et_ee"),
    eu_es("eu_es"),
    fi_fi("fi_fi"),
    fr_fr("fr_fr"),
    fr_ca("fr_ca"),
    ga_ie("ga_ie"),
    gl_es("gl_es"),
    he_il("he_il"),
    hi_in("hi_in"),
    hr_hr("hr_hr"),
    hu_hu("hu_hu"),
    hy_am("hy_am"),
    id_id("id_id"),
    is_is("is_is"),
    it_it("it_it"),
    ja_jp("ja_jp"),
    ka_ge("ka_ge"),
    ko_kr("ko_kr"),
    kw_gb("kw_ge"),
    la_Va("la_va"),
    lb_lu("lb_lu"),
    lt_lt("lt_lt"),
    lv_lv("lv_lv"),
    ms_my("ms_my"),
    mt_mt("mt_mt"),
    nl_nl("nl_nl"),
    nn_nO("nn_no"),
    no_nO("no_no"),
    cc_ct("cc_ct"),
    pl_pl("pl_pl"),
    pt_br("pt_br"),
    pt_pt("pt_pt"),
    qya_aa("qya_aa"),
    ro_ro("ro_ro"),
    ru_ru("ru_ru"),
    sk_sk("sk_sk"),
    sl_si("sl_si"),
    sr_sp("sr_sp"),
    sv_se("sv_se"),
    th_th("th_th"),
    tlh_aa("tlh_aa"),
    tr_tr("tr_tr"),
    uk_ua("uk_ua"),
    va_es("va_es"),
    vi_vn("vi_vn"),
    zh_cn("zh_cn"),
    zh_tw("zh_tw");

    private final String type;

    private LanguageType(String type) {
        this.type = type;
    }

    public String getString() {
        return type;
    }


}
