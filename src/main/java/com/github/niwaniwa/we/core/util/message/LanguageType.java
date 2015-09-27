package com.github.niwaniwa.we.core.util.message;

/**
 * Minecraft内で使用されている言語
 * @author niwaniwa
 *
 */
public enum LanguageType {

	af_ZA("af_ZA"),
	ar_SA("ar_SA"),
	as_ES("as_ES"),
	bg_BG("bg_BG"),
	ca_ES("ca_ES"),
	cs_CZ("cs_CZ"),
	cy_GB("cy_GB"),
	da_DK("da_DK"),
	de_DE("de_DE"),
	el_GR("el_GR"),
	en_AU("en_AU"),
	en_CA("en_CA"),
	en_GB("en_GB"),
	en_PT("en_PT"),
	en_US("en_US"),
	eo_UY("eo_UY"),
	es_AR("es_AR"),
	es_ES("es_ES"),
	es_MX("es_MX"),
	es_UY("es_UY"),
	es_VE("es_VE"),
	et_EE("et_EE"),
	eu_ES("eu_ES"),
	fi_FI("fi_FI"),
	fr_FR("fr_FR"),
	fr_CA("fr_CA"),
	ga_IE("ga_IE"),
	gl_ES("gl_ES"),
	he_IL("he_IL"),
	hi_IN("hi_IN"),
	hr_HR("hr_HR"),
	hu_HU("hu_HU"),
	hy_AM("hy_AM"),
	id_ID("id_ID"),
	is_IS("is_IS"),
	it_IT("it_IT"),
	ja_JP("ja_JP"),
	ka_GE("ka_GE"),
	ko_KR("ko_KR"),
	kw_GB("kw_GB"),
	la_VA("la_VA"),
	lb_LU("lb_LU"),
	lt_LT("lt_LT"),
	lv_LV("lv_LV"),
	ms_MY("ms_MY"),
	mt_MT("mt_MT"),
	nl_NL("nl_NL"),
	nn_NO("nn_NO"),
	no_NO("no_NO"),
	cc_CT("cc_CT"),
	pl_PL("pl_PL"),
	pt_BR("pt_BR"),
	pt_PT("pt_PT"),
	qya_AA("qya_AA"),
	ro_RO("ro_RO"),
	ru_RU("ru_RU"),
	sk_SK("sk_SK"),
	sl_SI("sl_SI"),
	sr_SP("sr_SP"),
	sv_SE("sv_SE"),
	th_TH("th_TH"),
	tlh_AA("tlh_AA"),
	tr_TR("tr_TR"),
	uk_UA("uk_UA"),
	va_ES("va_ES"),
	vi_VN("vi_VN"),
	zh_CN("zh_CN"),
	zh_TW("zh_TW");

	private final String type;

	private LanguageType(String type) {
		this.type = type;
	}

	public String getString(){
		return type;
	}


}
